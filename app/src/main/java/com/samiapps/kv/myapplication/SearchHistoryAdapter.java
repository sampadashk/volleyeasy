package com.samiapps.kv.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by KV on 28/2/18.
 */

public class SearchHistoryAdapter extends BaseAdapter {
    public List<String> Data;
    public Context mContext;

    public SearchHistoryAdapter(Context context, List data) {
        this.mContext = context;
        this.Data = data;

    }

    @Override
    public int getCount() {
        return Data.size();
    }

    @Override
    public Object getItem(int position) {
        return Data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_lishi_item, null);

            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.name.setText(Data.get(position));

        return convertView;
    }


    class ViewHolder {

        public TextView name;

    }
}