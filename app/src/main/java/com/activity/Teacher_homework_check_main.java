package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudclass.R;
import com.cloudclass.StudentCheckinHistoryItem;
import com.cloudclass.StudentCheckinHistoryItemAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Teacher_homework_check_main extends Activity {
    private List<StudentCheckinHistoryItem> checkinlist = new ArrayList<>();

    Button back;
    private ListView homeworkListView;
    StudentCheckinHistoryItemAdapter homeworkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_homework_check_main);

        back = findViewById(R.id.teacher_homework_check_main_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initHomeworkList();
        homeworkAdapter = new StudentCheckinHistoryItemAdapter(Teacher_homework_check_main.this,R.layout.student_checkin_item, checkinlist);
        homeworkListView = findViewById(R.id.teacher_homework_check_main_listview);
        homeworkListView.setAdapter(homeworkAdapter);
        homeworkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView hrid = view.findViewById(R.id.student_checkin_history_id);
                TextView score = view.findViewById(R.id.student_checkin_history_status);
                if((score.getText().toString()).equals("未提交")){

                }else {
                    Intent intent = new Intent(Teacher_homework_check_main.this, Teacher_homework_check_detail.class);
                    intent.putExtra("hrid", hrid.getText().toString());
                    intent.putExtra("score",score.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    public void initHomeworkList(){
        Intent intent = getIntent();
        String hid = intent.getStringExtra("hid");
        String url = "http://129.204.207.18:8079/homework/getresult";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
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
            checkinlist.clear();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String score;
                if(obj.getJSONObject("homeworkresult").getInt("value")==-1){
                    score = "未批改";
                }else if(obj.getJSONObject("homeworkresult").getInt("value")==-2){
                    score = "未提交";
                }else{
                    score = String.valueOf(obj.getJSONObject("homeworkresult").getInt("value"))+"分";
                }
                StudentCheckinHistoryItem h = new StudentCheckinHistoryItem(obj.getJSONObject("users").getString("name"),score,String.valueOf(obj.getJSONObject("homeworkresult").getInt("hrid")));
                checkinlist.add(h);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                homeworkListView.setAdapter(homeworkAdapter);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHomeworkList();
    }
}
