package com.samiapps.kv.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;

/**
 * Created by KV on 27/2/18.
 */

public class FragmentCart extends Fragment {
    public List<Contract> mItems;
    public ListView lv;
    public View headerView;
    public List<Product> shangpingList;
    public int singel;
    private RecyclerView recycleview;
    private CartTopAdapter adapter;
    public CartListAdapter Adapter;
    public TextView total_price;
    public float price=0;
    private RelativeLayout add_shouhuo;
    private RelativeLayout add_fapiao;
    private String shipingAdress_Str="";
    private String bilingAdress_Str="";
    private TextView shipingAdress;
    private TextView bilingAdress;
    private int chooseItem_one;
    private int chooseItem_two;
    private int month;
    private int Day;
    public boolean DateConfirmedOrNot=false;
    public boolean DateIsDirecteOrNot=true;
    public String Str_data="";
    private RelativeLayout chooseData;
    private OrderSubmit order;
    private RelativeLayout add_beizhu;
    private TextView data;
    private String Str_feedback="";
    private TextView beizhu_text;
    private Button submit;
    private RelativeLayout cart_exit;
    private LinearLayout cart_none;
    public TextView edit;
    TextView goHmText;
    public boolean isEditting=false;
    public TextView GoToProduct;
    public String[] items_shiping=new String[1];
    public String[] items_biling=new String[1];
    public ProgressDialog dialog;

