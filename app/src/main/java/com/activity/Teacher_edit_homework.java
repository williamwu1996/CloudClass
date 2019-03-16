package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class Teacher_edit_homework extends Activity {

    Button back;
    EditText tvtitle, tvvalue, tvprofile;
    Button post, notpost, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_edit_homework);
        Intent intent = getIntent();
        String hid = intent.getStringExtra("hid");

        tvtitle = findViewById(R.id.teacher_edit_homework_title);
        tvvalue = findViewById(R.id.teacher_edit_homework_value);
        tvprofile = findViewById(R.id.teacher_edit_homework_profile);

        getHomework(hid);

        back = findViewById(R.id.teacher_edit_homework_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        post = findViewById(R.id.teacher_edit_homework_save_and_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatehomework("dns");
            }
        });

        notpost = findViewById(R.id.teacher_edit_homework_save_not_post);
        notpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatehomework("going");
            }
        });

        delete = findViewById(R.id.teacher_edit_homework_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String hid = intent.getStringExtra("hid");
                String url = "http://192.168.3.169:8079/homework/deletehomework";
                OkHttpClient okHttpClient = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("hid", hid);
                formBody.add("code", "1");
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody.build())
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("-------------------------Failed----------------------------");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        finish();
                    }
                });
            }
        });

    }

    public void updatehomework(String status){
        Intent intent = getIntent();
        String hid = intent.getStringExtra("hid");
        String url = "http://192.168.3.169:8079/homework/gethomeworkbyid";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("hid", hid);
        formBody.add("status", status);
        formBody.add("value", tvvalue.getText().toString());
        formBody.add("profile", tvprofile.getText().toString());
        formBody.add("question", tvtitle.getText().toString());
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("-------------------------Failed----------------------------");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                finish();
            }
        });
    }

    public void getHomework(String hid){
        String url = "http://192.168.3.169:8079/homework/gethomeworkbyid";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("hid", hid);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("-------------------------Failed----------------------------");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                message.setText(response.body().string());
                String body = response.body().string();
                System.out.println("--------------------------------Homework Edit-------------------------------");
                System.out.println(body);
                inithomework(body);
            }
        });
    }
    String title;
    String value;
    String profile;
    public void inithomework(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject obj = jsonArray.getJSONObject(0);
//            System.out.println();
            title = obj.getString("question");
            value = obj.getString("value");
            profile = obj.getString("profile");
//            tvtitle.setText(title);
        }catch (Exception e){
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvtitle.setText(title);
                tvvalue.setText(value);
                tvprofile.setText(profile);
            }
        });
    }


}
