package com.samiapps.kv.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by KV on 20/2/18.
 */

public class Constants {
    //static public String baseUrlStr = "http://52.74.115.250:8080/";//platform business URL
    //static public String baseUrlStr = "http://192.168.1.103:8080/";
    static public String baseUrlStr = "http://52.74.210.202:8080/";//platform test URL
    static public String versionUrlStr=baseUrlStr+"api/version/android";
    static public String categoryUrlStr=baseUrlStr+"api/categorys";
    static public String contractUrlStr=baseUrlStr+"api/contracts";
    static public String orderListUrlStr = baseUrlStr + "api/orders";
    static public String loginUrlStr = baseUrlStr + "auth/local";
    static public String collectedStr=baseUrlStr + "api/collects";
    static public String supplyStr=baseUrlStr+"api/suppliers";
    static public String meUrlStr=baseUrlStr+"api/users/me";
    static public String productListUrlStr=baseUrlStr +"api/products";
    static public String staffsListUrlStr=baseUrlStr+"api/staffs";
    static public String customerUrlStr=baseUrlStr+"api/customers";
    static public String imgBaseUrlStr = "https://easybuy-products.s3-ap-southeast-1.amazonaws.com";
    static public String tokenStr = "token";
    static public String Language= "language";
    static public String psdChangeStr=baseUrlStr+"api/users/changePassword";
    static public String dealStr=baseUrlStr+"api/orders";
    static public String updateIndexUrlStr=baseUrlStr+"api/collects/update_index";

    public static final int LoginIntent = 90;
    public static final int UPEMPLOYER_INTENT = 91;
    public static final int UPECANDIDATE_INTENT = 92;
    public static final int UPDATASHOUCANGLIST = 93;
    public static final int ADDFEEDBACK = 94;
    public static final int UPDATASEARCHLIST=95;
    public static final int SEARCHLISTGOTOCART=96;
    public static final int GOTOCART=97;
    public static final int DETAILGOTOCART=97;
    public static final int SEARCHTOCART=98;
    public static final int UPDATEADDRESSINTENT=99;
    public static final int SEARCHTODETAIL=1000;
    static public final int SHIPPINGADDRESS=89;
    static public final int NEWSHIPPINGADDRESS=88;
    static public final int LANGUAGE=80;
    public static final String FILE = "E:/test.pdf";

    public static void setToken(Context context, String token) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        GlobalProvider globalProvider = GlobalProvider.getInstance(context);
        if (token.equals("")) {
            editor.putString(tokenStr, token);
            globalProvider.setToken(token);
        }else {
            editor.putString(tokenStr, "Bearer " + token);
            globalProvider.setToken("Bearer " + token);
        }
        Log.v("err", tokenStr);
        editor.commit();

    }

    public static String getToken(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        String auth_token_string = settings.getString(tokenStr, ""/*default value is ""*/);
        Log.v("err", tokenStr);
        // Log.d("mytoken",auth_token_string);
        return auth_token_string;
    }
    public static void setLanguage(Context context,String language) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
//        GlobalProvider globalProvider = GlobalProvider.getInstance();
//        if (token.equals("")) {
//            editor.putString(tokenStr, token);
//            globalProvider.setToken(token);
//        }else {
        editor.putString(Language, language);
//            globalProvider.setToken("Bearer " + token);
//        }
//        Log.v("err", tokenStr);
        editor.commit();
    }

    public static String getGetLanguage(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);

        String language_str = settings.getString(Language,"english");
        //Log.v("err", tokenStr);
        return language_str;
    }
}
