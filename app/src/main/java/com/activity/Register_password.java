package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudclass.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Register_password extends Activity {

    EditText password;
    EditText confirm;
    Button finish;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_password);
        password = findViewById(R.id.register_password);
        confirm = findViewById(R.id.register_confirm_password);
        finish = findViewById(R.id.register_finish);
        cancel = findViewById(R.id.register_password_cancel);

        Intent intent = getIntent();
        System.out.println("----------------------------------");
        System.out.println(intent.getStringExtra("address"));
        System.out.println("----------------------------------");
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //判断两个密码是否相同
                if((password.getText().toString()).equals(confirm.getText().toString())){
                    //注册
                    Intent intent = getIntent();
                    String address = intent.getStringExtra("address");
                    String pass = password.getText().toString();
                    register(address,pass);
                    finish();
                }else{
                    Toast.makeText(Register_password.this, "密码不一致", Toast.LENGTH_LONG).show();
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

    public void register(String address,String password){
        String url = "http://192.168.137.1:8079/users/register";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("email",address);
        formBody.add("password",password);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                message.setText(response.body().string());
                //成功true，不成功false
                System.out.println("---------------------------");
                System.out.println(response.body().string());
                System.out.println("---------------------------");
            }
        });

    }
}
