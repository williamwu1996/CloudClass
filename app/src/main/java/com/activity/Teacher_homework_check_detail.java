package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Teacher_homework_check_detail extends Activity {

    Button back,finish;
    TextView tvvalue, tvtitle, tvprofile, tvanswer;
    EditText etscore;
    String hrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_homework_check_detail);
        Intent intent = getIntent();
        hrid = intent.getStringExtra("hrid");

        tvvalue = findViewById(R.id.teacher_homework_check_detail_questionvalue);
        tvtitle = findViewById(R.id.teacher_homework_check_detail_title);
        tvprofile = findViewById(R.id.teacher_homework_check_detail_content);
        tvanswer = findViewById(R.id.teacher_homework_check_detail_answer);
        etscore = findViewById(R.id.teacher_homework_check_detail_score);

        back = findViewById(R.id.teacher_homework_check_detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        finish = findViewById(R.id.teacher_homework_check_detail_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = Integer.parseInt(etscore.getText().toString());
//                int value = Integer.parseInt(tvvalue.getText().toString());
                int vi = Integer.parseInt(value);
                if(score<0||score>vi){

                }else {
                    checkHomework();
                }
            }
        });

        initHomework(hrid);
    }

    public void initHomework(String hrid){
        String url = "http://192.168.3.169:8079/homework/getcheckhomework";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("hrid",hrid);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {

            }
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                System.out.println("-------------------------");
                System.out.println(body);
                initUI(body);
            }
        });
    }
    String value;
    String title;
    String profile;
    String answer;
    String score;
    public void initUI(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if(obj.getInt("score")==-1){
                    score = "";
                }else{
                    score = String.valueOf(obj.getInt("value"));
                }
                value = String.valueOf(obj.getInt("value"));
                title = String.valueOf(obj.getString("title"));
                profile = String.valueOf(obj.getString("profile"));
                answer = String.valueOf(obj.getString("answer"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etscore.setText(score);
                tvprofile.setText(profile);
                tvvalue.setText("分值："+value+"分");
                tvanswer.setText(answer);
                tvtitle.setText(title);
            }
        });
    }

    public void checkHomework(){
        String url = "http://192.168.3.169:8079/homework/checkhomework";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("hrid",hrid);
        formBody.add("score",etscore.getText().toString());
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
