package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.Bean.Checkinresult;
import com.cloudclass.CheckinHistoryAdapter;
import com.cloudclass.CheckinHistoryItem;
import com.cloudclass.R;
import com.cloudclass.SplashActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Checkin_result extends Activity {
    private List<CheckinHistoryItem> checkinlist = new ArrayList<>();
    private List<Checkinresult> updatelist = new ArrayList<>();
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
        initHistoryList();
//        testCheckdata();
        finish = findViewById(R.id.checkin_result_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存结果并退出
                finish();
            }
        });

        checkinAdapter = new CheckinHistoryAdapter(Checkin_result.this,R.layout.checkin_history_item, checkinlist);
        checkinListView = findViewById(R.id.checkin_result_listview);
        checkinListView.setAdapter(checkinAdapter);

    }

    SQLiteDatabase db = SplashActivity.dbHelper.getWritableDatabase();
    public void initHistoryList(){
        Cursor cursor = db.rawQuery("select * from checkin where chid = "+chid,null);
        if(cursor.moveToFirst()){
            do{
                String chid = cursor.getString(cursor.getColumnIndex("chid"));
                String uid = cursor.getString(cursor.getColumnIndex("uid"));
                String uname = cursor.getString(cursor.getColumnIndex("uname"));
                String distance = cursor.getString(cursor.getColumnIndex("distance"));
                System.out.println("---------------data------------------");
                System.out.println("uid = "+uid);
                System.out.println("uname = "+uname);
                System.out.println("chid = "+chid);
                System.out.println("distance = "+distance);
                if(distance.equals("-1")) {
                    CheckinHistoryItem sc1 = new CheckinHistoryItem(uname, "未签到");
                    checkinlist.add(sc1);
                }else{
                    CheckinHistoryItem sc1 = new CheckinHistoryItem(uname, distance+"米");
                    checkinlist.add(sc1);
                }
                Checkinresult cr = new Checkinresult();
                cr.setChid(Integer.parseInt(chid));
                cr.setUid(Integer.parseInt(uid));
                cr.setStatus(distance);
                updatelist.add(cr);
            }while (cursor.moveToNext());
        }
        cursor.close();
        updateCheckinresult();
    }

    public void updateCheckinresult(){
        //List转json
        JSONArray json = new JSONArray();
        try {

            for (Checkinresult item : updatelist) {
                JSONObject jo = new JSONObject();
                jo.put("chid", item.getChid());
                jo.put("status", item.getStatus());
                jo.put("uid", item.getUid());
                json.put(jo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(json.toString());
        String url = "http://192.168.3.169:8079/checkin/userscheckin";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("result", json.toString());
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
            }
        });
    }
}
