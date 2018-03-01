package com.samiapps.kv.myapplication;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KV on 20/2/18.
 */
public class FragmentContract extends Fragment {

    private ListView lv;

    public List<Contract> mItems;

    private RecyclerView recycleview;
    private ContractHeaderAdapter adapter;
    private ContractListAdapter mAdapter;
    private List<Category> data;
    private Boolean mNomore;
    GlobalProvider globalProvider;

    private List<List<Contract>> MiTems;
    public int states = 0;
    public List<ContractListAdapter> adapters;
    public List<ViewGroup> listViews;
    private ViewPager viewPager;
    public ViewPagerAdapter Adapter;
    public LayoutInflater inflater;

    private SwipeRefreshLayout mSwipeRefreshlayout;
    private ConnectivityManager mConnectivityManager;

    private NetworkInfo netInfo;
    public ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItems = new ArrayList<Contract>();
        data = new ArrayList<Category>();
        mNomore = false;
        globalProvider = GlobalProvider.getInstance(getActivity());


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, null);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);
        inflater = getActivity().getLayoutInflater();
        adapters = new ArrayList<ContractListAdapter>();
        MiTems = new ArrayList<List<Contract>>();
        listViews = new ArrayList<ViewGroup>();
        Adapter = new ViewPagerAdapter(listViews);
        viewPager.setAdapter(Adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                recycleview.smoothScrollToPosition(position);
                adapter.setPosition(position);
                states = position;
            }

            public void onPageScrollStateChanged(int state) {
            }
        });

        recycleview = (RecyclerView) getActivity().findViewById(R.id.lk);
        adapter = new ContractHeaderAdapter(getActivity(), data);
        recycleview.setAdapter(adapter);
        adapter.setOnItemClickListener(new ContractHeaderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewPager.setCurrentItem(position);
                states = position;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycleview.setLayoutManager(linearLayoutManager);
        adapter.notifyDataSetChanged();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getActivity().getString(R.string.loading));
        dialog.show();
        loadCategoryList();
    }

    public void doSetOnItemClick(ListView lv) {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                globalProvider.contract = MiTems.get(states).get(position);
                MainActivity.getDefault().setSelect(5);
                globalProvider.adjustToStart = "product";
            }
        });
    }

    public void loadCategoryList() {

        Utf8JsonRequest stringRequest=new Utf8JsonRequest(Request.Method.GET, Constants.categoryUrlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                parseCategoryList(response);
                loadContractList();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errvolley", error.toString());

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
        globalProvider.addRequest(stringRequest);



    }

    public void parseCategoryList(String json) {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonParser jsonParser = jsonFactory.createParser(json);

            CategoryList categoryList = (CategoryList) objectMapper.readValue(jsonParser, CategoryList.class);
            this.data.clear();
            this.data.addAll(categoryList.categorys);
            listViews.clear();
            MiTems.clear();
            adapters.clear();
            for (int i = 0; i < data.size(); i++) {
                listViews.add((LinearLayout) (inflater.inflate(R.layout.view_pager_listview, null)).findViewById(R.id.viewPager_layout));
                MiTems.add(new ArrayList<Contract>());
                //SwipeRefreshLayoutList.add((SwipeRefreshLayout) (inflater.inflate(R.layout.view_pager_layout, null)).findViewById(R.id.swiperefresh));
                //adapters.add(new ProductListAdapter(getActivity(), MiTems.get(i), this));
            }
            //sinal=new ArrayList<Integer>();
            for (int i = 0; i < data.size(); i++) {
                adapters.add(new ContractListAdapter(getActivity(), MiTems.get(i)));
            }
            for (int i = 0; i < data.size(); i++) {
                ((ListView) listViews.get(i).findViewById(R.id.lv)).setAdapter(adapters.get(i));
            }
            for (int i = 0; i < data.size(); i++) {
                doSetOnItemClick(((ListView) listViews.get(i).findViewById(R.id.lv)));
                //doSetScrollListener(((ListView) listViews.get(i).findViewById(R.id.lv)));
            }
            Adapter.notifyDataSetChanged();
            states = 0;
            viewPager.setCurrentItem(0);
            //category=data.get(0)._supplier;

            adapter.notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadContractList() {

        Utf8JsonRequest strRequest=new Utf8JsonRequest(Request.Method.GET, Constants.contractUrlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                parseContractList(response);
                loadMe();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volleryr",error.toString());

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
               /* HashMap<String, String> headers = new HashMap<String, String>();

                String token = Constants.getToken(getActivity());
                Log.d("gettoken", token);
                headers.put("Authorization",token);
                String language=Constants.getGetLanguage(getActivity());
                headers.put("Language",language);
                */


                Map<String, String> headers = new HashMap<String, String>();
                headers=globalProvider.addHeaderToken(getActivity());
                Log.d("langchk",headers.get("Language"));
                Log.d("tokenchk",headers.get("Authorization"));





                return headers;
            }
        };
        globalProvider.addRequest(strRequest);
    }

    public void saveMyBitmap(String bitName, Bitmap mBitmap)
            throws IOException {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + bitName;
//        File tmp = new File("/sdcard/pepper/");
//        if (!tmp.exists()) {
//            tmp.mkdir();
//        }
        File f = new File(path);
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseContractList(String json) {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            JsonParser jsonParser = jsonFactory.createParser(json);

            final ContractList contractList = (ContractList) objectMapper.readValue(jsonParser, ContractList.class);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //String url ="http://ww3.sinaimg.cn/bmiddle/6e91531djw1e8l3c7wo7xj20f00qo755.jpg";
                    Bitmap tmpBitmap = null;
                    try {
                        for (int i = 0; i < contractList.contracts.size(); i++) {
                            String Url = Constants.imgBaseUrlStr + "/" + contractList.contracts.get(i)._supplierInfo.getInvoiceHeader();
                            InputStream is = new java.net.URL(Url).openStream();
                            tmpBitmap = BitmapFactory.decodeStream(is);
                            saveMyBitmap(contractList.contracts.get(i)._supplierInfo.getInvoiceHeader(), tmpBitmap);
                            is.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            //}
            globalProvider.contractList.clear();
            globalProvider.contractList.addAll(contractList.contracts);
            //this.mItems.clear();

            for (int a = 0; a < data.size(); a++) {
                MiTems.get(a).clear();
                for (int i = 0; i < contractList.contracts.size(); i++) {
                    if (data.get(a).getId().equals(contractList.contracts.get(i)._supplierInfo.getCategory())) {
                        MiTems.get(a).add(contractList.contracts.get(i));
                    }
                }
                adapters.get(a).notifyDataSetChanged();
            }
            //this.mItems.addAll(contractList.contracts);
            //mAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMe() {

        Utf8JsonRequest stringRequest = new Utf8JsonRequest(Request.Method.GET, Constants.meUrlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseMe(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                String token = Constants.getToken(getActivity());
                Log.d("gettoken", token);
                headers.put("Authorization",token);
                return headers;
            }


        };
        globalProvider.addRequest(stringRequest);
    }

    public void parseMe(String json) {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonParser jsonParser = jsonFactory.createParser(json);
            Me me = (Me) objectMapper.readValue(jsonParser, Me.class);
            globalProvider.me = me;
            GoToAlertOrNo();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void GoToAlertOrNo() {

        CustomRequest stringRequest=new CustomRequest(Request.Method.GET, Constants.orderListUrlStr, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Log.d("checkalertres",response.toString());
                    JsonParser jsonParser = jsonFactory.createParser(response.toString());
                    OrderList orderList = (OrderList) objectMapper.readValue(jsonParser, OrderList.class);
                    if (orderList.orders.size() > 0) {
                        if ((String.valueOf(orderList.orders.get(0).orderDate.getMonth()).equals(String.valueOf(new Date().getMonth())) && String.valueOf(orderList.orders.get(0).orderDate.getDate()).equals(String.valueOf(new Date().getDate())))) {
                            getSingalOrder(orderList.orders.get(0)._id);
                        } else {
                            globalProvider.isShopping = false;
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
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                String token = Constants.getToken(getActivity());
                Log.d("gettoken", token);
                headers.put("Authorization",token);
                return headers;
            }

        }
                ;
        globalProvider.addRequest(stringRequest);


    }

    public void getSingalOrder(String id) {


        String url = Constants.orderListUrlStr + "/" + id;
        Utf8JsonRequest stringRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseSingalOrder(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((SwipeRefreshLayout) listViews.get(states).findViewById(R.id.swiperefresh)).setRefreshing(false);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                String token = Constants.getToken(getActivity());
                Log.d("gettoken", token);
                headers.put("Authorization",token);
                return headers;
            }


        };

     globalProvider.addRequest(stringRequest);
    }

    public void parseSingalOrder(String json) {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonParser jsonParser = jsonFactory.createParser(json);
            SingalOrder singalOrder = (SingalOrder) objectMapper.readValue(jsonParser, SingalOrder.class);
            if (singalOrder.status.equals("waitForPicking")) {
                globalProvider.isShopping = true;
            } else {
                globalProvider.isShopping = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
