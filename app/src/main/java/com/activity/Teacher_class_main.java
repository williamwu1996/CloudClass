package com.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

import com.cloudclass.HomeworkItem;
import com.cloudclass.HomeworkItemAdapter;
import com.cloudclass.R;
import com.cloudclass.StudentMemberItem;
import com.cloudclass.StudentMemberItemAdapter;

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

public class Teacher_class_main extends AppCompatActivity {

    private String[] data = {"aaa","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb"};
    private List<StudentMemberItem> memberlist = new ArrayList<>();
    private List<HomeworkItem> dnslist = new ArrayList<>();
    private List<HomeworkItem> goinglist = new ArrayList<>();
    private List<HomeworkItem> endlist = new ArrayList<>();


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
    TextView title;

    ListView dnsListView, goingListView, concludedListView;
    HomeworkItemAdapter dnsAdapter, goingAdapter, concludedAdapter;

    private ListView memberListView;
    StudentMemberItemAdapter memberAdapter;

    ImageView classcover;
    TextView tvclassname, tvcoursename, tvteacher, tvprofile, tvclasscode;

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
        String profile = intent.getStringExtra("profile");
        String classname = intent.getStringExtra("classname");
        String coursename = intent.getStringExtra("coursename");
        String teacher = intent.getStringExtra("teacher");

        tvteacher = findViewById(R.id.teacher_class_main_detail_teacher);
        tvcoursename = findViewById(R.id.teacher_class_main_detail_coursename);
        tvclassname = findViewById(R.id.teacher_class_main_detail_classname);
        tvprofile = findViewById(R.id.teacher_class_main_detail_profile);
        tvclasscode = findViewById(R.id.teacher_class_main_detail_classcode);
        classcover = findViewById(R.id.teacher_class_main_detail_img);

        tvteacher.setText(teacher);
        tvcoursename.setText(coursename);
        tvclassname.setText(classname);
        tvprofile.setText(profile);
        tvclasscode.setText(cid);
        initClasscover(cid);
//        initMemberList();

        title = findViewById(R.id.teacher_class_title);
        title.setText(coursename);
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
                Intent intent = getIntent();
                String cid = intent.getStringExtra("cid");
                Intent intent1 = new Intent(Teacher_class_main.this,Teacher_start_homework.class);
                intent1.putExtra("cid",cid);
                System.out.println("cid in main is :"+cid);
                startActivity(intent1);
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
                Intent intent = getIntent();
                String cid = intent.getStringExtra("cid");
                String profile = intent.getStringExtra("profile");
                String classname = intent.getStringExtra("classname");
                String coursename = intent.getStringExtra("coursename");
                String teacher = intent.getStringExtra("teacher");

                Intent intent1 = new Intent(Teacher_class_main.this,Teacher_class_edit_detail.class);
                intent1.putExtra("cid",cid);
                intent1.putExtra("profile",profile);
                intent1.putExtra("classname",classname);
                intent1.putExtra("coursename",coursename);
                intent1.putExtra("teacher",teacher);
                startActivity(intent1);
            }
        });

        memberAdapter = new StudentMemberItemAdapter(memberlist);
        memberListView = findViewById(R.id.teacher_class_main_members_listview);
//        memberListView.setAdapter(memberAdapter);

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

        initHomework(cid);


        dnsAdapter = new HomeworkItemAdapter(Teacher_class_main.this,R.layout.homework_item, dnslist);
        dnsListView = findViewById(R.id.teacher_homework_dns_listview);
        dnsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView hid = findViewById(R.id.homework_item_id);
                Intent intent = new Intent(Teacher_class_main.this,Teacher_edit_homework.class);
                intent.putExtra("hid",hid.getText().toString());
                startActivity(intent);
            }
        });

        goingAdapter = new HomeworkItemAdapter(Teacher_class_main.this,R.layout.homework_item, goinglist);
        goingListView = findViewById(R.id.teacher_homework_going_listview);
        goingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView hid = findViewById(R.id.homework_item_id);
                Intent intent = new Intent(Teacher_class_main.this,Teacher_homework_detail.class);
                intent.putExtra("hid",hid.getText().toString());
                startActivity(intent);
            }
        });

        concludedAdapter = new HomeworkItemAdapter(Teacher_class_main.this,R.layout.homework_item, endlist);
        concludedListView = findViewById(R.id.teacher_homework_concluded_listview);
        concludedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView hid = findViewById(R.id.homework_item_id);
                Intent intent = new Intent(Teacher_class_main.this,Teacher_homework_check_main.class);
                intent.putExtra("hid",hid.getText().toString());
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

    public void initHomework(String cid){
        String url = "http://192.168.3.169:8079/homework/gethomeworks";
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
                listhomework(body);
            }
        });
    }

    public void listhomework(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                HomeworkItem h = new HomeworkItem(obj.getString("question"),obj.getString("hid"));
                if ((obj.getString("status")).equals("dns")) {
                    dnslist.add(h);
                }else if((obj.getString("status")).equals("going")) {
                    goinglist.add(h);
                }else{
                    endlist.add(h);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dnsListView.setAdapter(dnsAdapter);
                goingListView.setAdapter(goingAdapter);
                concludedListView.setAdapter(concludedAdapter);
            }
        });
    }
}
