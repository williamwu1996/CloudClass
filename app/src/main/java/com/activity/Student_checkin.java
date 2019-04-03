package com.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.Util.LocationService;
import com.cloudclass.R;
import com.cloudclass.StudentCheckinHistoryItem;
import com.cloudclass.StudentCheckinHistoryItemAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Student_checkin extends Activity {

    private List<StudentCheckinHistoryItem> checkinlist = new ArrayList<>();
    Button back,checkinbtn;
    private ListView checkinListView;
    StudentCheckinHistoryItemAdapter checkinAdapter;
    String cid,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_checkin_detail);
        checkinbtn = findViewById(R.id.student_class_main_members_checkin_btn);
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        uid = sp.getString("userid","");//732315805@qq.com
        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
//        initHistoryList();
        checkinAdapter = new StudentCheckinHistoryItemAdapter(Student_checkin.this,R.layout.student_checkin_item, checkinlist);
        checkinListView = findViewById(R.id.student_checkin_history);
        checkinListView.setAdapter(checkinAdapter);

        checkinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查是否有签到
                requestCode();
            }
        });

        getCheckinRecords();


        back = findViewById(R.id.student_checkin_detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    public void getCheckinRecords(){
        String url = "http://192.168.3.169:8079/checkin/getuserscheckinfo";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("cid", cid);
        formBody.add("uid", uid);
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
                System.out.println("-----------------------Student records--------------------");
                System.out.println(body);
                initHistoryList(body);
            }
        });
    }

    public void initHistoryList(String json){
        try{
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                String time = obj.getString("chtime").replace("T"," ");
                String status = obj.getString("status");
                String chid = obj.getString("chid");
                String date = time.split("\\.")[0];
                if(status.equals("-1")) {
                    StudentCheckinHistoryItem m = new StudentCheckinHistoryItem(date, "未签到", chid);
                    checkinlist.add(m);
                }else{
                    StudentCheckinHistoryItem m = new StudentCheckinHistoryItem(date, "已签到", chid);
                    checkinlist.add(m);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkinListView.setAdapter(checkinAdapter);
            }
        });
    }

    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    //    private static final int PRIVATE_CODE = 1315;//开启GPS权限
    static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};
    public void requestCode(){
        //发送cid,返回班课码
        String url = "http://192.168.3.169:8079/checkin/getcheckincode";
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
                if(body.equals("0000")){
                    Looper.prepare();
                    Toast.makeText(Student_checkin.this, "尚未开始签到或签到已结束！", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{
                    LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                    boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if(ok) {
                        if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
                            if (ContextCompat.checkSelfPermission(Student_checkin.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PERMISSION_GRANTED) {// 没有权限，申请权限。
                                ActivityCompat.requestPermissions(Student_checkin.this, LOCATIONGPS,
                                        BAIDU_READ_PHONE_STATE);
                            } else {
                                Intent intent = new Intent(Student_checkin.this, Student_checkin_code.class);
                                intent.putExtra("cid", cid);
                                intent.putExtra("code", body);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(Student_checkin.this, Student_checkin_code.class);
                            intent.putExtra("cid", cid);
                            intent.putExtra("code", body);
                            startActivity(intent);
                        }

                    }else{
                        Toast.makeText(Student_checkin.this, " 请开启GPS！ ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                        startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                //如果用户取消，permissions可能为null.
                if (grantResults[0] == PERMISSION_GRANTED && grantResults.length > 0) { //有权限
                    // 获取到权限，作相应处理
                } else {
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                break;

        }
    }
}
