package com.samiapps.kv.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Locale;

/**
 * Created by KV on 20/2/18.
 */

public class ContractListAdapter extends BaseAdapter {
    public Context context;
    public List<Contract> Data;
    public static String character
            ;

    public ContractListAdapter(Context context,List Data){
        this.context=context;
        this.Data=Data;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Contract contract = Data.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contract_list_item, null);
            holder = new ViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.name);
            holder.descripetion= (TextView) convertView.findViewById(R.id.descripetion);
            holder.supplyInfo = (TextView) convertView.findViewById(R.id.supplyInfo);
            holder.img= (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        MyApplication myApplication=MyApplication.getInstance();
//        Configuration config = myApplication.getResources().getConfiguration();
        if (Constants.getGetLanguage(context).equals("english")||(Locale.getDefault().getLanguage().equals("en"))) {
            holder.descripetion.setText(contract._supplierInfo.getCompanyDescription_english());
        }
        else
            holder.descripetion.setText(contract._supplierInfo.getCompanyDescription());
        Log.d("companydes",contract._supplierInfo.getCompanyDescription());
        try {
            String decodedString = URLDecoder.decode(contract._supplierInfo.getCompanyName(), "UTF-16");
            Log.d("checkdecoded",decodedString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.name.setText(contract._supplierInfo.getCompanyName());

        holder.supplyInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               Intent intent = new Intent(context, SupplyInfoActivity.class);
                intent.putExtra("contract", contract);
                (context).startActivity(intent);

            }
        });

        //  holder.descripetion.setText(contract._supplierInfo.getCompanyDescription());
        if(Data.get(position)._supplierInfo.getLogo()!=null){
            String url= Constants.imgBaseUrlStr+"/"+Data.get(position)._supplierInfo.getLogo();
            Picasso.with(context).load(url).resize(240, 160).centerCrop().into(holder.img);
        }

        return convertView;
    }
    class ViewHolder {
        public TextView name;
        public TextView descripetion,supplyInfo;
        public ImageView img;

    }
    public static String getToken(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        String cha = settings.getString(character, ""/*default value is ""*/);
        //Log.v("err", tokenStr);
        return cha;
    }
}
