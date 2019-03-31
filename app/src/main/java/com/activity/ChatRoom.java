package com.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.Util.ChatServerConnection;
import com.cloudclass.MainPage;
import com.cloudclass.R;
import com.cloudclass.SplashActivity;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;


public class ChatRoom extends Activity {
    Button send_btn;
    EditText send_msg;
    ListView messageView;
    public static ArrayAdapter<String> mConversationArrayAdapter;
    static Message msg;
    public static String userFrom;
    EntityBareJid jid = null;
    SQLiteDatabase db = SplashActivity.dbHelper.getWritableDatabase();
    Button back,clearhis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userFrom=getIntent().getExtras().getString("chatuser");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        back = findViewById(R.id.chatroom_back);
        clearhis = findViewById(R.id.chatroom_clearhistory);

        mConversationArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String s = sp.getString("USER_NAME","");
        Cursor cursor = db.rawQuery("select * from chathistory where (sender = '"+userFrom.split("@")[0]+"' and receiver = '"+s.replace("@","#")+"') or (sender = '"+s.replace("@","#")+"' and receiver = '"+userFrom.split("@")[0]+"')",null);
        if(cursor.moveToFirst()){
            Handler mhandle = new Handler() {
                public void handleMessage(android.os.Message m) {
//                    text_out = (TextView) findViewById(R.id.text_out);
                    String respond = (String) m.obj;
                    Log.i("---", respond);
                    mConversationArrayAdapter.add(respond);
                }
            };
            do{
                String sender = cursor.getString(cursor.getColumnIndex("sender"));
                String receiver = cursor.getString(cursor.getColumnIndex("receiver"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String isread = cursor.getString(cursor.getColumnIndex("isread"));
                if(sender.equals(s.replace("@","#"))){
                    android.os.Message mm = new android.os.Message();
                    mm.obj = "me:" + content;
                    mhandle.handleMessage(mm);
                }else{
                    android.os.Message mm = new android.os.Message();
                    mm.obj = sender +":"+ content;
                    mhandle.handleMessage(mm);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String s = sp.getString("USER_NAME","");
                db.execSQL("update chathistory set isread = 'Y' where (sender = '"+userFrom.split("@")[0]+"' and receiver = '"+s.replace("@","#")+"') or (sender = '"+s.replace("@","#")+"' and receiver = '"+userFrom.split("@")[0]+"')");
                finish();
            }
        });

        clearhis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChatRoom.this);
                dialog.setTitle("清除历史记录");
                dialog.setMessage("确认清除历史记录？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        String s = sp.getString("USER_NAME","");
                        db.execSQL("delete from chathistory where (sender = '"+userFrom.split("@")[0]+"' and receiver = '"+s.replace("@","#")+"') or (sender = '"+s.replace("@","#")+"' and receiver = '"+userFrom.split("@")[0]+"')");
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

        send_btn = (Button) findViewById(R.id.chatroom_send);
        send_msg = (EditText) findViewById(R.id.chatroom_input);
        messageView = (ListView) findViewById(R.id.msg_recycler_view);

        messageView.setAdapter(mConversationArrayAdapter);
        XMPPConnection con = ChatServerConnection.getConnection();
        final ChatManager cm = ChatManager.getInstanceFor(con);

        try {
            jid = JidCreate.entityBareFrom(userFrom);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        final Chat chat=cm.createChat(jid, new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {
                    }
                });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg_content = send_msg.getText().toString();
                android.os.Message mm = new android.os.Message();
                mm.obj = "me:" + msg_content;
                mhandle.handleMessage(mm);
                System.out.println(msg_content);
                try {
                    Message m = new Message();
                    m.setBody(msg_content);
                    chat.sendMessage(m.getBody());
                    SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    String sender = sp.getString("USER_NAME","");
                    //插入消息
                    ContentValues values = new ContentValues();
                    values.put("sender",sender.replace("@","#"));
                    values.put("receiver",jid.toString().split("@")[0]);
                    values.put("content",m.getBody());
                    values.put("isread","Y");
                    db.insert("chathistory",null,values);
                    values.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Handler mhandle = new Handler() {
                public void handleMessage(android.os.Message m) {
//                    text_out = (TextView) findViewById(R.id.text_out);
                    String respond = (String) m.obj;
                    Log.i("---", respond);
                    mConversationArrayAdapter.add(respond);
                }
            };

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String s = sp.getString("USER_NAME","");
            db.execSQL("update chathistory set isread = 'Y' where (sender = '"+userFrom.split("@")[0]+"' and receiver = '"+s.replace("@","#")+"') or (sender = '"+s.replace("@","#")+"' and receiver = '"+userFrom.split("@")[0]+"')");
            Toast.makeText(ChatRoom.this,"监听系统返回键",
                    Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
