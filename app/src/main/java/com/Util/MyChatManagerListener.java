package com.Util;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.activity.ChatRoom;
import com.cloudclass.SplashActivity;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.offline.OfflineMessageManager;


public class MyChatManagerListener implements ChatManagerListener {
    static Message msg;
    private static Handler handler = new Handler(){
        public void handleMessage(android.os.Message m) {

            msg=new Message();
            msg=(Message) m.obj;
//            String[] message=new String[]{String.valueOf(msg.getFrom()), msg.getBody()};
            if(msg.getFrom().toString()!=null&msg.getBody()!=null&ChatRoom.userFrom!=null){
                if(ChatRoom.userFrom.split("@")[0].equals(msg.getFrom().toString().split("@")[0])){

                    System.out.println("==========收到消息  From：==========="+msg.getFrom());
                    System.out.println("==========收到消息  say：===========" + msg.getBody());
                    ChatRoom.mConversationArrayAdapter.add(msg.getFrom()+"say:"+msg.getBody());

                }
            }
        }
    };

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {

        chat.addMessageListener(new ChatMessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                System.out.println("--------------------------get message-----------------------");
                SQLiteDatabase db = SplashActivity.dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("sender",message.getFrom().toString().split("@")[0]);
                values.put("receiver",message.getTo().toString().split("@")[0]);
                values.put("content",message.getBody());
                values.put("isread","N");
                db.insert("chathistory",null,values);
                values.clear();
                android.os.Message m=handler.obtainMessage();
                m.obj=message;
                m.sendToTarget();
            }
        });
    }
}
