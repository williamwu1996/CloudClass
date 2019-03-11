package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudclass.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Register_validation_code extends Activity {

    TextView address;
    EditText code;
    Button next;
    String add;
    Button cancel;
    String validationcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_validation_code);
        code = findViewById(R.id.register_validation_code_code);
        next = findViewById(R.id.register_validation_code_next);
        address = findViewById(R.id.register_validation_address);
        cancel = findViewById(R.id.register_validation_cancel);

        Intent i = getIntent();
        address.setText("验证码已经发送至" + i.getStringExtra("address") + "，请输入验证码");
        add = i.getStringExtra("address");
        getValidationCode(add);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                System.out.println(validationcode+":"+code.getText().toString());
                if(validationcode.equals(code.getText().toString())) {
                    Intent intent = new Intent(Register_validation_code.this, Register_password.class);
                    intent.putExtra("address", add);
                    startActivity(intent);
                    finish();
                }else{
//                    Toast.makeText(this,"验证码错误，请重新输入",Toast.LENGTH_SHORT).show();
                    System.out.println("验证码错误！");
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

    public void getValidationCode(String address){
        String url = "http://192.168.3.169:8079/users/registerValidation";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("address",address);
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
                //获取收到的验证码
                validationcode = response.body().string();
                System.out.println("------------------------");
                System.out.println(validationcode);
                System.out.println("------------------------");
            }
        });

    }
}
