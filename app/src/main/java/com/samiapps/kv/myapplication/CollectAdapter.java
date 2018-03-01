package com.samiapps.kv.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by KV on 27/2/18.
 */

public class CollectAdapter extends BaseAdapter {
    private List<Product> data;
    private Context context;
    private boolean AK=true;
    private FragmentCollect parent;
    private boolean longpress;

    public CollectAdapter(Context context, List apply,FragmentCollect parent){
        this.data=apply;
        this.context=context;
        this.parent=parent;
    }
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void remove(int arg0) {//删除指定位置的item
        data.remove(arg0);
        this.notifyDataSetChanged();//不要忘记更改适配器对象的数据源
    }

    public void insert(Product item, int arg0) {
        data.add(arg0, item);
        this.notifyDataSetChanged();
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final Product product=data.get(position);
        final double[] i = {(double)data.get(position).getOrderQuantity()};
        i[0]=changeDouble(i[0]);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.collect_listitem, null);
            holder = new ViewHolder();
            holder.id= (TextView) view.findViewById(R.id.id_num);
            holder.name= (TextView) view.findViewById(R.id.name);
            holder.unitprice= (TextView) view.findViewById(R.id.unitprice);
            holder.unit= (TextView) view.findViewById(R.id.unit);
            holder.num= (EditText) view.findViewById(R.id.num);
            holder.num.setTag(position);
            holder.add= (LinearLayout) view.findViewById(R.id.add);
            holder.reduce= (LinearLayout) view.findViewById(R.id.reduce);
            holder.delete= (Button) view.findViewById(R.id.delete);
            holder.noPrice= (TextView) view.findViewById(R.id.noPrice);
            holder.main= (RelativeLayout) view.findViewById(R.id.main);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            holder.num.setTag(position);
        }
        if(AK==false){
            holder.delete.setVisibility(View.VISIBLE);
        }else{
            holder.delete.setVisibility(View.INVISIBLE);
        }


//        holder.main.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                longpress=true;
//                return true;
//            }
//        });
//
//        holder.main.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                    if(longpress) {
//                        parent.killReresh();
//                    }
//                } else if(event.getAction() == MotionEvent.ACTION_UP){
//                        longpress = false;
//                        parent.EnableRefresh();
//                }
//                return false;
//            }
//        });

        final ViewHolder finalHolder6 = holder;
        holder.name.setOnClickListener(new View.OnClickListener() {
            Boolean flag = true;
            public void onClick(View v) {
                if (flag) {
                    flag = false;
                    finalHolder6.name.setEllipsize(null); // 展开
                    finalHolder6.name.setMaxLines(99);
                    //tv.setSingleLine(flag);
                } else {
                    flag = true;
                    finalHolder6.name.setEllipsize(TextUtils.TruncateAt.END);
                    finalHolder6.name.setMaxLines(1);// 收缩
                    //tv.setSingleLine(flag);
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.deletecollected(product.get_id());

                for(int b=0;b<GlobalProvider.getInstance(context).shangpingListDefault.size();b++){

                    if(data.get(position).get_id().equals(GlobalProvider.getInstance(context).shangpingListDefault.get(b).get_id())){

                        GlobalProvider.getInstance(context).shangpingListDefault.get(b).setCollected("no");
                        GlobalProvider.getInstance(context).shangpingListDefault.get(b).setOrderQuantity(0);
                    }
                }
            }
        });
        final ViewHolder finalHolder2 = holder;
        holder.num.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (i[0] == 0) {
                        finalHolder2.num.setText("");
                        // ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(finalHolder2.num, 0);
                    }
                }
            }
        });
        holder.num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int position = (int) finalHolder2.num.getTag();
                if (finalHolder2.num.getText().length() > 0 && !CanBeDoubleOrNot(finalHolder2.num.getText().toString())) {
                    finalHolder2.num.setText(i[0] + "");
                    new AlertDialog.Builder(context)
                            .setMessage(context.getString(R.string.errorinput))
                            .setNegativeButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Log.i("alertdialog", " 请保存数据！");
                                }
                            }).show();
                    return;
                }
                if (finalHolder2.num.getText().length() > 0 && !data.get(position).getUnit().equals("kg") && !data.get(position).getUnit().equals("千克") && Double.parseDouble(finalHolder2.num.getText().toString()) - (int) Double.parseDouble(finalHolder2.num.getText().toString()) != 0) {

                    finalHolder2.num.setText(i[0] + "");
                    new AlertDialog.Builder(context)
                            .setMessage(context.getString(R.string.havexiaoshu))
                            .setNegativeButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Log.i("alertdialog", " 请保存数据！");
                                }
                            }).show();
                }
