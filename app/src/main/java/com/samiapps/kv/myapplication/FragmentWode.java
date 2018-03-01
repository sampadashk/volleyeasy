package com.samiapps.kv.myapplication;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by KV on 21/2/18.
 */

public class FragmentWode extends Fragment {
    public TextView name;
    public TextView gongsi_name;
    public TextView mob_number;
    public RelativeLayout finished;
    public RelativeLayout myInfo;
    public RelativeLayout myDeal;
    public RelativeLayout fankui;
    public RelativeLayout layout_language;
    public TextView ConfirmInfo;
    public TextView language;
    public RelativeLayout aboutUs;
    public int flag;
    GlobalProvider globalProvider;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wode, null);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        globalProvider=GlobalProvider.getInstance(getActivity());
        finished= (RelativeLayout) getActivity().findViewById(R.id.finished);
        myInfo= (RelativeLayout) getActivity().findViewById(R.id.layout_myInfo);
        myDeal= (RelativeLayout) getActivity().findViewById(R.id.layout_mydeal);
        ConfirmInfo= (TextView) getActivity().findViewById(R.id.ConfirmInfo);
        fankui= (RelativeLayout) getActivity().findViewById(R.id.fankui);
        aboutUs= (RelativeLayout) getActivity().findViewById(R.id.aboutUs);
        layout_language = (RelativeLayout) getActivity().findViewById(R.id.layout_language);
        language = (TextView) getActivity().findViewById(R.id.language);
        if(Constants.getGetLanguage(getActivity()) == null) {
            if (globalProvider.lan.equals("a")) {
                if (Locale.getDefault().getLanguage().equals("en")) {
                    language.setText("English");
                    flag = 1;
                } else {
                    language.setText("中文");
                    flag = 0;
                }
            } else {
                if (globalProvider.lan.equals("chinese")) {
                    language.setText("中文");
                    flag = 0;
                } else {
                    language.setText("English");
                    flag = 1;
                }
            }
        }else if(Constants.getGetLanguage(getActivity()).equals("english")){
            language.setText("English");
            flag = 1;
        }else{
            language.setText("中文");
            flag = 0;
        }

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
            }
        });
        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MyInfoActivity.class);
                startActivity(intent);
            }
        });
        //TODO MYDEAL
        /*

        myDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MyDealActivity.class);
                startActivity(intent);
            }
        });
        */

        fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), FankuiActivity.class);
                startActivity(intent);
            }
        });


        layout_language.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String [] languageStr = new String[]{"中文","English" };
                if(globalProvider.shangpingList.size() > 0){
                    new AlertDialog.Builder(getActivity()).setTitle("确认").setMessage(getActivity().getString(R.string.clear_cart))
                            .setPositiveButton(getActivity().getString(R.string.yes1),null)
                            .setNegativeButton(getActivity().getString(R.string.no1),null).show();
                }else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getActivity().getString(R.string.language_set))
                            .setSingleChoiceItems(languageStr, flag,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Configuration config = getResources().getConfiguration();
                                            DisplayMetrics dm = getResources().getDisplayMetrics();
                                            if (languageStr[which].equals("English")) {
                                                config.locale = Locale.ENGLISH;
                                                flag = 0;
                                            } else {
                                                config.locale = Locale.SIMPLIFIED_CHINESE;
                                                flag = 1;
                                            }
                                            getResources().updateConfiguration(config, dm);
                                            if (flag == 0) {
                                                globalProvider.lan = "english";
                                                Constants.setLanguage(getActivity(), "english");
                                                flag = 1;
                                            } else {
                                                globalProvider.lan = "chinese";
                                                Constants.setLanguage(getActivity(), "chinese");
                                                flag = 0;
                                            }
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            intent.putExtra("language", "x");
                                            startActivity(intent);
                                            // language.setText(languageStr[which]);
                                            dialog.dismiss();
                                        }
                                    }
                            )
                            .setNegativeButton(getActivity().getString(R.string.cancel), null)
                            .show();
                }
