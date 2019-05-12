package com.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Join_class_code extends Activity {

    EditText code;
    Button next;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_class_code);
        code = findViewById(R.id.join_class_code);
        next = findViewById(R.id.join_class_next);
        cancel = findViewById(R.id.join_class_code_cancel);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //检查是否存在，若存在存到intent进入下一页
                String url = "http://129.204.207.18:8079/course/checkclassexist";
                OkHttpClient okHttpClient = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("cid", code.getText().toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody.build())
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    public void onFailure(Call call, IOException e) {

                    }
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        //需要测试没有的情况
                        if(json.equals("")){
                            Looper.prepare();
                            Toast.makeText(Join_class_code.this, "班课不存在", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }else {
                            try {
                                JSONArray jsonArray = new JSONArray(json);
                                JSONObject obj = jsonArray.getJSONObject(0);
                                String teacher = obj.getString("teacherName");
                                String classname = obj.getJSONObject("course").getString("classname");
                                String coursename = obj.getJSONObject("course").getString("cname");
                                Intent intent = new Intent(Join_class_code.this, Join_class_result.class);
                                intent.putExtra("code", code.getText().toString());
                                intent.putExtra("teacher", teacher);
                                intent.putExtra("classname", classname);
                                intent.putExtra("coursename", coursename);
                                startActivity(intent);
                                finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
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
