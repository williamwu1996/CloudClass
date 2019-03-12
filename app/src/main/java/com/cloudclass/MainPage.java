package com.cloudclass;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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

import com.activity.Change_password;
import com.activity.Change_personal_info;
import com.activity.Create_class_info;
import com.activity.Join_class_code;
import com.activity.Student_class_main;
import com.activity.Teacher_class_main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

//    private Handler mHandler = new Handler(){
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    ClassAdapter adapter = new ClassAdapter(classlist);
//                    listView.setAdapter(adapter);
//                    break;
//
//                default:
//                    break;
//            }
//        }
//
//    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_page);
        headpic = findViewById(R.id.me_person_image);
        personname = findViewById(R.id.me_personname);

        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String t = sp.getString("userid","");
        personname.setText(sp.getString("personname",""));
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
                System.out.println("--------------------------------");
                System.out.println(body);
                System.out.println("--------------------------------");
            }
        });

        initHeadpic(t);
        initPerson(t);
        initMessageList();

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
                Intent intent = new Intent(MainPage.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        messageadapter = new MessageAdapter(MainPage.this, R.layout.message_item, messagelist);

//        classadapter = new ClassAdapter(MainPage.this,R.layout.class_item, classlist);
        classadapter = new ClassAdapter(classlist);
        listView = findViewById(R.id.list_view);
//        listView.setAdapter(classadapter);

        listViewmessage = findViewById(R.id.list_view_message);
        listViewmessage.setAdapter(null);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //先判断是老师还是学生
                //TextView iscreater = (TextView) view.findViewById(R.id.iscreater);
                TextView code = view.findViewById(R.id.code);
                SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String uid = sp.getString("userid","");
                if((code.getText().toString()).equals("")) {
                    //学生
                    Intent intent1 = new Intent(MainPage.this, Student_class_main.class);
                    intent1.putExtra("cid",code.getText().toString());
                    intent1.putExtra("uid",uid);
                    startActivity(intent1);
                }else{
                    //老师
                    Intent intent2 = new Intent(MainPage.this, Teacher_class_main.class);
                    intent2.putExtra("cid",code.getText().toString());
                    intent2.putExtra("uid",uid);
                    startActivity(intent2);
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.pop_add_class:{

                Intent intent = new Intent(MainPage.this,Create_class_info.class);
                startActivity(intent);
                Toast.makeText(this,"clicked add class",Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
            }
            break;
            case R.id.pop_join_class:{
                //切换界面
                Intent intent = new Intent(MainPage.this,Join_class_code.class);
                startActivity(intent);

                Toast.makeText(this,"clicked join class",Toast.LENGTH_SHORT).show();
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
        try{
            String url = "http://129.204.207.18:8079/resource/img/cover/";
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                if((obj.getJSONObject("course").getString("teacher")).equals(id)) {
                    ClassMain c = new ClassMain(url+obj.getJSONObject("course").getInt("cid")+".png", String.valueOf(obj.getJSONObject("course").getInt("cid")), "", obj.getJSONObject("course").getString("cname"), obj.getString("teacherName"), String.valueOf(obj.getJSONObject("course").getInt("cid")));
                    classlist.add(c);
                }else{
                    ClassMain c = new ClassMain(url+obj.getJSONObject("course").getInt("cid")+".png", "", "", obj.getJSONObject("course").getString("cname"), obj.getString("teacherName"), String.valueOf(obj.getJSONObject("course").getInt("cid")));
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

    private void initMessageList(){
        MessageMain a = new MessageMain(R.drawable.timg,"Java04","19-2-20 17:02","Williamwu","Hi, how are you?");
        messagelist.add(a);
        MessageMain b = new MessageMain(R.drawable.timg,"Java03","19-2-10 10:58","Williamwu","Nice to meet you");
        messagelist.add(b);
        MessageMain c = new MessageMain(R.drawable.timg,"OS04","19-2-09 12:02","Williamwu","你好");
        messagelist.add(c);
        MessageMain d = new MessageMain(R.drawable.timg,"Dotnet04","19-1-31 13:28","Williamwu","约吗");
        messagelist.add(d);
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
//                    System.out.println("-------------------Personal Info----------------------");
//                    System.out.println(obj.getString("email"));
//                    System.out.println(obj.getString("name"));
//                    System.out.println(obj.getString("phone"));
//                    System.out.println(obj.getString("gender"));
                }catch (Exception e){
                    e.printStackTrace();
                }
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

}
