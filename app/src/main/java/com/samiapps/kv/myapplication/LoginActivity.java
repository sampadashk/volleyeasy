package com.samiapps.kv.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.samiapps.kv.myapplication.Constants.loginUrlStr;

/**
 * Created by KV on 20/2/18.
 */

public class LoginActivity extends AppCompatActivity {


    private EditText email, psd;
    public ImageView deleteEmail;
    public ImageView deletePsd;
    public ImageView contactUs;
    GlobalProvider globalProvider;
    // public RecyclerView gdrecycler;
    private int a = 0;
    private Button sign_in_button, tourist_in_button;
    private List<String> history = new ArrayList<String>();
    private String usernameStr, newVersion = "x";
    public static String character = "character";
    //  public ImageView mail,phone;
    //创建onCreate（）方法，绑定布局activity_login
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;
    private ProgressDialog dialog;
    public Thread thread;
    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo == null) {
                    Toast.makeText(LoginActivity.this, "网络链接不可用！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    /*

    public void loadVersion() {

        globalProvider.get(getApplication(), Constants.versionUrlStr, new RequestListener() {
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //  Log.d("getloginresponse",new String(responseBody));
                parseVersion(new String(responseBody));
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Log.v("err", new String(responseBody));
            }

            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

            }
        });
    }
    */





    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        globalProvider = GlobalProvider.getInstance(LoginActivity.this);
        // loadVersion();
//        try{
//            String version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
//            if( ! version.equals(newVersion)){
//                Toast.makeText(this, "New version available on play store", Toast.LENGTH_SHORT);
//            }
//        }catch (PackageManager.NameNotFoundException e){
//            //No version do something
//        }
        // gdrecycler=(RecyclerView) findViewById(R.id.grid_recycler);
        //ArrayList<Integer> mThumbIds = new ArrayList(Arrays.asList( R.drawable.l1, R.drawable.l2,
        // R.drawable.l3, R.drawable.l4));
        // RecyclerImgAdapter recyclerImgAdapter=new RecyclerImgAdapter(LoginActivity.this,mThumbIds);
        //gdrecycler.setAdapter(recyclerImgAdapter);
        //GridLayoutManager gd=new GridLayoutManager(this,2);
        // gdrecycler.setLayoutManager(gd);

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
        //找到对象email、psd、sign_in_button并绑定监听事件
        email = (EditText) findViewById(R.id.email);
        psd = (EditText) findViewById(R.id.psd);
        contactUs = (ImageView) findViewById(R.id.contact_us);
        sign_in_button = (Button) findViewById(R.id.sign_in_button);
        tourist_in_button = (Button) findViewById(R.id.tourist_in_button);
        deleteEmail = (ImageView) findViewById(R.id.deleteEmail);
        deletePsd = (ImageView) findViewById(R.id.deletePsd);
        //  gview=(GridView) findViewById(R.id.grid_layout);
        // ImageAdapter imgAdapter=new ImageAdapter(LoginActivity.this);
        //gview.setAdapter(imgAdapter);
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
     /*  mail= (ImageView) findViewById(R.id.mail);
        phone= (ImageView) findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.CALL");
                intent.setData(Uri.parse("tel:" + "6591245862"));
                startActivity(intent);
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SendEmail.class);
                startActivity(intent);
            }
        });
        */
        deleteEmail.setVisibility(View.GONE);
        deletePsd.setVisibility(View.GONE);
        try {
            findHistoryList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    deleteEmail.setVisibility(View.VISIBLE);
                } else {
                    deleteEmail.setVisibility(View.GONE);
                }
            }
        });
        psd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    deletePsd.setVisibility(View.VISIBLE);
                } else {
                    deletePsd.setVisibility(View.GONE);
                }
            }
        });
        deleteEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                email.setText("");
            }
        });
        deletePsd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                psd.setText("");
            }
        });
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (email.getText() == null || email.getText().toString().equals("") || psd.getText() == null || email.getText().toString().equals("")) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setMessage(getString(R.string.notempty))
                            .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                } else {
                    try {
                        setHistoryList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setMessage(getString(R.string.loging));
                    dialog.show();
                    sign_in_button.setEnabled(false);
                    loginAction(v);
                }
            }
        });
        //为sign_in_button绑定监听事件，调用loginAction(v)
        tourist_in_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage(getString(R.string.loging));
                dialog.show();
                sign_in_button.setEnabled(false);
                loginActionTourist(v);
            }
        });
    }

    public static void setCharacter(Context context, String cha) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(character, cha);
        editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        String cha = settings.getString(character, ""/*default value is ""*/);
        //Log.v("err", tokenStr);
        return cha;
    }

    //创建loginAction（）方法
    public void loginAction(View view) {
        //分别把email、psd的值传递给usernameStr、passwordStr
        final String usernameStr = email.getText().toString();
        String passwordStr = psd.getText().toString();
        Log.d("checkentries",usernameStr+" "+passwordStr);
        setCharacter(this, "user");
        // Log.d("chkpassword",passwordStr);
        //GlobalProvider.getInstance().character =
        Map<String, String> params = new HashMap<>();


        params.put("password", passwordStr);
        params.put("email",usernameStr );


        CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.POST, loginUrlStr, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("responsevolley", response.getString("token"));
                    dialog.dismiss();
                    globalProvider.IsLoging = true;
                    sign_in_button.setEnabled(true);

                    String token;
                    token = response.getString("token");
                    token = token.replaceAll("\"", "");//把token中的"\""全部替换成""
                    Constants.setToken(LoginActivity.this, token);
                    globalProvider.isLogined=true;
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("isLogin", usernameStr);
                    startActivity(intent);
                    //this.setResult(Activity.RESULT_OK);//为结果绑定Activity.RESULT_OK
                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorvolley", error.toString());

                dialog.dismiss();
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage(getString(R.string.errorId))
                        .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                sign_in_button.setEnabled(true);

            }
        });


        globalProvider.addRequest(jsonObjectRequest);


    }


    public void loginActionTourist(View view) {

        //分别把email、psd的值传递给usernameStr、passwordStr
//        String usernameStr = "zgct@veg.com";
//        String passwordStr = "00000000";
        usernameStr = "guest@veg.com";
        String passwordStr = "12345678";
        setCharacter(this, "tourist");
        globalProvider.isLogined = true;
        // GlobalProvider.getInstance().character = "tourist";

        // 绑定参数
        Map<String,String> params = new HashMap();
        params.put("email", usernameStr);
        params.put("password", passwordStr);

        CustomRequest customRequest=new CustomRequest(Request.Method.POST, loginUrlStr, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                globalProvider.IsLoging = true;
                tourist_in_button.setEnabled(true);
                String token;
                try {
                    token = response.getString("token");
                    token = token.replaceAll("\"", "");
                    Constants.setToken(LoginActivity.this, token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //把token中的"\""全部替换成""

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("isLogin", usernameStr);
                startActivity(intent);
                //this.setResult(Activity.RESULT_OK);//为结果绑定Activity.RESULT_OK
                finish();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage(getString(R.string.errorId))
                        .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                tourist_in_button.setEnabled(true);

            }
        });
    }

    private void findHistoryList() throws IOException {

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EasybuyCustomer.txt");

        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        //简写如下
        //BufferedReader br = new BufferedReader(new InputStreamReader(
        //        new FileInputStream("E:/phsftp/evdokey/evdokey_201103221556.txt"), "UTF-8"));
//        LishiList.clear();
//        LishiList_Reload.clear();
        String s = null;

        while ((s = br.readLine()) != null) {
            history.add(s);
            //System.out.println(arrs[0] + " : " + arrs[1] + " : " + arrs[2]);
        }
        if (history.size() > 0) {
            email.setText(history.get(0));
            psd.setText(history.get(1));
        }
//        for(int i=0;i<2;i++){
//            LishiList_Reload.add(LishiList.get(LishiList.size()-(i+1)));
//        }
//        for(int i=0;i<LishiList_Reload.size();i++){
//            if(i>2){
//                LishiList_Reload.remove(LishiList_Reload.get(i));
//            }
//        }
        br.close();
        isr.close();
        fis.close();
    }

    public void setHistoryList() throws IOException {

        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EasybuyCustomer.txt");
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);
        //简写如下：
        //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
        //        new FileOutputStream(new File("E:/phsftp/evdokey/evdokey_201103221556.txt")), "UTF-8"));
        bw.write("");
        bw.write(email.getText().toString());
        bw.newLine();
        bw.write(psd.getText().toString());
        bw.newLine();
