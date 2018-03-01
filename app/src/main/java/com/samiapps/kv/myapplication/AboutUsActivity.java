package com.samiapps.kv.myapplication;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KV on 21/2/18.
 */


    public class AboutUsActivity extends BaseActivity{
        public ViewPagerAdapter adapter;
        public ViewPager viewPager;
        public List<ViewGroup> listViews;
        public LayoutInflater inflater;
        public LinearLayout back;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about_us);
            viewPager= (ViewPager) findViewById(R.id.viewPager);
            back= (LinearLayout) findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
            inflater =getLayoutInflater();
            listViews=new ArrayList<ViewGroup>();
            listViews.add((LinearLayout)(inflater.inflate(R.layout.one, null)).findViewById(R.id.one_layout));
            listViews.add((LinearLayout)(inflater.inflate(R.layout.two, null)).findViewById(R.id.two_layout));
            listViews.add((LinearLayout)(inflater.inflate(R.layout.three, null)).findViewById(R.id.thr_layout));
            listViews.add((LinearLayout)(inflater.inflate(R.layout.four, null)).findViewById(R.id.four_layout));

            adapter=new ViewPagerAdapter(listViews);
            viewPager.setAdapter(adapter);

        }
}
