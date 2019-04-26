package com.cloudclass;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Bean.Email;
import com.Util.ChatServerConnection;
import com.Util.MyChatManagerListener;
import com.Util.MyDatabaseHelper;
import com.activity.Change_password;
import com.activity.Change_personal_info;
import com.activity.ChatRoom;
import com.activity.Create_class_info;
import com.activity.Join_class_code;
import com.activity.Student_class_main;
import com.activity.Teacher_class_main;

import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainPage extends AppCompatActivity implements View.OnClickListener{

    private List<ClassMain> classlist = new ArrayList<>();
    private List<MessageMain> messagelist = new ArrayList<>();
    private PopupWindow mPopWindow;

    private ListView listView;
    private ListView listViewmessage;
    ClassAdapter classadapter;
    MessageAdapter messageadapter;
    LinearLayout linearLayout;
    Button addclass;
    TextView title;
    RelativeLayout changepassword;
    RelativeLayout changeinfo;
    Button exit;
    ImageView headpic;
    TextView personname;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    title.setText("班课");
                    addclass.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                    listView.setAdapter(classadapter);
                    listViewmessage.setAdapter(null);
                    return true;

                case R.id.navigation_dashboard:
                    title.setText("消息");
                    addclass.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                    listView.setAdapter(null);
                    listViewmessage.setAdapter(messageadapter);
                    return true;

                case R.id.navigation_notifications:
                    title.setText("我");
                    addclass.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    listView.setAdapter(null);
                    listViewmessage.setAdapter(null);
                    return true;
            }
            return false;
        }
    };
    String t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        headpic = findViewById(R.id.me_person_image);
        personname = findViewById(R.id.me_personname);
        ChatManager chatManager = ChatManager.getInstanceFor(ChatServerConnection.getConnection());
        chatManager.addChatListener(new MyChatManagerListener());

        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        t = sp.getString("userid","");

        initHeadpic(t);
        initPerson(t);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        exit = findViewById(R.id.me_exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context context = getApplicationContext();
                SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER_NAME","");
                editor.putString("PASSWORD","");
                editor.putString("userid","");
                editor.commit();
                //todo openfire退出当前账号并登录admin账号（貌似有问题）
                ChatServerConnection.closeConnection();
                ChatServerConnection.openConnection();
                boolean a = ChatServerConnection.login("admin","admin");
                System.out.println("---------------------------------Login status(退出)"+a);
                Intent intent = new Intent(MainPage.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        messageadapter = new MessageAdapter(messagelist);
        classadapter = new ClassAdapter(classlist);
        listView = findViewById(R.id.list_view);

        listViewmessage = findViewById(R.id.list_view_message);
        listViewmessage.setAdapter(null);
        listViewmessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = view.findViewById(R.id.message_fromclass);
                TextView personname = view.findViewById(R.id.message_personname);
                String email = tv.getText().toString();
                String temp = email.replace("@","#");
                Intent intent = new Intent();
                intent.putExtra("chatuser",temp+"@129.204.207.18");
                intent.putExtra("chatusername",personname.getText().toString());
                intent.setClass(MainPage.this, ChatRoom.class);
                startActivityForResult(intent,1);
//                Toast.makeText(getApplicationContext(),
//                        "Chat with " + temp,
//                        Toast.LENGTH_SHORT).show();
                messageadapter.notifyDataSetChanged();
            }
        });

        linearLayout = findViewById(R.id.me_navigation);
        addclass = findViewById(R.id.add_class);
        title = findViewById(R.id.main_title);
        changepassword = findViewById(R.id.me_change_password_btn);
        changeinfo = findViewById(R.id.main_page_to_change_info);

        addclass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainPage.this,Change_password.class);
                startActivity(intent);
            }
        });

        changeinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainPage.this,Change_personal_info.class);
                startActivity(intent);
            }
        });

        getMessageMember();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //先判断是老师还是学生
                TextView iscreater = (TextView) view.findViewById(R.id.iscreater);
                TextView code = view.findViewById(R.id.code);
                TextView profile = view.findViewById(R.id.profile);
                TextView classname = view.findViewById(R.id.class_name);
                TextView coursename  = view.findViewById(R.id.course);
                TextView teacher = view.findViewById(R.id.teacher);

                SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String uid = sp.getString("userid","");
                if((code.getText().toString()).equals("")) {
                    //学生
                    Intent intent1 = new Intent(MainPage.this, Student_class_main.class);
                    intent1.putExtra("cid",iscreater.getText().toString());
                    intent1.putExtra("uid",uid);
                    intent1.putExtra("profile",profile.getText().toString());
                    intent1.putExtra("classname",classname.getText().toString());
                    intent1.putExtra("coursename",coursename.getText().toString());
                    intent1.putExtra("teacher",teacher.getText().toString());

                    startActivity(intent1);
                }else{
                    //老师
                    Intent intent2 = new Intent(MainPage.this, Teacher_class_main.class);
                    intent2.putExtra("cid",iscreater.getText().toString());
                    intent2.putExtra("uid",uid);
                    intent2.putExtra("profile",profile.getText().toString());
                    intent2.putExtra("classname",classname.getText().toString());
                    intent2.putExtra("coursename",coursename.getText().toString());
                    intent2.putExtra("teacher",teacher.getText().toString());
                    startActivity(intent2);
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void getAllClass(){
        String url = "http://192.168.3.169:8079/course/getallclass";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("uid", t);
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
                initClassList(body);
                System.out.println("-----------------------------------------------");
                System.out.println(body);
            }
        });
    }

    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(MainPage.this).inflate(R.layout.addclass_popup, null);
        mPopWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);

        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });


        mPopWindow.setContentView(contentView);
        mPopWindow.setFocusable(true);
        mPopWindow.setOutsideTouchable(false);
        //设置各个控件的点击响应
        TextView tv1 = (TextView)contentView.findViewById(R.id.pop_add_class);
        TextView tv2 = (TextView)contentView.findViewById(R.id.pop_join_class);
        TextView tv3 = (TextView)contentView.findViewById(R.id.pop_cancel);


        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);


        //显示PopupWindow
        View rootview = LayoutInflater.from(MainPage.this).inflate(R.layout.activity_main_page, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String me = sp.getString("USER_NAME","");//732315805@qq.com
        if(requestCode == 1)
        {
            if(resultCode == RESULT_CANCELED)
            {
                listViewmessage.setAdapter(messageadapter);
                listView.setAdapter(null);
            }
            else
            {
                listViewmessage.setAdapter(messageadapter);
                listView.setAdapter(null);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.pop_add_class:{
                Intent intent = new Intent(MainPage.this,Create_class_info.class);
                startActivity(intent);
                mPopWindow.dismiss();
            }
            break;
            case R.id.pop_join_class:{
                //切换界面
                Intent intent = new Intent(MainPage.this,Join_class_code.class);
                startActivity(intent);
                mPopWindow.dismiss();
            }
            break;
            case R.id.pop_cancel:{
                //切换界面
                mPopWindow.dismiss();
            }
            break;
        }
    }


    private void initClassList(final String json){
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String id =  sp.getString("userid","");
        classlist.clear();
        try{
            String url = "http://129.204.207.18:8079/resource/img/cover/";
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                if((obj.getJSONObject("course").getString("teacher")).equals(id)) {
                    ClassMain c = new ClassMain(url+obj.getJSONObject("course").getInt("cid")+".png", String.valueOf(obj.getJSONObject("course").getInt("cid")), obj.getJSONObject("course").getString("classname"), obj.getJSONObject("course").getString("cname"), obj.getString("teacherName"), String.valueOf(obj.getJSONObject("course").getInt("cid")),obj.getJSONObject("course").getString("profile"));
                    classlist.add(c);
                }else{
                    ClassMain c = new ClassMain(url+obj.getJSONObject("course").getInt("cid")+".png", "", obj.getJSONObject("course").getString("classname"), obj.getJSONObject("course").getString("cname"), obj.getString("teacherName"), String.valueOf(obj.getJSONObject("course").getInt("cid")),obj.getJSONObject("course").getString("profile"));
                    classlist.add(c);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(classadapter);
            }
        });
    }

    SQLiteDatabase db = SplashActivity.dbHelper.getWritableDatabase();
    public void getMessageMember(){
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String me = sp.getString("USER_NAME","");//732315805@qq.com
        Set<String> emails = new HashSet<>();
        Cursor cursor = db.rawQuery("select distinct sender from chathistory where receiver = '"+me.replace("@","#")+"'",null);
        if(cursor.moveToFirst()){
            do{
                String email = cursor.getString(cursor.getColumnIndex("sender"));
                emails.add(email);
            }while (cursor.moveToNext());
        }
        cursor.close();
        Cursor cursor1 = db.rawQuery("select distinct receiver from chathistory where sender = '"+me.replace("@","#")+"'",null);
        if(cursor1.moveToFirst()){
            do{
                String email = cursor1.getString(cursor1.getColumnIndex("receiver"));
                emails.add(email);
            }while (cursor1.moveToNext());
        }
        cursor1.close();
        String testinfo = "";

        for(String e:emails){
            testinfo += e+"@";
        }
        //發送testinfo返回users
        String url = "http://129.204.207.18:8079/users/getusers";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("testinfo", testinfo);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                System.out.println("Message Members retrieve failed........");
            }

            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                System.out.println();
                System.out.println("-------------------Message Members-------------------");
                System.out.println(json);
                initMessageList(json);
            }
        });
    }

    private void initMessageList(final String json){
        messagelist.clear();
        try{
            String url = "http://129.204.207.18:8079/resource/img/head_pic/";
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                MessageMain m = new MessageMain(url+obj.getInt("uid")+".JPG",obj.getString("email"),"2019-3-27",obj.getString("name"),"this is content");
                messagelist.add(m);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listViewmessage.setAdapter(null);
            }
        });
    }

    //个人信息,放入sp
    public void initPerson(String id){
        String url = "http://192.168.3.169:8079/users/getuserinfo";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("uid", id);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {

            }
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
//                System.out.println(json);
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    JSONObject obj = jsonArray.getJSONObject(0);
                    SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("email", obj.getString("email"));
                    editor.putString("personname", obj.getString("name"));
                    editor.putString("phone", obj.getString("phone"));
                    editor.putString("gender", obj.getString("gender"));
                    editor.commit();
//                    personname.setText(sp.getString("personname",""));
                }catch (Exception e){
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        personname.setText(sp.getString("personname",""));
                    }
                });
            }
        });
    }

    public void initHeadpic(String id){
        String url = "http://129.204.207.18:8079/resource/img/head_pic/"+id+".JPG";
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
            headpic.setImageBitmap(bitmap);//将图片的流转换成图片
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        initHeadpic(t);
        initPerson(t);
//        initMessageList();
        getMessageMember();
        getAllClass();
    }

}
