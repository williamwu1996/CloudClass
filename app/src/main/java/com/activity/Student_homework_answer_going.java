package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
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

public class Student_homework_answer_going extends Activity {

    Button back,submit;
    TextView tvvalue, tvtitle, tvprofile;
    EditText etanswer;
    String hrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_homework_answer_going);
        tvvalue = findViewById(R.id.student_homework_answer_going_value);
        tvtitle = findViewById(R.id.student_homework_answer_going_question_title);
        tvprofile = findViewById(R.id.student_homework_answer_going_question_content);
        etanswer = findViewById(R.id.student_homework_answer_going_answer);

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String hid = intent.getStringExtra("hid");
        String value = intent.getStringExtra("value");
        String title = intent.getStringExtra("title");
        String profile = intent.getStringExtra("profile");

        tvvalue.setText("作业分值"+value+"分");
        tvprofile.setText(profile);
        tvtitle.setText(title);


        back = findViewById(R.id.student_homework_answer_going_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit = findViewById(R.id.student_homework_answer_going_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新作业
                updateHomeworkresult();
            }
        });

        initHomeworkresult(uid, hid);
    }
    String answer;
    public void initHomeworkresult(String uid, String hid){
        String url = "http://129.204.207.18:8079/homework/gethomeworkresult";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("uid",uid);
        formBody.add("hid",hid);
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

    public void initUI(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject obj = jsonArray.getJSONObject(0);
            answer = obj.getString("answer");
            hrid = obj.getString("hrid");
        }catch (Exception e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etanswer.setText(answer);
            }
        });
    }

    public void updateHomeworkresult(){
        String url = "http://129.204.207.18:8079/homework/doHomework";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("hrid",hrid);
        formBody.add("answer",etanswer.getText().toString());
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {

            }
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                Toast.makeText(Student_homework_answer_going.this, "提交成功", Toast.LENGTH_SHORT).show();
                finish();
                Looper.loop();
            }
        });

    }
}
