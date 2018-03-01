package com.samiapps.kv.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2015/7/19.
 */
public class ContractHeaderAdapter extends RecyclerView.Adapter<MyViewHoder>{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Category> mDatas;
    private int position;

    public interface  OnItemClickListener{

        void onItemClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;


    public  void setOnItemClickListener(OnItemClickListener mOnItemClickListener){

        this.mOnItemClickListener=mOnItemClickListener;

    }
    public ContractHeaderAdapter(Context context, List datas){

        this.mContext=context;
        this.mDatas=datas;
        mInflater=LayoutInflater.from(context);


    }
    @Override
    public MyViewHoder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=mInflater.inflate(R.layout.shangping_header_item,viewGroup,false);
        MyViewHoder viewhoder=new MyViewHoder(view);
        return viewhoder;
    }

    @Override
    public void onBindViewHolder(final MyViewHoder myViewHoder, final int i) {
        Log.d("suppliercategory",mDatas.get(i).getSupplierCategory());

        myViewHoder.tv.setText(mDatas.get(i).getSupplierCategory());
        if(position==i){
            myViewHoder.tv.setTextSize(15);
            myViewHoder.tv.setTextColor(0xffee2400);
            //myViewHoder.tv.setBackgroundColor(0xffffffff);

        }else{
            myViewHoder.tv.setTextSize(13);
            myViewHoder.tv.setTextColor(0xff000000);
            //myViewHoder.tv.setBackgroundColor(0xfff8f8f8);
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

    public void setPosition(int i){

        this.position=i;
        this.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}

class MyViewHoder extends RecyclerView.ViewHolder{

    TextView tv;

    public MyViewHoder(View arg0){
        super(arg0);

        tv= (TextView) arg0.findViewById(R.id.id_tv);

    }
}