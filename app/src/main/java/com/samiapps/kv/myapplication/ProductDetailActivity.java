package com.samiapps.kv.myapplication;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;

/**
 * Created by KV on 28/2/18.
 */

public class ProductDetailActivity extends BaseActivity{

    public TextView nameDetail;
    public Button collect;
    public TextView unit,gang;
    public TextView unitprice;
    public TextView available;
    public TextView total_price;
    public EditText orderQuantity;
    public ImageView backButton;

    public ImageView add;
    public ImageView reduce;
    public LinearLayout showDetail;
    public LinearLayout turn;
    public Boolean isShowing=false;

    public Product product;
    //  public LinearLayout turn_left;
    public TextView specification;
    public TextView decription;
    public TextView origin;
    public ImageView turnImg;
    public RelativeLayout four;
    public Button buyByCtn;
    public RelativeLayout discount_layout;
    public TextView discount;
    public TextView youhui;
    public LinearLayout youhui_layout;
    GlobalProvider globalProvider;

    public ViewPager viewPager;
    public LinearLayout indicator;
    public LayoutInflater inflater;
    public ProductDetailViewPagerAdapter adapter;
    public List<ViewGroup> listviews;


    public static String character="character";


    public double i;
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private CycleViewPager cycleViewPager;

    private String[] imageUrls ;
    private ConnectivityManager mConnectivityManager;

