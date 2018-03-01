package com.samiapps.kv.myapplication;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;

/**
 * Created by KV on 23/2/18.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentProduct extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private ProductListAdapter mAdapter;
    private ListView lv;
    private ListView lv2;
    private ListView lv3;

    private Integer mPage;
    private Integer mItemsPerPage;
    private List<Product> mItems;
    private List<List<Product>> MiTems;


    public int states=0;
    public List<Integer> sinal;
    private RecyclerView lk;
    private ProductHeaderAdapter adapter;
    private List<String>  data;
    private Boolean mNomore;
    public Contract contract;
    public String category="";
    public TextView name;
    public LinearLayout turn_left;
    private List<SwipeRefreshLayout> SwipeRefreshLayoutList;
    private SwipeRefreshLayout mSwipeRefreshlayout;
    private ViewPager viewPager;
    public ViewPagerAdapter Adapter;
    public List<ViewGroup> listViews;
    public LayoutInflater inflater;
    public List<ProductListAdapter> adapters;
    public List<Product> shangPingList;
    GlobalProvider globalProvider;

    private ViewFlipper viewFlipper;
    private GestureDetector detector; //手势检测

    MainActivity.MyOnTouchListener myOnTouchListener;

    private static final int FLING_MIN_DISTANCE = 300;
    private static final int FLING_MIN_VELOCITY = 200;




    public ProgressDialog dialog;

    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalProvider=GlobalProvider.getInstance(getActivity());

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, null);

        return view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initAction();
        mNomore = false;
        mPage=1;
        mItemsPerPage = 10;

        ImageView search= (ImageView) getActivity().findViewById(R.id.search);

        inflater = getActivity().getLayoutInflater();
        if(adapters==null){
            adapters=new ArrayList<ProductListAdapter>();}
        if(MiTems==null){
            MiTems=new ArrayList<List<Product>>();}

        if(listViews==null||globalProvider.shangpingListDefault.size()==0){
            listViews=new ArrayList<ViewGroup>();}
        if(Adapter==null||globalProvider.shangpingListDefault.size()==0){
            Adapter=new ViewPagerAdapter(listViews);}
        if(viewPager!=null){
            viewPager.setAdapter(Adapter);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                lk.smoothScrollToPosition(position);
                adapter.setPosition(position);
                mNomore = false;
                mPage = 1;
                states = position;
                if (position == 0) {
                    globalProvider.ShangpingHeaderLoadCategory = "";
                } else {
                    globalProvider.ShangpingHeaderLoadCategory = data.get(position);
                }
                if(((ListView)listViews.get(position).findViewById(R.id.lv)).getChildCount()>0){
                    for(int i=0;i<sinal.size();i++){
                        if(position==i){
                            adapters.get(position).notifyDataSetChanged();
                            sinal.remove(sinal.get(i));
                        }
                    }
                    return;
                }
                loadProductList();
            }

            public void onPageScrollStateChanged(int state) {

            }
        });
        if(globalProvider.contract!=null)
        {
            contract= globalProvider.contract;
            name.setText(contract._supplierInfo.getCompanyName());
        }
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(),SearchActivity.class);
                if(contract!=null) {
                    intent.putExtra("contract", contract);
                }
                getActivity().startActivityForResult(intent, Constants.UPDATASEARCHLIST);


            }
        });
//        if(mItems==null) {
//        mItems=new ArrayList<Product>();}
        if(data==null||globalProvider.shangpingListDefault.size()==0){
            data=new ArrayList<String>();}

        if(adapter==null||globalProvider.shangpingListDefault.size()==0){
            adapter=new ProductHeaderAdapter(getActivity(),data);}

        adapter.setOnItemClickListener(new ProductHeaderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                viewPager.setCurrentItem(position);
                mNomore = false;
                mPage = 1;
                states = position;
                if (position == 0) {
                    globalProvider.ShangpingHeaderLoadCategory = "";
                } else {
                    globalProvider.ShangpingHeaderLoadCategory = data.get(position);
                }
                if(((ListView)listViews.get(position).findViewById(R.id.lv)).getChildCount()>0){
                    for(int i=0;i<sinal.size();i++){
                        if(position==i){
                            adapters.get(position).notifyDataSetChanged();
                            sinal.remove(sinal.get(i));
                        }
                    }
                    return;
                }
                loadProductList();
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        lk.setLayoutManager(linearLayoutManager);
        if(lk!=null){
            lk.setAdapter(adapter);}

        if(MiTems.size()>0||!globalProvider.ShangpingHeaderLoadCategory.equals("")){
            return;
        }
        globalProvider.ShangpingHeaderLoadCategory="";

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getActivity().getString(R.string.loading));
        dialog.show();
        loadCategoryList();
    }


    public void ConfirmReceived(Order order) {

        Map<String, String> params = new HashMap<>();
        params.put("status", "received");

        //RequestParams params = new RequestParams();
        // params.put("status", "received");


        String url_l = Constants.orderListUrlStr + "/" + order._id;
        CustomRequest customRequest = new CustomRequest(Request.Method.PUT, url_l, params, new Response.Listener<JSONObject>() {
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
                Map<String, String> headers = new HashMap<>();
                headers = globalProvider.addHeaderToken(getActivity());


                //headers=globalProvider.addHeaderToken(getActivity());
                return headers;
            }
        };
        globalProvider.addRequest(customRequest);
    }




    //TODO uncomment loadSearch

    public void loadSearchList(final String mName){

        String url=null;

        //TODO TRIAL CHECK using utf8
        if(globalProvider.contract!=null&&globalProvider.contract._supplier!=null)
        {
            url=Constants.productListUrlStr+"?"+"_supplier"+"="+contract._supplier+"&"+"main"+"="+"2"+"&"+"name"+"="+mName;
        }

        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseSearchList(response,mName);

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
                headers=globalProvider.addHeaderToken(getActivity());


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
            JsonParser jsonParser = jsonFactory.createJsonParser(json);
            ProductList shangpingList = (ProductList) objectMapper.readValue(jsonParser,ProductList.class);

            Product productSearch=null;
            if(shangpingList.products.size()>0){
                productSearch=shangpingList.products.get(0);

            }else{
                //productSearch=shangpingList.products.get(0);
                //if(!productSearch.getUnit().equals("ctn")&&!productSearch.getUnit().equals("箱")&&!productSearch.getUnit().equals("件")) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(getActivity().getString(R.string.Product_Unit))
                        .setPositiveButton(getActivity().getString(R.string.confirm), new DialogInterface.OnClickListener() {
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
                }
            }
            Intent intent=new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra("product",productSearch);
            getActivity().startActivityForResult(intent, Constants.DETAILGOTOCART);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doSetFreshListener(SwipeRefreshLayout mSwipeRefreshlayout){
        mSwipeRefreshlayout.setOnRefreshListener(this);
    }
    public void doSetScrollListener(ListView lv){
        lv.setOnScrollListener(this);
    }
    public void doOnItemClickListener(ListView lv){
        lv.setOnItemClickListener(this);
    }

    public void onResume()  {
        super.onResume();
        for(int i=0;i<globalProvider.shangpingListDefault.size();i++){
            Log.v("err",globalProvider.shangpingListDefault.get(i).getOrderQuantity()+"");
        }
        //Log.v("err", globalProvider.shangpingListDefault + "");
        if(globalProvider.shangpingListDefault.size()>0) {
            for(int i=0;i<MiTems.size();i++){
                for(int a=0;a<MiTems.get(i).size();a++){
                    for(int b=0;b<globalProvider.shangpingListDefault.size();b++){
                        if(MiTems.get(i).get(a).get_id().equals(globalProvider.shangpingListDefault.get(b).get_id())){
                            MiTems.get(i).get(a).setOrderQuantity(globalProvider.shangpingListDefault.get(b).getOrderQuantity());
                            MiTems.get(i).get(a).setCollected(globalProvider.shangpingListDefault.get(b).getCollected());
                        }
                    }
                }
            }
            if(adapters!=null){
                for(int i=0;i<adapters.size();i++){
                    adapters.get(i).notifyDataSetChanged();
                }
            }
//            mItems = globalProvider.shangpingListDefault;
//            mAdapter.notifyDataSetChanged();
        }
    }

    public void onPause() {
        super.onPause();
        globalProvider.shangpingListDefault.clear();
        if(MiTems!=null) {
            for(int i=0;i<MiTems.size();i++){
                for(int a=0;a<MiTems.get(i).size();a++){
                    globalProvider.shangpingListDefault.add(MiTems.get(i).get(a));
                }
            }
            //globalProvider.shangpingListDefault = mItems;
        }
        List<OrderSubmit> orderList=new ArrayList<OrderSubmit>();
        this.shangPingList=globalProvider.shangpingList;
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
    public void balanceListOrderQuantity(Product product,float orderQuantity){
        for(int i=0;i<MiTems.size();i++){
            for(int a=0;a<MiTems.get(i).size();a++){
                if(MiTems.get(i).get(a).get_id().equals(product.get_id())){
                    MiTems.get(i).get(a).setOrderQuantity(orderQuantity);
                    int m=0;
                    for(int b=0;b<sinal.size();b++){
                        if(sinal.get(b)==i){
                            m=m+1;
                        }
                        if(m==0){
                            sinal.add(i);
                        }
                    }
                }
            }
            //adapters.get(i).notifyDataSetChanged();
        }
//        if(adapters!=null){
//            for(int i=0;i<adapters.size();i++){
//                adapters.get(i).notifyDataSetChanged();
//            }
//        }
    }
    public void fresh(){
        globalProvider.ShangpingHeaderLoadCategory="";
        MiTems.clear();
        adapter=null;
    }
    public void initView(){
        lv= (ListView) getActivity().findViewById(R.id.lv);
        lk= (RecyclerView)getActivity().findViewById(R.id.lk);
        name= (TextView) getActivity().findViewById(R.id.tv_headerTitle);
        turn_left= (LinearLayout) getActivity().findViewById(R.id.turn_left);
        viewPager= (ViewPager) getActivity().findViewById(R.id.viewPager);
        //mSwipeRefreshlayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.swiperefresh);
    }
    public void initAction(){

        turn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    fresh();
                    MainActivity.getDefault().setSelect(0);
                    globalProvider.adjustToStart = "contract";

            }
        });
    }
    public void loadProductList(){

        mPage=1;
        Map<String,String> params1=new HashMap<>();
       // params1.put("main",String.valueOf(1));
      //  params1.put("page",String.valueOf(mPage));
       // params1.put("itemsPerPage", String.valueOf(mItemsPerPage));

        if (contract!=null){
            if(contract._supplier!=null){
                Log.d("checksupplier",contract._supplier);
                params1.put("_supplier",contract._supplier);
            }
        }
        if(!globalProvider.ShangpingHeaderLoadCategory .equals("")){
            params1.put("category",globalProvider.ShangpingHeaderLoadCategory);
            Log.d("checkcategoryhe",globalProvider.ShangpingHeaderLoadCategory);
        }



        String uri= String.format(Constants.productListUrlStr+"?_supplier=%1$s",contract._supplier);
        //YOUR_URL + ? + PARAMETER_NAME =paramter
                String url=Constants.productListUrlStr+"?"+"_supplier"+"="+contract._supplier+"&"+"main"+"="+"1";


        Utf8JsonRequest stringRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("checksuc",response);
                dialog.dismiss();
                parseContractList(response);
//
                ((SwipeRefreshLayout)listViews.get(states).findViewById(R.id.swiperefresh)).setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checkstrerr",error.networkResponse.headers.toString());
            }
        })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers=globalProvider.addHeaderToken(getActivity());


                //headers=globalProvider.addHeaderToken(getActivity());
                return headers;
            }




        }
        ;
        globalProvider.addRequest(stringRequest);





               }







    /*
    public void parseError(String json) {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonParser jsonParser = jsonFactory.createJsonParser(json);
            Errorr error = (Errorr) objectMapper.readValue(jsonParser, Errorr.class);
            if(error.error!=null) {

                if (error.error.msg != "" & error.error.msg != null & error.error.msg.equals("customerGroup is not found")) {
                    Toast.makeText(getActivity(), "您尚未被添加进客户组，请联系供应商", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
    public void parseContractList(String json) {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonParser jsonParser = jsonFactory.createParser(json);
            ProductList productList = (ProductList) objectMapper.readValue(jsonParser, ProductList.class);
            if(globalProvider.shangpingList.size()>0){

                for(int i=0;i<productList.products.size();i++){

                    for(int a=0;a<globalProvider.shangpingList.size();a++){

                        if(productList.products.get(i).get_id().equals(globalProvider.shangpingList.get(a).get_id())){

                            productList.products.get(i).setOrderQuantity(globalProvider.shangpingList.get(a).getOrderQuantity());

                        }
                    }
                }
            }
            this.MiTems.get(states).clear();
            this.MiTems.get(states).addAll(productList.products);
            adapters.get(states).notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //TODO UNDO THIS FUNC

    private void loadMoreProductList() {

        mPage = mPage + 1;

        String url;

        //TODO TRIAL CHECK using utf8
        if (contract != null) {
            url = Constants.productListUrlStr + "?" + "_supplier" + "=" + contract._supplier + "&" + "main" + "=" + "1" + "&" + "page" + "=" + mPage + "&" + "itemsPerPage" + "=" + mItemsPerPage;
        } else
            url = Constants.productListUrlStr + "?" + "main" + "=" + "2" + "&" + "page" + "=" + mPage + "&" + "itemsPerPage" + "=" + mItemsPerPage;

        if (!globalProvider.ShangpingHeaderLoadCategory.equals("")) {
            url += "&" + "category" + "=" + globalProvider.ShangpingHeaderLoadCategory;

        }
        Utf8JsonRequest utf8JsonRequest = new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseMoreProductList(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers = globalProvider.addHeaderToken(getActivity());


                //headers=globalProvider.addHeaderToken(getActivity());
                return headers;
            }

        };
        globalProvider.addRequest(utf8JsonRequest);
    }
    private void parseMoreProductList(String json){

        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonParser jsonParser = jsonFactory.createJsonParser(json);
            ProductList productList = (ProductList) objectMapper.readValue(jsonParser, ProductList.class);
            if(productList.products.size()<mItemsPerPage){
                mNomore = true;
            }
            for(int i=0;i<productList.products.size();i++){

                for(int a=0;a<globalProvider.shangpingList.size();a++){

                    if(productList.products.get(i).get_id().equals(globalProvider.shangpingList.get(a).get_id())){

                        productList.products.get(i).setOrderQuantity(globalProvider.shangpingList.get(a).getOrderQuantity());

                    }
                }
            }
            this.MiTems.get(states).addAll(productList.products);
            //globalProvider.shangpingListDefault=mItems;
            adapters.get(states).notifyDataSetChanged();
            //loadme();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO Undo this functiom

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

                    for(int i=0;i<MiTems.size();i++){
                        for(int a=0;a<MiTems.get(i).size();a++){
                            if(MiTems.get(i).get(a).get_id().equals(product._id)){
                                MiTems.get(i).get(a).setCollected("yes");
                                adapters.get(i).notifyDataSetChanged();

                            }
                        }

                    }
                    Toast.makeText(getActivity(), getActivity().getString(R.string.sucToCollect), Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error!=null){
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            })


            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers = globalProvider.addHeaderToken(getActivity());


                    //headers=globalProvider.addHeaderToken(getActivity());
                    return headers;
                }
            };
            globalProvider.addRequest(jsonObjectRequest);

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    //TODO uncomment this

    public void deletecollected(final Product product){
        String url=Constants.collectedStr + "/" +product.get_id();
        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                for(int i=0;i<MiTems.size();i++){
                    for(int a=0;a<MiTems.get(i).size();a++){
                        if(MiTems.get(i).get(a).get_id().equals(product.get_id())){
                            MiTems.get(i).get(a).setCollected("no");
                            adapters.get(i).notifyDataSetChanged();

                        }
                    }

                }
                Toast.makeText(getActivity(), getActivity().getString(R.string.sucToCancel), Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers = globalProvider.addHeaderToken(getActivity());


                //headers=globalProvider.addHeaderToken(getActivity());
                return headers;
            }
        };
        globalProvider.addRequest(utf8JsonRequest);

    }

    public void loadCategoryList(){

        String url=Constants.categoryUrlStr;
        if(contract!=null){
            if(contract._supplierInfo!=null){
                if(contract._supplierInfo.category!=null){
                    url=url+"/"+contract._supplierInfo.category;
                }
            }
        }
        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseCategoryList(response);
                loadProductList();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {

              /*  HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                String token = Constants.getToken(getActivity());
                Log.d("gettoken", token);
                headers.put("Authorization",token);
                */
            Map<String,String> headers=new HashMap<>();
            headers=globalProvider.addHeaderToken(getActivity());

            return headers;
        }
    };
                globalProvider.addRequest(utf8JsonRequest);

    }
    //TODO check parseCategory with original have made changes
    public void parseCategoryList(String json){
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonParser jsonParser = jsonFactory.createParser(json);
            Category category = (Category) objectMapper.readValue(jsonParser, Category.class);
            this.data.clear();
            this.data.add(getActivity().getString(R.string.All));
            if(category.getProductCategorys()!=null) {
                List<String> productCategorys=new ArrayList<>();

               productCategorys= category.getProductCategorys();
                for (int i = 0; i < productCategorys.size(); i++) {
                    this.data.add(productCategorys.get(i));
                }
            }
            listViews.clear();
            MiTems.clear();
            adapters.clear();
            for(int i=0;i<data.size();i++){
                listViews.add((LinearLayout) (inflater.inflate(R.layout.view_pager_layout, null)).findViewById(R.id.viewPager_layout));
                MiTems.add(new ArrayList<Product>());


            }
            sinal=new ArrayList<Integer>();
            for(int i=0;i<data.size();i++){
                adapters.add(new ProductListAdapter(getActivity(), MiTems.get(i), this));
            }
            for(int i=0;i<data.size();i++){
                ((ListView) listViews.get(i).findViewById(R.id.lv)).setAdapter(adapters.get(i));
            }
            for(int i=0;i<data.size();i++){
                doSetFreshListener((SwipeRefreshLayout)listViews.get(i).findViewById(R.id.swiperefresh));
                doSetScrollListener(((ListView) listViews.get(i).findViewById(R.id.lv)));

            }

            Adapter.notifyDataSetChanged();
            states=0;
            viewPager.setCurrentItem(0);

            adapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRefresh() {
        mNomore = false;
        mPage = 1;
        loadProductList();
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if((ListView) listViews.get(states).findViewById(R.id.lv)!=null) {
            if (!mNomore && ((ListView) listViews.get(states).findViewById(R.id.lv)).getCount() != 0 && ((ListView) listViews.get(states).findViewById(R.id.lv)).getLastVisiblePosition() >= (((ListView) listViews.get(states).findViewById(R.id.lv)).getCount() - 2)) {

                 loadMoreProductList();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == (ListView) listViews.get(states).findViewById(R.id.lv)){

            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra("product", MiTems.get(states).get(position));
            getActivity().startActivityForResult(intent, Constants.DETAILGOTOCART);

        }
    }
}