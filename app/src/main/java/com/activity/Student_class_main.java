package com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.cloudclass.R;
import com.cloudclass.StudentMemberItem;
import com.cloudclass.StudentMemberItemAdapter;
import com.cloudclass.StudentResourceItem;
import com.cloudclass.StudentResourceItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class Student_class_main extends AppCompatActivity {

    private String[] data = {"aaa","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb"};
    private List<StudentResourceItem> resourcelist = new ArrayList<>();
    private List<StudentMemberItem> memberlist = new ArrayList<>();

    private LinearLayout memberLinearLayout;
    private LinearLayout detailLinearLayout;
    private TextView mTextMessage;
    Button back;
    Button checkin;
    private ListView resourceListView;
    StudentResourceItemAdapter resourceAdapter;
    private ListView memberListView;
    StudentMemberItemAdapter memberAdapter;

    private TabHost tabHost;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.student_class_navigation_tab_resource:
                    mTextMessage.setText("");
                    resourceListView.setVisibility(View.VISIBLE);
                    memberLinearLayout.setVisibility(View.INVISIBLE);
                    detailLinearLayout.setVisibility(View.INVISIBLE);
                    tabHost.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.student_class_navigation_tab_member:
                    mTextMessage.setText("");
                    resourceListView.setVisibility(View.INVISIBLE);
                    memberLinearLayout.setVisibility(View.VISIBLE);
                    detailLinearLayout.setVisibility(View.INVISIBLE);
                    tabHost.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.student_class_navigation_tab_homework:
                    mTextMessage.setText("");
                    resourceListView.setVisibility(View.INVISIBLE);
                    memberLinearLayout.setVisibility(View.INVISIBLE);
                    detailLinearLayout.setVisibility(View.INVISIBLE);
                    tabHost.setVisibility(View.VISIBLE);
                    return true;
                case R.id.student_class_navigation_tab_classdetail:
                    mTextMessage.setText("");
                    resourceListView.setVisibility(View.INVISIBLE);
                    memberLinearLayout.setVisibility(View.INVISIBLE);
                    detailLinearLayout.setVisibility(View.VISIBLE);
                    tabHost.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_class_main);
        memberLinearLayout = findViewById(R.id.student_class_main_members_page);
        detailLinearLayout = findViewById(R.id.student_class_main_detail_page);
        initResourceList();
        initMemberList();



        tabHost = (TabHost) findViewById(R.id.student_class_main_homework_page);
        tabHost.setup();

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        layoutInflater.inflate(R.layout.student_class_main_homework_going, tabHost.getTabContentView());
        layoutInflater.inflate(R.layout.student_class_main_homework_concluded, tabHost.getTabContentView());

        tabHost.addTab(tabHost.newTabSpec("").setIndicator("进行中")
                .setContent(R.id.student_class_main_homework_going_tab));

        tabHost.addTab(tabHost.newTabSpec("").setIndicator("已结束")
                .setContent(R.id.student_class_main_homework_concluded_tab));


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Student_class_main.this,android.R.layout.simple_list_item_1,data);
        ListView goingListView = findViewById(R.id.student_class_main_homework_going_listview);
        goingListView.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Student_class_main.this,android.R.layout.simple_list_item_1,data);
        ListView concludedListView = findViewById(R.id.student_class_main_homework_concluded_listview);
        concludedListView.setAdapter(adapter2);

        resourceAdapter = new StudentResourceItemAdapter(Student_class_main.this,R.layout.student_resource_item, resourcelist);
        resourceListView = findViewById(R.id.student_class_main_resource_listview);
        resourceListView.setAdapter(resourceAdapter);

        memberAdapter = new StudentMemberItemAdapter(Student_class_main.this,R.layout.student_member_item, memberlist);
        memberListView = findViewById(R.id.student_class_main_members_listview);
        memberListView.setAdapter(memberAdapter);

        mTextMessage = (TextView) findViewById(R.id.student_class_main_test_message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.student_class_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        back = findViewById(R.id.student_class_main_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        checkin = findViewById(R.id.student_class_main_members_checkin_btn);
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Student_class_main.this,Student_checkin.class);
                startActivity(intent);
            }
        });

        goingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Student_class_main.this,Student_homework_answer_going.class);
                startActivity(intent);
            }
        });

        concludedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Student_class_main.this,Student_homework_answer_concluded.class);
                startActivity(intent);
            }
        });
    }

    private void initResourceList(){
        for(int i = 0;i<3;i++) {
            StudentResourceItem r1 = new StudentResourceItem(R.drawable.timg, "资源1", "2019-02-22 11:11");
            resourcelist.add(r1);
            StudentResourceItem r2 = new StudentResourceItem(R.drawable.timg, "资源2", "2019-02-23 11:11");
            resourcelist.add(r2);
            StudentResourceItem r3 = new StudentResourceItem(R.drawable.timg, "资源3", "2019-02-24 11:11");
            resourcelist.add(r3);
            StudentResourceItem r4 = new StudentResourceItem(R.drawable.timg, "资源4", "2019-02-25 11:11");
            resourcelist.add(r4);
        }
    }

    private void initMemberList(){
        for(int i = 0;i<3;i++) {
            StudentMemberItem r1 = new StudentMemberItem(R.drawable.timg,"Student1");
            memberlist.add(r1);
            StudentMemberItem r2 = new StudentMemberItem(R.drawable.timg,"Student2");
            memberlist.add(r2);
            StudentMemberItem r3 = new StudentMemberItem(R.drawable.timg,"Student3");
            memberlist.add(r3);
            StudentMemberItem r4 = new StudentMemberItem(R.drawable.timg,"Student4");
            memberlist.add(r4);

        }
    }
}
