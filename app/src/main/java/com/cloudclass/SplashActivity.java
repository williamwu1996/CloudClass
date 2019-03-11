package com.cloudclass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends Activity {

    private static final int GO_HOME = 0;//去主页
    private static final int GO_LOGIN = 1;//去登录页
    /**
     * 跳转判断
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME://去主页
                    Intent intent = new Intent(SplashActivity.this, MainPage.class);
                    startActivity(intent);
                    finish();
                    break;
                case GO_LOGIN://去登录页
                    Intent intent2 = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        UserManage um = new UserManage();

        UserInfo userinfo = um.getUserInfo(this);
        if(userinfo!=null) {
            String email = userinfo.getUserName();
            String password = userinfo.getPassword();
//            String status = "";
            String url = "http://192.168.3.169:8079/users/login";
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder formBody = new FormBody.Builder();

            formBody.add("email", email);
            formBody.add("password", password);
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
                public void onResponse(Call call, Response response) throws IOException{
                    String result = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        if ((jsonArray.get(1).toString()).equals("true")) {
                            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("userid", jsonArray.get(0).toString());
                            editor.commit();
                            mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
                        } else {
                            mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }else{
            mHandler.sendEmptyMessageAtTime(GO_LOGIN, 2000);
        }




        //判断，将sharePreference里的数据发送至服务器做验证，若成功则跳入主页

//        if (UserManage.getInstance().validate(this))//自动登录判断，SharePrefences中有数据，则跳转到主页，没数据则跳转到登录页
//        {
//            mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
//        } else {
//            mHandler.sendEmptyMessageAtTime(GO_LOGIN, 2000);
//        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent1=new Intent(SplashActivity.this,MainPage.class);
//                startActivity(intent1);
//                SplashActivity.this.finish();
//            }
//        },3000);

    }


}
