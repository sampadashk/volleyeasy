package com.samiapps.kv.myapplication;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KV on 20/2/18.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<ViewGroup> listViews = new ArrayList<ViewGroup>();

    public  ViewPagerAdapter( List<ViewGroup> listViews){
        this.listViews = listViews;
    }
    @Override
    public int getCount() {
        return listViews.size();
    }
    @Override
    public Object instantiateItem(View container, int position) {
        System.out.println("第几个pager=="+position);
        try {
            if(listViews.get(position).getParent()==null)
                ((ViewPager) container).addView(listViews.get(position), 0);
            else{
                ((ViewGroup)listViews.get(position).getParent()).removeView(listViews.get(position));
                ((ViewPager) container).addView(listViews.get(position), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listViews.get(position);
    }
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }
}
