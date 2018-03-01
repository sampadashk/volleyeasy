package com.samiapps.kv.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by KV on 26/2/18.
 */

public class ChooseBillingAddressTAdapter extends BaseAdapter {
    private Context context;
    private List<String> address;
    public int position = 999;

    public ChooseBillingAddressTAdapter(Context context, List address) {
        this.address = address;
        this.context = context;
    }

    public int getCount() {
        return address.size();
    }

    public Object getItem(int i) {
        return address.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final String w = address.get(i);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.address_list, null);
            holder = new ViewHolder();
            holder.address = (TextView) view.findViewById(R.id.address);
            holder.edit = (LinearLayout) view.findViewById(R.id.edit);
            holder.chooseAddress = (RelativeLayout) view.findViewById(R.id.chooseAddress);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.address.setText(w);
        if (position == i) {
            holder.address.setTextColor(0xffd32f2f);
        } else {
            holder.address.setTextColor(0xff000000);
        }
        final ViewHolder finalHolder1 = holder;
        holder.chooseAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //GlobalProvider.getInstance().Adress[1] = w;
                // GlobalProvider.getInstance().orders.get(GlobalProvider.getInstance().whichOrder).setBillingAddress(w);
                setPosition(i);
                ((EditBillingAddressTActivity) context).doResetAddress(w);
            }
        });
        final ViewHolder finalHolder = holder;
        holder.edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new AlertDialog.Builder(context).setTitle(R.string.choose).setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[]{context.getString(R.string.delete), context.getString(R.string.edit2)}, 0, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            if (w.equals(GlobalProvider.getInstance(context).me._customerProfile.billingAddress)) {
                                                Toast.makeText(context, R.string.oldAddress, Toast.LENGTH_SHORT).show();
                                            } else {
                                                ((EditBillingAddressTActivity) context).doReset(w);
                                            }
                                        } else {
                                            final EditText inputServer = new EditText(context);
                                            inputServer.setText(w);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setTitle(R.string.editAddress).setView(inputServer)
                                                    .setNegativeButton("Cancel", null);
                                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //finalHolder.address.setText(inputServer.getText().toString());
                                                    if (inputServer.getText().toString() == null || inputServer.getText().toString().equals("")) {
                                                        Toast.makeText(context, R.string.notEmpty, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        ((EditBillingAddressTActivity) context).doChange(w, inputServer.getText().toString());
                                                        if (w.equals(GlobalProvider.getInstance(context).me._customerProfile.billingAddress)) {
                                                            ((EditBillingAddressTActivity) context).doResetAddress(inputServer.getText().toString());
                                                        }
                                                        Toast.makeText(context, R.string.suctoupdate, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            builder.show();
                                        }
                                        dialog.dismiss();
                                    }
                                }
                        ).show();
            }
        });
        return view;
    }

    public void setPosition(int i) {
        this.position = i;
        this.notifyDataSetChanged();
    }

    class ViewHolder {
        public TextView address;
        public LinearLayout edit;
        public RelativeLayout chooseAddress;
    }
}
