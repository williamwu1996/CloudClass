package com.cloudclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.Forget_email;
import com.activity.Register_email;

import java.awt.font.TextAttribute;

public class LoginActivity extends Activity {

    private EditText edt_username;
    private EditText edt_password;
    private TextView register;
    private TextView forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initViews();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this,Register_email.class);
                startActivity(intent);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this,Forget_email.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        findViewById(R.id.btn_login).setOnClickListener(mOnClickListener);
        register = findViewById(R.id.login_register);
        forget = findViewById(R.id.login_forget);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_login://登录
                    String userName = edt_username.getText().toString();
                    String userPwd = edt_password.getText().toString();
                    UserManage.getInstance().saveUserInfo(LoginActivity.this, userName, userPwd);
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainPage.class);//跳转到主页
                    startActivity(intent);
                    finish();
                    break;
            }

        }
    };


}
