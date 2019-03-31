package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudclass.R;
import com.cloudclass.StudentCheckinHistoryItem;
import com.cloudclass.StudentCheckinHistoryItemAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Student_checkin extends Activity {

    private List<StudentCheckinHistoryItem> checkinlist = new ArrayList<>();
    Button back,checkinbtn;
    private ListView checkinListView;
    StudentCheckinHistoryItemAdapter checkinAdapter;
    String cid;
    boolean ischecking = false;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_checkin_detail);
        checkinbtn = findViewById(R.id.student_class_main_members_checkin_btn);
        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        initHistoryList();

        checkinAdapter = new StudentCheckinHistoryItemAdapter(Student_checkin.this,R.layout.student_checkin_item, checkinlist);
        checkinListView = findViewById(R.id.student_checkin_history);
        checkinListView.setAdapter(checkinAdapter);

        checkinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查是否有签到
                requestCode();
            }
        });


        back = findViewById(R.id.student_checkin_detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    public void initHistoryList(){
        for(int i = 0;i<13;i++) {
            StudentCheckinHistoryItem sc1 = new StudentCheckinHistoryItem("2017-09-09 19:22", "已签到","111");
            checkinlist.add(sc1);
        }
    }

    public void requestCode(){
        //发送cid,返回班课码
        String url = "http://192.168.3.169:8079/checkin/getcheckincode";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("cid", cid);
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
                String body = response.body().string();
                if(body.equals("0000")){
                    Looper.prepare();
                    Toast.makeText(Student_checkin.this, "尚未开始签到或签到已结束！", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{
                    Intent intent = new Intent(Student_checkin.this,Student_checkin_code.class);
                    intent.putExtra("cid",cid);
                    intent.putExtra("code",body);
                    startActivity(intent);
                }
            }
        });
    }
}
