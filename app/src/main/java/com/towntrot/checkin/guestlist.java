package com.towntrot.checkin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.towntrot.checkin.httputils.MyCustomFeedManager;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;

public class guestlist extends SampleActivityBase {

    public String[] name;
    public String[] id;
    public String[] status;
    public String[] bid;
    public String[] bname;
    public String[] bprice;
    public String[] booking_id;
    public ArrayList<String> list = new ArrayList<String>();
    public String event_id;
    public TextView[] textView;
    public int no_of_people;
    public int no_of_ticket_types;
    private Context mContext;
    MyCustomAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticketselect);
        Intent intent = getIntent();
        event_id = intent.getStringExtra("event_pass");
        mContext = this;
        list();
    }

    private void displayListView() {

        ArrayList<Country> countryList = new ArrayList<Country>();
        Country country;
        for (int i=0;i<no_of_ticket_types;i++)
        {
            if (bprice[i].equals("-1"))bprice[i]="FREE";
            country=new Country(bprice[i],bname[i],false);
            countryList.add(country);
        }

        dataAdapter = new MyCustomAdapter(this,
                R.layout.ticketlistlayout, countryList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

    }

    private class MyCustomAdapter extends ArrayAdapter<Country> {

        private ArrayList<Country> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Country> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Country>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.ticketlistlayout, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Country country = (Country) cb.getTag();
                        country.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Country country = countryList.get(position);
            holder.code.setText(" (" +  country.getCode() + ")");
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);

            return convertView;

        }

    }

    private void checkButtonClick() {


        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");
                ArrayList<Country> countryList = dataAdapter.countryList;

                for (int i = 0; i < countryList.size(); i++) {
                    Country country = countryList.get(i);
                    if (country.isSelected()) {
                        list.add(bid[i]);
                        responseText.append("\n" + country.getName());
                    }
                }

                setContentView(R.layout.guestlist);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
                transaction.replace(R.id.sample_content_fragment, fragment);
                transaction.commit();
            }
        });

    }

    public void list() {
        list task = new list();
        String url = getApiEndpoint() + "get_merchant_event_attendees";
        filereader read1 = new filereader();
        filereader read2 = new filereader();
        String id = read1.readfile("tt_id", getApplicationContext());
        String key = read2.readfile("tt_key", getApplicationContext());
        task.execute(url, id, key, event_id);
    }

    class list extends AsyncTask<String, Void, String> {
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
            post_params.add(new BasicNameValuePair("event_id", params[3]));
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
            List_Details listDetails = (List_Details) MyCustomFeedManager.getMappedModel(result, List_Details.class);
            if (listDetails.authentication) {
                try {
                    progDialog.dismiss();
                } catch (Exception e) {
                }
                no_of_ticket_types=listDetails.book_det.bid.length;
                no_of_people = listDetails.list_det.length;
                id = new String[no_of_people];
                name = new String[no_of_people];
                status = new String[no_of_people];
                booking_id=new String[no_of_people];
                textView=new TextView[no_of_people];
                bname=new String[no_of_ticket_types];
                bprice=new String[no_of_ticket_types];
                bid=new String[no_of_ticket_types];
                for (int i = 0; i < no_of_people; i++) {

                    id[i] = listDetails.list_det[i].getId();
                    name[i] = listDetails.list_det[i].getName();
                    status[i] = listDetails.list_det[i].getStatus();
                    booking_id[i]=listDetails.list_det[i].getBooking_id();
                }
                for (int i=0;i<no_of_ticket_types;i++){
                    bid[i]=listDetails.book_det.getBid(i);
                    bname[i]=listDetails.book_det.getBname(i);
                    bprice[i]=listDetails.book_det.getBprice(i);
                }
                    displayListView();
                    checkButtonClick();

            } else {
                try {
                    progDialog.dismiss();
                } catch (Exception e) {
                }
                Toast.makeText(mContext, "There was some error sending your response! Please check your internet connection and try again!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void postnewstatus(int x,String old_status,String new_status) {
        postnewstatus task = new postnewstatus();
        String url = getApiEndpoint() + "change_attendee_status";
        filereader read1 = new filereader();
        filereader read2 = new filereader();
        String merchant_id = read1.readfile("tt_id", getApplicationContext());
        String key = read2.readfile("tt_key", getApplicationContext());
        task.execute(url, merchant_id, key, event_id,id[x],old_status,new_status);
    }

    class postnewstatus extends AsyncTask<String, Void, String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            List post_params = new ArrayList<NameValuePair>();
            post_params.add(new BasicNameValuePair("merchant_id", params[1]));
            post_params.add(new BasicNameValuePair("key", params[2]));
            post_params.add(new BasicNameValuePair("event_id", params[3]));
            post_params.add(new BasicNameValuePair("transaction_id", params[4]));
            post_params.add(new BasicNameValuePair("old_status", params[5]));
            post_params.add(new BasicNameValuePair("new_status", params[6]));
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
            Post_check post_check = (Post_check) MyCustomFeedManager.getMappedModel(result, Post_check.class);
            if (post_check.authentication) {
                try {
                    progDialog.dismiss();
                } catch (Exception e) {
                }

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
    public String[] getNamelist(){
        return name;
    }
    public int getNo0fPeople(){
        return no_of_people;
    }

    public String[] getStatuslist(){
        return status;
    }
    public String[] getBooking_id(){
        return booking_id;
    }
    public ArrayList getselectedid(){
        return list;
    }
    public void onItemClick(int x,int pageno,int n){
        int j=0,i=0;
        String s=""+pageno;
        for (;j<=x;i++)if (status[i].equals(s))j++;
        i--;
        status[i]=""+n;
        postnewstatus(i,s,status[i]);
    }
}
