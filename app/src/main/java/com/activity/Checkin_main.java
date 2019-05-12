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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudclass.CheckinMainItem;
import com.cloudclass.CheckinMainItemAdapter;
import com.cloudclass.R;

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

public class Checkin_main extends Activity {

    private List<CheckinMainItem> checklist = new ArrayList<>();
    CheckinMainItemAdapter checkinMainItemAdapter;
    ListView checkinListview;
    Button back;
    Button startcheck;
    String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in_main);
        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        getCheckinRecords();

        back = findViewById(R.id.checkin_main_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startcheck = findViewById(R.id.checkin_main_start_check_btn);
        startcheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(ok) {
                    if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
                        if (ContextCompat.checkSelfPermission(Checkin_main.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PERMISSION_GRANTED) {// 没有权限，申请权限。
                            ActivityCompat.requestPermissions(Checkin_main.this, LOCATIONGPS,
                                    BAIDU_READ_PHONE_STATE);
                        } else {
                            //进入签到码页
                            Intent i = new Intent(Checkin_main.this, Teacher_checkin_code.class);
                            i.putExtra("cid", cid);
                            startActivity(i);
                        }
                    } else {
                        //进入签到码页
                        Intent i = new Intent(Checkin_main.this, Teacher_checkin_code.class);
                        i.putExtra("cid", cid);
                        startActivity(i);
                    }
                }else{
                    Toast.makeText(Checkin_main.this, " 请开启GPS！ ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
                }
            }
        });
        checkinMainItemAdapter = new CheckinMainItemAdapter(Checkin_main.this,R.layout.checkin_main_item, checklist);
        checkinListview = findViewById(R.id.checkin_main_history_listview);
        checkinListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转至签到结果
                TextView tvtime = view.findViewById(R.id.checkin_main_item_time);
                TextView tvchid = view.findViewById(R.id.checkin_main_item_chid);
                Toast.makeText(Checkin_main.this,tvtime.getText().toString()+" "+tvchid.getText().toString(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Checkin_main.this,Checkin_result_history.class);
                intent.putExtra("chid",tvchid.getText().toString());
                startActivity(intent);
            }
        });

    }

    public void getCheckinRecords(){
        String url = "http://129.204.207.18:8079/checkin/teachergetcheckinrecords";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("cid",cid);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {

            }
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("---------------------Checkin Records---------------");
                System.out.println(result);
                initCheckinRecords(result);
            }
        });
    }

    public void initCheckinRecords(String json){
        try{
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                String origin = obj.getString("checktime").replace("T"," ");
                String date = origin.split("\\.")[0];
                CheckinMainItem m = new CheckinMainItem(date,obj.getString("chid"));
                checklist.add(m);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkinListview.setAdapter(checkinMainItemAdapter);
            }
        });
    }

    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    //    private static final int PRIVATE_CODE = 1315;//开启GPS权限
    static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};
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
