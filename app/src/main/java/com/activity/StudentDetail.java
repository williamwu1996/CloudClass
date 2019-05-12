package com.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudclass.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudentDetail extends Activity {

    Button back,chatbtn,moveout;
    TextView personame,tvpersonid;
    String chatuser,chatusername,email,personid,cid;
    ImageView personcover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_detail);

        personcover = findViewById(R.id.student_detail_pic);
        personame = findViewById(R.id.student_detail_name);
        tvpersonid = findViewById(R.id.student_detail_id);
        back = findViewById(R.id.student_detail_back);
        chatbtn = findViewById(R.id.student_detail_chatbtn);
        moveout = findViewById(R.id.student_detail_moveout);
        Intent intent = getIntent();
        chatuser = intent.getStringExtra("chatuser");
        chatusername = intent.getStringExtra("chatusername");
        email = intent.getStringExtra("email");
        cid = intent.getStringExtra("cid");

        personame.setText(chatusername);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initPerson(email);

        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("chatuser",chatuser);
                intent.putExtra("chatusername",chatusername);
                intent.putExtra("email",email);
                intent.setClass(StudentDetail.this, ChatRoom.class);
                startActivity(intent);
            }
        });

        moveout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(StudentDetail.this);
                dialog.setTitle("退出班课");
                dialog.setMessage("确认将"+chatusername+"移出班课？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitclass(personid, cid);
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

    }

    public void initPerson(String email){
        String url = "http://129.204.207.18:8079/users/getuidbyemail";
        System.out.println("-----------email is:"+email);
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("email", email);
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
                initUI(body);
            }
        });
    }

    public void initUI(String json){
//        try {
//            JSONArray jsonArray = new JSONArray(json);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject obj = jsonArray.getJSONObject(i);
//                personid = obj.getString("uid");
//                System.out.println("--------------uid:"+personid);
//                initPersoncover(personid);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        personid = json;
        initPersoncover(personid);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvpersonid.setText(personid);
            }
        });
    }

    public void initPersoncover(String cid){
        String url = "http://129.204.207.18:8079/resource/img/head_pic/"+cid+".jpg";
//        String url = "http://129.204.207.18:8079/resource/img/head_pic/64.jpg";

        System.out.println("----------------------url:"+url);
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
            personcover.setImageBitmap(bitmap);//将图片的流转换成图片
        }
    };

    public void exitclass(String uid, String cid){
        String url = "http://129.204.207.18:8079/course/exitclass";
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
                Looper.prepare();
                Toast.makeText(StudentDetail.this, "移出成功", Toast.LENGTH_SHORT).show();
                finish();
                Looper.loop();
            }
        });
    }

}