    GlobalProvider globalProvider;
    public List<OrderSubmit> orders;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalProvider=GlobalProvider.getInstance(getActivity());
        orders=globalProvider.orders;
        mItems= globalProvider.contractListToCart;
        shangpingList=new ArrayList<Product>();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, null);
        GoToProduct=(TextView)view.findViewById(R.id.GoToProduct);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LayoutInflater inflater =  (LayoutInflater)getActivity().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        cart_exit= (RelativeLayout) getActivity().findViewById(R.id.cart_exit);
        cart_none= (LinearLayout) getActivity().findViewById(R.id.cart_none);
        if(globalProvider.shangpingList.size()==0){
            cart_exit.setVisibility(View.GONE);
            cart_none.setVisibility(View.VISIBLE);
        }else{
            cart_exit.setVisibility(View.VISIBLE);
            cart_none.setVisibility(View.GONE);
        }
        headerView=inflater.inflate(R.layout.header_view,lv, false);
        init();
        GoToProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (globalProvider.adjustToStart.equals("product")) {

                        MainActivity.getDefault().setSelect(5);
                        MainActivity.getDefault().item = 5;
                    } else {
                        MainActivity.getDefault().setSelect(0);
                        MainActivity.getDefault().item = 0;
                    }


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
                Adapter.ReSet_AK();
            }
        });
        //TODO finish this activity
        add_shouhuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditShippingAddressActivity.class);
                (getActivity()).startActivityForResult(intent, Constants.NEWSHIPPINGADDRESS);
            }
        });
        //TODO finish this activity1
        add_fapiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditBillingAddressActivity.class);
                (getActivity()).startActivityForResult(intent, Constants.NEWSHIPPINGADDRESS);
            }
        });
        final Calendar c = Calendar.getInstance();
        month=c.get(Calendar.MONTH)+1;

        chooseData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        if(year<c.get(Calendar.YEAR)){
                            //DateIsDirecteOrNot=false;
                            showMistake();
                            return;
                        }else if(year==c.get(Calendar.YEAR)&&(monthOfYear + 1)<month){
                            //DateIsDirecteOrNot=false;
                            showMistake();
                            return;
                        }else if(year==c.get(Calendar.YEAR)&&(monthOfYear + 1)==month&&dayOfMonth<c.get(Calendar.DAY_OF_MONTH)){
                            // DateIsDirecteOrNot=false;
                            showMistake();
                            return;
                        }
                        Toast.makeText(getActivity(), year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, Toast.LENGTH_SHORT).show();
                        Str_data = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        data.setText(Str_data);
                        try {
                            globalProvider.orders.get(globalProvider.whichOrder).setShippingDate(ConverToDate(Str_data));
                            order.setShippingDate(ConverToDate(Str_data));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        try {
//                                globalProvider.data_Str=ConverToDate(Str_data);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                        //DateIsDirecteOrNot=true;
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePicker.show();

            }
        });
        //Str_feedback=globalProvider.feedback_Str;

        add_beizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BeizhuActivity.class);
                intent.putExtra("beizhu", order.getFeedback());
                getActivity().startActivityForResult(intent, Constants.ADDFEEDBACK);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setEnabled(false);
                if(order.getBillingAddress().equals("")||order.getShippingAddress().equals("")){
                    new AlertDialog.Builder(getActivity())
                            .setMessage(getActivity().getString(R.string.ninhaimeiyoutianjiashouhuo))
                            .setPositiveButton(getActivity().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    submit.setEnabled(true);
                    return;
                }
                if(data.getText().equals(getActivity().getString(R.string.xuanzeriqi))){
                    new AlertDialog.Builder(getActivity())
                            .setMessage(getActivity().getString(R.string.qingxuanzesonghuoshijian))
                            .setPositiveButton(getActivity().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //DateConfirmedOrNot=true;
                                }
                            }).show();
                    submit.setEnabled(true);
                    return;
                }

                new AlertDialog.Builder(getActivity()).setTitle(getActivity().getString(R.string.xitongtishi))
                        .setMessage(getActivity().getString(R.string.quedingtijiaoorder))
                        .setNegativeButton(getActivity().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //submit.setEnabled(false);
                                try {
                                    postSubmit();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setPositiveButton(getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                submit.setEnabled(true);
                            }
                        }).show();
                submit.setEnabled(true);
            }
        });
        lv.addHeaderView(headerView);
        Adapter=new CartListAdapter(getActivity(),shangpingList,this);
        lv.setAdapter(Adapter);
        setSelect();
        adapter=new CartTopAdapter(getActivity(),mItems);
        adapter.setPosition(globalProvider.whichOrder);
        recycleview.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recycleview.setLayoutManager(linearLayoutManager);
        adapter.setOnItemClickListener(new CartTopAdapter.OnItemClickListener() {

            public void onItemClick(View view, int position) {
                globalProvider.whichOrder = position;
                setSelect();
            }
        });
    }

    private void init() {
        // GoToProduct= (TextView) getActivity().findViewById(R.id.GoToProduct);

        recycleview= (RecyclerView) getActivity().findViewById(R.id.lk);
        edit= (TextView) headerView.findViewById(R.id.edit);
        lv= (ListView) getActivity().findViewById(R.id.lv);
        submit=(Button)getActivity().findViewById(R.id.submit);
        total_price= (TextView) getActivity().findViewById(R.id.total_price);
        shipingAdress= (TextView)headerView.findViewById(R.id.shouhuoAdress);
        bilingAdress= (TextView) headerView.findViewById(R.id.billingAdress);
        beizhu_text= (TextView) headerView.findViewById(R.id.beizhu_text);
        data= (TextView) headerView.findViewById(R.id.data);

        add_shouhuo= (RelativeLayout)headerView.findViewById(R.id.add_shouhuo);
        add_fapiao= (RelativeLayout) headerView.findViewById(R.id.add_fapiao);
        chooseData= (RelativeLayout) headerView.findViewById(R.id.chooseData);
        add_beizhu= (RelativeLayout) headerView.findViewById(R.id.add_beizhu);
    }

    public void showMistake(){
        Toast.makeText(getActivity(),getActivity().getString(R.string.errordate), Toast.LENGTH_SHORT).show();
        data.setText(getActivity().getString(R.string.xuanzeriqi));
        Str_data="";
    }
    public void showCartByProductNum(){
        if(globalProvider.shangpingList.size()==0){
            cart_exit.setVisibility(View.GONE);
            cart_none.setVisibility(View.VISIBLE);
            return;
        }else{
            cart_exit.setVisibility(View.VISIBLE);
            cart_none.setVisibility(View.GONE);
        }
    }

    public void setSelect(){
        shangpingList.clear();
        if(globalProvider.orders.size()>0){
            if(globalProvider.whichOrder>=globalProvider.orders.size()){
                globalProvider.whichOrder=0;
            }
            order=globalProvider.orders.get(globalProvider.whichOrder);
        }
        if(order!=null) {
            if (order.getFeedback() != null && !order.getFeedback().equals("")) {
                beizhu_text.setText(order.getFeedback());
            } else {
                beizhu_text.setText(getActivity().getString(R.string.add_beizhu));
            }
            if (order.getShippingDate() != null) {
                data.setText(ConverToString(order.getShippingDate()));
            } else {
                data.setText(getActivity().getString(R.string.xuanzeriqi));
            }
            if(order.getBillingAddress()!=null){
                bilingAdress.setText(order.getBillingAddress());
            }else{
                bilingAdress.setText(globalProvider.me._customerProfile.getBillingAddress());
                order.setBillingAddress(globalProvider.me._customerProfile.getBillingAddress());
            }
            if(order.getShippingAddress()!=null){
                shipingAdress.setText(order.getShippingAddress());
            }else{
                shipingAdress.setText(globalProvider.me._customerProfile.getShippingAddress());
                order.setShippingAddress(globalProvider.me._customerProfile.getShippingAddress());
            }
        }
        if(mItems.size()>0){
            Log.d("checksz",""+order.products.size());
            for(int i=0;i<order.products.size();i++){
                if(order.products.get(i)._supplier==null&&globalProvider.order._supplier._id!=null) {
                    order.products.get(i).set_supplier(globalProvider.order._supplier._id);
                }
            }
            shangpingList.addAll(order.products);
        }
        setTotal_price();
        Adapter.notifyDataSetChanged();
    }

    public void setTotal_price( ){
        price=0;
        for(int b=0;b<shangpingList.size();b++){
            float a=price+shangpingList.get(b).getOrderQuantity()*shangpingList.get(b).getUnitPrice();
            price=(float)(Math.round(a*100))/100;
            //price=price+shangpingList.get(b).getOrderQuantity()*shangpingList.get(b).getUnitPrice();
        }
        total_price.setText(price + "");
    }

    public void ChangeNotifyDataSetChanged(int position,float num){
        shangpingList.get(position).setOrderQuantity(num);
        order.products.get(position).setOrderQuantity(num);
        showCartByProductNum();
        setTotal_price();
    }
    public void DeleteNotifyDataSetChanged(int position){
        order.products.remove(shangpingList.get(position));
        globalProvider.orders.get(globalProvider.whichOrder).products.remove(shangpingList.get(position));
        shangpingList.clear();
        shangpingList.addAll(order.products);
        Adapter.notifyDataSetChanged();
        setTotal_price();
        showCartByProductNum();

        if(shangpingList.size()==0){
            globalProvider.contractListToCart.remove(mItems.get(globalProvider.whichOrder));
            globalProvider.orders.remove(order);

            if(globalProvider.whichOrder==0){
                globalProvider.editOrder._id="";
                globalProvider.order=new Order();
            }
            globalProvider.whichOrder=0;
            adapter.notifyDataSetChanged();
            adapter.setPosition(0);
            setSelect();
        }else{
            adapter.notifyDataSetChanged();
        }
    }
