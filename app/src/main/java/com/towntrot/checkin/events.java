package com.towntrot.checkin;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.towntrot.checkin.httputils.MyCustomFeedManager;
import com.towntrot.checkin.lazylist.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.FileOutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class events extends Activity {
    public Context mContext;
    private String[] event_id;
    private String[] name;
    private String[] image;
    private String[] venue;
    private String[] time;
    private ImageView[] imageViews;
    private TextView[] textViews;
    private TextView[] ven_text;
    private TextView[] time_text;
    private static final String TAG = events.class.getName();
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/nexa_bold.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        setContentView(R.layout.activity_events);
        ActionBar bar = getActionBar();
        getActionBar().setIcon(R.drawable.logo);
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F4511E")));
        mContext=this;
        if (isInternetOn()) {
            event();
        } else {
            signout();
            Toast.makeText(mContext, "Please check your internet connection and try again!", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public final boolean isInternetOn() {
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            return false;
        }
        return false;
    }

    public void event() {
        event task = new event();
        String url = getApiEndpoint() + "get_merchant_events";
        filereader read1=new filereader();
        filereader read2=new filereader();
        String id=read1.readfile("tt_id", getApplicationContext());
        String key=read2.readfile("tt_key", getApplicationContext());
        task.execute(url,id,key);
    }

    class event extends AsyncTask<String, Void, String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progDialog = new ProgressDialog(mContext);
            progDialog.setMessage("Fetching Data...");
            progDialog.setIndeterminate(false);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setCancelable(false);
            progDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            List post_params = new ArrayList<NameValuePair>();
            post_params.add(new BasicNameValuePair("merchant_id", params[1]));
            post_params.add(new BasicNameValuePair("key", params[2]));
            try {
                return MyCustomFeedManager.executePostRequest(params[0], post_params);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("applogs", "Exception: " + e.getMessage());
                Toast.makeText(mContext, "Please check your internet connection and try again!", Toast.LENGTH_LONG).show();
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Event_details eventDetails = (Event_details) MyCustomFeedManager.getMappedModel(result, Event_details.class);
            if (eventDetails.authentication) {
                try {
                    progDialog.dismiss();
                } catch (Exception e) {
                }
                if(eventDetails.event_det!=null) {
                int no_of_events=eventDetails.event_det.length;
                event_id=new String[no_of_events];
                name=new String[no_of_events];
                image =new String[no_of_events];
                time= new String[no_of_events];
                venue= new String[no_of_events];
                ImageLoader imageLoader=new ImageLoader(getApplicationContext());
                LinearLayout imagelayout= (LinearLayout)findViewById(R.id.imageLayout);
                LinearLayout textlayout = (LinearLayout)findViewById(R.id.textLayout);
                LinearLayout textlayout2= (LinearLayout)findViewById(R.id.textLayout2);
                LinearLayout textlayout3= (LinearLayout)findViewById(R.id.textLayout3);
                int dimension = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,182, getResources().getDisplayMetrics());
                imageViews=new ImageView[no_of_events];
                textViews=new TextView[no_of_events];
                time_text=new TextView[no_of_events];
                ven_text=new TextView[no_of_events];
                int top=0;
                int left=0;

                for(int i=0;i<no_of_events;i++){

                    //font
                    Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/nexa_bold.otf");
                    //display metrics
                    float scale = getResources().getDisplayMetrics().density;
                    int dp = (int) (4*scale + 0.5f);

                    //all the strings are taken here
                    event_id[i] = eventDetails.event_det[i].getevent_Id();
                    name[i] = eventDetails.event_det[i].getName();
                    image[i] = eventDetails.event_det[i].getImage();
                    venue[i] = eventDetails.event_det[i].getVenue_name();
                    time[i] =eventDetails.event_det[i].getTime_string();

                    //sets parameters
                    LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(dimension,dimension);
                    lp.setMargins(-left, top, 0, 0);

                    //logic of the views
                    if(i%2!=0){top+=dimension;left+=2*(dimension);}
                    if(i%2==0&&i!=0){left-=2*(dimension);}

                    //Image declarations
                    imageViews[i] =new ImageView(mContext);
                    imageViews[i].setLayoutParams(lp);
                    imageViews[i].setColorFilter(Color.argb(125, 0, 0, 0));
                    imageViews[i].setId(i);
                    imagelayout.addView(imageViews[i]);
                    imageLoader.DisplayImage("http://towntrot.com/uploads/125/" + image[i], imageViews[i]);

                    //event name declarations
                    textViews[i]=new TextView(mContext);
                    textViews[i].setText(name[i]);
                    textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                    textViews[i].setAllCaps(true);
                    textViews[i].setTypeface(custom_font);
                    textViews[i].setGravity(Gravity.CENTER);
                    textViews[i].setTextColor(Color.parseColor("#ffffff"));
                    textViews[i].setLayoutParams(lp);
                    textViews[i].setPadding(2*dp,0,2*dp,0);
                    textlayout.addView(textViews[i]);

                    //event time declarations
                    time_text[i]=new TextView(mContext);
                    time_text[i].setText(time[i]);
                    time_text[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                    time_text[i].setTypeface(Typeface.DEFAULT_BOLD);
                    time_text[i].setTypeface(custom_font);
                    time_text[i].setGravity(Gravity.BOTTOM);
                    time_text[i].setTextColor(Color.parseColor("#F05324"));
                    time_text[i].setLayoutParams(lp);
                    time_text[i].setPadding(2 * dp, dp, dp, 12 * dp);
                    textlayout2.addView(time_text[i]);

                    //event venue declarations
                    ven_text[i]=new TextView(mContext);
                    ven_text[i].setText(venue[i]);
                    ven_text[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                    ven_text[i].setGravity(Gravity.BOTTOM);
                    ven_text[i].setTypeface(custom_font);
                    ven_text[i].setTextColor(Color.parseColor("#ffffff"));
                    ven_text[i].setLayoutParams(lp);
                    ven_text[i].setPadding(2*dp, dp, dp, 8*dp);
                    textlayout3.addView(ven_text[i]);


                    imageViews[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.startAnimation(buttonClick);
                            if(isInternetOn()){
                            Intent intent = new Intent(mContext, guestlist.class);
                            Bundle b=new Bundle();
                            b.putStringArray("event_pass", new String[]{event_id[v.getId()], name[v.getId()]});
                            intent.putExtras(b);
                            startActivity(intent);}
                            else{
                                Toast.makeText(mContext,"Please Check your Internet Connection and try again!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
                else
                    setContentView(R.layout.no_events);
            }

                else {
                try {
                    progDialog.dismiss();
                } catch (Exception e) {
                }
                Toast.makeText(mContext, "Authentication Error", Toast.LENGTH_LONG).show();
            }
            }
        }


    public String getApiEndpoint() {
        return "http://towntrot.com/mobile/mv1/";
    }

    public void signout(){
        String id_file = "tt_id";
        String key_file = "tt_key";
        FileOutputStream outputStream1;
        FileOutputStream outputStream2;
        try {
            outputStream1 = openFileOutput(id_file, Context.MODE_PRIVATE);
            outputStream2 = openFileOutput(key_file, Context.MODE_PRIVATE);
            outputStream1.write("".getBytes());
            outputStream2.write("".getBytes());
            outputStream1.close();
            outputStream2.close();
        } catch (Exception e) {
            Log.e(TAG, "File not found: " + e.toString());
        }
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_events, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.signout:
                signout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}