package com.samiapps.kv.myapplication;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by KV on 20/2/18.
 */

public class MyApplication extends Application {
    public static MyApplication ma;


    public static MyApplication getInstance()
    {

        return ma;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ma =this;
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {

                // new activity created; force its orientation to portrait
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }


        });
    }
    public void change(String str){
        Configuration config = getResources().getConfiguration();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        if(str.equals("english")) {
            config.locale = Locale.ENGLISH;
        }else{
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        getResources().updateConfiguration(config, dm);

    }
}
