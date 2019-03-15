package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cloudclass.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Teacher_start_homework extends Activity {
    Button back,post,notpost,discard;
    TextView title,value,profile;
    String cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_start_homework);
        title = findViewById(R.id.teacher_start_homework_title);
        value = findViewById(R.id.teacher_start_homework_value);
        profile = findViewById(R.id.teacher_start_homework_profile);

        back = findViewById(R.id.teacher_start_homework_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        discard = findViewById(R.id.teacher_start_homework_delete);
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        System.out.println("cid in addHomewok is "+cid);


        post = findViewById(R.id.teacher_start_homework_save_and_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHomework("going");
            }
        });

        notpost = findViewById(R.id.teacher_start_homework_save_not_post);
        notpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHomework("dns");
            }
        });
    }

    public void addHomework(String status){

        String titl = title.getText().toString();
        String valu = value.getText().toString();
        String profil = profile.getText().toString();
        String url = "http://192.168.3.169:8079/homework/addhomework";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("cid",cid);
        formBody.add("status",status);
        formBody.add("profile",profil);
        formBody.add("question",titl);
        formBody.add("value",valu);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {

            }
            public void onResponse(Call call, Response response) throws IOException {
                finish();
            }
        });
    }
}
