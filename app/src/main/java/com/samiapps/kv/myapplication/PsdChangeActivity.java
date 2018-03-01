package com.samiapps.kv.myapplication;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Created by KV on 21/2/18.
 */

public class PsdChangeActivity extends BaseActivity{
    public EditText newPassword;
    public EditText oldPassword;
    public EditText confirm_psd;
    public Button ok;
    public LinearLayout back;
    private ConnectivityManager mConnectivityManager;

    private NetworkInfo netInfo;


    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if(netInfo == null) {
                    Toast.makeText(PsdChangeActivity.this, "网络链接不可用！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(myNetReceiver);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psd_change);
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
        back= (LinearLayout) findViewById(R.id.back);
        newPassword= (EditText) findViewById(R.id.newPassword);
        oldPassword= (EditText) findViewById(R.id.oldPassword);
        confirm_psd= (EditText) findViewById(R.id.confirm_psd);
        ok= (Button) findViewById(R.id.ok);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doChange();
            }
        });
    }
    public void doChange(){
        if(oldPassword.getText()==null||oldPassword.getText().toString().length()==0||newPassword.getText()==null||newPassword.getText().toString().length()==0||confirm_psd.getText()==null||confirm_psd.getText().toString().length()==0){
            new AlertDialog.Builder(PsdChangeActivity.this)
                    .setMessage(getString(R.string.notempty))
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    }).show();
            return;
        }
        String a=newPassword.getText().toString();
        String b=confirm_psd.getText().toString();
        if(!newPassword.getText().toString().equals(confirm_psd.getText().toString())){
            new AlertDialog.Builder(PsdChangeActivity.this)
                    .setMessage(getString(R.string.twicenotsame))
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    }).show();
            return;
        }
        //分别把email、psd的值传递给usernameStr、passwordStr
        String oldPassword_str = oldPassword.getText().toString();
        String newPassword_str = newPassword.getText().toString();

        // 绑定参数

        ChangePsdBody body=new ChangePsdBody();
        body.setOldPassword(oldPassword_str);
        body.setNewPassword(newPassword_str);

        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = "";
        //TODO CHANGEPWD
        /*
        try {
            json = ow.writeValueAsString(body);

            ByteArrayEntity entity= new ByteArrayEntity(json.getBytes("UTF-8"));

            globalProvider.put(this, Constants.psdChangeStr, entity, "application/json", new RequestListener() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(PsdChangeActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                    Constants.setToken(PsdChangeActivity.this, "");
                    GlobalProvider.getInstance().shangpingList.clear();
                    Intent intent = new Intent(PsdChangeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    new AlertDialog.Builder(PsdChangeActivity.this)
                            .setMessage(getString(R.string.failtoupdate))
                            .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            }).show();

                }
                @Override
                public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

}

