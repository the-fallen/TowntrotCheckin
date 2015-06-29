package com.towntrot.checkin;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class SlidingTabsBasicFragment extends Fragment {

    CustomAdapter adapter;
    ListView list;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private String[] eventtype={"    BOOKED    ","    CHECKED IN    ","    CANCELLED    "};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return eventtype[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);
            guestlist gg=(guestlist)getActivity();
            ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();
            CustomListViewValuesArr=setListData(gg.getNamelist(), gg.getStatuslist(),gg.getBooking_id(),gg.getselectedid(), gg.getNo0fPeople(), position);
            list= ( ListView )view.findViewById(R.id.list);
            adapter=new CustomAdapter( getActivity(), CustomListViewValuesArr,SlidingTabsBasicFragment.this,position);
            list.setAdapter(adapter);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
    public ArrayList setListData(String[] string,String[] status,String[] booking_id,ArrayList list,int no,int pos) {
        ArrayList<ListModel> arr = new ArrayList<ListModel>();
        for (int i = 0; i < no; i++) {

            final ListModel name = new ListModel();
            int x=Integer.parseInt(status[i]);
           for (int j=0;j<list.size();j++){
               if (booking_id[i].equals(list.get(j)))
            if(pos==x){ name.setCompanyName(string[i]);
            arr.add(name);}}
        }
        return arr;
    }
    public void update(){
        mViewPager.getAdapter().notifyDataSetChanged();
    }
    public int getPageno(){
        return mViewPager.getCurrentItem();
    }
}
