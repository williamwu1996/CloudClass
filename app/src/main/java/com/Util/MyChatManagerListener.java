package com.Util;

import android.os.Handler;

import com.activity.ChatRoom;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;


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
                android.os.Message m=handler.obtainMessage();
                m.obj=message;
                m.sendToTarget();
            }
        });
    }
}
