package com.towntrot.checkin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import java.util.ArrayList;
import java.util.List;

public class events extends Activity {
    public Context mContext;
    private String[] event_id;
    private String[] name;
    private String[] image;
    private ImageView[] imageViews;
    private TextView[] textViews;
    private static final String TAG = events.class.getName();
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        mContext=this;
        event();
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
                Toast.makeText(mContext, "There was some error sending your response! Please check your internet connection and try again!", Toast.LENGTH_LONG).show();
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

                int no_of_events=eventDetails.event_det.length;
                event_id=new String[no_of_events];
                name=new String[no_of_events];
                image =new String[no_of_events];
                ImageLoader imageLoader=new ImageLoader(getApplicationContext());
                LinearLayout imagelayout= (LinearLayout)findViewById(R.id.imageLayout);
                LinearLayout textlayout = (LinearLayout)findViewById(R.id.textLayout);
                int dimension = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,182, getResources().getDisplayMetrics());
                imageViews=new ImageView[no_of_events];
                textViews=new TextView[no_of_events];
                int top=0;
                int left=0;

                for(int i=0;i<no_of_events;i++){

                    event_id[i] = eventDetails.event_det[i].getevent_Id();
                    name[i] = eventDetails.event_det[i].getName();
                    image[i] = eventDetails.event_det[i].getImage();
                    imageViews[i] =new ImageView(mContext);
                    LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(dimension,dimension);
                    lp.setMargins(-left, top, 0, 0);
                    if(i%2!=0){top+=dimension;left+=2*(dimension);}
                    if(i%2==0&&i!=0){left-=2*(dimension);}
                    imageViews[i].setLayoutParams(lp);
                    imageViews[i].setColorFilter(Color.argb(125, 125, 125, 125));
                    imageViews[i].setId(i);
                    imagelayout.addView(imageViews[i]);
                    imageLoader.DisplayImage("http://towntrot.com/uploads/125/" + image[i], imageViews[i]);
                    textViews[i]=new TextView(mContext);
                    textViews[i].setText(name[i]);
                    textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    textViews[i].setTextColor(Color.parseColor("#ffffff"));
                    textViews[i].setTypeface(null, Typeface.BOLD_ITALIC);
                    textViews[i].setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                    textViews[i].setLayoutParams(lp);
                    textlayout.addView(textViews[i]);
                    imageViews[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.startAnimation(buttonClick);
                            Intent intent = new Intent(mContext, guestlist.class);
                            intent.putExtra("event_pass", event_id[v.getId()]);
                            startActivity(intent);
                        }
                    });
                }
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
        return "http://192.168.1.176/app/mobile/mv1/";
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