package com.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.cloudclass.R;
import com.cloudclass.StudentMemberItem;
import com.cloudclass.StudentMemberItemAdapter;
import com.cloudclass.StudentResourceItem;
import com.cloudclass.StudentResourceItemAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Student_class_main extends AppCompatActivity {

    private String[] data = {"aaa","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb"};
    private List<StudentResourceItem> resourcelist = new ArrayList<>();
    private List<StudentMemberItem> memberlist = new ArrayList<>();

    private LinearLayout memberLinearLayout;
    private LinearLayout detailLinearLayout;
    private TextView mTextMessage;
    Button back;
    Button checkin;
    Button exitclass;
    private ListView resourceListView;
    StudentResourceItemAdapter resourceAdapter;
    private ListView memberListView;
    StudentMemberItemAdapter memberAdapter;

    ImageView classcover;
    TextView tvclassname, tvcoursename, tvteacher, tvprofile, tvclasscode;

    TextView title;

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
        Intent intent = getIntent();
        String cid = intent.getStringExtra("cid");
        String uid = intent.getStringExtra("uid");
        String profile = intent.getStringExtra("profile");
        String classname = intent.getStringExtra("classname");
        String coursename = intent.getStringExtra("coursename");
        String teacher = intent.getStringExtra("teacher");

        tvteacher = findViewById(R.id.student_class_main_detail_teacher);
        tvcoursename = findViewById(R.id.student_class_main_detail_coursename);
        tvclassname = findViewById(R.id.student_class_main_detail_classname);
        tvprofile = findViewById(R.id.student_class_main_detail_profile);
        tvclasscode = findViewById(R.id.student_class_main_detail_classcode);
        classcover = findViewById(R.id.student_class_main_detail_img);
        tvteacher.setText(teacher);
        tvcoursename.setText(coursename);
        tvclassname.setText(classname);
        tvprofile.setText(profile);
        tvclasscode.setText(cid);

        title = findViewById(R.id.student_class_title);
        title.setText(coursename);

        initResourceList();
//        initMemberList();
        initClasscover(cid);


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

//        memberAdapter = new StudentMemberItemAdapter(Student_class_main.this,R.layout.student_member_item, memberlist);
        memberAdapter = new StudentMemberItemAdapter(memberlist);
        memberListView = findViewById(R.id.student_class_main_members_listview);
//        memberListView.setAdapter(memberAdapter);

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

        exitclass = findViewById(R.id.student_class_main_detail_exit_button);
        exitclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String coursename = intent.getStringExtra("coursename");
                AlertDialog.Builder dialog = new AlertDialog.Builder(Student_class_main.this);
                dialog.setTitle("退出班课");
                dialog.setMessage("确认退出班课"+coursename+"？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
                        String cid = intent.getStringExtra("cid");
                        String uid = intent.getStringExtra("uid");
                        exitclass(uid, cid);

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

        String url = "http://192.168.3.169:8079/course/getstudents";
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
//                message.setText(response.body().string());
                String body = response.body().string();
                System.out.println("--------------------------------Member List-------------------------------");
                System.out.println(body);
                initMemberList(body);
            }
        });
    }

    public void exitclass(String uid, String cid){
        String url = "http://192.168.3.169:8079/course/exitclass";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("uid",uid);
        formBody.add("cid",cid);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {

            }
            public void onResponse(Call call, Response response) throws IOException {
                finish();
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

    private void initMemberList(final String json){
        Intent intent = getIntent();
        String cid = intent.getStringExtra("cid");
        String uid = intent.getStringExtra("uid");
        try{
            String url = "http://129.204.207.18:8079/resource/img/head_pic/";
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
//                if((obj.getJSONObject("course").getString("teacher")).equals(id)) {
                    StudentMemberItem c = new StudentMemberItem(url+obj.getString("uid")+".JPG",obj.getString("name"));
                    memberlist.add(c);
//                }else{
//                    ClassMain c = new ClassMain(url+obj.getJSONObject("course").getInt("cid")+".png", "", "", obj.getJSONObject("course").getString("cname"), obj.getString("teacherName"), String.valueOf(obj.getJSONObject("course").getInt("cid")),obj.getJSONObject("course").getString("profile"));
//                    classlist.add(c);
//                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                memberListView.setAdapter(memberAdapter);
            }
        });
    }

    public void initClasscover(String cid){
        String url = "http://129.204.207.18:8079/resource/img/cover/"+cid+".png";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("----------------------------------------------------------------------------------------------");
                System.out.println("图片图片picture");
                InputStream inputStream = response.body().byteStream();//得到图片的流
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Message msg = new Message();
                msg.obj = bitmap;
                handler.sendMessage(msg);
            }
        });
    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap)msg.obj;
            classcover.setImageBitmap(bitmap);//将图片的流转换成图片
        }
    };
}
