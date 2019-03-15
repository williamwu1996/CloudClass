package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Teacher_homework_detail extends Activity {

    Button back;
    TextView tvtitle, tvvalue, tvprofile;
    Button close, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_homework_detail);
        Intent intent = getIntent();
        String hid = intent.getStringExtra("hid");
        getHomework(hid);

        tvtitle = findViewById(R.id.teacher_homework_detail_title);
        tvvalue = findViewById(R.id.teacher_homework_detail_value);
        tvprofile = findViewById(R.id.teacher_homework_detail_profile);

        back = findViewById(R.id.teacher_homework_detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        close = findViewById(R.id.teacher_homework_detail_end);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String hid = intent.getStringExtra("hid");
                String url = "http://192.168.3.169:8079/homework/closehomework";
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
                        finish();
                    }
                });
            }
        });

        delete = findViewById(R.id.teacher_homework_detail_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除homework和homeworkresult两个表的数据
                Intent intent = getIntent();
                String hid = intent.getStringExtra("hid");
                String url = "http://192.168.3.169:8079/homework/deletehomework";
                OkHttpClient okHttpClient = new OkHttpClient();
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("hid", hid);
                formBody.add("code", "2");
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
//            tvtitle.setText(title);
        }catch (Exception e){
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvtitle.setText(title);
            }
        });
    }
}
