package com.towntrot.checkin;

import android.app.Activity;
import android.content.Context;
import android.view.View.OnClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomAdapter extends BaseAdapter implements OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private SlidingTabsBasicFragment ff;
    private static LayoutInflater inflater=null;
    ListModel tempValues=null;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private int i=0;

    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Activity a, ArrayList d,SlidingTabsBasicFragment fragment,int p) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        ff=fragment;
        i=p;
        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView text;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.tabitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.text);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text.setText("This list is empty!");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (ListModel) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.text.setText( tempValues.getCompanyName() );
            ImageView imageView1=(ImageView)vi.findViewById(R.id.imageView1);
            ImageView imageView2=(ImageView)vi.findViewById(R.id.imageView2);
            ImageView imageViewi=(ImageView)vi.findViewById(R.id.imageViewi);
            if (i==0){
                imageView1.setImageResource(R.drawable.checked_in);
                imageView2.setImageResource(R.drawable.checked_out);
            }
            else if (i==1){
                imageView1.setImageResource(R.drawable.booked);
                imageView2.setImageResource(R.drawable.checked_out);
            }
            else if (i==2){
                imageView1.setImageResource(R.drawable.booked);
                imageView2.setImageResource(R.drawable.checked_in);
            }
            imageViewi.setImageResource(R.drawable.infoicon);
            TextView textview=(TextView)vi.findViewById(R.id.text2);
            textview.setText(position+1+" ");
            imageViewi.setOnClickListener(new OnItemClickListener( position+0.1f ));
            imageView1.setOnClickListener(new OnItemClickListener( position ));
            imageView2.setOnClickListener(new OnItemClickListener(-(position+1) ));

        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements OnClickListener{
        private float mPosition;

        OnItemClickListener(float position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            arg0.startAnimation(buttonClick);
            guestlist sct = (guestlist)activity;
            int pageno=ff.getPageno();
            if (mPosition%1!=0)
                sct.onInfoClick(mPosition-0.1f,pageno);
            else if(pageno==0){
                if (mPosition>=0)
            sct.onItemClick(mPosition,pageno,1,ff);
                else
            sct.onItemClick(-mPosition-1,pageno,2,ff);}
            else if(pageno==1){
                if (mPosition>=0)
                    sct.onItemClick(mPosition,pageno,0,ff);
                else
                    sct.onItemClick(-mPosition-1,pageno,2,ff);}
            else if(pageno==2){
                if (mPosition>=0)
                    sct.onItemClick(mPosition,pageno,0,ff);
                else
                    sct.onItemClick(-mPosition-1,pageno,1,ff);}
        }
    }
}