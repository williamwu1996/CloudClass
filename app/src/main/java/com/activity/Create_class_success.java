package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cloudclass.R;

public class Create_class_success extends Activity {

    Button finish;
    TextView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_class_success);
        code = findViewById(R.id.create_class_code);
        Intent i = getIntent();
        code.setText(i.getStringExtra("cid"));
        finish = findViewById(R.id.create_class_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}
