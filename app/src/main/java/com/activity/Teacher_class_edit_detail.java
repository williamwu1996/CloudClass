package com.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudclass.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Teacher_class_edit_detail extends Activity implements View.OnClickListener {

    Button back;
    Button save;

    private PopupWindow mPopWindow;
    ImageView iv;
    private Uri imgUri; // 拍照时返回的uri
    private Uri mCutUri;// 图片裁剪时返回的uri
    private boolean hasPermission = true;

    EditText etclassname, etcoursename, etprofile;
    TextView tvclasscode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_class_edit_detail);

        back = findViewById(R.id.teacher_class_detail_edit_back);
        save = findViewById(R.id.teacher_class_detail_save_button);
        iv = findViewById(R.id.teacher_class_main_detail_edit_img);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //先保存后结束activity
                String classname = etclassname.getText().toString();
                String coursename = etcoursename.getText().toString();
                String profile = etprofile.getText().toString();
                String classcode = tvclasscode.getText().toString();

                updateClass(classcode,classname,coursename,profile);
//                finish();
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showPopupWindow();
            }
        });

        Intent intent = getIntent();
        String cid = intent.getStringExtra("cid");
        String profile = intent.getStringExtra("profile");
        String classname = intent.getStringExtra("classname");
        String coursename = intent.getStringExtra("coursename");

        initClasscover(cid);
        etclassname = findViewById(R.id.teacher_class_main_detail_edit_classname);
        etcoursename = findViewById(R.id.teacher_class_main_detail_edit_coursename);
        etprofile = findViewById(R.id.teacher_class_main_detail_edit_profile);
        tvclasscode = findViewById(R.id.teacher_class_main_detail_edit_classcode);

        etclassname.setText(classname);
        etcoursename.setText(coursename);
        etprofile.setText(profile);
        tvclasscode.setText(cid);

    }

    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(Teacher_class_edit_detail.this).inflate(R.layout.popup_getpicture, null);
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
        TextView tv1 = (TextView)contentView.findViewById(R.id.btn_takephoto);
        TextView tv2 = (TextView)contentView.findViewById(R.id.btn_open_photo_album);
        TextView tv3 = (TextView)contentView.findViewById(R.id.create_class_cancel);


        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);


        //显示PopupWindow
        View rootview = LayoutInflater.from(Teacher_class_edit_detail.this).inflate(R.layout.teacher_class_edit_detail, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_takephoto:
                if (hasPermission) {
                    Toast.makeText(this, "take photo！", Toast.LENGTH_SHORT).show();
                    takePhone();
                    mPopWindow.dismiss();
                }else{
                    checkPermissions();
                    Toast.makeText(this, "没权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_open_photo_album:
                if (hasPermission) {
                    Toast.makeText(this, "take photo！", Toast.LENGTH_SHORT).show();
                    openGallery();
                    mPopWindow.dismiss();
                }else{
                    checkPermissions();
                    Toast.makeText(this, "没权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.create_class_cancel:{
                //切换界面
                mPopWindow.dismiss();
            }
            break;
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Create_class_info.SCAN_OPEN_PHONE);
    }


    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否有存储和拍照权限
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    ) {
                hasPermission = true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Create_class_info.REQUEST_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Create_class_info.REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
            } else {
                Toast.makeText(this, "权限授予失败！", Toast.LENGTH_SHORT).show();
                hasPermission = false;
            }
        }
    }
    File imgFile = null;
    private void takePhone() {
        // 要保存的文件名
        String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String fileName = "photo_" + time;
        // 创建一个文件夹
        String path = Environment.getExternalStorageDirectory() + "/take_photo";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 要保存的图片文件
        imgFile = new File(file, fileName + ".jpeg");
        // 将file转换成uri
        // 注意7.0及以上与之前获取的uri不一样了，返回的是provider路径
        imgUri = getUriForFile(this, imgFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 添加Uri读取权限
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        // 添加图片保存位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, Create_class_info.REQUEST_TAKE_PHOTO);
    }

    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.rain.takephotodemo.FileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 拍照并进行裁剪
                case Create_class_info.REQUEST_TAKE_PHOTO:
                    Log.e(Create_class_info.TAG, "onActivityResult: imgUri:REQUEST_TAKE_PHOTO:" + imgUri.toString());
                    cropPhoto(imgUri, true);
                    break;

                // 裁剪后设置图片
                case Create_class_info.REQUEST_CROP:
                    iv.setImageURI(mCutUri);
                    Log.e(Create_class_info.TAG, "onActivityResult: imgUri:REQUEST_CROP:" + mCutUri.toString());
                    break;
                // 打开图库获取图片并进行裁剪
                case Create_class_info.SCAN_OPEN_PHONE:
                    Log.e(Create_class_info.TAG, "onActivityResult: SCAN_OPEN_PHONE:" + data.getData().toString());
                    cropPhoto(data.getData(), false);
                    break;
            }
        }
    }

    private void cropPhoto(Uri uri, boolean fromCapture) {
        Intent intent = new Intent("com.android.camera.action.CROP"); //打开系统自带的裁剪图片的intent
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("scale", true);

        // 设置裁剪区域的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // 设置裁剪区域的宽度和高度
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        // 图片输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // 若为false则表示不返回数据
        intent.putExtra("return-data", false);

        // 指定裁剪完成以后的图片所保存的位置,pic info显示有延时
        if (fromCapture) {
            // 如果是使用拍照，那么原先的uri和最终目标的uri一致
            mCutUri = uri;
        } else { // 从相册中选择，那么裁剪的图片保存在take_photo中
            String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
            String fileName = "photo_" + time;
            imgFile = new File(Environment.getExternalStorageDirectory() + "/take_photo", fileName + ".jpeg");
            if (!imgFile.getParentFile().exists()) {
                imgFile.getParentFile().mkdirs();
            }
            mCutUri = getUriForFile(this, imgFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCutUri);
        Toast.makeText(this, "剪裁图片", Toast.LENGTH_SHORT).show();
        // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
        Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc.setData(uri);
        this.sendBroadcast(intentBc);

        startActivityForResult(intent, Create_class_info.REQUEST_CROP); //设置裁剪参数显示图片至ImageVie
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
            iv.setImageBitmap(bitmap);//将图片的流转换成图片
        }
    };

    public void updateClass(String cid, String classname, String coursename, String profile){
        String url = "http://192.168.3.169:8079/course/updatecourse";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add("cid", cid);
        formBody.add("coursename", coursename);
        formBody.add("classname", classname);
        formBody.add("profile", profile);
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
                if (imgFile != null) {
                    String cid = tvclasscode.getText().toString();
                    updateCover(cid);
                }else {
                    Looper.prepare();
                    Toast.makeText(Teacher_class_edit_detail.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("classname",etclassname.getText().toString());
                    intent.putExtra("coursename",etcoursename.getText().toString());
                    intent.putExtra("profile",etprofile.getText().toString());
                    setResult(2, intent);
                    finish();
                    Looper.loop();
                }
            }
        });
    }

    public void updateCover(String cid){
        try {
            OkHttpClient client=new OkHttpClient();
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile);//将file转换成RequestBody文件
            RequestBody requestBody=new MultipartBody.Builder()
                    .addFormDataPart("file","", fileBody)
                    .addFormDataPart("path","\\cover\\" + cid + ".png")
                    .build();
            Request request=new Request.Builder()
                    .url("http://192.168.3.169:8079/resource/uploadpic")
                    .post(requestBody)
                    .build();
            Response response=client.newCall(request).execute();
            String responseBody=response.body().string();
            Looper.prepare();
            Toast.makeText(Teacher_class_edit_detail.this, "修改成功", Toast.LENGTH_SHORT).show();
            finish();
            Looper.loop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