    private NetworkInfo netInfo;
    public List<ImageView> imgs;


    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if(netInfo == null) {
                    Toast.makeText(ProductDetailActivity.this, "网络链接不可用！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    @Override
    public void onPause() {
        super.onPause();

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
                    orderList.get(a).setBillingAddress(globalProvider.orders.get(i).getBillingAddress());
                    orderList.get(a).setShippingAddress(globalProvider.orders.get(i).getShippingAddress());
                    if(globalProvider.orders.get(i).getFeedback()!=null) {
                        orderList.get(a).setFeedback(globalProvider.orders.get(i).getFeedback());
                    }
                }
            }
        }
//
        globalProvider.orders.clear();
        globalProvider.orders.addAll(orderList);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(myNetReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        globalProvider=GlobalProvider.getInstance(ProductDetailActivity.this);
        initView();
        initAction();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
        Intent intent = this.getIntent();
        inflater = LayoutInflater.from(this);
        listviews=new ArrayList<ViewGroup>();
        if(intent.getSerializableExtra("product")!=null) {
            product= (Product) intent.getSerializableExtra("product");
            doFirst(product);
        }

     /*   specification.setOnClickListener(new View.OnClickListener() {
            Boolean flag = true;
            @Override
            public void onClick(View v) {
                if(flag){
                    flag = false;
                    specification.setEllipsize(null); // 展开
                    specification.setMaxLines(99);
                    //tv.setSingleLine(flag);
                }else{
                    flag = true;
                    specification.setEllipsize(TextUtils.TruncateAt.END);
                    specification.setMaxLines(1);// 收缩
                    //tv.setSingleLine(flag);
                }
            }
        });
        */
     /*   decription.setOnClickListener(new View.OnClickListener() {
            Boolean flag = true;
            @Override
            public void onClick(View v) {
                if(flag){
                    flag = false;
                    decription.setEllipsize(null); // 展开
                    decription.setMaxLines(99);
                    //tv.setSingleLine(flag);
                }else{
                    flag = true;
                    decription.setEllipsize(TextUtils.TruncateAt.END);
                    decription.setMaxLines(1);// 收缩
                    //tv.setSingleLine(flag);
                }
            }
        });
        */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<indicator.getChildCount();i++){
                    if(i==position){
                        ((ImageView)indicator.getChildAt(i)).setImageResource(R.drawable.selected);
                    }else{
                        ((ImageView)indicator.getChildAt(i)).setImageResource(R.drawable.select);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        buyByCtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSearchList(product.name);
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(product!=null){
                    if(product.getCollected()!=null){
                        if(product.getCollected().equals("yes")){
                            deletecollected(product.get_id());
                        }else{
                            addcollected(product);
                        }
                    } else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                        builder.setTitle("");
                        builder.setMessage(getString(R.string.noquanxian));
                        builder.setPositiveButton(getString(R.string.confirm), null);
                        builder.show();
                    }
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = i + 1;
                i = changeDouble(i);
                orderQuantity.setText(i + "");
                for (int b = 0; b < globalProvider.shangpingListDefault.size(); b++) {
                    if (product.get_id().equals(globalProvider.shangpingListDefault.get(b).get_id())) {
                        globalProvider.shangpingListDefault.get(b).setOrderQuantity((float) i);
                    }
                }
                for (int b = 0; b < globalProvider.shangpingList.size(); b++) {
                    Product sp = globalProvider.shangpingList.get(b);
                    if (sp.get_id().equals(product.get_id())) {
                        product.setOrderQuantity((float) i);
                        globalProvider.shangpingList.get(b).setOrderQuantity((float) i);
                        doTotal_price();
                        return;
                    }
                }
                product.setOrderQuantity((float) i);
                globalProvider.shangpingList.add(product);
                //GoToAlertOrNo();
                doTotal_price();
                for (int i = 0; i < globalProvider.contractListToCart.size(); i++) {
                    Contract contract = globalProvider.contractListToCart.get(i);
                    if (contract._supplier.equals(product.get_supplier())) {
                        return;
                    }
                }
                for (int i = 0; i < globalProvider.contractList.size(); i++) {
                    if (globalProvider.contractList.get(i)._supplier.equals(product.get_supplier())) {
                        globalProvider.contractListToCart.add(globalProvider.contractList.get(i));
                    }
                }
                if (globalProvider.shangpingList.size() > 0) {

                        MainActivity.getDefault().setCartNum();

                } else {

                        MainActivity.getDefault().HideCartNum();

                }

            }
        });
        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = i - 1;
                i = changeDouble(i);
                if (i > 0) {
                    orderQuantity.setText(i + "");
                } else {
                    i = 0;
                    orderQuantity.setText(i + "");
                }
                for (int b = 0; b < globalProvider.shangpingListDefault.size(); b++) {
                    if (product.get_id().equals(globalProvider.shangpingListDefault.get(b).get_id())) {
                        globalProvider.shangpingListDefault.get(b).setOrderQuantity((float) i);
                    }
                }
                for (int b = 0; b < globalProvider.shangpingList.size(); b++) {
                    Product sp = globalProvider.shangpingList.get(b);
                    if (sp.get_id().equals(product.get_id()) && i == 0) {
                        product.setOrderQuantity(0);
                        globalProvider.shangpingList.get(b).setOrderQuantity((float) i);
                        globalProvider.shangpingList.remove(sp);
                        doTotal_price();
                        if (globalProvider.shangpingList.size() > 0) {
                                MainActivity.getDefault().setCartNum();

                        } else {

                                MainActivity.getDefault().HideCartNum();

                        }
                        for (int i = 0; i < globalProvider.shangpingList.size(); i++) {
                            if (sp._supplier.equals(globalProvider.shangpingList.get(i).get_supplier())) {
                                return;
                            }
                        }
                        for (int i = 0; i < globalProvider.contractListToCart.size(); i++) {
                            if (sp.get_supplier().equals(globalProvider.contractListToCart.get(i)._supplier)) {
                                globalProvider.contractListToCart.remove(globalProvider.contractListToCart.get(i));
                            }
                        }
                    } else if (sp.get_id().equals(product.get_id()) && i > 0) {
                        product.setOrderQuantity((float) i);
                        globalProvider.shangpingList.get(b).setOrderQuantity((float) i);
                        doTotal_price();
                    }
                }
                if (globalProvider.shangpingList.size() > 0) {

                        MainActivity.getDefault().setCartNum();

                } else {

                        MainActivity.getDefault().HideCartNum();

                }


            }
        });
        orderQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (orderQuantity.getText().length() > 0 && !CanBeDoubleOrNot(orderQuantity.getText().toString())) {

                    orderQuantity.setText(i + "");
                    new AlertDialog.Builder(ProductDetailActivity.this)
                            .setMessage(getString(R.string.errorinput))
                            .setNegativeButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Log.i("alertdialog", " 请保存数据！");
                                }
                            }).show();
                    return;
                }
                if (orderQuantity.getText().length() > 0 && !product.getUnit().equals("kg") && !product.getUnit().equals("千克") && Double.parseDouble(orderQuantity.getText().toString()) - (int) Double.parseDouble(orderQuantity.getText().toString()) != 0) {

                    orderQuantity.setText(i + "");
                    new AlertDialog.Builder(ProductDetailActivity.this)
                            .setMessage(getString(R.string.havexiaoshu))
                            .setNegativeButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Log.i("alertdialog", " 请保存数据！");

                                }
                            }).show();

                }
                if (orderQuantity.getText().length() != 0 && Double.parseDouble(orderQuantity.getText().toString()) > 0) {
                    i = changeDouble(Double.parseDouble(orderQuantity.getText().toString()));


                    for (int b = 0; b < globalProvider.shangpingListDefault.size(); b++) {
                        if (product.get_id().equals(globalProvider.shangpingListDefault.get(b).get_id())) {
                            globalProvider.shangpingListDefault.get(b).setOrderQuantity((float) i);
                        }
                    }
                    //doBalancce(data.get(position), (float) i[0]);
                    for (int b = 0; b < globalProvider.shangpingList.size(); b++) {
                        Product sp = globalProvider.shangpingList.get(b);
                        if (sp.get_id().equals(product.get_id())) {
                            product.setOrderQuantity((float) i);
                            globalProvider.shangpingList.get(b).setOrderQuantity((float) i);
                            doTotal_price();
                            return;
                        }
                    }
                    int a=globalProvider.shangpingList.size();
                    product.setOrderQuantity((float) i);
                    doTotal_price();
                    globalProvider.shangpingList.add(product);
                    if(a==0){
                        GoToAlertOrNo();
                    }
                    for (int i = 0; i < globalProvider.contractListToCart.size(); i++) {
                        Contract contract = globalProvider.contractListToCart.get(i);
                        if (contract._supplier.equals(product.get_supplier())) {
                            return;
                        }
                    }
                    for (int i = 0; i < globalProvider.contractList.size(); i++) {
                        if (globalProvider.contractList.get(i)._supplier.equals(product.get_supplier())) {
                            globalProvider.contractListToCart.add(globalProvider.contractList.get(i));
                        }
                    }
                    // ((MainActivity) context).setCartNum();
                } else if (orderQuantity.getText().length() == 0 || Double.parseDouble(orderQuantity.getText().toString()) <= 0) {
                    i = 0;

                    for (int b = 0; b < globalProvider.shangpingListDefault.size(); b++) {
                        if (product.get_id().equals(globalProvider.shangpingListDefault.get(b).get_id())) {
                            globalProvider.shangpingListDefault.get(b).setOrderQuantity((float) i);
                        }
                    }
                    //doBalancce(data.get(position), (float) i[0]);
                    int num = 0;
                    for (int b = 0; b < globalProvider.shangpingList.size(); b++) {
                        Product sp = globalProvider.shangpingList.get(b);
                        if (sp.get_id().equals(product.get_id())) {
                            product.setOrderQuantity((float) i);
                            doTotal_price();
                            globalProvider.shangpingList.get(b).setOrderQuantity((float) i);
                            globalProvider.shangpingList.remove(sp);
                            num = num + 1;
                            for (int i = 0; i < globalProvider.shangpingList.size(); i++) {
                                if (sp._supplier.equals(globalProvider.shangpingList.get(i).get_supplier())) {
                                    return;
                                }
                            }
                            for (int i = 0; i < globalProvider.contractListToCart.size(); i++) {
                                if (sp._supplier != null) {
                                    if (sp.get_supplier().equals(globalProvider.contractListToCart.get(i)._supplier)) {
                                        globalProvider.contractListToCart.remove(globalProvider.contractListToCart.get(i));
                                    }
                                }
                            }
                        }
                    }
                    if (num == 0) {
                        product.setOrderQuantity((float) i);
                    }
                }
                if (globalProvider.shangpingList.size() > 0) {
                        MainActivity.getDefault().setCartNum();
                } else {
                        MainActivity.getDefault().HideCartNum();
                }
            }
        });
       /* turn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=this.getIntent();
                setResult(Constants.SEARCHTODETAIL);
                finish();
            }
        });
        */


        orderQuantity.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (i == 0) {
                        orderQuantity.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //按下键盘上返回按钮
        if(keyCode == KeyEvent.KEYCODE_BACK){
            setResult(Constants.SEARCHTODETAIL);
            finish();
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
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
    private boolean CanBeDoubleOrNot(String str){
        boolean ret = true;
        try{
            double d = Double.parseDouble(str);
            ret = true;
        }catch(Exception ex){
            ret = false;
        }
        return ret;
    }
    public void loadSearchList(final String mName){


        String url;
        int mainval;
        if(product.getMain()==1){
            mainval=2;
        }
        else{
            //Log.d("checkhere","main1");
            mainval=1;
        }

        //TODO TRIAL CHECK using utf8

        if(globalProvider.contract!=null&&globalProvider.contract._supplier!=null)
        {
            url=Constants.productListUrlStr+"?"+"_supplier"+"="+globalProvider.contract._supplier+"&"+"main"+"="+mainval+"&"+"name"+"="+mName;
        }
        else
            url=Constants.productListUrlStr+"?"+"main"+"="+mainval+"&"+"name"+"="+mName;

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
                headers=globalProvider.addHeaderToken(ProductDetailActivity.this);


                //headers=globalProvider.addHeaderToken(getActivity());
                return headers;
            }

        };
        globalProvider.addRequest(utf8JsonRequest);

    }
    public void parseSearchList(String json,String name){
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonParser jsonParser = jsonFactory.createParser(json);
            ProductList shangpingList = (ProductList) objectMapper.readValue(jsonParser,ProductList.class);

            if(shangpingList.products.size()>0){

                product=shangpingList.products.get(0);

            }else{
                //product=shangpingList.products.get(0);
                //if(product.getUnit().equals("ctn")||product.getUnit().equals("箱")||product.getUnit().equals("件")){
                if(product.getMain()==2){
                    new AlertDialog.Builder(ProductDetailActivity.this)
                            .setMessage(getString(R.string.noanjin))
                            .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    return;
                }else{
                    new AlertDialog.Builder(ProductDetailActivity.this)
                            .setMessage(getString(R.string.Product_Unit))
                            .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    return;
                }
            }
            for(int a=0;a<globalProvider.shangpingList.size();a++){
                if(globalProvider.shangpingList.get(a).get_id().equals(product.get_id())){
                    product.setOrderQuantity(globalProvider.shangpingList.get(a).getOrderQuantity());
                }
            }
            doFirst(product);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doFirst(Product product){

        nameDetail.setText(product.getName());
        unitprice.setText(product.getUnitPrice() + "");
        if (Constants.getGetLanguage(getApplication()).equals("english")) {
            unit.setText(product.getUnit());
        }else if(Constants.getGetLanguage(getApplication()).equals("chinese")){
            if(product.cunit != null) {
                unit.setText(product.cunit);
            }else{
                unit.setText(product.getUnit());
            }
        }else{
            if (Locale.getDefault().getLanguage().equals("en")) {
                unit.setText(product.getUnit());
            } else {
                if(product.cunit != null) {
                    unit.setText(product.cunit);
                }else{
                    unit.setText(product.getUnit());
                }
            }
        }

        if(product.getUnitPrice() == 0){
            unitprice.setText(getString(R.string.Unpriced));
            unitprice.setTextColor(Color.parseColor("#BCBCBC"));
            available.setVisibility(View.GONE);

            unit.setText("");
            gang.setText("");
        }
        //unit.setText(product.getUnit());
        if(product.getSellByCtn()==1){ //CTN option is there
            buyByCtn.setVisibility(View.VISIBLE);
            if(product.getMain()==1&&product.getUnit().equals("kg")){  //current parameter is kg ;show option CTN
                buyByCtn.setText(getString(R.string.sellByCtn));
            }else if(product.getMain()==2){
                buyByCtn.setText(getString(R.string.Buy_KG)+" KG");
            }
            else
                buyByCtn.setVisibility(View.GONE);
        }else{
            buyByCtn.setVisibility(View.GONE);
        }
        if(product.getDiscount()>0){
            discount_layout.setVisibility(View.VISIBLE);
            //Log.d("checkdiscount",""+product.getDiscount());
            //  Log.d("checktot",""+product.getSubTotal());
            double val=product.getSubTotal()/Math.abs(product.getDiscount()-product.getSubTotal());
            // Log.d("checkva",""+ val);
            float a=(1 - product.getDiscount()) * 100;
            float b=(float)(Math.round(a*100))/100;
            discount.setText(getString(R.string.Ctn_Dis)+" " +b+ "%"+" "+getString(R.string.Ctn_count));
            if(product.getMain()==2) {
                youhui_layout.setVisibility(View.VISIBLE);
                discount_layout.setVisibility(View.GONE);
            }else{
                youhui_layout.setVisibility(View.INVISIBLE);
                discount_layout.setVisibility(View.VISIBLE);
            }
        }else{
            youhui_layout.setVisibility(View.INVISIBLE);

        }
        if(product.getSpecification()!=null){decription.setText(product.getSpecification());}
        if(product.getDecription()!=null){specification.setText(getString(R.string.decripetion)+" "+product.getDecription());}
        if(product.getOrigin()!=null){origin.setText(product.getOrigin());}
        orderQuantity.setText(product.getOrderQuantity() + "");
        if(product.getCollected() != null){
            if(product.getCollected().equals("yes")){
                collect.setText(getString(R.string.collected));
            }else{
                collect.setText(getString(R.string.collect));
            }
        }else{
            collect.setText(getString(R.string.collect));
        }

        if(product.getUnitPrice()>0){
            four.setVisibility(View.VISIBLE);
        }else{
            four.setVisibility(View.INVISIBLE);
        }
        i=changeDouble((double)product.getOrderQuantity());
        doTotal_price();
        listviews.clear();
        //imgs.clear();
        indicator.removeAllViews();
        if(product.imagelist!=null&&product.imagelist.length>0){
            String url="";
            for(int a=0;a<product.imagelist.length;a++){
                ViewGroup pager= (ViewGroup) inflater.inflate(R.layout.share_list_viewpager_layout, null);
                ImageView img= (ImageView) pager.findViewById(R.id.img);
                url=Constants.imgBaseUrlStr+"/"+product.imagelist[a];
                Picasso.with(this).load(url).resize(500, 500).centerCrop().into(img);
                listviews.add(pager);
            }
            adapter=new ProductDetailViewPagerAdapter(listviews);
            viewPager.setAdapter(adapter);
            if(product.imagelist.length>1){
                for(int i=0;i<product.imagelist.length;i++){
                    if(i==0){
                        ImageView img=new ImageView(this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                                (15,15);
                        //lp.setMargins(5,0,0,0);
                        img.setLayoutParams(lp);
                        img.setImageResource(R.drawable.selected);
                        //imgs.add(img);
                        indicator.addView(img);
                    }else{
                        ImageView img=new ImageView(this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                                (15,15);
                        lp.setMargins(20, 0, 0, 0);
                        img.setLayoutParams(lp);
                        img.setImageResource(R.drawable.select);
                        // imgs.add(img);
                        indicator.addView(img);
                    }
                }
            }else{
                ImageView img=new ImageView(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                        (15,15);
                //lp.setMargins(5,0,0,0);
                img.setLayoutParams(lp);
                img.setImageResource(R.drawable.selected);
                //imgs.add(img);
                indicator.addView(img);
            }
//            configImageLoader();
//            initialize();
        }else{
            if(product.getImageName()!=null){
                String url="";
                ViewGroup pager= (ViewGroup) inflater.inflate(R.layout.share_list_viewpager_layout, null);
                ImageView img= (ImageView) pager.findViewById(R.id.img);
                url=Constants.imgBaseUrlStr+"/"+product.getImageName();
                Picasso.with(this).load(url).resize(500, 500).centerCrop().into(img);
                listviews.add(pager);

            }
            adapter=new ProductDetailViewPagerAdapter(listviews);
            viewPager.setAdapter(adapter);
            ImageView img=new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (15,15);
            //lp.setMargins(5,0,0,0);
            img.setLayoutParams(lp);
            img.setImageResource(R.drawable.selected);
            // imgs.add(img);
            indicator.addView(img);
        }
    }

    private void GoToAlertOrNo(){
        if(globalProvider.shangpingList.size()==1&&globalProvider.isShopping){
            new AlertDialog.Builder(ProductDetailActivity.this)
                    .setMessage(getString(R.string.ordered))
                    .setPositiveButton(getString(R.string.editOrder), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            setResult(111);
                            MainActivity.getDefault().item=2;
                            finish();
                        }
                    }).setNegativeButton(getString(R.string.zhiyiorder), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    globalProvider.editOrder._id="";
                    globalProvider.order=new Order();
                    // TODO Auto-generated method stub

                    //Log.i("alertdialog", " 请保存数据！");
                }

            }).show();
        }
    }
    public void doTotal_price(){
        float a=product.getOrderQuantity() * product.getUnitPrice();
        float b=(float)(Math.round(a*100))/100;
        //mData.get(position).setSubTotal(b);
        total_price.setText(b+ "");
        // total_price.setText(b+ "");

        //
        // float c=b*(1-product.getDiscount());
        //  c=(float)(Math.round(c*100))/100;
        double c=(float)b/(float)product.getDiscount()-b;
        // c=(float)b-c;
        c= Math.floor(c * 100) / 100;


        //float c= product.getOrderQuantity()/product.getDiscount()-product.getOrderQuantity();
        youhui.setText(getString(R.string.save)+" $"+c);
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
            JSONObject object=new JSONObject(json);

          JsonObjectRequest customRequest=new JsonObjectRequest(Request.Method.POST, Constants.collectedStr, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    for(int b=0;b<globalProvider.shangpingListDefault.size();b++){
                        if(product._id.equals(globalProvider.shangpingListDefault.get(b).get_id())){
                            globalProvider.shangpingListDefault.get(b).setCollected("yes");
                        }
                    }
                    product.setCollected("yes");
                    collect.setText(getString(R.string.collected));
                    Toast.makeText(ProductDetailActivity.this, getString(R.string.sucToCollect), Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
              @Override
              public Map<String, String> getHeaders() throws AuthFailureError {
                  Map<String, String> headers = new HashMap<>();
                  headers = globalProvider.addHeaderToken(ProductDetailActivity.this);


                  //headers=globalProvider.addHeaderToken(getActivity());
                  return headers;
              }
          }
                  ;
          globalProvider.addRequest(customRequest);
          /*

            globalProvider.post(this,Constants.collectedStr,entity,"application/json",new RequestListener() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    for(int b=0;b<globalProvider.shangpingListDefault.size();b++){
                        if(product._id.equals(globalProvider.shangpingListDefault.get(b).get_id())){
                            globalProvider.shangpingListDefault.get(b).setCollected("yes");
                        }
                    }
                    product.setCollected("yes");
                    collect.setText(getString(R.string.collected));
                    Toast.makeText(ProductDetailActivity.this, getString(R.string.sucToCollect), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    //Toast.makeText(ProductDetailActivity.this, new String(responseBody), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

                }
            });
            */
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }
    public void deletecollected(final String _id){
        String url=Constants.collectedStr + "/" +_id;


        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                product.setCollected("no");
                collect.setText(getString(R.string.collect));
                for(int b=0;b<globalProvider.shangpingListDefault.size();b++){
                    if(_id.equals(globalProvider.shangpingListDefault.get(b).get_id())){
                        globalProvider.shangpingListDefault.get(b).setCollected("no");
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
                headers=globalProvider.addHeaderToken(ProductDetailActivity.this);


                //headers=globalProvider.addHeaderToken(getActivity());
                return headers;
            }


        };
        globalProvider.addRequest(utf8JsonRequest);


    }
    public void initAction(){

    }

    public void initView(){


        nameDetail= (TextView) findViewById(R.id.nameDetail);
        collect= (Button) findViewById(R.id.collect);
        unit= (TextView) findViewById(R.id.unit);
        unitprice= (TextView) findViewById(R.id.unitprice);
        available=(TextView) findViewById(R.id.available);
        gang = (TextView) findViewById(R.id.gang);
        total_price= (TextView) findViewById(R.id.total_price);
        orderQuantity= (EditText) findViewById(R.id.orderQuantity);
        backButton=(ImageView) findViewById(R.id.bkbutton);

        add= (ImageView) findViewById(R.id.add);
        reduce= (ImageView) findViewById(R.id.reduce);

        //showDetail= (LinearLayout) findViewById(R.id.showDetail);
        //turn= (LinearLayout) findViewById(R.id.turn);
        specification= (TextView) findViewById(R.id.specification);
        decription= (TextView) findViewById(R.id.decripetion);
        origin= (TextView) findViewById(R.id.origin);
        //turnImg= (ImageView) findViewById(R.id.turn_img);
        four= (RelativeLayout) findViewById(R.id.four);
        buyByCtn= (Button) findViewById(R.id.buyByCtn);
        discount_layout= (RelativeLayout) findViewById(R.id.discount_layout);
        discount= (TextView) findViewById(R.id.discount);
        youhui_layout= (LinearLayout) findViewById(R.id.youhui_layout);
        youhui= (TextView) findViewById(R.id.youhui);
        viewPager= (ViewPager) findViewById(R.id.viewPager);
        indicator= (LinearLayout) findViewById(R.id.indicator);
//        cycleViewPager = (CycleViewPager) getFragmentManager()
//                .findFragmentById(R.id.fragment_cycle_viewpager_content);

    }
    public double changeDouble(Double dou){
        NumberFormat nf=new DecimalFormat( "0.0 ");
        String s = nf.format(dou);
        s = s.replace(',','.');
        dou = Double.parseDouble(s);
        return dou;
    }
}
