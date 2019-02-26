package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cloudclass.R;

public class Forget_validation_code extends Activity {
    TextView address;
    EditText code;
    Button next;
    String add;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_validation_code);
        code = findViewById(R.id.forget_validation_code_code);
        next = findViewById(R.id.forget_validation_code_next);
        address = findViewById(R.id.forget_validation_address);
        cancel = findViewById(R.id.forget_validation_cancel);

        Intent i = getIntent();
        address.setText("验证码已经发送至" + i.getStringExtra("address") + "，请输入验证码");
        add = i.getStringExtra("address");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Forget_validation_code.this, Forget_password.class);
                intent.putExtra("address", add);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}
