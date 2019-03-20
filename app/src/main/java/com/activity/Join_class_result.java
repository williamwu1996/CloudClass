package com.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudclass.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Join_class_result extends Activity {
    TextView course;
    TextView classname;
    TextView teacher;
    Button join;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_class_result);
        course = findViewById(R.id.join_class_result_course);
        classname = findViewById(R.id.join_class_result_class);
        teacher = findViewById(R.id.join_class_result_teacher);
        join = findViewById(R.id.join_class_finish);
        cancel = findViewById(R.id.join_class_result_cancel);

        Intent intent = getIntent();
        course.setText("课程："+intent.getStringExtra("coursename"));
        teacher.setText("老师："+intent.getStringExtra("teacher"));
        classname.setText("班级："+intent.getStringExtra("classname"));

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //加入课程
                SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String uid =  sp.getString("userid","");
                String name =  sp.getString("personname","");
                Intent intent = getIntent();
                String url = "http://192.168.3.169:8079/member/joinclass";
                OkHttpClient okHttpClient = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("uid", uid);
                formBody.add("cid", intent.getStringExtra("code"));
                formBody.add("name", name);
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody.build())
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        //需要测试没有的情况
                        System.out.println("Join class result :"+json);
                        Looper.prepare();
                        Toast.makeText(Join_class_result.this, "加入成功", Toast.LENGTH_SHORT).show();
                        finish();
                        Looper.loop();
                    }
                });
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
