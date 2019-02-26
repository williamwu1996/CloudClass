package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.cloudclass.R;
import com.cloudclass.StudentCheckinHistoryItem;
import com.cloudclass.StudentCheckinHistoryItemAdapter;

import java.util.ArrayList;
import java.util.List;

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
                Intent intent = new Intent(Teacher_homework_check_main.this,Teacher_homework_check_detail.class);
                startActivity(intent);
            }
        });
    }

    public void initHomeworkList(){
        for(int i = 0;i<13;i++) {
            StudentCheckinHistoryItem sc1 = new StudentCheckinHistoryItem("吴玠璘", "0分");
            checkinlist.add(sc1);
        }
    }
}