//        for(int i=0;i<LishiList_Reload.size();i++){
//            bw.write(LishiList_Reload.get(LishiList_Reload.size()-(i+1)));
//            bw.newLine();
//        }
//        bw.write(list);
//        bw.newLine();
        //注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
        bw.close();
        osw.close();
        fos.close();
    }


    /*
    public void parseLoginResult(String json) {
        // 绑定参数
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonParser jsonParser = jsonFactory.createJsonParser(json);
            JsonNode rootNode =  mapper.readTree(jsonParser);
            JsonNode tokenNode = rootNode.path("token");
            String token = tokenNode.toString();
           // Log.d("getvaltoken",token);
            token = token.replaceAll("\"", "");//把token中的"\""全部替换成""
            Constants.setToken(LoginActivity.this, token);//绑定LoginActivity中的token
            //Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
            //显示token信息
            Log.v("err", token);
            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("isLogin",usernameStr);
            startActivity(intent);
            //this.setResult(Activity.RESULT_OK);//为结果绑定Activity.RESULT_OK
            finish();//完成
        } catch (IOException e) {
            e.printStackTrace();
        }//当try中代码发生错误时，就会返回所写异常的处理

    }
    */

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {


            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {

                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //按下键盘上返回按钮
        if(keyCode == KeyEvent.KEYCODE_BACK){

            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.youconfirmtologout))
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            a=a+1;
                            finish();

                        }
                    }).show();

            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(myNetReceiver);
        if(a>0){
            System.exit(0);
        }
        a=0;
        //或者下面这种方式
        //android.os.Process.killProcess(android.os.Process.myPid());
    }


}
