package com.towntrot.checkin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.towntrot.checkin.httputils.MyCustomFeedManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    public Context mContext;
    private EditText  user_email=null;
    private static final String TAG = MainActivity.class.getName();
    private EditText  user_password=null;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

            filereader read=new filereader();
            String key=read.readfile("tt_key", getApplicationContext());
            if(key!="") {

                Intent intent = new Intent(mContext, events.class);
                startActivity(intent);
                finish();
            }

        setContentView(R.layout.activity_main);
        user_email = (EditText)findViewById(R.id.editText1);
        user_password = (EditText)findViewById(R.id.editText2);
        login = (Button)findViewById(R.id.button1);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                authentication();
       }

        });

    }

       public void authentication() {
           authentication task = new authentication();
           String url = getApiEndpoint() + "authenticate_user";
           task.execute(url, user_email.getText().toString(),user_password.getText().toString());
       }

       class authentication extends AsyncTask<String, Void, String> {
           ProgressDialog progDialog;

           @Override
           protected void onPreExecute() {
               super.onPreExecute();

                   progDialog = new ProgressDialog(mContext);
                   progDialog.setMessage("Sending...");
                   progDialog.setIndeterminate(false);
                   progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                   progDialog.setCancelable(false);
                   progDialog.show();

           }

           @Override
           protected String doInBackground(String... params) {
               List<NameValuePair> post_params = new ArrayList<NameValuePair>();
               post_params.add(new BasicNameValuePair("user_email", params[1]));
               post_params.add(new BasicNameValuePair("user_password", params[2]));
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
               if (result != null) {
                   User_details userdetails = (User_details) MyCustomFeedManager.getMappedModel(result, User_details.class);
                   try {
                       progDialog.dismiss();
                   } catch (Exception e) {
                   }
                   String id=userdetails.getID();
                   if(id=="0"){
                       Toast.makeText(mContext, "Wrong Credentials", Toast.LENGTH_LONG).show();
                       user_email.setText("");
                       user_password.setText("");
                   }
                   else{
                   String key=userdetails.getKey();
                   String id_file = "tt_id";
                   String key_file = "tt_key";
                   FileOutputStream outputStream1;
                   FileOutputStream outputStream2;
                   try {
                       outputStream1 = openFileOutput(id_file, Context.MODE_PRIVATE);
                       outputStream2 = openFileOutput(key_file, Context.MODE_PRIVATE);
                       outputStream1.write(id.getBytes());
                       outputStream2.write(key.getBytes());
                       outputStream1.close();
                       outputStream2.close();
                   } catch (Exception e) {
                       Log.e(TAG, "File not found: " + e.toString());
                   }
                   Intent intent = new Intent(mContext, events.class);
                   startActivity(intent);
                   finish();}
               } else {
                   try {
                       progDialog.dismiss();
                   } catch (Exception e) {
                   }
                   Toast.makeText(mContext, "There was some error sending your response! Please check your internet connection and try again!", Toast.LENGTH_LONG).show();
               }
           }
       }

       public String getApiEndpoint() {
           return "http://192.168.1.176/app/mobile/mv1/";
       }
}