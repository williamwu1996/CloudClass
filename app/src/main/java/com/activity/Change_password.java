package com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cloudclass.R;

public class Change_password extends Activity {

    EditText oldpass;
    EditText newpass;
    EditText confirm;
    Button submit;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        oldpass = findViewById(R.id.change_password_oldpass);
        newpass = findViewById(R.id.change_password_newpass);
        confirm = findViewById(R.id.change_password_confirmpass);
        submit = findViewById(R.id.change_password_submit);
        cancel = findViewById(R.id.change_password_back);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //先验证，后修改
            }
        });
    }
}
