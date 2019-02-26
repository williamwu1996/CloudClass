package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudclass.CheckinHistoryAdapter;
import com.cloudclass.CheckinHistoryItem;
import com.cloudclass.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Checkin_result extends Activity {

    private PopupWindow mPopWindow;

    private List<CheckinHistoryItem> checkinlist = new ArrayList<>();
    Button back;
    private ListView checkinListView;
    CheckinHistoryAdapter checkinAdapter;
    String status="";

    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_result);
        initHistoryList();

        finish = findViewById(R.id.checkin_result_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存结果并退出
                finish();
            }
        });

        checkinAdapter = new CheckinHistoryAdapter(Checkin_result.this,R.layout.checkin_history_item, checkinlist);
        checkinListView = findViewById(R.id.checkin_result_listview);
        checkinListView.setAdapter(checkinAdapter);


        checkinListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showPopupWindow(position);

            }
        });
    }

    private void showPopupWindow(final int position) {
        //设置contentView

        View contentView = LayoutInflater.from(Checkin_result.this).inflate(R.layout.popup_checkin_status, null);
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
        TextView tv1 = (TextView)contentView.findViewById(R.id.pop_ask_for_leave);
        TextView tv2 = (TextView)contentView.findViewById(R.id.pop_absent);
        TextView tv3 = (TextView)contentView.findViewById(R.id.pop_late);
        TextView tv4 = (TextView)contentView.findViewById(R.id.pop_checkin);
        TextView tv5 = (TextView)contentView.findViewById(R.id.pop_checkin_cancel);


        tv1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                status = "请假";
                checkinlist.get(position).setStatus(status);
                checkinAdapter.notifyDataSetChanged();
                mPopWindow.dismiss();
            }
        });
//        tv2.setOnClickListener(this);
//        tv3.setOnClickListener(this);
//        tv4.setOnClickListener(this);
//        tv5.setOnClickListener(this);
        tv2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                status = "旷课";
                checkinlist.get(position).setStatus(status);
                checkinAdapter.notifyDataSetChanged();
                mPopWindow.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                status = "迟到";
                checkinlist.get(position).setStatus(status);
                checkinAdapter.notifyDataSetChanged();
                mPopWindow.dismiss();
            }
        });
        tv4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                status = "已签到";
                checkinlist.get(position).setStatus(status);
                checkinAdapter.notifyDataSetChanged();
                mPopWindow.dismiss();
            }
        });
        tv5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });


        //显示PopupWindow
        View rootview = LayoutInflater.from(Checkin_result.this).inflate(R.layout.checkin_result, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);

    }

//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id){
//            case R.id.pop_ask_for_leave:{
//                status = "请假";
//                mPopWindow.dismiss();
//            }
//            break;
//            case R.id.pop_absent:{
//                //切换界面
//                status = "旷课";
//                mPopWindow.dismiss();
//            }
//            break;
//            case R.id.pop_late:{
//                //切换界面
//                status = "迟到";
//                mPopWindow.dismiss();
//            }
//            break;
//            case R.id.pop_checkin:{
//                //切换界面
//                status = "已签到";
//                mPopWindow.dismiss();
//            }
//            break;
//            case R.id.pop_checkin_cancel:{
//                //切换界面
//                mPopWindow.dismiss();
//            }
//            break;
//        }
//    }

    public void initHistoryList(){
        for(int i = 0;i<4;i++) {
            CheckinHistoryItem sc1 = new CheckinHistoryItem("2017-09-09 19:22", "已签到");
            checkinlist.add(sc1);
        }
    }
}
