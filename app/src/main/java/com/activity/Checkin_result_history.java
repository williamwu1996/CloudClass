package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.cloudclass.CheckinHistoryAdapter;
import com.cloudclass.CheckinHistoryItem;
import com.cloudclass.R;

import java.util.ArrayList;
import java.util.List;

public class Checkin_result_history extends Activity {
    private List<CheckinHistoryItem> checkinlist = new ArrayList<>();
    Button back;
    private ListView checkinListView;
    CheckinHistoryAdapter checkinAdapter;
    String status="";

    Button finish;
    String chid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_result);
        Intent intent = getIntent();
        chid = intent.getStringExtra("chid");
        initHistoryList();
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
        checkinListView.setAdapter(checkinAdapter);
    }

        public void initHistoryList(){
        for(int i = 0;i<4;i++) {
//            CheckinHistoryItem sc1 = new CheckinHistoryItem(uname, distance);
//            checkinlist.add(sc1);
        }
    }

}
