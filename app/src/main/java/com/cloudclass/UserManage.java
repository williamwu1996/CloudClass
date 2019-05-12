package com.cloudclass;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserManage {

    public static UserManage instance;
    String result="";


    public UserManage() {
    }

    public static UserManage getInstance() {
        if (instance == null) {
            instance = new UserManage();
        }
        return instance;
    }

    public void saveUserInfo(Context context, String username, String password) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_NAME", username);
        editor.putString("PASSWORD", password);
        editor.commit();
//        MyApplication app = (MyApplication)this.getApplication();
    }

    public UserInfo getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(sp.getString("USER_NAME", ""));
        userInfo.setPassword(sp.getString("PASSWORD", ""));
        return userInfo;
    }

    public boolean hasUserInfo(Context context) {
        UserInfo userInfo = getUserInfo(context);
        if (userInfo != null) {
            if ((!TextUtils.isEmpty(userInfo.getUserName())) && (!TextUtils.isEmpty(userInfo.getPassword()))) {//有数据
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean validate(Context context){
        UserInfo userinfo = getUserInfo(context);
        if(userinfo!=null) {
            String email = userinfo.getUserName();
            String password = userinfo.getPassword();
//            String status = "";
            String url = "http://129.204.207.18/users/login";
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
                    result = response.body().string();
                }
            });
//            try {
//                Response response = okHttpClient.newCall(request).execute();
//                if(response.isSuccessful()){
//                    result = response.body().string();
//                }else{
//                    System.out.println("failed------------");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            if(result.equals("true")){
                System.out.println("-----------------------");
                System.out.println("It is true in UserManage");
                System.out.println("-----------------------");
                return true;
            }else{
                System.out.println("-----------------------");
                System.out.println(result);
                System.out.println("-----------------------");
                return false;
            }

        }else{
            System.out.println("-----------------------");
            System.out.println("Not exist");
            System.out.println("-----------------------");
            return false;
        }
    }
}
