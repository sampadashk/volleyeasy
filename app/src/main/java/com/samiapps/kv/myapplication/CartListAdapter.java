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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by KV on 27/2/18.
 */

public class CartListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Product> mData;
    private FragmentCart parent;
    private boolean AK=true;
    private float total_price=0;

    public CartListAdapter(Context context,List data,FragmentCart parent){

        this.mContext=context;
        this.mData=data;
        this.parent=parent;

    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final double[] i = {(double)mData.get(position).getOrderQuantity()};
        i[0]=changeDouble(i[0]);
        ViewHolder holder = null;
        //final Shangping shangping=mData.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cart_list_item, null);
            holder=new ViewHolder();
            holder.num=(EditText)convertView.findViewById(R.id.shuliang);
            holder.delete= (ImageView) convertView.findViewById(R.id.delete);
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.price= (TextView) convertView.findViewById(R.id.danjia);
            holder.jiage= (TextView) convertView.findViewById(R.id.jiage);
            holder.add= (ImageView) convertView.findViewById(R.id.add);
            holder.reduce= (ImageView) convertView.findViewById(R.id.reduce);
            holder.unit= (TextView) convertView.findViewById(R.id.unit);
            holder.id_num= (TextView) convertView.findViewById(R.id.id_num);
            holder.num.setTag(position);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder)convertView.getTag();
            holder.num.setTag(position);
        }
        if(AK==false){
            holder.delete.setVisibility(View.VISIBLE);
        }else{
            holder.delete.setVisibility(View.INVISIBLE);
        }
        holder.num.setText(i[0] + "");
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

                for(int b=0;b<GlobalProvider.getInstance(mContext).shangpingListDefault.size();b++){

                    if(mData.get(position).get_id().equals(GlobalProvider.getInstance(mContext).shangpingListDefault.get(b).get_id())){

                        GlobalProvider.getInstance(mContext).shangpingListDefault.get(b).setOrderQuantity(0);

                    }
                }
                GlobalProvider.getInstance(mContext).shangpingList.remove(mData.get(position));

                if (GlobalProvider.getInstance(mContext).shangpingList.size() > 0) {

                        ((MainActivity) mContext).setCartNum();

                }
                else {

                        ((MainActivity) mContext).HideCartNum();

                }

                doDelete(position);
            }
        });
        final ViewHolder finalHolder = holder;
        final ViewHolder finalHolder1 = holder;
        final ViewHolder finalHolder3 = holder;
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] = i[0] +1;
                finalHolder.num.setText(i[0] + "");
                for(int b=0;b<GlobalProvider.getInstance(mContext).shangpingListDefault.size();b++){

                    if(mData.get(position).get_id().equals(GlobalProvider.getInstance(mContext).shangpingListDefault.get(b).get_id())){

                        GlobalProvider.getInstance(mContext).shangpingListDefault.get(b).setOrderQuantity((float) i[0]);

                    }
                }
                for(int a=0;a<GlobalProvider.getInstance(mContext).shangpingList.size();a++){
                    if(GlobalProvider.getInstance(mContext).shangpingList.get(a)._id.equals(mData.get(position).get_id())){
                        GlobalProvider.getInstance(mContext).shangpingList.get(a).setOrderQuantity((float) i[0]);
                    }
                }
                float a= (float) (i[0] * mData.get(position).getUnitPrice());
                float b=(float)(Math.round(a*1000))/1000;

                //mData.get(position).setSubTotal(b);
                finalHolder3.jiage.setText(b + "");
                doChange(position, (float) i[0]);
            }
        });
        holder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i[0] = i[0] - 1;
                if (i[0] > 0) {
                    finalHolder.num.setText(i[0] + "");
                    float a= (float) (i[0] * mData.get(position).getUnitPrice());
                    float b=(float)(Math.round(a*1000))/1000;
                    //mData.get(position).setSubTotal(b);
                    finalHolder3.jiage.setText(b + "");
                } else {
                    i[0] = 0;
                    finalHolder.num.setText(i[0] + "");
                }
                for(int b=0;b<GlobalProvider.getInstance(mContext).shangpingListDefault.size();b++){
                    if(mData.get(position).get_id().equals(GlobalProvider.getInstance(mContext).shangpingListDefault.get(b).get_id())){
                        GlobalProvider.getInstance(mContext).shangpingListDefault.get(b).setOrderQuantity((float) i[0]);
                    }
                }
                if (i[0] == 0) {
                    GlobalProvider.getInstance(mContext).shangpingList.remove(mData.get(position));
                    doDelete(position);
                } else if (i[0] > 0) {
                    for(int a=0;a<GlobalProvider.getInstance(mContext).shangpingList.size();a++){
                        if(GlobalProvider.getInstance(mContext).shangpingList.get(a)._id.equals(mData.get(position).get_id())){
                            GlobalProvider.getInstance(mContext).shangpingList.get(a).setOrderQuantity((float) i[0]);
                        }
                    }
//                    GlobalProvider.getInstance(mContext).shangpingList.get(position).setOrderQuantity((float) i[0]);
                    doChange(position, (float) i[0]);
                }
                if (GlobalProvider.getInstance(mContext).shangpingList.size() > 0) {

                        ((MainActivity) mContext).setCartNum();

                }
                else {

                        ((MainActivity) mContext).HideCartNum();

                }

            }
        });
        final ViewHolder finalHolder2 = holder;
        final ViewHolder finalHolder4 = holder;
        final ViewHolder finalHolder5 = holder;
        final ViewHolder finalHolder7 = holder;
        holder.num.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                if (!hasFocus) {
                    if(i[0]==0) {
                        //do something
                        GlobalProvider.getInstance(mContext).shangpingList.remove(mData.get(position));
                        //mData.remove(mData.get(position));
//                        doDelete(position);

                        if (GlobalProvider.getInstance(mContext).shangpingList.size() > 0) {


                                ((MainActivity) mContext).setCartNum();

                        }
                        else {
                                ((MainActivity) mContext).HideCartNum();

                        }

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

                int position = (int) finalHolder4.num.getTag();

                if (finalHolder2.num.getText().length() > 0 && !CanBeDoubleOrNot(finalHolder2.num.getText().toString())) {

                    finalHolder2.num.setText(i[0] + "");
                    new AlertDialog.Builder(mContext)
                            .setMessage(mContext.getString(R.string.errorinput))
                            .setNegativeButton(mContext.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //Log.i("alertdialog", " 请保存数据！");
                                }
                            }).show();
                    return;
                }
                if (finalHolder2.num.getText().length() > 0 && !mData.get(position).getUnit().equals("kg") && !mData.get(position).getUnit().equals("千克") && Double.parseDouble(finalHolder2.num.getText().toString()) - (int) Double.parseDouble(finalHolder2.num.getText().toString()) != 0) {

                    finalHolder2.num.setText(i[0] + "");
                    new AlertDialog.Builder(mContext)
                            .setMessage(mContext.getString(R.string.havexiaoshu))
                            .setNegativeButton(mContext.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Log.i("alertdialog", " 请保存数据！");
                                }
                            }).show();
                }
                if(finalHolder5.num.getText().toString().length()!=0) {
                    if (Double.parseDouble(finalHolder2.num.getText().toString()) > 0) {
                        i[0] = changeDouble(Double.parseDouble(finalHolder2.num.getText().toString()));
                    }else if(Double.parseDouble(finalHolder2.num.getText().toString()) < 0){
                        i[0]=0;
                    }else{
                        i[0]=0;
                    }
                }else{
                    i[0]=0;
                }
                //if (Double.parseDouble(finalHolder2.num.getText().toString()) > 0) {
                float a = (float) (i[0] * mData.get(position).getUnitPrice());
                float c = (float) (Math.round(a * 1000)) / 1000;

                //mData.get(position).setSubTotal(b);
                finalHolder3.jiage.setText(c + "");

                for (int b = 0; b < GlobalProvider.getInstance(mContext).shangpingListDefault.size(); b++) {
                    if (mData.get(position).get_id().equals(GlobalProvider.getInstance(mContext).shangpingListDefault.get(b).get_id())) {
                        GlobalProvider.getInstance(mContext).shangpingListDefault.get(b).setOrderQuantity((float) i[0]);
                    }
                }
                for(int d=0;d<GlobalProvider.getInstance(mContext).shangpingList.size();d++){
                    if(GlobalProvider.getInstance(mContext).shangpingList.get(d)._id.equals(mData.get(position).get_id())){
                        GlobalProvider.getInstance(mContext).shangpingList.get(d).setOrderQuantity((float) i[0]);
                    }
                }
                doChange(position, (float) i[0]);


                if (GlobalProvider.getInstance(mContext).shangpingList.size() > 0) {

                        ((MainActivity) mContext).setCartNum();

                }
                else {

                        ((MainActivity) mContext).HideCartNum();

                }
            }
        });

        holder.name.setText(mData.get(position).getName());
        holder.unit.setText(mData.get(position).getUnit());
        if (Constants.getGetLanguage(mContext).equals("english")) {
            holder.unit.setText(mData.get(position).getUnit());
        }else if(Constants.getGetLanguage(mContext).equals("chinese")){
            if(mData.get(position).cunit != null) {
                holder.unit.setText(mData.get(position).cunit);
            }else{
                holder.unit.setText(mData.get(position).getUnit());
            }
        }else{
            if (Locale.getDefault().getLanguage().equals("en")) {
                holder.unit.setText(mData.get(position).getUnit());
            } else {
                if(mData.get(position).cunit != null) {
                    holder.unit.setText(mData.get(position).cunit);
                }else{
                    holder.unit.setText(mData.get(position).getUnit());
                }
            }
        }
