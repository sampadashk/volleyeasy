package com.samiapps.kv.myapplication;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.mobeta.android.dslv.DragSortListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KV on 27/2/18.
 */

public class FragmentCollect extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {
    public CartTopAdapter Adapter;
    public RecyclerView lk;
    private List<Contract> data;
    public String supplierId;
    public List<Product> shangpingList;
    // public TextView total_price;
    public float price=0;
    // public Button submit;
    public TextView edit;
    public Boolean isEditting=false;
    private Boolean mNomore;
    private Integer mPage;
    private Integer mItemsPerPage;
    private List<List<Product>> MiTems;
    public int states=0;
    public List<CollectAdapter> adapters;
    public List<ViewGroup> listViews;
    private ViewPager viewPager;
    public ViewPagerAdapter mAdapter;
    public LayoutInflater inflater;
    public ProgressDialog dialog;
    public EditText editText;

    public TextView cancel;
    GlobalProvider globalProvider;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inflater = getActivity().getLayoutInflater();
        lk= (RecyclerView) getActivity().findViewById(R.id.lk);
        viewPager= (ViewPager) getActivity().findViewById(R.id.viewPager);
        //  total_price= (TextView) getActivity().findViewById(R.id.total_price);
        // submit= (Button) getActivity().findViewById(R.id.submit);
        edit= (TextView) getActivity().findViewById(R.id.edit);
        editText= (EditText) getActivity().findViewById(R.id.search_edit);

