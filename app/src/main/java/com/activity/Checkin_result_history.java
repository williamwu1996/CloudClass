package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudclass.CheckinHistoryAdapter;
import com.cloudclass.CheckinHistoryItem;
import com.cloudclass.R;

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

public class Checkin_result_history extends Activity {
    private List<CheckinHistoryItem> checkinlist = new ArrayList<>();
    Button back;
    private ListView checkinListView;
    CheckinHistoryAdapter checkinAdapter;
    Button finish;
    String chid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_result);
        Intent intent = getIntent();
        chid = intent.getStringExtra("chid");

//        initHistoryList();
        getCheckinDetail();
        finish = findViewById(R.id.checkin_result_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存结果并退出
                finish();
            }
        });

        checkinAdapter = new CheckinHistoryAdapter(Checkin_result_history.this,R.layout.checkin_history_item, checkinlist);
        checkinListView = findViewById(R.id.checkin_result_listview);
//        checkinListView.setAdapter(checkinAdapter);
    }

    public void getCheckinDetail(){
        String url = "http://129.204.207.18:8079/checkin/getusersnamebychid";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("chid", chid);
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
                System.out.println("---------------------Checkin history--------------------");
                System.out.println(body);
                initHistoryList(body);
            }
        });
    }

    public void initHistoryList(String json){
        try{
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                String status = obj.getString("status");
                if(status.equals("-1")) {
                    CheckinHistoryItem m = new CheckinHistoryItem(obj.getString("username"),"未签到");
                    checkinlist.add(m);
                }else{
                    CheckinHistoryItem m = new CheckinHistoryItem(obj.getString("username"), obj.getString("status")+"米");
                    checkinlist.add(m);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkinListView.setAdapter(checkinAdapter);
            }
        });
    }

}
