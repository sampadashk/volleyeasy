package com.samiapps.kv.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE =123 ;
    private LinearLayout mTab_shangping;
    private LinearLayout mTab_dingdan;
    private LinearLayout mTab_wode;
    private LinearLayout mTab_cart;
    private LinearLayout mTab_collect;
    private CircleBadgeView buyNumView;
    //private static int num=0;

    private ImageView shangping_Img;
    private ImageView cart_Img;
    private ImageView dingdan_Img;
    private  ImageView wode_Img;

    private TextView one;
    private  TextView two;
    private TextView three;
    private  TextView four;
    public String Str_text="";


    private InputMethodManager manager;
    private TextView cart_num;
    private LinearLayout cart_tab;
    private  int DEFAULT_LR_PADDING_DIP = 5;
    GlobalProvider globalProvider;

    private int[] end_location;
    //private  FragmentShangping shangping_fragment;
    public FragmentContract fragment_contract;
   // public FragmentProduct fragment_product;
    private int a=0,index=0;
    public int item=0;
    public static String character="character";
    private ConnectivityManager mConnectivityManager;
    private FragmentProduct fragment_product;

    private NetworkInfo netInfo;
    private static MainActivity defaultActivity;

    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if(netInfo == null) {
                    Toast.makeText(MainActivity.this, "网络链接不可用！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    public static MainActivity getDefault() {


        return defaultActivity;
    }
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);


    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d("checkpermission"," "+permissionCheck);
        if(permissionCheck==-1)
        {
            requestPermission();
        }

        defaultActivity = this;

        globalProvider=GlobalProvider.getInstance(MainActivity.this);
        Log.d("checkb",globalProvider.isLogined+"");
        Log.d("checkt",getToken(this));
        //getToken(this).equals("tourist")&&
       /* if(getToken(this).equals("tourist")&&!GlobalProvider.getInstance().isLogined){
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            return;
        }
        */

        if(!globalProvider.isLogined){
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            return;
        }

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        shangping_Img= (ImageView) findViewById(R.id.shangping_Img);
        cart_Img= (ImageView) findViewById(R.id.cart_Img);
        dingdan_Img= (ImageView) findViewById(R.id.dingdan_Img);
        wode_Img= (ImageView) findViewById(R.id.wode_Img);
        //cart_num= (TextView) findViewById(R.id.cart_num);
        cart_tab= (LinearLayout) findViewById(R.id.cart_tab);

        one= (TextView) findViewById(R.id.one);
        two= (TextView) findViewById(R.id.two);
        three= (TextView) findViewById(R.id.three);
        four= (TextView) findViewById(R.id.four);
        initView();
        initEvent();
        Intent intent = this.getIntent();
        if(intent.getSerializableExtra("goToComfirm")!=null) {
            if(globalProvider.singal==2){
                globalProvider.singal=6;
            }
            setSelect(2);
            return;
        }
        if(globalProvider.singal==0){
            globalProvider.singal=6;
        }
        setSelect(0);
        if(intent.getSerializableExtra("isLogin")!=null){
            loadDelivered();
        }
        if(intent.getSerializableExtra("language") != null){
            setSelect(3);
        }
        if(intent.getSerializableExtra("l") != null){
            setSelect(0);
            return;
        }
        if(intent.getSerializableExtra("singal")!=null){
            if(globalProvider.singal==5){
                globalProvider.singal=6;
            }
            setSelect(5);
            globalProvider.adjustToStart = "product";
        }
        Log.v("err", Constants.getToken(this));
    }

    private void loadDelivered(){
        Map<String,String> params1=new HashMap();
        params1.put("status","delivered");

        CustomRequest customRequest=new CustomRequest(Request.Method.GET, Constants.orderListUrlStr, params1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("successvolley","success");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorvolley",error.toString());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                String token= Constants.getToken(MainActivity.this);
                Log.d("gettoken",token);
                headers.put("Authorization",token);
                return headers;
            }
        }


                ;
        globalProvider.addRequest(customRequest);



