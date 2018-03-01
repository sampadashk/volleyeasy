package com.samiapps.kv.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2015/12/2.
 */
public class ProductHeaderAdapter extends RecyclerView.Adapter<ViewHoder>{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> mDatas;
    private int position;

    public interface  OnItemClickListener{

        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;


    public  void setOnItemClickListener(OnItemClickListener mOnItemClickListener){

        this.mOnItemClickListener=mOnItemClickListener;

    }
    public ProductHeaderAdapter(Context context, List datas){

        this.mContext=context;
        this.mDatas=datas;
        mInflater=LayoutInflater.from(context);
    }
    @Override
    public ViewHoder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=mInflater.inflate(R.layout.shangping_header_item,viewGroup,false);
        ViewHoder viewhoder=new ViewHoder(view);
        return viewhoder;
    }

    @Override
    public void onBindViewHolder(final ViewHoder myViewHoder, final int i) {

        myViewHoder.tv.setText(mDatas.get(i));
        if(position==i){
            myViewHoder.tv.setTextSize(16);
            myViewHoder.tv.setTextColor(0xffee2400);
            //myViewHoder.tv.setBackgroundColor(0xffffffff);

        }else{
            myViewHoder.tv.setTextSize(14);
            myViewHoder.tv.setTextColor(0xff000000);
           // myViewHoder.tv.setBackgroundColor(0xfff8f8f8);
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
class ViewHoder extends RecyclerView.ViewHolder{

    TextView tv;

    public ViewHoder(View arg0){
        super(arg0);

        tv= (TextView) arg0.findViewById(R.id.id_tv);

    }
}


