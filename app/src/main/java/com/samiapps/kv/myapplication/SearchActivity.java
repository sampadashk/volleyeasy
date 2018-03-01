package com.samiapps.kv.myapplication;

import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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

import cz.msebera.android.httpclient.entity.ByteArrayEntity;

/**
 * Created by KV on 28/2/18.
 */

public class SearchActivity extends BaseActivity {


    private static List<String> name;
    private String Str;
    public Contract contract;
    private SearchListAdapter adapter;

    private SearchHistoryAdapter mAdapter;
    private List<Product> list;

    private List<String> LishiList;
    private List<String> LishiList_Reload;


    private ListView search_list;
    private ListView list_search_lishi;
    private  String mName;
    private ImageView search_back;
    private ImageView Img_gotocart;
    private EditText search_edit;

    private LinearLayout search_lishi;
    private LinearLayout search_layout;

    private RelativeLayout delete_history;
    private RelativeLayout none_layout;
    private RelativeLayout GoToCart;
    private Button Submit;

    private ConnectivityManager mConnectivityManager;
    public static String character="character";
    private NetworkInfo netInfo;
    GlobalProvider globalProvider;


    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if(netInfo == null) {
                    Toast.makeText(SearchActivity.this, "网络链接不可用！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    public static String getToken(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        String cha = settings.getString(character, ""/*default value is ""*/);
        //Log.v("err", tokenStr);
        return cha;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
        Intent intent=this.getIntent();
        if(intent.getSerializableExtra("contract")!=null) {
            contract = (Contract) intent.getSerializableExtra("contract");
        }
        globalProvider=GlobalProvider.getInstance(SearchActivity.this);
        search_back= (ImageView) findViewById(R.id.search_back);
        Submit= (Button) findViewById(R.id.submit);
        Img_gotocart= (ImageView) findViewById(R.id.Img_gotocart);
        GoToCart= (RelativeLayout) findViewById(R.id.goToCart);

        search_edit= (EditText) findViewById(R.id.search_edit);


        search_list= (ListView) findViewById(R.id.search_list);
        list_search_lishi= (ListView) findViewById(R.id.list_search_lishi);

        search_lishi= (LinearLayout) findViewById(R.id.search_lishi);

        none_layout= (RelativeLayout) findViewById(R.id.none_layout);
        search_layout= (LinearLayout) findViewById(R.id.search_layout);

        delete_history= (RelativeLayout) findViewById(R.id.delete_history);
        list=new ArrayList<Product>();
        LishiList=new ArrayList<String>();
        LishiList_Reload=new ArrayList<String>();
        try {
            findHistoryList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter=new SearchListAdapter(this,list);
        mAdapter=new SearchHistoryAdapter(this,LishiList_Reload);
        search_list.setAdapter(adapter);
        list_search_lishi.setAdapter(mAdapter);

        search_lishi.setVisibility(View.VISIBLE);
        search_layout.setVisibility(View.GONE);
        Img_gotocart.setVisibility(View.INVISIBLE);
        GoToCart.setVisibility(View.INVISIBLE);

        list_search_lishi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                search_edit.setText(LishiList_Reload.get(position));
            }
        });
        delete_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SearchActivity.this).setTitle(getString(R.string.xitongtishi))
                        .setMessage(getString(R.string.confirmtodeletelishi))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                try {
                                    FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/lishi.txt");
                                    OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                                    BufferedWriter bw = new BufferedWriter(osw);
                                    //简写如下：
                                    //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                                    //        new FileOutputStream(new File("E:/phsftp/evdokey/evdokey_201103221556.txt")), "UTF-8"));
                                    bw.write("");
                                    bw.close();
                                    osw.close();
                                    fos.close();
                                }catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //注意关闭的先后顺序，先打开的后关闭，后打开的先关闭

                                try {
                                    findHistoryList();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mAdapter.notifyDataSetChanged();

                            }
                        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // TODO Auto-generated method stub

                        // Log.i("alertdialog", " 请保存数据！");
                    }

                }).show();

            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getToken(SearchActivity.this).equals("tourist")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    builder.setTitle("");
                    builder.setMessage(getString(R.string.noquanxian));
                    builder.setPositiveButton(getString(R.string.confirm), null);
                    builder.show();
                }else{
                    if (!search_edit.getText().toString().equals("")) {
                        int m = 0;
                        for (int i = 0; i < LishiList_Reload.size(); i++) {
                            if (search_edit.getText().toString().equals(LishiList_Reload.get(i))) {
                                m = m + 1;
                            }
                        }
                        if (m == 0) {
                            try {
                                //LishiList_Reload.add(search_edit.getText().toString());
                                setHistoryList(search_edit.getText().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        setResult(Constants.GOTOCART);
                        MainActivity.getDefault().item=1;
                        finish();
                    }
                }
            }
        });
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                if(!search_edit.getText().toString().equals("")){

                    mName=s.toString();
                    Log.d("checknmentered",mName);

                    try {
                        loadSearchList();
                    } catch (JSONException e) {
                        Log.d("checkj",e.toString());
                        e.printStackTrace();
                    }
                }
                if(search_edit.getText().toString().equals("")){

                    try {
                        findHistoryList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    search_lishi.setVisibility(View.VISIBLE);
                    search_layout.setVisibility(View.GONE);
                    Img_gotocart.setVisibility(View.INVISIBLE);
                    GoToCart.setVisibility(View.INVISIBLE);
                    mAdapter.notifyDataSetChanged();
                    list.clear();
                    adapter.notifyDataSetChanged();
                }

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
//        balProvider.getInstance().shangpingListDefault.clear();
//        List<Date> dataStr=new ArrayList<Date>();
//        String[] feedBack=new String[globalProvider.orders.size()];
//        for(int i=0;i<globalProvider.orders.size();i++){
//            dataStr.add(globalProvider.orders.get(i).getShippingDate());
//            feedBack[i]=globalProvider.orders.get(i).getFeedback();
//        }
        //globalProvider.orders.clear();
        List<OrderSubmit> orderList=new ArrayList<OrderSubmit>();
        //this.shangPingList=globalProvider.shangpingList;
        List<Contract> contractList=globalProvider.contractListToCart;
        for (int i = 0; i < globalProvider.contractListToCart.size(); i++) {
            //globalProvider.orders.add(new OrderSubmit());
            OrderSubmit order = new OrderSubmit();
            order.products = new ArrayList<Product>();
            //Contract contract=globalProvider.contractListToCart.get(i);
            for (int a = 0; a < globalProvider.shangpingList.size(); a++) {
                Product product = globalProvider.shangpingList.get(a);
                if (globalProvider.contractListToCart.get(i)._supplier.equals(product.get_supplier())) {
                    order.products.add(product);
                }
            }
            orderList.add(order);
        }
        for(int i=0;i<globalProvider.orders.size();i++){
            for(int a=0;a<orderList.size();a++){
                if(globalProvider.orders.get(i).products.get(0).get_supplier().equals(orderList.get(a).products.get(0).get_supplier())){
                    orderList.get(a).setShippingDate(globalProvider.orders.get(i).getShippingDate());
                    if(globalProvider.orders.get(i).getFeedback()!=null) {
                        orderList.get(a).setFeedback(globalProvider.orders.get(i).getFeedback());
                    }
                }
            }
        }
//            int m=0;
//            for(int b=0;b<globalProvider.orders.size();b++){
////                if(order.products.size()>0){
//                if(order.products.get(0)._supplier.equals(globalProvider.orders.get(b).products.get(0).get_supplier())){
//                    globalProvider.orders.get(b).products.clear();
//                    globalProvider.orders.get(b).products.addAll(order.products);
//                    orderList.add(globalProvider.orders.get(b));
//                    m=m+1;
//                }
////            }
//            }
        globalProvider.orders.clear();
        globalProvider.orders.addAll(orderList);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(myNetReceiver);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.SEARCHTOCART:
                if (resultCode == RESULT_OK) {
                    setResult(Constants.GOTOCART);
                    finish();
                }else if(resultCode==Constants.SEARCHTODETAIL){
                    try {
                        loadSearchList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    public void loadSearchList(final String mName){

        String url;

        //TODO TRIAL CHECK using utf8
        if(globalProvider.contract!=null&&globalProvider.contract._supplier!=null)
        {
             url=Constants.productListUrlStr+"?"+"_supplier"+"="+contract._supplier+"&"+"main"+"="+"2"+"&"+"name"+"="+mName;
        }
        else
            url=Constants.productListUrlStr+"?"+"main"+"="+"2"+"&"+"name"+"="+mName;

        Log.d("checksearchurl",url);



        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("checkresponse",response);
                parseSearchList(response,mName);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checeee",error.toString());

            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers=globalProvider.addHeaderToken(SearchActivity.this);


                //headers=globalProvider.addHeaderToken(getActivity());
                return headers;
            }

        };
        globalProvider.addRequest(utf8JsonRequest);

        /*

        RequestParams params = new RequestParams();
        params.put("main",2);
        params.put("name", mName);
        if(globalProvider.contract!=null&&globalProvider.contract._supplier!=null)
        {
            params.put("_supplier",globalProvider.contract._supplier);
        }
       
        globalProvider.get(this, Constants.productListUrlStr, params, new RequestListener() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                parseSearchList(new String(responseBody),mName);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Log.v("err", new String(responseBody));
            }

            @Override
            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

            }
        });
        */
    }
    public void parseSearchList(String json,String name){
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonParser jsonParser = jsonFactory.createParser(json);
            ProductList shangpingList = (ProductList) objectMapper.readValue(jsonParser,ProductList.class);
            Log.d("checkps",shangpingList.products.size()+"");
//            if(globalProvider.shangpingList.size()==0){
//                globalProvider.shangpingListSearchDefault=shangpingList.products;
//            }else{
//                globalProvider.shangpingListSearchDefault=shangpingList.products;
//                for(int i=0;i<globalProvider.shangpingListSearchDefault.size();i++){
            Product productSearch=null;

            if(shangpingList.products.size()>0){
                productSearch=shangpingList.products.get(0);


            }else{
                //productSearch=shangpingList.products.get(0);
                //if(!productSearch.getUnit().equals("ctn")&&!productSearch.getUnit().equals("箱")&&!productSearch.getUnit().equals("件")) {
                new AlertDialog.Builder(SearchActivity.this)
                        .setMessage(getString(R.string.Product_Unit))
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                return;
                // }
            }

            for(int a=0;a<globalProvider.shangpingList.size();a++){
                if(globalProvider.shangpingList.get(a).get_id().equals(productSearch.get_id())){
                    productSearch.setOrderQuantity(globalProvider.shangpingList.get(a).getOrderQuantity());
                    //product.setCollected(globalProvider.shangpingList.get(a).getCollected());
                }
            }


//TODO TESTING
            /*
            Intent intent=new Intent(SearchActivity.this, ProductDetailActivity.class);
            intent.putExtra("product",productSearch);
            startActivityForResult(intent, Constants.SEARCHTOCART);
            */

            //}
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void findHistoryList()  throws IOException {

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/lishi.txt");

        FileInputStream fis=new FileInputStream(file);
        InputStreamReader isr=new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        LishiList.clear();
        LishiList_Reload.clear();
        String s=null;

        while ((s = br.readLine())!=null) {
            LishiList.add(s);
            //System.out.println(arrs[0] + " : " + arrs[1] + " : " + arrs[2]);
        }
        for(int i=0;i<LishiList.size();i++){
            LishiList_Reload.add(LishiList.get(LishiList.size()-(i+1)));
        }
        for(int i=0;i<LishiList_Reload.size();i++){
            if(i>2){
                LishiList_Reload.remove(LishiList_Reload.get(i));
            }
        }
        br.close();
        isr.close();
        fis.close();
    }
    public void setHistoryList(String list) throws IOException {

        FileOutputStream fos=new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/lishi.txt");
        OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw=new BufferedWriter(osw);
        //简写如下：
        //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
        //        new FileOutputStream(new File("E:/phsftp/evdokey/evdokey_201103221556.txt")), "UTF-8"));
        bw.write("");
        for(int i=0;i<LishiList_Reload.size();i++){
            bw.write(LishiList_Reload.get(LishiList_Reload.size()-(i+1)));
            bw.newLine();
        }
        bw.write(list);
        bw.newLine();
        //注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
        bw.close();
        osw.close();
        fos.close();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
                v.clearFocus();
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
    public void addcollected(final Product product){
//        RequestParams params = new RequestParams();
//        params.put("_product", _id);
        AddCollectBody body=new AddCollectBody();
        body.set_product(product._id);
        body.set_supplier(product.get_supplier());
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(body);
            ByteArrayEntity entity= new ByteArrayEntity(json.getBytes("UTF-8"));
            JSONObject obj=new JSONObject(json);

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Constants.collectedStr, obj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    for(int i=0;i<list.size();i++) {
                        if (list.get(i).get_id().equals(product._id)) {
                            list.get(i).setCollected("yes");

                        }
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(SearchActivity.this, getString(R.string.sucToCollect), Toast.LENGTH_SHORT).show();

                    for(int i=0;i<globalProvider.shangpingListDefault.size();i++){
                        if(globalProvider.shangpingListDefault.get(i).get_id().equals(product.get_id())){
                            globalProvider.shangpingListDefault.get(i).setCollected("yes");
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers=globalProvider.addHeaderToken(SearchActivity.this);


                    //headers=globalProvider.addHeaderToken(getActivity());
                    return headers;
                }
            };
            globalProvider.addRequest(jsonObjectRequest);

            /*

            globalProvider.post(this,Constants.collectedStr,entity,"application/json",new RequestListener() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    for(int i=0;i<list.size();i++) {
                        if (list.get(i).get_id().equals(product._id)) {
                            list.get(i).setCollected("yes");

                        }
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(SearchActivity.this, getString(R.string.sucToCollect), Toast.LENGTH_SHORT).show();

                    for(int i=0;i<globalProvider.shangpingListDefault.size();i++){
                        if(globalProvider.shangpingListDefault.get(i).get_id().equals(product.get_id())){
                            globalProvider.shangpingListDefault.get(i).setCollected("yes");
                        }
                    }

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    // Toast.makeText(getActivity(), new String(responseBody), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

                }
            });
            */
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(JSONException ce)
        {
            ce.printStackTrace();
        }

    }
    public void deletecollected(final Product product){
        String url=Constants.collectedStr + "/" +product.get_id();

        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).get_id().equals(product._id)) {
                        list.get(i).setCollected("no");
                        ;
                    }
                }

                adapter.notifyDataSetChanged();
                Toast.makeText(SearchActivity.this, getString(R.string.sucToCancel), Toast.LENGTH_SHORT).show();
                for(int i=0;i<globalProvider.shangpingListDefault.size();i++){
                    if(globalProvider.shangpingListDefault.get(i).get_id().equals(product.get_id())){
                        globalProvider.shangpingListDefault.get(i).setCollected("no");
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers=globalProvider.addHeaderToken(SearchActivity.this);


                //headers=globalProvider.addHeaderToken(getActivity());
                return headers;
            }
        };
        globalProvider.addRequest(utf8JsonRequest);
        /*

        globalProvider.delete(this, url, new RequestListener() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).get_id().equals(product._id)) {
                        list.get(i).setCollected("no");
                        ;
                    }
                }

                adapter.notifyDataSetChanged();
                Toast.makeText(SearchActivity.this, getString(R.string.sucToCancel), Toast.LENGTH_SHORT).show();
                for(int i=0;i<globalProvider.shangpingListDefault.size();i++){
                    if(globalProvider.shangpingListDefault.get(i).get_id().equals(product.get_id())){
                        globalProvider.shangpingListDefault.get(i).setCollected("no");
                    }
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(getActivity(), new String(responseBody), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

            }
        });
        */
    }
    public void loadSearchList() throws JSONException {




        String url;


        if(globalProvider.contract!=null&&globalProvider.contract._supplier!=null)
        {
            url=Constants.productListUrlStr+"?"+"_supplier"+"="+contract._supplier+"&"+"name"+"="+mName;
        }
        else
            url=Constants.productListUrlStr+"?"+"&"+"name"+"="+mName;

        Log.d("checksearchurl",url);



        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("checkresponse",response);
                parseSearchList(response,mName);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checeee",error.toString());

            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers=globalProvider.addHeaderToken(SearchActivity.this);


                //headers=globalProvider.addHeaderToken(getActivity());
                return headers;
            }

        };
        globalProvider.addRequest(utf8JsonRequest);

