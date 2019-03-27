package com.cloudclass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Util.ChatServerConnection;
import com.activity.Forget_email;
import com.activity.Register_email;

import org.json.JSONArray;

import java.awt.font.TextAttribute;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {

    private EditText edt_username;
    private EditText edt_password;
    private TextView register;
    private TextView forget;
    String userName;
    String userPwd;
    Button l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initViews();
        l = findViewById(R.id.btn_login);
        l.getBackground().setAlpha(70);
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

                    userName = edt_username.getText().toString();
                    userPwd = edt_password.getText().toString();
                    System.out.println("username:"+userName);
                    System.out.println("password"+userPwd);
                    String url = "http://192.168.3.169:8079/users/login";
                    OkHttpClient okHttpClient = new OkHttpClient();
                    FormBody.Builder formBody = new FormBody.Builder();
                    formBody.add("email", userName);
                    formBody.add("password", userPwd);
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
                            String result = response.body().string();
                            try {
                                JSONArray jsonArray = new JSONArray(result);
                                if ((jsonArray.get(1).toString()).equals("true")) {
                                    SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("userid", jsonArray.get(0).toString());
                                    editor.putString("userid", jsonArray.get(0).toString());
                                    editor.commit();
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                                    UserManage.getInstance().saveUserInfo(LoginActivity.this, userName, userPwd);
                                    //todo 以用户身份登录openfire
                                    ChatServerConnection.closeConnection();
                                    boolean a = ChatServerConnection.login(userName.replace("@","#"),"12345");
                                    System.out.println("-----------------------------------------------------Login status(in login): "+a);
                                    Intent intent = new Intent(LoginActivity.this, MainPage.class);//跳转到主页
                                    startActivity(intent);
                                    Looper.loop();
                                    finish();
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, "用户名密码错误", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
            }

        }
    };


}
