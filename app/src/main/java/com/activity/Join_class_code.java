package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cloudclass.R;

public class Join_class_code extends Activity {

    EditText code;
    Button next;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_class_code);
        code = findViewById(R.id.join_class_code);
        next = findViewById(R.id.join_class_next);
        cancel = findViewById(R.id.join_class_code_cancel);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Join_class_code.this, Join_class_result.class);
                intent.putExtra("code", code.getText().toString());
                startActivity(intent);
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
