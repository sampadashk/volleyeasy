package com.samiapps.kv.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by KV on 28/2/18.
 */

public class SearchListAdapter extends BaseAdapter {
    public Context context;
    public List<Product> data;
    //public FragmentProduct parent;

    public SearchListAdapter(Context context,List Data){
        this.context=context;
        this.data=Data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Product product = data.get(position);
        final double[] i = {(double)data.get(position).getOrderQuantity()};
        i[0]=changeDouble(i[0]);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_list_item, null);
            holder = new ViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.name);

            // holder.descripetion= (TextView) convertView.findViewById(R.id.descripetion);
            holder.asd=(TextView) convertView.findViewById(R.id.asd);
            holder.fuhao=(TextView) convertView.findViewById(R.id.fuhao);
            holder.origin= (TextView) convertView.findViewById(R.id.origin);
            holder.unit= (TextView) convertView.findViewById(R.id.unit);
            holder.unitPrice= (TextView) convertView.findViewById(R.id.unitprice);
            holder.collect= (ImageView) convertView.findViewById(R.id.collect);
            holder.add= (LinearLayout) convertView.findViewById(R.id.add);
            holder.reduce= (LinearLayout) convertView.findViewById(R.id.reduce);
            holder.orderQuantity= (EditText) convertView.findViewById(R.id.num);
            holder.orderQuantity.setTag(position);
            holder.product_Img= (ImageView) convertView.findViewById(R.id.product_Img);
            // holder.havePrice= (LinearLayout) convertView.findViewById(R.id.havePrice);

            holder.sellByCtn= (TextView) convertView.findViewById(R.id.sellByCtn);
            holder.main= (RelativeLayout) convertView.findViewById(R.id.main);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
            holder.orderQuantity.setTag(position);
        }
        if(product.getSellByCtn()==1){
            holder.sellByCtn.setVisibility(View.VISIBLE);
        }else{
            holder.sellByCtn.setVisibility(View.INVISIBLE);
        }
        if(product.getCollected()!=null){
            if(product.getCollected().equals("yes")){
                // holder.collect.setBackgroundColor(0xffeaeaea);
                holder.collect.setImageResource(R.drawable.xinhong);
            }else{
                // holder.collect.setBackgroundColor(0xff000000);
                holder.collect.setImageResource(R.drawable.xinhui);
            }
        }
        holder.sellByCtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBuyByCtn(data.get(position).getName());
            }
        });

        final ViewHolder finalHolder3 = holder;
        holder.orderQuantity.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(i[0]==0){
                        finalHolder3.orderQuantity.setText("");
                    }
                }
            }
        });
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", data.get(position));
                ((SearchActivity) context).startActivityForResult(intent, Constants.SEARCHTOCART);

            }
        });
        final ViewHolder finalHolder = holder;
        holder.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.getCollected()!=null){
                    doCollect(data.get(position).getCollected(),data.get(position));
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("");
                    builder.setMessage(context.getString(R.string.noquanxian));
                    builder.setPositiveButton(context.getString(R.string.confirm), null);
                    builder.show();
                }
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] = i[0] +1;
                i[0]=changeDouble(i[0]);
                finalHolder.orderQuantity.setText(i[0] + "");

            }
        });
        final ViewHolder finalHolder1 = holder;
        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] = i[0] - 1;
                i[0] = changeDouble(i[0]);
                if (i[0] > 0) {
                    finalHolder1.orderQuantity.setText(i[0] + "");
                } else {
                    i[0] = 0;
                    finalHolder.orderQuantity.setText(i[0] + "");
                }

            }
        });
        final ViewHolder finalHolder2 = holder;

        holder.orderQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                int position = (int) finalHolder1.orderQuantity.getTag();

                if (finalHolder2.orderQuantity.getText().length() > 0 && !CanBeDoubleOrNot(finalHolder2.orderQuantity.getText().toString())) {

                    finalHolder2.orderQuantity.setText(i[0] + "");
                    new AlertDialog.Builder(context)
                            .setMessage(context.getString(R.string.errorinput))
                            .setNegativeButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Log.i("alertdialog", " 请保存数据！");
                                }
                            }).show();
                    return;
                }
                if (finalHolder2.orderQuantity.getText().length() > 0 && !data.get(position).getUnit().equals("kg") && !data.get(position).getUnit().equals("千克") && Double.parseDouble(finalHolder2.orderQuantity.getText().toString()) - (int) Double.parseDouble(finalHolder2.orderQuantity.getText().toString()) != 0) {

                    finalHolder2.orderQuantity.setText(i[0] + "");
                    new AlertDialog.Builder(context)
                            .setMessage(context.getString(R.string.havexiaoshu))
                            .setNegativeButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //   Log.i("alertdialog", " 请保存数据！");

                                }
                            }).show();

                }
                if (finalHolder2.orderQuantity.getText().length() != 0 && Double.parseDouble(finalHolder2.orderQuantity.getText().toString()) > 0) {
                    i[0] = changeDouble(Double.parseDouble(finalHolder2.orderQuantity.getText().toString()));
                    //doBalancce(data.get(position),(float) i[0]);
                    for (int b = 0; b < GlobalProvider.getInstance(context).shangpingList.size(); b++) {
                        Product sp = GlobalProvider.getInstance(context).shangpingList.get(b);
                        if (sp.get_id().equals(data.get(position).get_id())) {
                            for (int c = 0; c < GlobalProvider.getInstance(context).shangpingListDefault.size(); c++) {

                                if (data.get(position).get_id().equals(GlobalProvider.getInstance(context).shangpingListDefault.get(c).get_id())) {

                                    GlobalProvider.getInstance(context).shangpingListDefault.get(c).setOrderQuantity((float) i[0]);

                                }
                            }
                            data.get(position).setOrderQuantity((float) i[0]);
                            GlobalProvider.getInstance(context).shangpingList.get(b).setOrderQuantity((float) i[0]);
                            return;
                        }
                    }
                    for (int c = 0; c < GlobalProvider.getInstance(context).shangpingListDefault.size(); c++) {

                        if (data.get(position).get_id().equals(GlobalProvider.getInstance(context).shangpingListDefault.get(c).get_id())) {

                            GlobalProvider.getInstance(context).shangpingListDefault.get(c).setOrderQuantity((float) i[0]);

                        }
                    }
                    int a=GlobalProvider.getInstance(context).shangpingList.size();
                    data.get(position).setOrderQuantity((float) i[0]);
                    GlobalProvider.getInstance(context).shangpingList.add(data.get(position));
                    if(a==0){
                        GoToAlertOrNo();
                    }
                    for (int i = 0; i < GlobalProvider.getInstance(context).contractListToCart.size(); i++) {
                        Contract contract = GlobalProvider.getInstance(context).contractListToCart.get(i);
                        if (contract._supplier.equals(data.get(position).get_supplier())) {
                            return;
                        }
                    }
                    for (int i = 0; i < GlobalProvider.getInstance(context).contractList.size(); i++) {
                        if (GlobalProvider.getInstance(context).contractList.get(i)._supplier.equals(data.get(position).get_supplier())) {
                            GlobalProvider.getInstance(context).contractListToCart.add(GlobalProvider.getInstance(context).contractList.get(i));
                        }
                    }
                    // ((MainActivity) context).setCartNum();
                } else if (finalHolder2.orderQuantity.getText().length() == 0 || Double.parseDouble(finalHolder2.orderQuantity.getText().toString()) <= 0) {
                    i[0] = 0;
                    //doBalancce(data.get(position),(float) i[0]);
                    int num = 0;
                    for (int b = 0; b < GlobalProvider.getInstance(context).shangpingList.size(); b++) {
                        Product sp = GlobalProvider.getInstance(context).shangpingList.get(b);
                        if (sp.get_id().equals(data.get(position).get_id())) {
                            for (int c = 0; c < GlobalProvider.getInstance(context).shangpingListDefault.size(); c++) {

                                if (data.get(position).get_id().equals(GlobalProvider.getInstance(context).shangpingListDefault.get(c).get_id())) {

                                    GlobalProvider.getInstance(context).shangpingListDefault.get(c).setOrderQuantity(0);

                                }
                            }
                            data.get(position).setOrderQuantity((float) i[0]);
                            GlobalProvider.getInstance(context).shangpingList.get(b).setOrderQuantity((float) i[0]);
                            GlobalProvider.getInstance(context).shangpingList.remove(sp);
                            num = num + 1;
                            for (int i = 0; i < GlobalProvider.getInstance(context).shangpingList.size(); i++) {
                                if (sp._supplier.equals(GlobalProvider.getInstance(context).shangpingList.get(i).get_supplier())) {
                                    return;
                                }
                            }
                            for (int i = 0; i < GlobalProvider.getInstance(context).contractListToCart.size(); i++) {
                                if (sp._supplier != null) {
                                    if (sp.get_supplier().equals(GlobalProvider.getInstance(context).contractListToCart.get(i)._supplier)) {
                                        GlobalProvider.getInstance(context).contractListToCart.remove(GlobalProvider.getInstance(context).contractListToCart.get(i));
                                    }
                                }
                            }
                        }
                    }
                    if (num == 0) {
                        data.get(position).setOrderQuantity((float) i[0]);
                    }
                }
                if (GlobalProvider.getInstance(context).shangpingList.size() > 0) {
                    MainActivity.getDefault().setCartNum();
                } else {
                    MainActivity.getDefault().HideCartNum();
                }
            }
        });
        holder.product_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", data.get(position));
                ((SearchActivity) context).startActivityForResult(intent, Constants.SEARCHTOCART);

            }
        });


        holder.orderQuantity.setText(i[0] + "");
        String url=Constants.imgBaseUrlStr+"/"+data.get(position).getImageName();
        Picasso.with(context).load(url).resize(150, 150).centerCrop().into(holder.product_Img);
        holder.name.setText(product.getName());
        // holder.descripetion.setText(product.getDecription());
        if(product.getOrigin()!=null){holder.origin.setText(product.getOrigin());}
        if (Constants.getGetLanguage(context).equals("english")) {
            holder.unit.setText(product.getUnit());
        }else if(Constants.getGetLanguage(context).equals("chinese")){
            if(product.cunit != null) {
                holder.unit.setText(product.cunit);
            }else{
                holder.unit.setText(product.getUnit());
            }
        }else{
            if (Locale.getDefault().getLanguage().equals("en")) {
                holder.unit.setText(product.getUnit());
            } else {
                if(product.cunit != null) {
                    holder.unit.setText(product.cunit);
                }else{
                    holder.unit.setText(product.getUnit());
                }
            }
        }

        if(product.getUnitPrice()>0){
            // holder.unitPrice.setText(product.getUnitPrice()+"");
            holder.unit.setVisibility(View.VISIBLE);

            holder.unit.setText("$ "+product.getUnitPrice()+"/"+product.getUnit());


        }else{
            // holder.unitPrice.setText(R.string.noPrice);
            holder.unit.setVisibility(View.GONE);
            // holder.asd.setVisibility(View.GONE);
            // holder.fuhao.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void GoToAlertOrNo(){
        if(GlobalProvider.getInstance(context).shangpingList.size()==1&&GlobalProvider.getInstance(context).isShopping){
            new AlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.ordered))
                    .setPositiveButton(context.getString(R.string.editOrder), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            ((SearchActivity)context).setResult(111);
                            ((SearchActivity)context).finish();
                            MainActivity.getDefault().item=2;

                        }
                    }).setNegativeButton(context.getString(R.string.zhiyiorder), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GlobalProvider.getInstance(context).editOrder._id="";
                    GlobalProvider.getInstance(context).order=new Order();
                    // TODO Auto-generated method stub

                    // Log.i("alertdialog", " 请保存数据！");
                }

            }).show();
        }
    }
    public void doBuyByCtn(String name){
        ((SearchActivity)context).loadSearchList(name);
    }
    public void doCollect(String status,Product product){
        if(status.equals("no")){
            ((SearchActivity)context).addcollected(product);
        }else if(status.equals("yes")){
            ((SearchActivity)context).deletecollected(product);
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
    class ViewHolder {
        public TextView name;
        // public TextView descripetion;
        public TextView origin;
        public TextView unit;
        public TextView unitPrice;
        public TextView asd;
        public TextView fuhao;
        public ImageView collect;
        public LinearLayout add;
        public LinearLayout reduce;
        public EditText orderQuantity;
        public ImageView product_Img;

        public LinearLayout havePrice;
        public TextView sellByCtn;
        public RelativeLayout main;
    }
    public double changeDouble(Double dou){
        NumberFormat nf=new DecimalFormat( "0.00 ");
        dou = Double.parseDouble(nf.format(dou));
        return dou;
    }
}
