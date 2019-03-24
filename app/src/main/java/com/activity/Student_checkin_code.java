package com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudclass.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Student_checkin_code extends Activity {
    EditText code1,code2,code3,code4;
    Button back,reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_checkin_code);
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
                }else{
                    Toast.makeText(Student_checkin_code.this,"请输入数字",Toast.LENGTH_SHORT).show();
                    code4.setText("");
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
}
