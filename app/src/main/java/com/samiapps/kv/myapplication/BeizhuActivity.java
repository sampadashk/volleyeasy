package com.samiapps.kv.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by KV on 27/2/18.
 */
//TODO finish this activity
public class BeizhuActivity extends BaseActivity{

    //声明对象back
    private LinearLayout back;
    private EditText beizhu;
    private LinearLayout save;
    private String Str="";
    private InputMethodManager manager;
    GlobalProvider globalProvider;

    //重写onCreate（）方法，绑定布局activity_beizhu，找到对象back并绑定监听事件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beizhu);
        back= (LinearLayout) findViewById(R.id.back);
        beizhu= (EditText) findViewById(R.id.ywfw);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        globalProvider=GlobalProvider.getInstance(BeizhuActivity.this);


        Intent intent=this.getIntent();
        if(intent.getStringExtra("beizhu")!=null){
            Str=intent.getStringExtra("beizhu");
        }
        if(Str!=null&&Str.length()>0){

            beizhu.setText(Str);
        }

        save= (LinearLayout) findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("return", beizhu.getText().toString());
                globalProvider.orders.get(globalProvider.whichOrder).setFeedback(beizhu.getText().toString());
                setResult(RESULT_OK);
                finish();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BeizhuActivity.this)
                        .setMessage(getString(R.string.iftosave))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent();
//                                intent.putExtra("return", beizhu.getText().toString());
                                //globalProvider.feedback_Str=beizhu.getText().toString();
                                globalProvider.orders.get(globalProvider.whichOrder).setFeedback(beizhu.getText().toString());
                                setResult(RESULT_OK);
                                finish();
                            }
                        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }

                }).show();

            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // TODO Auto-generated method stub
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
}