//                new AlertDialog.Builder(getActivity()).setTitle(getActivity().getString(R.string.language_set)).setItems(languageStr,new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int which){
//                        Configuration config = getResources().getConfiguration();
//                        DisplayMetrics dm = getResources().getDisplayMetrics();
//                        if (languageStr[which] == getActivity().getString(R.string.english)) {
//                            config.locale = Locale.ENGLISH;
//                        } else {
//                            config.locale = Locale.SIMPLIFIED_CHINESE;
//                        }
//                        getResources().updateConfiguration(config, dm);
//                        language.setText(languageStr[which]);
//                        Intent intent = new Intent(getActivity(),MainActivity.class);
//                        startActivityForResult(intent, Constants.LANGUAGE);
//                        //Toast.makeText(getActivity(), "您已经选择了: " + which + ":" + language[which],Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                }).show();
            }
        });
        ConfirmInfo.setText(getActivity().getString(R.string.yiwanshan));
//        name= (TextView) getActivity().findViewById(R.id.name);
//        gongsi_name= (TextView) getActivity().findViewById(R.id.gongsi_name);
//        mob_number= (TextView) getActivity().findViewById(R.id.mob_phone);
//        if(GlobalProvider.getInstance().me!=null)
//        {
//            if(GlobalProvider.getInstance().me.name!=null){name.setText(GlobalProvider.getInstance().me.name);}
//            if(GlobalProvider.getInstance().me._customerProfile!=null&&GlobalProvider.getInstance().me._customerProfile.companyName!=null){gongsi_name.setText(GlobalProvider.getInstance().me._customerProfile.companyName);}
//            if(GlobalProvider.getInstance().me._customerProfile!=null&&GlobalProvider.getInstance().me._customerProfile.contactNumber!=null){mob_number.setText(GlobalProvider.getInstance().me._customerProfile.contactNumber);}
//        }

        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.getToken(getActivity()).equals("")) {
                    Toast.makeText(getActivity(), "you have not logged in", Toast.LENGTH_SHORT).show();
                } else {
                    Constants.setToken(getActivity(), "");
                    globalProvider.shangpingList.clear();
//                    GlobalProvider.getInstance().shangpingListDefault.clear();
//                    GlobalProvider.getInstance().ShangpingHeaderLoadCategory="";
                    Toast.makeText(getActivity(), "you have successfully logged out", Toast.LENGTH_SHORT).show();
                    gotoLogin();
                }
            }
        });
//        adress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), AdressActivity.class);
//                startActivity(intent);
//            }
//        });
//        RelativeLayout jianyi_enter= (RelativeLayout) getActivity().findViewById(R.id.jianyi_enter);
//        jianyi_enter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), FankuiActivity.class);
//                startActivity(intent);
//            }
//        });

    }
    public void gotoLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();

        //MainActivity.getDefault().setSelect(0);
    }



//    public void loadMe() {
//        RequestParams params = new RequestParams();
//
//        GlobalProvider globalProvider = GlobalProvider.getInstance();
//        globalProvider.get(getActivity(), Constants.meUrlStr, params, new RequestListener() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                parseMe(new String(responseBody));
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.v("err", new String(responseBody));
//            }
//
//            @Override
//            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
//
//            }
//        });
//    }
//
//
//    public void parseMe(String json) {
//        JsonFactory jsonFactory = new JsonFactory();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            JsonParser jsonParser = jsonFactory.createJsonParser(json);
//            Me me = (Me) objectMapper.readValue(jsonParser, Me.class);
//
//            this.name.setText(me.getName());
////            this.phone_num.setText(me._staffProfile.getContactNumber());
////            this.psd.setText(me.getEmail());
////            this.customers=me._staffProfile._customers;
//
//
//
////            this.mItems.clear();
////            this.mItems.addAll(orderlist.orders);
////            adapter.notifyDataSetChanged();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//
//    }

}