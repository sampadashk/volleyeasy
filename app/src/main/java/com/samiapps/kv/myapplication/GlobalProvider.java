package com.samiapps.kv.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by KV on 20/2/18.
 */

public class GlobalProvider {
    private static GlobalProvider instance;

    private static RequestQueue requestQueue;
    public static int c=0;
    public boolean IsLoging=false;
    Context context;
    Map<String,String> params;
    public boolean IsLogingOut=false;
    public List<String> Data;
    public Contract contract;
    public String adjustToStart="contract";
    public List<Contract> contractList=new ArrayList<Contract>();
    public List<Contract> contractListToCart=new ArrayList<Contract>();
    public List<Product> shangpingList=new ArrayList<Product>();
    public List<Product> shangpingListDefault=new ArrayList<Product>();
    public List<Product> shangpingListSearchDefault=new ArrayList<Product>();

    public Me me;
    public int index=0;
    public int whichOrder;
    public String lan="a";
    public List<OrderSubmit> orders=new ArrayList<OrderSubmit>();

    public OrderSubmit editOrder=new OrderSubmit();
    public Order order=new Order();

    public String character;
//    public String Adress[]={"",""};
//    public Date data_Str;
//    public String feedback_Str="";
//    public String ShangpingHeaderLoadCategory="";

    public String Adress[]={"",""};
    public Date data_Str;
    public String feedback_Str="";
    public String ShangpingHeaderLoadCategory="";
    public boolean isShopping=false;
    public boolean isActive=false;
    public boolean isLogined;
    public int singal;

    private GlobalProvider(Context context){
        this.context=context;
        requestQueue = Volley.newRequestQueue(context);
        params=new HashMap<>();


    }



    public static synchronized GlobalProvider getInstance(Context context) {

        if(instance == null) {
            instance = new GlobalProvider(context);
        }
        return instance;
    }
    public static void addRequest(Request request)
    {
        requestQueue.add(request);
    }
    public Map<String,String> setToken(String token) {

        params.put("Authorization", token);
        return params;

    }


    public Map<String, String> addHeaderToken(Context context) {
        if ( context != null && Constants.getToken(context) != null ) {
            Log.d("checkheader",Constants.getToken(context));

            params.put("Authorization", Constants.getToken(context));
            if (Constants.getGetLanguage(context).equals("english")) {
                params.put("Language", "english");

                MyApplication ma=MyApplication.getInstance();

                // MainActivity ma=MainActivity.getDefault();




                ma.change("english");



                if(index == 0){
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.putExtra("l","x");
                    context.startActivity(intent);
                    index++;
                }
            }else if(Constants.getGetLanguage(context).equals("chinese")){
                params.put("Language", "chinese");

                // MainActivity.getDefault().change("chinese");
                MyApplication ma=MyApplication.getInstance();






                ma.change("chinese");
                if(index == 0){
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.putExtra("l","x");
                    context.startActivity(intent);
                    index++;
                }
            }else{
                if (instance.lan.equals("a")) {
                    if (Locale.getDefault().getLanguage().equals("en")) {
                        params.put("Language", "english");
                    } else {
                        params.put("Language", "chinese");
                    }
                } else if (instance.lan.equals("english")) {
                    params.put("Language", "english");
                } else {
                    params.put("Language", "chinese");
                }
            }
        }
        return params;
    }





    public void gotoLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        ((Activity)context).startActivity(intent);
        ((Activity)context).finish();
        GlobalProvider.getInstance(context).shangpingList.clear();
        GlobalProvider.getInstance(context).contractListToCart.clear();
        GlobalProvider.getInstance(context).contractList.clear();
        GlobalProvider.getInstance(context).shangpingListDefault.clear();
        GlobalProvider.getInstance(context).ShangpingHeaderLoadCategory="";

    }
}