/*

        RequestParams params1=new RequestParams();
        params1.put("status","delivered");
//        if (contract!=null){
//            params1.put("_supplier",contract._supplier);
//        }
        GlobalProvider globalProvider = GlobalProvider.getInstance();
        globalProvider.get(this, Constants.orderListUrlStr, params1, new RequestListener() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonParser jsonParser = jsonFactory.createJsonParser(new String(responseBody));
                    final OrderList orderList = (OrderList) objectMapper.readValue(jsonParser, OrderList.class);
                    if (orderList.orders.size() > 0) {
                        new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.tongzhi))
                                .setMessage(getString(R.string.youhave) + orderList.orders.size() + getString(R.string.pieces))
                                .setNegativeButton(getString(R.string.onekeyconfirm), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < orderList.orders.size(); i++) {
                                            ConfirmReceived(orderList.orders.get(i));
                                        }
                                    }
                                })
                                .setPositiveButton(getString(R.string.gotoconfirm), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        MainActivity.getDefault().setSelect(2);
                                        MainActivity.getDefault().item = 2;
                                    }
                                }).show();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Log.v("err", new String(responseBody));
            }

            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

            }
        });
        */
    }
    //update by sampada removing function change
    /*

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
    */
//TODO UNDO COMMENT
    /*
    public void ConfirmReceived(Order order){
        RequestParams params = new RequestParams();
        params.put("status", "received");
        GlobalProvider globalProvider = GlobalProvider.getInstance();
        String url_l = Constants.orderListUrlStr + "/" + order._id;
        globalProvider.put(this, url_l, params, new RequestListener() {
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //String result_l = new String(responseBody);
                //loadOrderList();
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(getActivity(), new String(responseBody), Toast.LENGTH_SHORT).show();
            }

            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

            }
        });
    }
    */

    private void initEvent() {
        mTab_shangping.setOnClickListener(this);
        mTab_dingdan.setOnClickListener(this);
        mTab_wode.setOnClickListener(this);
        mTab_cart.setOnClickListener(this);
        mTab_collect.setOnClickListener(this);
    }

    private void initView() {
        ImageView Image=(ImageView)findViewById(R.id.cart_Img);
        mTab_shangping= (LinearLayout) findViewById(R.id.mTab_shangping);
        mTab_dingdan= (LinearLayout) findViewById(R.id.mTab_dingdan);
        mTab_wode=(LinearLayout)findViewById(R.id.mTab_wode);
        mTab_cart= (LinearLayout) findViewById(R.id.mTab_cart);
        mTab_collect=(LinearLayout)findViewById(R.id.mTab_collect);
        buyNumView = new CircleBadgeView(this, Image);
        buyNumView.setTextColor(Color.WHITE);
        buyNumView.setBackgroundColor(Color.RED);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
                v.clearFocus();
            }
        }
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.LoginIntent:
                if (resultCode == RESULT_OK) {
                    if(globalProvider.shangpingList.size()>0){
                        setCartNum();
                    }else{
                        HideCartNum();
                    }
                    if(globalProvider.singal==0){
                        globalProvider.singal=6;
                    }
                    setSelect(0);
                }
                break;
            case Constants.LANGUAGE:
                setSelect(3);
                break;
            case Constants.DETAILGOTOCART:
                if (resultCode == RESULT_OK) {
                    if(globalProvider.singal==1){
                        globalProvider.singal=6;
                    }
                    setSelect(1);
                }else if(resultCode == 111){
                    if(globalProvider.singal==2){
                        globalProvider.singal=6;
                    }
                    setSelect(2);
                }else{
                    if(globalProvider.singal==5){
                        globalProvider.singal=6;
                    }
                    setSelect(5);
                }
                break;
            case Constants.UPEMPLOYER_INTENT:
                if (resultCode == RESULT_OK) {
                    if(globalProvider.singal==0){
                        globalProvider.singal=6;
                    }
                    setSelect(0);
                }
                break;
            case Constants.UPECANDIDATE_INTENT:
                if (resultCode == RESULT_OK) {
                    if(globalProvider.singal==0){
                        globalProvider.singal=6;
                    }
                    setSelect(0);
                }
                break;
            case Constants.UPDATASHOUCANGLIST:
                if(resultCode == RESULT_OK){
                    if(globalProvider.singal==0){
                        globalProvider.singal=6;
                    }
                    setSelect(0);

                }else if(resultCode == 101){
                    if(globalProvider.singal==1){
                        globalProvider.singal=6;
                    }
                    setSelect(1);
                }
