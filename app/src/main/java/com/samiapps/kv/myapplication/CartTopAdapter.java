package com.samiapps.kv.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by KV on 27/2/18.
 */

public class CartTopAdapter extends RecyclerView.Adapter<ViewMyHoder>{
    // private CircleBadgeView buyNumView;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Contract> mDatas;
    private int position;
    //private ViewMyHoder viewhoder;
    //private List<CircleBadgeView> views;
    private  int DEFAULT_LR_PADDING_DIP = 5;

    public interface  OnItemClickListener{

        void onItemClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;

    public  void setOnItemClickListener(OnItemClickListener mOnItemClickListener){

        this.mOnItemClickListener=mOnItemClickListener;

    }
    public CartTopAdapter(Context context, List datas){

        this.mContext=context;
        this.mDatas=datas;


        mInflater=LayoutInflater.from(context);
    }
    @Override
    public ViewMyHoder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=mInflater.inflate(R.layout.shangping_header_item,viewGroup,false);
        ViewMyHoder viewhoder=new ViewMyHoder(view);
        viewhoder.buyNumView = new CircleBadgeView(mContext, viewhoder.tv);
        viewhoder.buyNumView.setTextColor(Color.WHITE);
        viewhoder.buyNumView.setBackgroundColor(Color.RED);

        return viewhoder;
    }
    @Override
    public void onBindViewHolder(final ViewMyHoder myViewHoder, final int i) {
        myViewHoder.tv.setText(mDatas.get(i)._supplierInfo.getCompanyName());
        if(position==i){
            myViewHoder.tv.setTextSize(15);
            myViewHoder.tv.setTextColor(0xffee2400);
            //myViewHoder.tv.setBackgroundColor(0xffffffff);

        }else{
            myViewHoder.tv.setTextSize(13);
            myViewHoder.tv.setTextColor(0xff000000);
            // myViewHoder.tv.setBackgroundColor(0xfff8f8f8);
        }
        int num=0;
        for(int a=0;a<GlobalProvider.getInstance(mContext).shangpingList.size();a++){
            if(GlobalProvider.getInstance(mContext).shangpingList.get(a)._supplier.equals(mDatas.get(i)._supplier)){
                num=num+1;
            }
        }
        if(num>0){
            myViewHoder.buyNumView.setText(num + "");//
            if(num>=10){
                DEFAULT_LR_PADDING_DIP = 3;
                int paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP);
                myViewHoder.buyNumView.setPadding(paddingPixels, 0, paddingPixels, 0);
                myViewHoder.buyNumView.setTextSize(10);
            } else {
                DEFAULT_LR_PADDING_DIP = 5;
                int paddingPixels_ = dipToPixels(DEFAULT_LR_PADDING_DIP);
                myViewHoder.buyNumView.setPadding(paddingPixels_, 0, paddingPixels_, 0);
                myViewHoder.buyNumView.setTextSize(11);
            }
            //buyNumView.setTextSize(12);
            myViewHoder.buyNumView.setBadgePosition(CircleBadgeView.POSITION_TOP_RIGHT);
            myViewHoder.buyNumView.setGravity(Gravity.CENTER);
            myViewHoder.buyNumView.show();
        }else{
            myViewHoder.buyNumView.hide();
        }
        if(mOnItemClickListener!=null){

            myViewHoder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(myViewHoder.itemView,i);
                    setPosition(i);
                }
            });
        }
    }
    private int dipToPixels(int dip) {
        Resources r = mContext.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }
    public void setPosition(int i){
        this.position=i;
        this.notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
class ViewMyHoder extends RecyclerView.ViewHolder{
    TextView tv;
    CircleBadgeView buyNumView;
    public ViewMyHoder(View arg0){
        super(arg0);
        tv= (TextView) arg0.findViewById(R.id.id_tv);
    }
}
