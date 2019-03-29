package com.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
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
import android.widget.Toast;

import com.Util.FileUtils;
import com.Util.MapTable;
import com.cloudclass.HomeworkItem;
import com.cloudclass.HomeworkItemAdapter;
import com.cloudclass.R;
import com.cloudclass.ResourceItem;
import com.cloudclass.ResourceItemAdapter;
import com.cloudclass.SplashActivity;
import com.cloudclass.StudentMemberItem;
import com.cloudclass.StudentMemberItemAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
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

    private List<StudentMemberItem> memberlist = new ArrayList<>();
    private List<HomeworkItem> dnslist = new ArrayList<>();
    private List<HomeworkItem> goinglist = new ArrayList<>();
    private List<HomeworkItem> endlist = new ArrayList<>();
    private List<ResourceItem> resourcelist = new ArrayList<>();

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
    ResourceItemAdapter resourceItemAdapter;
    private ListView memberListView;
    ListView resourceListView;
    StudentMemberItemAdapter memberAdapter;

    ImageView classcover;
    TextView tvclassname, tvcoursename, tvteacher, tvprofile, tvclasscode;

    String cid;

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
        cid = intent.getStringExtra("cid");
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
//                startActivity(intent1);
                startActivityForResult(intent1,1);
            }
        });



        memberAdapter = new StudentMemberItemAdapter(memberlist);
        memberListView = findViewById(R.id.teacher_class_main_members_listview);
        //todo openfire
        memberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                TextView tv = arg1.findViewById(R.id.student_main_members_email);
                String email = tv.getText().toString();
                String temp = email.replace("@","#");
                Intent intent = new Intent();
                intent.putExtra("chatuser",temp+"@129.204.207.18");
                intent.setClass(Teacher_class_main.this, ChatRoom.class);
//                startActivity(intent);
                startActivityForResult(intent,3);
                Toast.makeText(getApplicationContext(),
                        "Chat with " + temp,
                        Toast.LENGTH_SHORT).show();
                memberAdapter.notifyDataSetChanged();
            }
        });

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

        resourceItemAdapter = new ResourceItemAdapter(Teacher_class_main.this,R.layout.resource_item, resourcelist);
        resourceListView = findViewById(R.id.teacher_main_resource_listview);
        resourceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView title = view.findViewById(R.id.resource_item_title);
                TextView filename = view.findViewById(R.id.resource_item_filename);
//                Toast.makeText(Teacher_class_main.this,filename.getText().toString(),Toast.LENGTH_SHORT).show();
                System.out.println("----------------------------");
                downloadFile(title.getText().toString(),filename.getText().toString());
            }
        });

        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_class_main.this,Teacher_resource_upload_pic.class);
                intent.putExtra("cid",cid);
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

        getStudents();
        getResources(cid);
    }

    public void downloadFile(final String title,String filename){
        final String suffix = filename.split("\\.")[1];
        final String name = title+"."+suffix;
        String url = "http://129.204.207.18:8079/resource/resource/"+filename;
        System.out.println("url is "+url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Success");
                InputStream is = response.body().byteStream();//得到图片的流
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                FileOutputStream fileOutputStream = null;//文件输出流
                if (is != null) {
                    FileUtils fileUtils = new FileUtils();
                    fileOutputStream = new FileOutputStream(fileUtils.createFile(title)+"."+suffix);//指定文件保存路径，代码看下一步
                    byte[] buf = new byte[1024];
                    int ch;
                    while ((ch = is.read(buf)) != -1) {
                        fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                    }
                    openFile(Teacher_class_main.this, name);
                }
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }
        });
    }

    public static void openFile(Context context, String filename){
        File file = new File(Environment.getExternalStorageDirectory() + "/com.cloudclass/"+filename);
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.fromFile(new File(file)), "*/*");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);//android 7.0以上
                Uri fileUri = FileProvider.getUriForFile(context, "com.rain.takephotodemo.FileProvider", file);//android 7.0以上
                intent.setDataAndType(fileUri, MapTable.getMIMEType(filename));
                grantUriPermission(context, fileUri, intent);
            } else {
//                intent.setDataAndType(/*uri*/Uri.fromFile(file), "*/*");
                intent.setDataAndType(/*uri*/Uri.fromFile(file), MapTable.getMIMEType(filename));
            }

            context.startActivity(intent);
            Intent.createChooser(intent, "请选择对应的软件打开该附件！");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }

    private static void grantUriPermission(Context context, Uri fileUri, Intent intent) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    public void getResources(String cid){
        String url = "http://192.168.3.169:8079/resource/getclassresources";
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
                System.out.println("-------------------------Resource retrieved Failed----------------------------");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                System.out.println("-------------------------Resources List----------------------------");
                System.out.println(body);
                initResources(body);
            }
        });
    }

    public void initResources(String json){
        resourcelist.clear();
        try{
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                ResourceItem item = new ResourceItem(obj.getString("name"),obj.getString("path"));
                resourcelist.add(item);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resourceListView.setAdapter(resourceItemAdapter);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 1)
        {
            if(resultCode == RESULT_CANCELED)
            {
//                text2.setText("点击了返回");
//                Toast.makeText(this,"点击了返回",Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (data != null)
                {
//                    text2.setText("得到第二个activity返回的结果:\n"+data.getAction());
//                    Toast.makeText(this,data.getAction(),Toast.LENGTH_LONG).show();
                    System.out.println("---------------------------------");
                    tvclassname.setText(data.getStringExtra("classname"));
                    tvcoursename.setText(data.getStringExtra("coursename"));
                    tvprofile.setText(data.getStringExtra("profile"));
                }
            }
        }
        if(requestCode == 2)
        {
            if(resultCode == RESULT_CANCELED)
            {
//                text2.setText("点击了返回");
//                Toast.makeText(this,"点击了返回",Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (data != null)
                {
                    tvclassname.setText(data.getStringExtra("classname"));
                    tvcoursename.setText(data.getStringExtra("coursename"));
                    tvprofile.setText(data.getStringExtra("profile"));
                }
            }
        }
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String me = sp.getString("USER_NAME","");//732315805@qq.com
        if(requestCode == 3)
        {
            if(resultCode == RESULT_CANCELED)
            {
                ContentValues values = new ContentValues();
                values.put("isread", "Y");//key为字段名，value为值
                //todo 將兩人的聊天記錄標為已讀
//                db.update("chathistory", values, "sender", new String[]{me.replace("@","#")});
//                db.update("chathistory", values, "receiver", new String[]{me.replace("@","#")});
            }
            else
            {
                ContentValues values = new ContentValues();
                values.put("isread", "Y");//key为字段名，value为值
//                db.update("chathistory", values, "sender", new String[]{me.replace("@","#")});
//                db.update("chathistory", values, "receiver", new String[]{me.replace("@","#")});
            }
        }
    }
    SQLiteDatabase db = SplashActivity.dbHelper.getWritableDatabase();
    public void getStudents(){
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
                String body = response.body().string();
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
            System.out.println("----------------------------------------memberlist--------------------------------");
            System.out.println(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                StudentMemberItem c = new StudentMemberItem(url+obj.getString("uid")+".JPG",obj.getString("name"),obj.getString("email"));
                memberlist.add(c);
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
            dnslist.clear();
            goinglist.clear();
            endlist.clear();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                HomeworkItem h = new HomeworkItem(obj.getString("question"),obj.getString("hid"),obj.getString("value"),obj.getString("profile"));
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

    @Override
    protected void onResume() {
        super.onResume();
//        getStudents();
        getResources(cid);
        initHomework(cid);
        initClasscover(cid);
    }
}
