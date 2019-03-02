package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cloudclass.R;

public class Register_email extends Activity {

    EditText address;
    Button next;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_email);
        address = findViewById(R.id.register_email_address);
        next = findViewById(R.id.register_email_next);
        cancel = findViewById(R.id.register_email_cancel);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Register_email.this,Register_validation_code.class);
                intent.putExtra("address",address.getText().toString());
                startActivity(intent);
                finish();
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
