package com.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
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
                if(validate(oldpass.getText().toString(),newpass.getText().toString(),confirm.getText().toString())){
                    changePass(newpass.getText().toString());
                }
            }
        });
    }

    public boolean validate(String old, String newp, String confirm){
        if(newp.equals("")){
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if(newp.equals(confirm)){
                SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String pass = sp.getString("PASSWORD","");
                if(pass.equals(old)){
                    return true;
                }else{
                    Toast.makeText(this,"原密码输入错误",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    public void changePass(String password){
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userid = sp.getString("userid","");
        String url = "http://192.168.3.169:8079/users/updatepassword";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("uid",userid);
        formBody.add("password",password);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {

            }
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                Toast.makeText(Change_password.this, "修改成功", Toast.LENGTH_SHORT).show();
                finishUpdate();
                Looper.loop();
            }
        });
    }

    public void finishUpdate(){
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("PASSWORD", newpass.getText().toString());
        editor.commit();
        finish();
    }
}
