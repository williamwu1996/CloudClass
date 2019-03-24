package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.cloudclass.R;

public class Checkin_main extends Activity {

    private String[] data = {"aaa","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb","bbb"};

    Button back;
    Button startcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in_main);

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
                //进入开始签到页
//                Intent intent = new Intent(Checkin_main.this,Checkin_going.class);
//                startActivity(intent);
                Intent i = new Intent(Checkin_main.this,Teacher_checkin_code.class);
                startActivity(i);
            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(Checkin_main.this,android.R.layout.simple_list_item_1,data);
        ListView history = findViewById(R.id.checkin_main_history_listview);
        history.setAdapter(adapter1);
        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转至签到结果
                Intent intent = new Intent(Checkin_main.this,Checkin_result.class);
                startActivity(intent);
            }
        });

    }


}