//        switch (mData.get(position).getUnit()){
//            case "kg":
//                holder.unit.setText("公斤");
//                break;
//            case "ctn":
//                holder.unit.setText("箱");
//                break;
//            case "pkt":
//                holder.unit.setText("包");
//                break;
//            case "pcs":
//                holder.unit.setText("粒");
//                break;
//            default:holder.unit.setText(mData.get(position).getUnit());
//                break;
//        }
        holder.price.setText(mData.get(position).getUnitPrice()+"");
        holder.id_num.setText(position+1+"");

        float a=mData.get(position).getOrderQuantity() * mData.get(position).getUnitPrice();
        float b=(float)(Math.round(a*1000))/1000;

        //mData.get(position).setSubTotal(b);

        holder.jiage.setText(b+ "");

        //getTotal_price(shangping);
        //this.parent.setTotal_price(total_price/2);

        return convertView;
    }
    public void ReSet_AK(){
        if(AK==true){
            this.AK=false;
        }else{
            this.AK=true;
        }
        notifyDataSetChanged();
    }
    public  void getTotal_price(){
        parent.price=0;
        parent.setTotal_price();

    }
    public void doDelete(int position){
        parent.DeleteNotifyDataSetChanged(position);
    }
    public void doChange(int position,float num){
        parent.ChangeNotifyDataSetChanged(position,num);
    }

    public double changeDouble(Double dou){
        NumberFormat nf=new DecimalFormat( "0.00 ");
        String s = nf.format(dou);
        s = s.replace(',','.');
        dou = Double.parseDouble(s);
        return dou;
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
        public ImageView delete;
        public EditText num;
        public TextView price;
        public TextView jiage;
        public ImageView add;
        public ImageView reduce;
        public TextView unit;
        public TextView id_num;
    }
}