//                if(GlobalProvider.getInstance().shangpingList.size()>0){
//
//                    setCartNum();
//
//                }
                break;
            case Constants.ADDFEEDBACK:
                if(resultCode == RESULT_OK){
                    if(globalProvider.singal==1){
                        globalProvider.singal=6;
                    }
                    setSelect(1);
                }
                break;
            case Constants.UPDATASEARCHLIST:
                if(resultCode==RESULT_OK){
                    if(globalProvider.singal==5){
                        globalProvider.singal=6;
                    }
                    setSelect(5);
                }else if(resultCode==Constants.SEARCHLISTGOTOCART){

                    if(globalProvider.singal==2){
                        globalProvider.singal=6;
                    }
                    setSelect(2);
                }else if(resultCode==Constants.GOTOCART){
                    if(globalProvider.singal==1){
                        globalProvider.singal=6;
                    }
                    setSelect(1);
                }else if(resultCode==111) {
                    if(globalProvider.singal==2){
                        globalProvider.singal=6;
                    }
                    setSelect(2);
                }
                break;
            case Constants.NEWSHIPPINGADDRESS:
                if(resultCode == RESULT_OK){
                    if(globalProvider.singal==1){
                        globalProvider.singal=6;
                    }
                    setSelect(1);
                }
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void gotoLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        //intent.putExtra("hospParam", hospParam);
        this.startActivity(intent);
    }

    public void setSelect(int i){
        FragmentManager fm=getFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        //hide(transaction);
        switch(i){
            case 0:
                if(globalProvider.singal==0){
                    return;
                }
                globalProvider.singal=0;
                FragmentContract fragment_contract = new FragmentContract();
                transaction.replace(R.id.main, fragment_contract);
                shangping_Img.setImageResource(R.drawable.shangping_pressed);
                cart_Img.setImageResource(R.drawable.cart);
                dingdan_Img.setImageResource(R.drawable.dingdan);
                wode_Img.setImageResource(R.drawable.my_info);
                one.setTextColor(0xffee2400);
                two.setTextColor(0xff666666);
                three.setTextColor(0xff666666);
                four.setTextColor(0xff666666);
                break;
            case 1:
                if(globalProvider.singal==1){
                    return;
                }
                globalProvider.singal=1;
               FragmentCart cart_fragment=new FragmentCart();
               transaction.replace(R.id.main, cart_fragment);
                shangping_Img.setImageResource(R.drawable.shangping);
                cart_Img.setImageResource(R.drawable.cart_pressed);
                dingdan_Img.setImageResource(R.drawable.dingdan);
                wode_Img.setImageResource(R.drawable.my_info);
                one.setTextColor(0xff666666);
                two.setTextColor(0xffee2400);
                three.setTextColor(0xff666666);
                four.setTextColor(0xff666666);
                break;
            case 2:
                if(globalProvider.singal==2){
                    return;
                }
                globalProvider.singal=2;
              // FragmentOrder dingdan_fragment=new FragmentOrder();
               //transaction.replace(R.id.main, dingdan_fragment);
                shangping_Img.setImageResource(R.drawable.shangping);
                cart_Img.setImageResource(R.drawable.cart);
                dingdan_Img.setImageResource(R.drawable.dingdan_pressed);
                wode_Img.setImageResource(R.drawable.my_info);
                one.setTextColor(0xff666666);
                two.setTextColor(0xff666666);
                three.setTextColor(0xffee2400);
                four.setTextColor(0xff666666);
                break;
            case 3:
                if(globalProvider.singal==3){
                    return;
                }
                globalProvider.singal=3;
               FragmentWode wode_fragment=new FragmentWode();
                transaction.replace(R.id.main,wode_fragment);
                shangping_Img.setImageResource(R.drawable.shangping);
                cart_Img.setImageResource(R.drawable.cart);
                dingdan_Img.setImageResource(R.drawable.dingdan);
                wode_Img.setImageResource(R.drawable.my_info_pressed);
                one.setTextColor(0xff666666);
                two.setTextColor(0xff666666);
                three.setTextColor(0xff666666);
                four.setTextColor(0xffee2400);
                break;
            case 4:
                if(globalProvider.singal==4){
                    return;
                }
                globalProvider.singal=4;
                FragmentCollect collect_fragment=new FragmentCollect();
                transaction.replace(R.id.main,collect_fragment);
                shangping_Img.setImageResource(R.drawable.shangping);
                cart_Img.setImageResource(R.drawable.cart);
                dingdan_Img.setImageResource(R.drawable.dingdan);
                wode_Img.setImageResource(R.drawable.my_info);
                one.setTextColor(0xff666666);
                two.setTextColor(0xff666666);
                three.setTextColor(0xff666666);
                four.setTextColor(0xff666666);
                break;
            case 5:
                if(globalProvider.singal==5){
                    return;
                }
                globalProvider.singal=5;
               if(fragment_product==null){
                    fragment_product=new FragmentProduct();
                }
                transaction.replace(R.id.main,fragment_product);

                shangping_Img.setImageResource(R.drawable.shangping_pressed);
                cart_Img.setImageResource(R.drawable.cart);
                dingdan_Img.setImageResource(R.drawable.dingdan);
                wode_Img.setImageResource(R.drawable.my_info);
                one.setTextColor(0xffee2400);
                two.setTextColor(0xff666666);
                three.setTextColor(0xff666666);
                four.setTextColor(0xff666666);
        }
        transaction.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        String cha = settings.getString(character, ""/*default value is ""*/);
        return cha;
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mTab_shangping:
                if(globalProvider.adjustToStart.equals("contract")){
                    setSelect(0);
                }else{
                    setSelect(5);
                }
                break;
            case R.id.mTab_dingdan:
                if(getToken(this).equals("tourist")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("");
                    builder.setMessage(getString(R.string.noquanxian));
                    builder.setPositiveButton(getString(R.string.confirm), null);
                    builder.show();
                }else{
                    setSelect(2);
                }
                break;
            case R.id.mTab_wode:
                setSelect(3);
                break;

            case R.id.mTab_cart:
                if(getToken(this).equals("tourist")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("");
                    builder.setMessage(getString(R.string.noquanxian));
                    builder.setPositiveButton(getString(R.string.confirm), null);
                    builder.show();
                }else{

                    setSelect(1);

                }
                break;

            case R.id.mTab_collect:
                if(getToken(this).equals("tourist")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("");
                    builder.setMessage(getString(R.string.noquanxian));
                    builder.setPositiveButton(getString(R.string.confirm), null);
                    builder.show();
                }else{

                    setSelect(4);

                }
        }


    }
    public void setCartNum() {

        buyNumView.setText(globalProvider.contractListToCart.size() + "");//

        if(globalProvider.contractListToCart.size()>=10){

            DEFAULT_LR_PADDING_DIP = 3;
            int paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP);
            buyNumView.setPadding(paddingPixels, 0,paddingPixels, 0);
            buyNumView.setTextSize(10);

        } else {
            DEFAULT_LR_PADDING_DIP = 5;
            int paddingPixels_ = dipToPixels(DEFAULT_LR_PADDING_DIP);
            buyNumView.setPadding(paddingPixels_, 0,paddingPixels_, 0);
            buyNumView.setTextSize(11);

        }

        //buyNumView.setTextSize(12);
        buyNumView.setBadgePosition(CircleBadgeView.POSITION_TOP_RIGHT);
        buyNumView.setGravity(Gravity.CENTER);
        buyNumView.show();


    }

    public void HideCartNum(){
        buyNumView.hide();

    }
    private int dipToPixels(int dip) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //按下键盘上返回按钮
        if(keyCode == KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.youconfirmtologout))
                    .setNegativeButton(getString(R.string.errorclick), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            a=a+1;
//                            GlobalProvider.getInstance().shangpingList.clear();
//                            GlobalProvider.getInstance().shangpingListDefault.clear();
                            //GlobalProvider.getInstance().IsLogingOut=true;
                            //Toast.makeText(MainActivity.this, "you have successfully logged out", Toast.LENGTH_SHORT).show();
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
        try{
            this.unregisterReceiver(myNetReceiver);
        }catch (Exception e) {

        }
//        this.unregisterReceiver(myNetReceiver);
        if(defaultActivity!=null){
            defaultActivity = null;
        }
        if(a>0){
            System.exit(0);
        }
        a=0;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                } else {



                }
                break;
        }
    }
    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){



        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }
}
