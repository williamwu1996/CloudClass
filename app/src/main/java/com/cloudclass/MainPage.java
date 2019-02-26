package com.cloudclass;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

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
        initClassList();
        initMessageList();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        messageadapter = new MessageAdapter(MainPage.this, R.layout.message_item, messagelist);
        classadapter = new ClassAdapter(MainPage.this,R.layout.class_item, classlist);

        listView = findViewById(R.id.list_view);
        listView.setAdapter(classadapter);

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
                //创建班课
                Intent intent = new Intent(MainPage.this,Change_password.class);
                startActivity(intent);
            }
        });

        changeinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //创建班课
                Intent intent = new Intent(MainPage.this,Change_personal_info.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //先判断是老师还是学生
                if(position<2) {
                    //老师
                    Intent intent2 = new Intent(MainPage.this, Teacher_class_main.class);
                    startActivity(intent2);
                }else {
                    //学生
                    Intent intent1 = new Intent(MainPage.this, Student_class_main.class);
                    startActivity(intent1);
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


    private void initClassList(){
        ClassMain c = new ClassMain(R.drawable.timg,"123456","Java04","C Language","williamwu");
        classlist.add(c);
        ClassMain java = new ClassMain(R.drawable.timg,"345678","OS04","Java","williamwu");
        classlist.add(java);
        ClassMain python = new ClassMain(R.drawable.timg,"","Java03","Python","williamwu");
        classlist.add(python);
        ClassMain go = new ClassMain(R.drawable.timg,"","Dotnet01","GO","williamwu");
        classlist.add(go);
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

}
