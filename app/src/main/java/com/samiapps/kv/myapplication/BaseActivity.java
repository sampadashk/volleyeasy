package com.samiapps.kv.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KV on 21/2/18.
 */

public class BaseActivity extends Activity {
    GlobalProvider globalProvider=GlobalProvider.getInstance(BaseActivity.this);
    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            globalProvider.isActive = false;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (! globalProvider.isActive) {
            loadDelivered();
            globalProvider.isActive = true;
        }
    }
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
    private void loadDelivered() {
        Map<String,String> params1 = new HashMap<>();
        params1.put("status", "delivered");
//        if (contract!=null){
//            params1.put("_supplier",contract._supplier);
//        }

        CustomRequest request=new CustomRequest(Request.Method.GET, Constants.orderListUrlStr,params1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                String token = Constants.getToken(getApplicationContext());
                Log.d("gettoken", token);
                headers.put("Authorization",token);
                return headers;
            }
        };
        globalProvider.addRequest(request);

    }








    public void ConfirmReceived(Order order){

        Map<String,String> params = new HashMap<>();
        params.put("status", "received");


        String url_l = Constants.orderListUrlStr + "/" + order._id;
        CustomRequest customRequest=new CustomRequest(Request.Method.GET, url_l, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                String token = Constants.getToken(getApplicationContext());
                Log.d("gettoken", token);
                headers.put("Authorization", token);
                return headers;
            }
        };

       globalProvider.addRequest(customRequest);
    }
    public String  getCurrentActivityName(){
        ActivityManager activityManager = (ActivityManager)  getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> forGroundActivity = activityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo currentActivity;
        currentActivity = forGroundActivity.get(0);
        String activityName = currentActivity.topActivity.getClassName();
        return activityName;
    }
    public static Activity getGlobleActivity() throws ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException{
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);
        Map activities = (Map) activitiesField.get(activityThread);
        for(Object activityRecord:activities.values()){
            Class activityRecordClass = activityRecord.getClass();
            Field pausedField = activityRecordClass.getDeclaredField("paused");
            pausedField.setAccessible(true); if(!pausedField.getBoolean(activityRecord)) {
                Field activityField = activityRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                Activity activity = (Activity) activityField.get(activityRecord);
                return activity;
            }
        }
        return null;
    }
}
