package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Util.CheckEmailAddr;
import com.cloudclass.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
                System.out.println("---------------------------------------");
                System.out.println(CheckEmailAddr.isEmail(address.getText().toString()));
                if(!CheckEmailAddr.isEmail(address.getText().toString())){
                    showEmpty();
                }else {
                    String url = "http://129.204.207.18:8079/users/emailCheck";
                    OkHttpClient okHttpClient = new OkHttpClient();
                    FormBody.Builder formBody = new FormBody.Builder();
                    formBody.add("email", address.getText().toString());
                    final Request request = new Request.Builder()
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
                            String result = response.body().string();
                            if (result.equals("true")) {
                                Intent intent = new Intent(Register_email.this, Register_validation_code.class);
                                intent.putExtra("address", address.getText().toString());
                                startActivity(intent);
                                finish();
                            } else {
                                Looper.prepare();
                                Toast.makeText(Register_email.this, "邮箱已被注册", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    });

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


    public void showEmpty(){
        Toast.makeText(this,"邮箱地址不合法",Toast.LENGTH_SHORT).show();
    }

}
