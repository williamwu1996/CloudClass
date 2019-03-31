package com.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Util.ChatServerConnection;
import com.cloudclass.R;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Student_checkin_code extends Activity {
    EditText code1,code2,code3,code4;
    Button back,reset;
    String code,cid;
    String longitude,latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_checkin_code);
        openGPSSettings();
        getLocation();
        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        cid = intent.getStringExtra("cid");
        code1 = findViewById(R.id.student_checkin_code1);
        code2 = findViewById(R.id.student_checkin_code2);
        code3 = findViewById(R.id.student_checkin_code3);
        code4 = findViewById(R.id.student_checkin_code4);
        back = findViewById(R.id.student_checkin_code_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reset = findViewById(R.id.student_checkin_code_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code1.getText().clear();
                code2.getText().clear();
                code3.getText().clear();
                code4.getText().clear();
                code1.requestFocus();
            }
        });

        code1.setFocusableInTouchMode(true);
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String num = code1.getText().toString();
                Pattern p = Pattern.compile("[0-9]");
                Matcher m = p.matcher(num);
                if (m.matches()) {
                    code2.requestFocus();
                }else if(num.equals("")) {

                }else{
                    Toast.makeText(Student_checkin_code.this,"请输入数字",Toast.LENGTH_SHORT).show();
                    code1.setText("");
                }
            }
        });

        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String num = code2.getText().toString();
                Pattern p = Pattern.compile("[0-9]");
                Matcher m = p.matcher(num);
                if (m.matches()) {
                    code3.requestFocus();
                } else if(num.equals("")) {

                }else{
                    Toast.makeText(Student_checkin_code.this,"请输入数字",Toast.LENGTH_SHORT).show();
                    code2.setText("");
                }
            }
        });

        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String num = code3.getText().toString();
                Pattern p = Pattern.compile("[0-9]");
                Matcher m = p.matcher(num);
                if (m.matches()) {
                    code4.requestFocus();
                } else if(num.equals("")) {

                }else{
                    Toast.makeText(Student_checkin_code.this,"请输入数字",Toast.LENGTH_SHORT).show();
                    code3.setText("");
                }
            }
        });

        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String num = code4.getText().toString();
                Pattern p = Pattern.compile("[0-9]");
                Matcher m = p.matcher(num);
                if (m.matches()) {
                    String txcode = code1.getText().toString()+code2.getText().toString()+code3.getText().toString()+code4.getText().toString();
                    //显示签到结果
                    checkCode(txcode);
                }else{
                    Toast.makeText(Student_checkin_code.this,"请输入数字",Toast.LENGTH_SHORT).show();
//                    code4.setText("");
                }
            }
        });

        code2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN){
                    code1.getText().clear();
                    code1.requestFocus();
                }
                return false;
            }
        });

        code3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN){
                    code2.getText().clear();
                    code2.requestFocus();
                }
                return false;
            }
        });

        code4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN){
                    code3.getText().clear();
                    code3.requestFocus();
                }
                return false;
            }
        });
    }

    public void checkCode(String input){
        if(code.equals(input)){
            //正确, 发送消息
            //uid, name, longitude, latitude, #隔开
            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String uid = sp.getString("userid","");
            String name = sp.getString("personname","");
            String s = "0000"+cid;
            s = s.substring(s.length()-4,s.length());
            s += code;
            String jid = s + "@conference.129.204.207.18";
            String message = uid+"#"+name+"#"+longitude+"#"+latitude+"#";
            System.out.println("--------------------Student checkin code------------------------");
            System.out.println("Jid = "+jid);
            System.out.println("Message = "+message);
            System.out.println("S = "+s);
            //todo 学生签到尚未测试
            XMPPConnection connection = ChatServerConnection.getConnection();
            try {
                MultiUserChat muc = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(JidCreate.entityBareFrom(jid));
                muc.join(Resourcepart.from(uid));
                muc.sendMessage(message);
            }catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(Student_checkin_code.this,"签到成功！", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            //错误
            Toast.makeText(Student_checkin_code.this,"签到码不正确，请重新输入", Toast.LENGTH_SHORT).show();
            code1.getText().clear();
            code2.getText().clear();
            code3.getText().clear();
            code4.getText().clear();
            code1.requestFocus();
            }
    }

    private void openGPSSettings() {

        LocationManager alm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (alm
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, " GPS模块正常 ", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, " 请开启GPS！ ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面

    }

    private void getLocation() {
// 获取位置管理服务
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) this.getSystemService(serviceName);
// 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"没有权限",Toast.LENGTH_SHORT).show();
            return;
        }
        Location location=locationManager.getLastKnownLocation(provider);
        longitude = location.getLongitude()+"";
        latitude = location.getLatitude()+"";
    }
}
