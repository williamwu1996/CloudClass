package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudclass.R;

public class Forget_password extends Activity {
    EditText password;
    EditText confirm;
    Button finish;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        password = findViewById(R.id.forget_password);
        confirm = findViewById(R.id.forget_confirm_password);
        finish = findViewById(R.id.forget_finish);
        cancel = findViewById(R.id.forget_password_cancel);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //判断两个密码是否相同
                if(password.equals(confirm)){
                    //重置密码

                    Intent intent = getIntent();
                    String address = intent.getStringExtra("address");
                    String pass = password.getText().toString();
                }else{
                    Toast.makeText(Forget_password.this, "密码不一致", Toast.LENGTH_LONG).show();
                    password.setText("");
                    confirm.setText("");
                }
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
