package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.cloudclass.R;

public class Checkin_going extends Activity {

    LinearLayout discard;
    LinearLayout end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_going);

        discard = findViewById(R.id.chechin_going_discard_btn);
        discard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        end = findViewById(R.id.chechin_going_end_btn);
        end.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Checkin_going.this,Checkin_result.class);
                startActivity(intent);
            }
        });
    }
}