//                i[0] = changeDouble(Double.parseDouble(finalHolder2.num.getText().toString()));
                if (finalHolder2.num.getText().length() != 0 && Double.parseDouble(finalHolder2.num.getText().toString()) > 0) {
                    i[0] = changeDouble(Double.parseDouble(finalHolder2.num.getText().toString()));
                    for (int b = 0; b < GlobalProvider.getInstance(context).shangpingList.size(); b++) {
                        Product sp = GlobalProvider.getInstance(context).shangpingList.get(b);
                        if (sp.get_id().equals(data.get(position).get_id())) {
                            data.get(position).setOrderQuantity((float) i[0]);
                            GlobalProvider.getInstance(context).shangpingList.get(b).setOrderQuantity((float) i[0]);
                            parent.ChangeNotifyDataSetChanged(data.get(position).get_id(), (float) i[0]);
                            return;
                        }
                    }
                    int a=GlobalProvider.getInstance(context).shangpingList.size();
                    data.get(position).setOrderQuantity((float) i[0]);
                    parent.ChangeNotifyDataSetChanged(data.get(position).get_id(), (float) i[0]);
                    GlobalProvider.getInstance(context).shangpingList.add(data.get(position));
                    if(a==0){
                        GoToAlertOrNo();
                    }
                    //((MainActivity) context).setCartNum();
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
                } else if (finalHolder2.num.getText().length() == 0 || Double.parseDouble(finalHolder2.num.getText().toString()) <= 0) {
                    i[0] = 0;
                    int num = 0;
                    for (int b = 0; b < GlobalProvider.getInstance(context).shangpingList.size(); b++) {
                        Product sp = GlobalProvider.getInstance(context).shangpingList.get(b);
                        if (sp.get_id().equals(data.get(position).get_id())) {
                            data.get(position).setOrderQuantity((float) i[0]);
                            GlobalProvider.getInstance(context).shangpingList.get(b).setOrderQuantity((float) i[0]);
                            GlobalProvider.getInstance(context).shangpingList.remove(sp);
                            num = num + 1;
                            parent.ChangeNotifyDataSetChanged(data.get(position).get_id(), (float) i[0]);
                            for (int i = 0; i < GlobalProvider.getInstance(context).shangpingList.size(); i++) {
                                if (sp._supplier.equals(GlobalProvider.getInstance(context).shangpingList.get(i).get_supplier())) {
                                    return;
                                }
                            }
                            for (int i = 0; i < GlobalProvider.getInstance(context).contractListToCart.size(); i++) {
                                if (sp.get_supplier().equals(GlobalProvider.getInstance(context).contractListToCart.get(i)._supplier)) {
                                    GlobalProvider.getInstance(context).contractListToCart.remove(GlobalProvider.getInstance(context).contractListToCart.get(i));
                                }
                            }
                        }
                    }
                }


                if (GlobalProvider.getInstance(context).shangpingList.size() > 0) {

                        ((MainActivity) context).setCartNum();

                } else {


                        ((MainActivity) context).HideCartNum();


                }



                if (data.size() > 0) {
                    for (int b = 0; b < GlobalProvider.getInstance(context).shangpingListDefault.size(); b++) {
                        if (data.get(position).get_id().equals(GlobalProvider.getInstance(context).shangpingListDefault.get(b).get_id())) {
                            GlobalProvider.getInstance(context).shangpingListDefault.get(b).setOrderQuantity((float) i[0]);
                        }
                    }
                }
            }
        });
        final ViewHolder finalHolder = holder;
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] = i[0] + 1;
                i[0] = changeDouble(i[0]);
                finalHolder.num.setText(i[0] + "");

            }
        });
        final ViewHolder finalHolder1 = holder;
        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] = i[0] - 1;
                i[0] = changeDouble(i[0]);
                if (i[0] > 0) {
                    finalHolder1.num.setText(i[0] + "");
                } else {
                    i[0] = 0;
                    finalHolder.num.setText(i[0] + "");
                }

            }
        });
        holder.id.setText(position+1+"");
        holder.name.setText(product.getName());
        holder.unitprice.setText(product.getPrice()+"");
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
//        switch (product.getUnit()){
//            case "kg":
//                holder.unit.setText(context.getString(R.string.kg));
//                break;
//            case "ctn":
//                holder.unit.setText(context.getString(R.string.ctn));
//                break;
//            case "pkt":
//                holder.unit.setText(context.getString(R.string.pkt));
//                break;
//            case "pcs":
//                holder.unit.setText(context.getString(R.string.pcs));
//                break;
//            default:holder.unit.setText(product.getUnit());
//                break;
//        }
        //holder.unit.setText(product.getUnit());

        holder.num.setText(i[0] + "");
        if(product.getUnitPrice()>0){
            //holder.havePrice.setVisibility(View.VISIBLE);
            holder.add.setVisibility(View.VISIBLE);
            holder.reduce.setVisibility(View.VISIBLE);
            holder.num.setVisibility(View.VISIBLE);
            holder.noPrice.setVisibility(View.GONE);
        }else{
            //holder.havePrice.setVisibility(View.GONE);
            holder.add.setVisibility(View.GONE);
            holder.reduce.setVisibility(View.GONE);
            holder.num.setVisibility(View.GONE);
            holder.noPrice.setVisibility(View.VISIBLE);
        }
        return view;
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

                    // Log.i("alertdialog", " 请保存数据！");
                }

            }).show();
        }
    }
    public void ReSet_AK(){
        if(AK==true){
            this.AK=false;
        }else{
            this.AK=true;
        }
        notifyDataSetChanged();
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
        public TextView id;
        public TextView name;
        public TextView unitprice;
        public TextView unit;
        public EditText num;
        public LinearLayout add;
        public Button delete;
        public LinearLayout reduce;
        public TextView noPrice;
        public RelativeLayout main;
    }
    public double changeDouble(Double dou){
        NumberFormat nf=new DecimalFormat( "0.00 ");
        String s = nf.format(dou);
        s = s.replace(',','.');
        dou = Double.parseDouble(s);
        return dou;
    }
}
