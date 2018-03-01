package com.samiapps.kv.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
 * Created by KV on 23/2/18.
 */

public class ProductListAdapter extends BaseAdapter {
    public Context context;
    public List<Product> data;
    public FragmentProduct parent;

    public ProductListAdapter(Context context,List Data,FragmentProduct parent){
        this.context=context;
        this.data=Data;
        this.parent=parent;

    }
    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        final Product product = data.get(position);
        final double[] i = {(double)data.get(position).getOrderQuantity()};
        i[0]=changeDouble(i[0]);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_list_item, null);
            holder = new ViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.name);

            holder.specs= (TextView) convertView.findViewById(R.id.specs);
            holder.origin= (TextView) convertView.findViewById(R.id.origin);
            holder.unit= (TextView) convertView.findViewById(R.id.unit);
            holder.asd=(TextView) convertView.findViewById(R.id.asd);
            holder.fuhao=(TextView) convertView.findViewById(R.id.fuhao);

            // holder.unitPrice= (TextView) convertView.findViewById(R.id.unitprice);
            holder.collect= (ImageView) convertView.findViewById(R.id.collect);
            holder.add= (LinearLayout) convertView.findViewById(R.id.add);
            holder.reduce= (LinearLayout) convertView.findViewById(R.id.reduce);
            holder.orderQuantity= (EditText) convertView.findViewById(R.id.num);
            holder.orderQuantity.setTag(position);
            holder.product_Img= (ImageView) convertView.findViewById(R.id.product_Img);
            // holder.havePrice= (LinearLayout) convertView.findViewById(R.id.havePrice);
            // holder.noPrice= (TextView) convertView.findViewById(R.id.noPrice);
            holder.sellByCtn= (TextView) convertView.findViewById(R.id.sellByCtn);
            holder.main= (RelativeLayout) convertView.findViewById(R.id.main);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
            holder.orderQuantity.setTag(position);
        }

        if(product.getSellByCtn()==1){
            if(product.getUnit().equals("ctn")||product.getUnit().equals("箱")){
                holder.sellByCtn.setVisibility(View.INVISIBLE);
            }else{
                holder.sellByCtn.setVisibility(View.VISIBLE);
            }

        }else{
            holder.sellByCtn.setVisibility(View.INVISIBLE);
        }
        if(product.getCollected() != null){
            if(product.getCollected().equals("yes")){
                //holder.collect.setBackgroundColor(0xffeaeaea);
                holder.collect.setImageResource(R.drawable.xinhong);
            }else{
                // holder.collect.setBackgroundColor(0xff000000);
                holder.collect.setImageResource(R.drawable.xinhui);
            }
        }
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", data.get(position));
                //TODO activity_re


                    ((MainActivity) context).startActivityForResult(intent, Constants.DETAILGOTOCART);

            }

        });
        holder.sellByCtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBuyByCtn(data.get(position).getName());
            }
        });
        final ViewHolder finalHolder3 = holder;

        final ViewHolder finalHolder = holder;
        holder.collect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(product.getCollected() != null){
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
        final ViewHolder finalHolder4 = holder;
        holder.orderQuantity.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (i[0] == 0) {
                        finalHolder4.orderQuantity.setText("");
                    }
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

                                    // Log.i("alertdialog", " 请保存数据！");

                                }
                            }).show();

                }
                if (finalHolder2.orderQuantity.getText().length() != 0 && Double.parseDouble(finalHolder2.orderQuantity.getText().toString()) > 0) {
                    i[0] = changeDouble(Double.parseDouble(finalHolder2.orderQuantity.getText().toString()));
                    doBalancce(data.get(position), (float) i[0]);
                    for (int b = 0; b < GlobalProvider.getInstance(context).shangpingList.size(); b++) {
                        Product sp = GlobalProvider.getInstance(context).shangpingList.get(b);
                        if (sp.get_id().equals(data.get(position).get_id())) {
                            data.get(position).setOrderQuantity((float) i[0]);
                            GlobalProvider.getInstance(context).shangpingList.get(b).setOrderQuantity((float) i[0]);
                            return;
                        }
                    }
                    data.get(position).setOrderQuantity((float) i[0]);
                    int a=GlobalProvider.getInstance(context).shangpingList.size();
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
                    doBalancce(data.get(position), (float) i[0]);
                    int num = 0;
                    for (int b = 0; b < GlobalProvider.getInstance(context).shangpingList.size(); b++) {
                        Product sp = GlobalProvider.getInstance(context).shangpingList.get(b);
                        if (sp.get_id().equals(data.get(position).get_id())) {
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

                        ((MainActivity) context).setCartNum();


                } else {

                        ((MainActivity) context).HideCartNum();


                }
            }
        });

        holder.product_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO create_ac

                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", data.get(position));
                    ((MainActivity) context).startActivityForResult(intent, Constants.DETAILGOTOCART);

            }

        });
        holder.orderQuantity.setText(i[0] + "");

        String url="";
        if(data.get(position).getImageName()==null||data.get(position).getImageName().equals("")){
            url=Constants.imgBaseUrlStr+"/"+GlobalProvider.getInstance(context).contract._supplierInfo.getLogo();
        }else{
            url=Constants.imgBaseUrlStr+"/"+data.get(position).getImageName();
        }
        Picasso.with(context).load(url).resize(150, 150).centerCrop().into(holder.product_Img);
        holder.name.setText(product.getName());
        holder.specs.setText(product.getSpecification());
        if(product.getOrigin()!=null){holder.origin.setText(product.getOrigin());}
        if(product.getUnit()!=null) {



        }


        //  holder.unitPrice.setText(product.getUnitPrice()+"");
      /*  if(product.getUnitPrice()>0){
            holder.havePrice.setVisibility(View.VISIBLE);
            holder.noPrice.setVisibility(View.GONE);
        }else{
            holder.havePrice.setVisibility(View.GONE);
            holder.noPrice.setVisibility(View.VISIBLE);
        }
        */

        if(product.getUnitPrice()>0){
            String unitval=null;
            // holder.unitPrice.setText(product.getUnitPrice()+"");
            holder.unit.setVisibility(View.VISIBLE);
            if (Constants.getGetLanguage(context).equals("english")||(Locale.getDefault().getLanguage().equals("en"))) {
                unitval=product.getUnit();
            } else
            {
                if (product.getcUnit()!=null) {

                    unitval = product.getcUnit();
                }




            }

            holder.unit.setText("$ "+product.getUnitPrice()+"/"+unitval);


        }else{
            // holder.unitPrice.setText(R.string.noPrice);
            //holder.unit.setVisibility(View.GONE);
            holder.unit.setText(R.string.Unpriced);
            holder.unit.setTextColor(Color.parseColor("#BCBCBC"));
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
                                MainActivity.getDefault().setSelect(2);
                                MainActivity.getDefault().item = 2;

                        }
                    }).setNegativeButton(context.getString(R.string.zhiyiorder), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GlobalProvider.getInstance(context).editOrder._id="";
                    GlobalProvider.getInstance(context).order=new Order();
                    // TODO Auto-generated method stub

                    //Log.i("alertdialog", " 请保存数据！");
                }

            }).show();
        }
    }
    public void doBuyByCtn(String name){
        //TODO uncomment this
       // parent.loadSearchList(name);
    }
    public void doBalancce(Product product,float orderQuantity){
        parent.balanceListOrderQuantity(product,orderQuantity);
    }
    public void doCollect(String status,Product product){
        //TODO uncomment this
        /*
        if(status.equals("no")){
            parent.addcollected(product);

        }else if(status.equals("yes")){
            parent.deletecollected(product);
        }
        */
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
        public TextView specs;
        public TextView origin;
        public TextView unit;
        public TextView asd;
        public TextView fuhao;
        //  public TextView unitPrice;
        public ImageView collect;
        public LinearLayout add;
        public LinearLayout reduce;
        public EditText orderQuantity;
        public ImageView product_Img;
        //    public TextView noPrice;
        // public LinearLayout havePrice;
        public TextView sellByCtn;
        public  RelativeLayout main;
    }

    public double changeDouble(Double dou){
        NumberFormat nf=new DecimalFormat( "0.00 ");
        String s = nf.format(dou);
        s = s.replace(',','.');
        dou = Double.parseDouble(s);
        return dou;
    }
}