/*
        JSONObject params=new JSONObject();

        params.put("name", mName);
        if(contract!=null&&contract._supplier!=null){
            params.put("_supplier",contract._supplier);
        }



        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, Constants.productListUrlStr, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseSearchList(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers=globalProvider.addHeaderToken(SearchActivity.this);


                //headers=globalProvider.addHeaderToken(getActivity());
                return headers;
            }

        };
        globalProvider.addRequest(jsonObjectRequest);
        */

      //  RequestParams params = new RequestParams();
        //params.put("main",1);

        /*

        globalProvider.get(this, Constants.productListUrlStr, params, new RequestListener() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                parseSearchList(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Log.v("err", new String(responseBody));
            }

            @Override
            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

            }
        });
        */

    }
    public void parseSearchList(String json){

        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonParser jsonParser = jsonFactory.createJsonParser(json);
            ProductList shangpingList = (ProductList) objectMapper.readValue(jsonParser,ProductList.class);

            if(globalProvider.shangpingList.size()==0){

                globalProvider.shangpingListSearchDefault=shangpingList.products;

            }else{
                globalProvider.shangpingListSearchDefault=shangpingList.products;
                for(int i=0;i<globalProvider.shangpingListSearchDefault.size();i++){

                    for(int a=0;a<globalProvider.shangpingList.size();a++){

                        if(globalProvider.shangpingListSearchDefault.get(i).get_id().equals(globalProvider.shangpingList.get(a).get_id())){

                            globalProvider.shangpingListSearchDefault.get(i).setOrderQuantity(globalProvider.shangpingList.get(a).getOrderQuantity());

                        }

                    }

                }

            }
            this.list.clear();
            this.list.addAll(globalProvider.shangpingListSearchDefault);
            if(list.size()==0){

                search_lishi.setVisibility(View.GONE);
                search_layout.setVisibility(View.VISIBLE);
                search_list.setVisibility(View.GONE);
                none_layout.setVisibility(View.VISIBLE);
                Img_gotocart.setVisibility(View.INVISIBLE);
                GoToCart.setVisibility(View.INVISIBLE);


            }else{

                search_lishi.setVisibility(View.GONE);
                search_layout.setVisibility(View.VISIBLE);
                search_list.setVisibility(View.VISIBLE);
                none_layout.setVisibility(View.GONE);

                Img_gotocart.setVisibility(View.VISIBLE);
                GoToCart.setVisibility(View.VISIBLE);


            }
            adapter.notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {

        this.setResult(RESULT_OK);
        super.onBackPressed();
    }

}