//TODO Home
    private void postSubmit() throws Exception {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getActivity().getString(R.string.ordering));
        dialog.show();
        for(int i=0;i< order.products.size();i++){
            order.products.get(i)._product=order.products.get(i)._id;
        }
        order._id=globalProvider.editOrder._id;
        order.set_supplier(mItems.get(globalProvider.whichOrder)._supplier);
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(order);
            JSONObject obj=new JSONObject(json);
            ByteArrayEntity entity= new ByteArrayEntity(json.getBytes("UTF-8"));

            if(globalProvider.whichOrder==0&&order._id!=null&&!order._id.equals("")){
                String url=Constants.orderListUrlStr+"/"+order._id;

                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, url, obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        goBack();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(),getResources().getString(R.string.unedit),Toast.LENGTH_SHORT).show();
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


            }else{

                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Constants.orderListUrlStr, obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        goBack();

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
                globalProvider.addRequest(jsonObjectRequest);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dealError(String json) {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonParser jsonParser = jsonFactory.createParser(json);
            BigError error = (BigError) objectMapper.readValue(jsonParser,BigError.class);
            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void GoToAlertOrNo(){


        Utf8JsonRequest utfreq=new Utf8JsonRequest(Request.Method.GET, Constants.orderListUrlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonParser jsonParser = jsonFactory.createParser(response);
                    OrderList orderList  = (OrderList) objectMapper.readValue(jsonParser, OrderList.class);
                    if (orderList.orders.size() > 0) {
                        if (String.valueOf(orderList.orders.get(0).orderDate.getMonth()).equals(String.valueOf(new Date().getMonth())) && String.valueOf(orderList.orders.get(0).orderDate.getDate()).equals(String.valueOf(new Date().getDate()))) {
                            globalProvider.isShopping=true;
                        }else{
                            globalProvider.isShopping=false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
                headers = globalProvider.addHeaderToken(getActivity());


                //headers=globalProvider.addHeaderToken(getActivity());
                return headers;
            }
        };
        globalProvider.addRequest(utfreq);


    }
    public void goBack() {
        Synchronize();
        GoToAlertOrNo();
        new AlertDialog.Builder(getActivity())
                .setMessage(getActivity().getString(R.string.xiadanchenggong))
                .setPositiveButton(getActivity().getString(R.string.confirm), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.i("alertdialog", " 保存数据");

                    }
                }).setNegativeButton(getActivity().getString(R.string.checkOrder), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                    MainActivity.getDefault().setSelect(2);
                    MainActivity.getDefault().item = 2;

            }
        }).show();
        submit.setEnabled(true);
    }
    public void Synchronize(){
        if(globalProvider.whichOrder==0){
            globalProvider.editOrder._id="";
            globalProvider.order=new Order();
        }
        for(int i=0;i<shangpingList.size();i++){
            for(int a=0;a<globalProvider.shangpingList.size();a++){
                if(shangpingList.get(i).get_id().equals(globalProvider.shangpingList.get(a)._id)){
                    globalProvider.shangpingList.remove(globalProvider.shangpingList.get(a));
                }
            }
        }
        if (globalProvider.shangpingList.size() > 0) {

                MainActivity.getDefault().setCartNum();

            cart_exit.setVisibility(View.VISIBLE);
            cart_none.setVisibility(View.GONE);
        } else {

                MainActivity.getDefault().HideCartNum();


            cart_exit.setVisibility(View.GONE);
            cart_none.setVisibility(View.VISIBLE);
            //show();
        }
        for(int i=0;i<shangpingList.size();i++){
            for(int a=0;a<globalProvider.shangpingListDefault.size();a++){
                if(shangpingList.get(i).get_id().equals(globalProvider.shangpingListDefault.get(a)._id)){
                    globalProvider.shangpingListDefault.get(a).setOrderQuantity(0);
                }
            }
        }
        shangpingList.clear();

        globalProvider.contractListToCart.remove(mItems.get(globalProvider.whichOrder));
        globalProvider.orders.remove(order);
        //orders.remove(order);
        globalProvider.whichOrder=0;
        adapter.notifyDataSetChanged();
        adapter.setPosition(0);
        setSelect();
    }
    public static Date ConverToDate(String strDate) throws Exception
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }

    public static String ConverToString(Date date)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(date);
    }
    public int setDay(int Month){
        Calendar c = Calendar.getInstance();
        int day=0;
        switch (Month){
            case 1:
                if(c.get(Calendar.DAY_OF_MONTH)>30){

                    day=1;
                    month=month+1;
                }else if(c.get(Calendar.DAY_OF_MONTH)<=30){

                    day=c.get(Calendar.DAY_OF_MONTH)+1;
                }

                break;
            case 2:
                if(c.get(Calendar.DAY_OF_MONTH)>29){

                    day=1;
                    month=month+1;
                }else if(c.get(Calendar.DAY_OF_MONTH)<=29){

                    day=c.get(Calendar.DAY_OF_MONTH)+1;
                }
                break;
            case 3:
                if(c.get(Calendar.DAY_OF_MONTH)>30){

                    day=1;
                    month=month+1;
                }else if(c.get(Calendar.DAY_OF_MONTH)<=30){

                    day=c.get(Calendar.DAY_OF_MONTH)+1;
                }
                break;
            case 4:
                if(c.get(Calendar.DAY_OF_MONTH)>29){

                    day=1;
                    month=month+1;
                }else if(c.get(Calendar.DAY_OF_MONTH)<=29){

                    day=c.get(Calendar.DAY_OF_MONTH)+1;
                }
                break;
            case 5:
                if(c.get(Calendar.DAY_OF_MONTH)>30){

                    day=1;
                    month=month+1;
                }else if(c.get(Calendar.DAY_OF_MONTH)<=30){

                    day=c.get(Calendar.DAY_OF_MONTH)+1;
                }
                break;
            case 6:
                if(c.get(Calendar.DAY_OF_MONTH)>29){

                    day=1;
                    month=month+1;
                }else if(c.get(Calendar.DAY_OF_MONTH)<=29){

                    day=c.get(Calendar.DAY_OF_MONTH)+1;
                }
                break;
            case 7:
                if(c.get(Calendar.DAY_OF_MONTH)>30){

                    day=1;
                    month=month+1;
                }else if(c.get(Calendar.DAY_OF_MONTH)<=30){

                    day=c.get(Calendar.DAY_OF_MONTH)+1;
                }
                break;
            case 8:
                if(c.get(Calendar.DAY_OF_MONTH)>30){

                    day=1;
                    month=month+1;
                }else if(c.get(Calendar.DAY_OF_MONTH)<=30){

                    day=c.get(Calendar.DAY_OF_MONTH)+1;
                }
                break;
            case 9:
                if(c.get(Calendar.DAY_OF_MONTH)>29){

                    day=1;
                    month=month+1;
                }else if(c.get(Calendar.DAY_OF_MONTH)<=29){

                    day=c.get(Calendar.DAY_OF_MONTH)+1;
                }
                break;
            case 10:
                if(c.get(Calendar.DAY_OF_MONTH)>30){

                    day=1;
                    month=month+1;
                }else if(c.get(Calendar.DAY_OF_MONTH)<=30){

                    day=c.get(Calendar.DAY_OF_MONTH)+1;
                }
                break;
            case 11:
                if(c.get(Calendar.DAY_OF_MONTH)>29){

                    day=1;
                    month=month+1;
                }else if(c.get(Calendar.DAY_OF_MONTH)<=29){

                    day=c.get(Calendar.DAY_OF_MONTH)+1;
                }
                break;
            case 12: if(c.get(Calendar.DAY_OF_MONTH)>30){

                day=1;
                month=1;
            }else if(c.get(Calendar.DAY_OF_MONTH)<=30){

                day=c.get(Calendar.DAY_OF_MONTH)+1;
            }
                break;



        }

        return day;
    }

}
