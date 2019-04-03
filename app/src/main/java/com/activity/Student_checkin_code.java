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
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.Util.ChatServerConnection;
import com.Util.LocationService;
import com.Util.LocationUtil;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cloudclass.R;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Student_checkin_code extends Activity {
    EditText code1,code2,code3,code4;
    Button back,reset;
    String code,cid;
    private LocationService locationService;
    TextView txlongitude,txlatitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_checkin_code);
        txlatitude = findViewById(R.id.student_latitude);
        txlongitude = findViewById(R.id.student_longitude);
        openGPSSettings();
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
            locationService.unregisterListener(mListener);
            locationService.stop();
            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String uid = sp.getString("userid","");
            String name = sp.getString("personname","");
            String s = "0000"+cid;
            s = s.substring(s.length()-4,s.length());
            s += code;
            String jid = s + "@conference.129.204.207.18";
//            String jid = "12345678"+"@conference.129.204.207.18";
            String message = uid+"#"+name+"#"+txlongitude.getText().toString()+"#"+txlatitude.getText().toString()+"#";
            System.out.println("--------------------Student checkin code------------------------");
            System.out.println("Jid = "+jid);
            System.out.println("Message = "+message);
            System.out.println("S = "+s);
            //todo 学生签到尚未测试
            XMPPConnection connection = ChatServerConnection.getConnection();
            try {
                MultiUserChat muc = MultiUserChatManager.getInstanceFor(connection).getMultiUserChat(JidCreate.entityBareFrom(jid));
                muc.join(Resourcepart.from(jid));
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

    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};


    private void openGPSSettings() {
//
////        LocationManager alm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////        if (alm
////                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
////            Toast.makeText(this, " GPS模块正常 ", Toast.LENGTH_SHORT).show();
////            return;
////        }
////
////        Toast.makeText(this, " 请开启GPS！ ", Toast.LENGTH_SHORT).show();
////        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
////        startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
//
        LocationManager lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {//开了定位服务
            if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PERMISSION_GRANTED) {// 没有权限，申请权限。
                    ActivityCompat.requestPermissions(this, LOCATIONGPS,
                            BAIDU_READ_PHONE_STATE);
                } else {
//                    getLocation();//getLocation为定位方法
                    locationService = new LocationService(this);
                    //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
                    locationService.registerListener(mListener);
                    locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                    locationService.start();
                }
            } else {
//                getLocation();//getLocation为定位方法
                locationService = new LocationService(this);
                //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
                locationService.registerListener(mListener);
                locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                locationService.start();
            }
        }else{
            Toast.makeText(this, " 请开启GPS！ ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
        }
//
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
//                    getLocation();
                    locationService = new LocationService(this);
                    //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
                    locationService.registerListener(mListener);
                    locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                    locationService.start();
                } else {
                    openGPSSettings();
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
                openGPSSettings();
                break;

        }
    }

    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                Log.d("LocationService",sb.toString());
                showLocation(location.getLatitude()+"",location.getLongitude()+"");
            }
        }

    };

    public void showLocation(final String latitude, final String longitude){
        final String flatitude = latitude;
        final String flongitude = longitude;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txlatitude.setText(latitude);
                txlongitude.setText(longitude);
            }
        });
    }

}
