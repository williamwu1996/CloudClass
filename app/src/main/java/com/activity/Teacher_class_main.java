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

import java.util.ArrayList;
import java.util.List;

public class Teacher_class_main extends AppCompatActivity {

    private String[] data = {"aaa","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb"};
    private List<StudentMemberItem> memberlist = new ArrayList<>();

    private TextView mTextMessage;
    Button back;
    LinearLayout detailLinearLayout;
    LinearLayout membersLinearLayout;
    LinearLayout resourceLinearLayout;
    LinearLayout uploadpc;
    LinearLayout uploadpic;
    Button editDetail;
    private TabHost tabHost;
    Button starthomework;
    Button checkin;

    private ListView memberListView;
    StudentMemberItemAdapter memberAdapter;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.teacher_class_navigation_tab_resource:
                    mTextMessage.setText("");
                    detailLinearLayout.setVisibility(View.INVISIBLE);
                    membersLinearLayout.setVisibility(View.INVISIBLE);
                    tabHost.setVisibility(View.INVISIBLE);
                    starthomework.setVisibility(View.INVISIBLE);
                    resourceLinearLayout.setVisibility(View.VISIBLE);
                    return true;
                case R.id.teacher_class_navigation_tab_member:
                    mTextMessage.setText("");
                    detailLinearLayout.setVisibility(View.INVISIBLE);
                    membersLinearLayout.setVisibility(View.VISIBLE);
                    tabHost.setVisibility(View.INVISIBLE);
                    starthomework.setVisibility(View.INVISIBLE);
                    resourceLinearLayout.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.teacher_class_navigation_tab_homework:
                    mTextMessage.setText("");
                    detailLinearLayout.setVisibility(View.INVISIBLE);
                    membersLinearLayout.setVisibility(View.INVISIBLE);
                    tabHost.setVisibility(View.VISIBLE);
                    starthomework.setVisibility(View.VISIBLE);
                    resourceLinearLayout.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.teacher_class_navigation_tab_classdetail:
                    mTextMessage.setText("");
                    detailLinearLayout.setVisibility(View.VISIBLE);
                    membersLinearLayout.setVisibility(View.INVISIBLE);
                    tabHost.setVisibility(View.INVISIBLE);
                    starthomework.setVisibility(View.INVISIBLE);
                    resourceLinearLayout.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_class_main);
        Intent intent = getIntent();
        String cid = intent.getStringExtra("cid");
        String uid = intent.getStringExtra("uid");

        initMemberList();

        checkin = findViewById(R.id.teacher_class_main_members_checkin_btn);
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_class_main.this,Checkin_main.class);
                startActivity(intent);
            }
        });

        detailLinearLayout = findViewById(R.id.teacher_class_main_detail_page);
        membersLinearLayout = findViewById(R.id.teacher_class_main_members_page);
        resourceLinearLayout = findViewById(R.id.teacher_class_main_resource_page);
        uploadpc = findViewById(R.id.teacher_resource_upload_from_pc);
        uploadpic = findViewById(R.id.teacher_resource_upload_from_pic);

        starthomework = findViewById(R.id.teacher_start_homework_btn);
        starthomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_class_main.this,Teacher_edit_homework.class);
                startActivity(intent);
            }
        });

        mTextMessage = (TextView) findViewById(R.id.teacher_class_main_test_message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.teacher_class_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        back = findViewById(R.id.teacher_class_main_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        editDetail = findViewById(R.id.teacher_class_main_detail_edit_button);
        editDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Teacher_class_main.this,Teacher_class_edit_detail.class);
                startActivity(intent);
            }
        });

        memberAdapter = new StudentMemberItemAdapter(Teacher_class_main.this,R.layout.student_member_item, memberlist);
        memberListView = findViewById(R.id.teacher_class_main_members_listview);
        memberListView.setAdapter(memberAdapter);

        tabHost = (TabHost) findViewById(R.id.teacher_class_main_homework_page);
        tabHost.setup();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        layoutInflater.inflate(R.layout.teacher_class_main_homework_dns, tabHost.getTabContentView());
        layoutInflater.inflate(R.layout.teacher_class_main_homework_going, tabHost.getTabContentView());
        layoutInflater.inflate(R.layout.teacher_class_main_homework_concluded, tabHost.getTabContentView());

        tabHost.addTab(tabHost.newTabSpec("").setIndicator("未开始")
                .setContent(R.id.teacher_homework_dns_tab));
        tabHost.addTab(tabHost.newTabSpec("").setIndicator("进行中")
                .setContent(R.id.teacher_homework_going_tab));
        tabHost.addTab(tabHost.newTabSpec("").setIndicator("已结束")
                .setContent(R.id.teacher_homework_concluded_tab));

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Teacher_class_main.this,android.R.layout.simple_list_item_1,data);
        ListView dnsListView = findViewById(R.id.teacher_homework_dns_listview);
        dnsListView.setAdapter(adapter1);
        dnsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Teacher_class_main.this,Teacher_edit_homework.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Teacher_class_main.this,android.R.layout.simple_list_item_1,data);
        ListView goingListView = findViewById(R.id.teacher_homework_going_listview);
        goingListView.setAdapter(adapter2);
        goingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Teacher_class_main.this,Teacher_homework_detail.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Teacher_class_main.this,android.R.layout.simple_list_item_1,data);
        ListView concludedListView = findViewById(R.id.teacher_homework_concluded_listview);
        concludedListView.setAdapter(adapter3);
        concludedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Teacher_class_main.this,Teacher_homework_check_main.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(Teacher_class_main.this,android.R.layout.simple_list_item_1,data);
        ListView resourceListView = findViewById(R.id.teacher_main_resource_listview);
        resourceListView.setAdapter(adapter4);

        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_class_main.this,Teacher_resource_upload_pic.class);
                startActivity(intent);
            }
        });

        uploadpc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_class_main.this,Teacher_resource_upload_pc.class);
                startActivity(intent);
            }
        });
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