        cancel= (TextView) getActivity().findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editText.setText("");
                loadProducts();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            public void afterTextChanged(Editable editable) {
                String textView = editText.getText().toString();
                if (textView.equals("") || textView.length() == 0) {
                    loadProducts();
                    cancel.setVisibility(View.GONE);
                } else {
                    cancel.setVisibility(View.VISIBLE);
                    char[] str = textView.toCharArray();
                    List<Product> list = new ArrayList<Product>();
                    for (int i = 0; i <  MiTems.get(states).size(); i++) {
                        for(int j=0;j<str.length;j++) {
                            if ( MiTems.get(states).get(i).name.contains(textView)) {
                                boolean flag = true;
                                for(int k=0;k<list.size();k++){
                                    if(list.get(k).name.equals(MiTems.get(states).get(i).name)){
                                        flag = false;
                                    }
                                }
                                if(flag) {
                                    list.add(MiTems.get(states).get(i));
                                }
                            }
                        }
                    }
                    MiTems.get(states).clear();
                    MiTems.get(states).addAll(list);
                    list.clear();
                    adapters.get(states).notifyDataSetChanged();
                }
            }
        });
        data=new ArrayList<Contract>();
        shangpingList=new ArrayList<Product>();
        Adapter=new CartTopAdapter(getActivity(),data);
        mNomore = false;
        mPage=1;
        mItemsPerPage=60;
        adapters=new ArrayList<CollectAdapter>();
        MiTems=new ArrayList<List<Product>>();
        listViews=new ArrayList<ViewGroup>();
        mAdapter=new ViewPagerAdapter(listViews);
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                lk.smoothScrollToPosition(position);
                Adapter.setPosition(position);
                mNomore = false;
                mPage = 1;
                states = position;
                supplierId = data.get(position)._supplier;
                shangpingList.clear();
                shangpingList.addAll(MiTems.get(position));
                if(((ListView)listViews.get(position).findViewById(R.id.lv)).getChildCount()>0){
                    // setTotal_price();
                    return;
                }
                loadProducts();
            }

            public void onPageScrollStateChanged(int state) {

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEditting){
                    edit.setText(getActivity().getString(R.string.finish));
                }else{
                    edit.setText(getActivity().getString(R.string.edit));
                }
                isEditting=!isEditting;
                for(int i=0;i<adapters.size();i++){
                    adapters.get(i).ReSet_AK();
                }
            }
        });


        Adapter.setOnItemClickListener(new CartTopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mPage = 1;
                mNomore = false;
                supplierId = data.get(position)._supplier;
                states = position;
                viewPager.setCurrentItem(position);
                shangpingList.clear();
                shangpingList.addAll(MiTems.get(position));
                if(((ListView)listViews.get(position).findViewById(R.id.lv)).getChildCount()>0){
                    // setTotal_price();
                    return;
                }
                loadProducts();
            }
        });
        lk.setAdapter(Adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        lk.setLayoutManager(linearLayoutManager);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getActivity().getString(R.string.loading));
        dialog.show();
        loadContractList();
    }

    public void onPause() {
        super.onPause();
        List<OrderSubmit> orderList=new ArrayList<OrderSubmit>();
        List<Contract> contractList=globalProvider.contractListToCart;
        for (int i = 0; i < globalProvider.contractListToCart.size(); i++) {
            OrderSubmit order = new OrderSubmit();
            order.products = new ArrayList<Product>();
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
        globalProvider.orders.clear();
        globalProvider.orders.addAll(orderList);
    }

    public void doSetFreshListener(SwipeRefreshLayout mSwipeRefreshlayout){
        mSwipeRefreshlayout.setOnRefreshListener(this);
    }

    public void doSetScrollListener(ListView lv){
        lv.setOnScrollListener(this);
    }

    public void loadContractList() {

        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, Constants.contractUrlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                parseContractList(response);
                if (data.size() > 0) {
                    supplierId = data.get(0)._supplier;
                    loadProducts();

            }
        }}, new Response.ErrorListener() {
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

    public void parseContractList(String json) {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonParser jsonParser = jsonFactory.createJsonParser(json);
            ContractList contractList = (ContractList) objectMapper.readValue(jsonParser, ContractList.class);
            this.data.clear();
            this.data.addAll(contractList.contracts);
            listViews.clear();
            MiTems.clear();
            adapters.clear();
            for(int i=0;i<data.size();i++){
                listViews.add((LinearLayout) (inflater.inflate(R.layout.collect_pager_layout, null)).findViewById(R.id.viewPager_layout));
                MiTems.add(new ArrayList<Product>());
            }
            for(int i=0;i<data.size();i++){
                adapters.add(new CollectAdapter(getActivity(), MiTems.get(i), this));
            }
            for(int i=0;i<data.size();i++){
                ((DragSortListView) listViews.get(i).findViewById(R.id.lv)).setAdapter(adapters.get(i));
            }
            for(int i=0;i<data.size();i++){
                doSetFreshListener((SwipeRefreshLayout)listViews.get(i).findViewById(R.id.swiperefresh));
                doSetScrollListener(((DragSortListView) listViews.get(i).findViewById(R.id.lv)));
                ((DragSortListView) listViews.get(i).findViewById(R.id.lv)).setDragEnabled(true);
                final int finalI = i;
                ((DragSortListView) listViews.get(i).findViewById(R.id.lv)).setDropListener(new DragSortListView.DropListener() {
                    @Override
                    public void drop(int from, int to) {
                        if(from>to){
                            int index=MiTems.get(finalI).get(from).index;
                            MiTems.get(finalI).get(from).index=MiTems.get(finalI).get(to).index;
                            for(int b=0;b<from-to;b++){
                                if(b==from-to-1){
                                    MiTems.get(finalI).get(from - 1).index=index;
                                }else{
                                    MiTems.get(finalI).get(to + b).index=MiTems.get(finalI).get(to + b + 1).index;
                                }
                            }
                        }else if(from<to){
                            int index=MiTems.get(finalI).get(from).index;
                            MiTems.get(finalI).get(from).index=MiTems.get(finalI).get(to).index;
                            for(int b=0;b<to-from;b++){
                                if(b==to-from-1){
                                    MiTems.get(finalI).get(from + 1).index=index;
                                }else{
                                    MiTems.get(finalI).get(to - b).index=MiTems.get(finalI).get(to - b - 1).index;
                                }
                            }
                        }
                        Product item = (Product) adapters.get(finalI).getItem(from);//得到listview的适配器
                        adapters.get(finalI).remove(from);//在适配器中”原位置“的数据。
                        adapters.get(finalI).insert(item, to);

                        doResetIndex(MiTems.get(finalI), finalI,from,to);
                    }
                });
            }
            mAdapter.notifyDataSetChanged();
            states=0;
            if(data.size()>0){
                viewPager.setCurrentItem(0);
            }
            Adapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doResetIndex(List<Product> list, final int i, final int from, final int to){

        Update_index_body body=new Update_index_body();
        body.collects=new ArrayList<Update_index>();
        for(int a=0;a<list.size();a++){
            Update_index update=new Update_index();
            update._product=list.get(a).get_id();
            update.index=list.get(a).index;
            update._customer=globalProvider.me._id;
            body.collects.add(update);
        }
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = "";


        try {

            json = ow.writeValueAsString(body);


            JSONObject jsonObj = new JSONObject(json);

           // ByteArrayEntity entity= new ByteArrayEntity(json.getBytes("UTF-8"));

          JsonObjectRequest jsonRequest=new JsonObjectRequest(Request.Method.PUT, Constants.updateIndexUrlStr,jsonObj, new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                  Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.suc_to_update), Toast.LENGTH_SHORT).show();

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
          globalProvider.addRequest(jsonRequest);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadProducts(){
        mPage = 1;
        //TODO add more parameters
      /*  Map<String,String> params = new HashMap<>();
        params.put("page", mPage);
        params.put("itemsPerPage", mItemsPerPage);
        params.put("_supplier",supplierId);
        */
        String url=Constants.collectedStr+"?"+"_supplier"+"="+supplierId+"&"+"itemsPerPage"+"="+mItemsPerPage;
        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseShoucangList(response);
                ((SwipeRefreshLayout)listViews.get(states).findViewById(R.id.swiperefresh)).setRefreshing(false);

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

    public void  parseShoucangList(String json){
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonParser jsonParser = jsonFactory.createJsonParser(json);
            ProductList productList = (ProductList) objectMapper.readValue(jsonParser,ProductList.class);
            shangpingList.clear();
            shangpingList.addAll(productList.products);

            if(globalProvider.shangpingList.size()>0){
                for(int i=0;i<globalProvider.shangpingList.size();i++){
                    for(int a=0;a<productList.products.size();a++){
                        if(globalProvider.shangpingList.get(i).get_id().equals(productList.products.get(a).get_id())){
                            productList.products.get(a).setOrderQuantity(globalProvider.shangpingList.get(i).getOrderQuantity());
                        }
                    }
                }
            }
            // setTotal_price();
            this.MiTems.get(states).clear();
            this.MiTems.get(states).addAll(productList.products);
            adapters.get(states).notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMoreProductList(){
        mPage = mPage + 1;
        //TODO add param mPage

        String url=Constants.collectedStr+"?"+"_supplier"+"="+supplierId+"&"+"itemsPerPage"+"="+mItemsPerPage;
        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseMoreCollecList(response);
                ((SwipeRefreshLayout)listViews.get(states).findViewById(R.id.swiperefresh)).setRefreshing(false);

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

    public void parseMoreCollecList(String json) {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonParser jsonParser = jsonFactory.createJsonParser(json);
            ProductList productList = (ProductList) objectMapper.readValue(jsonParser,ProductList.class);
            if(productList.products.size()<mItemsPerPage){
                mNomore = true;
            }
            shangpingList.addAll(productList.products);
            if(globalProvider.shangpingList.size()>0){
                for(int i=0;i<globalProvider.shangpingList.size();i++){
                    for(int a=0;a<productList.products.size();a++){
                        if(globalProvider.shangpingList.get(i).get_id().equals(productList.products.get(a).get_id())){
                            productList.products.get(a).setOrderQuantity(globalProvider.shangpingList.get(i).getOrderQuantity());
                        }
                    }
                }
            }
            //setTotal_price();
            this.MiTems.get(states).addAll(productList.products);
            adapters.get(states).notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ChangeNotifyDataSetChanged(String id,float num){
        for(int i=0;i<shangpingList.size();i++){
            if(shangpingList.get(i).get_id().equals(id)){
                shangpingList.get(i).setOrderQuantity(num);
            }
        }

        Adapter.notifyDataSetChanged();
    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect, null);
        globalProvider=GlobalProvider.getInstance(getActivity());
        return view;
    }

    public void deletecollected(final String _id){
        String url=Constants.collectedStr + "/" +_id;
        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                for (int i = 0; i < MiTems.get(states).size(); i++) {
                    if (MiTems.get(states).get(i).get_id().equals(_id)) {
                        MiTems.get(states).remove(MiTems.get(states).get(i));
                        adapters.get(states).notifyDataSetChanged();
                    }
                }
                shangpingList.clear();
                shangpingList.addAll(MiTems.get(states));
//                setTotal_price();
                for (int i = 0; i < globalProvider.shangpingList.size(); i++) {
                    if (globalProvider.shangpingList.get(i).get_id().equals(_id)) {
                        globalProvider.shangpingList.remove(globalProvider.shangpingList.get(i));


                        if (globalProvider.shangpingList.size() > 0) {

                                MainActivity.getDefault().setCartNum();

                        } else {


                                MainActivity.getDefault().HideCartNum();


                    }
                }
                Adapter.notifyDataSetChanged();

            }
        }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

       globalProvider.addRequest(utf8JsonRequest);
    }

    public void onRefresh() {
        mNomore = false;
        mPage = 1;
        loadProducts();
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if((ListView) listViews.get(states).findViewById(R.id.lv)!=null) {
            if (!mNomore && ((ListView) listViews.get(states).findViewById(R.id.lv)).getCount() != 0 && ((ListView) listViews.get(states).findViewById(R.id.lv)).getLastVisiblePosition() >= (((ListView) listViews.get(states).findViewById(R.id.lv)).getCount() - 2)) {
                loadMoreProductList();
            }
        }
    }
    public List Data(){
        List<Collect> list=new ArrayList<Collect>();
        Collect collect=new Collect();
        collect.setName("大白菜");
        collect.setUnitPrice("5.0/公斤");
        list.add(collect);

        Collect collect1=new Collect();
        collect1.setName("大白菜");
        collect1.setUnitPrice("5.0/公斤");
        list.add(collect1);

        Collect collect2=new Collect();
        collect2.setName("大白菜");
        collect2.setUnitPrice("5.0/公斤");
        list.add(collect2);
        return list;
    }
}
