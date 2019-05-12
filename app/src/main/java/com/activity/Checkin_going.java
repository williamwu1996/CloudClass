package com.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Util.ChatServerConnection;
import com.Util.DistanceUtil;
import com.Util.TimeUtils;
import com.cloudclass.R;
import com.cloudclass.SplashActivity;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Checkin_going extends Activity {

    LinearLayout discard;
    LinearLayout end;
    TextView checkincode;
    String teacher_longitude,teacher_latitude,cid,code;
    String checkednum;//已签到人数
    String chid;//请求到的chid
    String roomnum;
    SQLiteDatabase db = SplashActivity.dbHelper.getWritableDatabase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_going);
        checkincode = findViewById(R.id.checkin_going_code);
        discard = findViewById(R.id.chechin_going_discard_btn);
        discard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        end = findViewById(R.id.chechin_going_end_btn);
        end.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                endCheckin();
                XMPPConnection connection = ChatServerConnection.getConnection();
                try {
                    MultiUserChat muc = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(JidCreate.entityBareFrom(jid));
                    muc.destroy("Checkin end",JidCreate.entityBareFrom(jid));
                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(Checkin_going.this,Checkin_result.class);
                intent.putExtra("chid",chid);
                startActivity(intent);
                finish();
            }
        });
        Intent intent = getIntent();
        teacher_longitude = intent.getStringExtra("longitude");
        teacher_latitude = intent.getStringExtra("latitude");
        Toast.makeText(this,"Longitude is "+teacher_longitude,Toast.LENGTH_SHORT).show();
        Toast.makeText(this,"Latitude is "+teacher_latitude,Toast.LENGTH_SHORT).show();
        System.out.println("-------------------------Checkin going-------------------------");
        System.out.println(teacher_latitude);
        System.out.println(teacher_longitude);
        cid = intent.getStringExtra("cid");
        code = intent.getStringExtra("code");
        checkincode.setText("签到码："+code);
        //聊天房间号
        roomnum = "0000"+cid;
        roomnum = roomnum.substring(roomnum.length()-4,roomnum.length());
        roomnum += code;
        requestChid(code);

    }

    public void endCheckin(){
        String url = "http://129.204.207.18:8079/checkin/closecheckin";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("chid", chid);
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

            }
        });
    }

    public void getAllStudent(){
        String url = "http://129.204.207.18:8079/course/getstudents";
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
                System.out.println("--------------------------------Member List-------------------------------"+body);
                insertData(body);
            }
        });
    }

    public void insertData(String json){
        //chid, uid
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String uid = obj.getString("uid");
                String uname = obj.getString("name");
                ContentValues values = new ContentValues();
                values.put("chid",chid);
                values.put("uid",uid);
                values.put("distance",-1);
                values.put("uname",uname);
                db.insert("checkin",null,values);
                values.clear();
            }
            testCheckdata();
            createChatroom();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void testCheckdata(){
        Cursor cursor = db.rawQuery("select * from checkin where chid = "+chid,null);
        if(cursor.moveToFirst()){
            do{
                String chid = cursor.getString(cursor.getColumnIndex("chid"));
                String uid = cursor.getString(cursor.getColumnIndex("uid"));
                String uname = cursor.getString(cursor.getColumnIndex("uname"));
                String distance = cursor.getString(cursor.getColumnIndex("distance"));
                System.out.println("---------------data------------------");
                System.out.println("uid = "+uid);
                System.out.println("uname = "+uname);
                System.out.println("chid = "+chid);
                System.out.println("distance = "+distance);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void requestChid(String checkcode){
        //cid, code, checktime
        String timeStamp = TimeUtils.timeStamp();
        String date = TimeUtils.timeStamp2Date(timeStamp, "yyyy-MM-dd HH:mm:ss");
        String timeStamp2 = TimeUtils.date2TimeStamp(date, "yyyy-MM-dd HH:mm:ss");
        String url = "http://129.204.207.18:8079/checkin/startcheckin";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("checktime", date);
        formBody.add("cid", cid);
        formBody.add("checkcode", checkcode);
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
                chid = body;
                System.out.println("--------------------------------Chid-------------------------------"+chid);
                getAllStudent();
            }
        });
    }
    String jid;
    public void createChatroom(){
        jid = roomnum+"@conference.129.204.207.18";
//        jid = "12345678"+"@conference.129.204.207.18";
        XMPPConnection connection = ChatServerConnection.getConnection();
        try{
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(JidCreate.entityBareFrom(jid));

            muc.createOrJoin(Resourcepart.from("teacher"));
            Form form = muc.getConfigurationForm();
            Form submitForm = form.createAnswerForm();
            submitForm.setAnswer("muc#roomconfig_roomname",roomnum+"00000");
            muc.sendConfigurationForm(submitForm);


            //todo 接收签到消息尚未测试
            muc.addMessageListener(new MessageListener() {
                @Override
                public void processMessage(Message message) {
                    //更新数据库
                    //uid, name, longitude, latitude, #隔开
                    System.out.println("--------------------receive message---------------------");
                    System.out.println("--------------------receive message---------------------"+message.getBody());
                    String[] content = message.getBody().split("#");
                    String uid = content[0];
                    String name = content[1];
                    String longitude = content[2];
                    String latitude = content[3];
                    System.out.println("My lat "+teacher_latitude);
                    System.out.println("My log "+teacher_longitude);
                    System.out.println("Your lat "+latitude);
                    System.out.println("Your log "+longitude);
                    System.out.println(DistanceUtil.GetDistance(Double.parseDouble(teacher_latitude),Double.parseDouble(teacher_longitude),Double.parseDouble(latitude),Double.parseDouble(longitude)));
                    int distance = (int) DistanceUtil.GetDistance(Double.parseDouble(teacher_latitude),Double.parseDouble(teacher_longitude),Double.parseDouble(latitude),Double.parseDouble(longitude));
                    System.out.println("Int distance "+distance);
                    db.execSQL("update checkin set distance = "+distance+" where chid = "+chid+" and uid = "+uid);
                    testCheckdata();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
