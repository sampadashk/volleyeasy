package com.samiapps.kv.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by KV on 21/2/18.
 */

public class MyAccountActivity extends BaseActivity{
    public TextView turn_text;
    public TextView name;
    public TextView email;
    public TextView clickToChange;
    GlobalProvider globalProvider;
    public Me me;
    public LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        globalProvider=GlobalProvider.getInstance(MyAccountActivity.this);
        me=globalProvider.me;
        turn_text= (TextView) findViewById(R.id.turn_text);
        name= (TextView) findViewById(R.id.name);
        email= (TextView) findViewById(R.id.email);
        clickToChange= (TextView) findViewById(R.id.clickToChangePsd);
        back= (LinearLayout) findViewById(R.id.back);


        clickToChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyAccountActivity.this,PsdChangeActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(me!=null&&me.name!=null){
            turn_text.setText(me.name);
            name.setText(me.name);
        }
        if (me!=null&&me.email!=null){
            email.setText(me.email);
        }


    }
}