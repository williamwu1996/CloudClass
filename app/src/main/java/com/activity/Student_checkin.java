package com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.cloudclass.R;
import com.cloudclass.StudentCheckinHistoryItem;
import com.cloudclass.StudentCheckinHistoryItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class Student_checkin extends Activity {

    private List<StudentCheckinHistoryItem> checkinlist = new ArrayList<>();
    Button back;
    private ListView checkinListView;
    StudentCheckinHistoryItemAdapter checkinAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_checkin_detail);
        initHistoryList();

        checkinAdapter = new StudentCheckinHistoryItemAdapter(Student_checkin.this,R.layout.student_checkin_item, checkinlist);
        checkinListView = findViewById(R.id.student_checkin_history);
        checkinListView.setAdapter(checkinAdapter);


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
            StudentCheckinHistoryItem sc1 = new StudentCheckinHistoryItem("2017-09-09 19:22", "已签到");
            checkinlist.add(sc1);
        }
    }
}
