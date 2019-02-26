package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cloudclass.R;


public class Join_class_result extends Activity {
    TextView course;
    TextView classname;
    TextView teacher;
    Button join;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_class_result);
        course = findViewById(R.id.join_class_result_course);
        classname = findViewById(R.id.join_class_result_class);
        teacher = findViewById(R.id.join_class_result_teacher);
        join = findViewById(R.id.join_class_finish);
        cancel = findViewById(R.id.join_class_result_cancel);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //加入课程操作
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //创建班课
                finish();
            }
        });
    }
}
