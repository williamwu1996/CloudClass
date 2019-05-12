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

public class Student_homework_answer_concluded extends Activity {

    Button back;
    TextView tvvalue, tvtitle, tvprofile, tvanswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_homework_answer_concluded);
        Intent intent = getIntent();
        String hid = intent.getStringExtra("hid");
        String uid = intent.getStringExtra("uid");
        value = intent.getStringExtra("value");
        String title = intent.getStringExtra("title");
        String profile = intent.getStringExtra("profile");

        tvvalue = findViewById(R.id.student_homework_answer_concluded_score);
        tvtitle = findViewById(R.id.student_homework_answer_concluded_question_title);
        tvprofile = findViewById(R.id.student_homework_answer_concluded_question_content);
        tvanswer = findViewById(R.id.student_homework_answer_concluded_answer);

        tvtitle.setText(title);
        tvprofile.setText(profile);

        initHomework(hid,uid);
        back = findViewById(R.id.student_homework_answer_concluded_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initHomework(String hid, String uid){
        String url = "http://129.204.207.18:8079/homework/studentgethomeworkresult";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("hid",hid);
        formBody.add("uid",uid);
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
    String answer;
    String value;
    String score;
    public void initUI(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
                JSONObject obj = jsonArray.getJSONObject(0);
                if(obj.getInt("value")==-1){
                    score = "--";
                }else{
                    score = String.valueOf(obj.getInt("value"));
                }
                answer = String.valueOf(obj.getString("answer"));
        }catch (Exception e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvvalue.setText("您的得分："+score+"/"+value);
                tvanswer.setText(answer);
            }
        });
    }
}
