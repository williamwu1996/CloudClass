package com.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudclass.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Teacher_resource_upload_pic extends Activity implements View.OnClickListener {

    //0代表未选
    //1代表上传并发布
    //2代表上传但不发布
    int mode = 0;

    Button back;
    Button upload;

    private PopupWindow mPopWindow;
    ImageView iv;
    private Uri imgUri; // 拍照时返回的uri
    private Uri mCutUri;// 图片裁剪时返回的uri
    private boolean hasPermission = true;

    RelativeLayout post;
    RelativeLayout notpost;
    ImageView posticon;
    ImageView notposticon;

    EditText tvtitle,tvprofile;
    String cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_resource_upload_pic);
        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        post = findViewById(R.id.teacher_resource_upload_pic_upload_and_post);
        notpost = findViewById(R.id.teacher_resource_upload_pic_upload_not_post);
        posticon = findViewById(R.id.teacher_resource_upload_pic_upload_and_post_icon);
        notposticon = findViewById(R.id.teacher_resource_upload_pic_upload_not_post_icon);
        tvtitle = findViewById(R.id.teacher_resource_upload_pic_title);
        tvprofile = findViewById(R.id.teacher_resource_upload_pic_profile);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 1;
                posticon.setVisibility(View.VISIBLE);
                notposticon.setVisibility(View.INVISIBLE);
            }
        });

        notpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 2;
                posticon.setVisibility(View.INVISIBLE);
                notposticon.setVisibility(View.VISIBLE);
            }
        });

        iv = findViewById(R.id.teacher_resource_upload_pic_img);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showPopupWindow();
            }
        });

        back = findViewById(R.id.resource_upload_pic_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upload = findViewById(R.id.teacher_resource_upload_pic_upload_btn);
        upload.setOnClickListener(this);
    }

    public void uploadFile(){
//        try {
            OkHttpClient client=new OkHttpClient();
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile);//将file转换成RequestBody文件
            RequestBody requestBody=new MultipartBody.Builder()
                    .addFormDataPart("files",tvtitle.getText().toString()+".JPG", fileBody)
                    .addFormDataPart("status","going")
                    .addFormDataPart("cid",cid)
                    .build();
            Request request=new Request.Builder()
                    .url("http://129.204.207.18:8079/resource/uploadfile")
                    .post(requestBody)
                    .build();
//            Response response=client.newCall(request).execute();
//            String responseBody=response.body().string();
//            System.out.println("---------------upload File response----------------------"+responseBody);
//            finish();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("-------------------------Failed----------------------------");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                System.out.println("---------------upload File response----------------------"+body);
                finish();
            }
        });
    }

    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(Teacher_resource_upload_pic.this).inflate(R.layout.popup_getpicture, null);
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
        View rootview = LayoutInflater.from(Teacher_resource_upload_pic.this).inflate(R.layout.teacher_resource_upload_pic, null);
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
            case R.id.teacher_resource_upload_pic_upload_btn:
                uploadFile();
                if(mode==1){
                    //上传并发布
                    Toast.makeText(this, "上传并发布", Toast.LENGTH_SHORT).show();
                }else if(mode==2){
                    //上传但不发布
                    Toast.makeText(this, "上传但不发布", Toast.LENGTH_SHORT).show();
                }else{
                    //未选择
                    Toast.makeText(this, "请选择发布状态", Toast.LENGTH_SHORT).show();
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
    File imgFile;
    private void takePhone() {
        // 要保存的文件名
        String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
//        String fileName = "photo_" + time;
        String fileName = tvtitle.getText().toString();
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
//            String fileName = "photo_" + time;
            String fileName = tvtitle.getText().toString();
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
}
