package com.samiapps.kv.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by KV on 20/2/18.
 */

public class ContactActivity extends AppCompatActivity {
    TextView pnumText;
    TextView emalText;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_res);
        //getActionBar().hide();
        pnumText=(TextView)findViewById(R.id.pnum);
        emalText=(TextView)findViewById(R.id.emal);
        pnumText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "6591245862"));
                startActivity(intent);
            }
        });
        emalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dataIntent = new Intent(Intent.ACTION_SENDTO);
                dataIntent.setData(Uri.parse("mailto:wongeasybuy@gmail.com"));
                startActivity(dataIntent);

            }
        });

    }
}
