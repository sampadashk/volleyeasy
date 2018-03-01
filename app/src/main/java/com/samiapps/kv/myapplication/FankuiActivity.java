package com.samiapps.kv.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by KV on 21/2/18.
 */

public class FankuiActivity extends BaseActivity {
    public LinearLayout back;
    public RelativeLayout scoreToUs;
    public RelativeLayout sendMsgToUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fankui);
        back= (LinearLayout) findViewById(R.id.back);
        scoreToUs= (RelativeLayout) findViewById(R.id.scoreToUs);
        sendMsgToUs= (RelativeLayout) findViewById(R.id.sendMsgToUs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        scoreToUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(FankuiActivity.this).setTitle("请给我们评分！")
                        .setMessage("您是否喜欢我们的产品？ 请在app商城上为我们评分！" +
                                "如果您有任何想法和意见可以帮助我们改善我们的产品，请发送邮件告诉我们！我们将竭诚为您服务" +
                                "谢谢！" +
                                "娃娃科技工作室")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("alertdialog", " 保存数据");

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                }).show();
            }
        });
        sendMsgToUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(FankuiActivity.this).setTitle("系统通知")
                        .setMessage("你的设备无法发送邮件，请检查邮件的相关配置然后重新使用")
                        .setPositiveButton("好的", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        }).show();
            }
        });
    }
}

