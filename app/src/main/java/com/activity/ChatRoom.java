package com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.Util.ChatServerConnection;
import com.cloudclass.R;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userFrom=getIntent().getExtras().getString("chatuser");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        send_btn = (Button) findViewById(R.id.chatroom_send);
        send_msg = (EditText) findViewById(R.id.chatroom_input);
        messageView = (ListView) findViewById(R.id.msg_recycler_view);
        mConversationArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        messageView.setAdapter(mConversationArrayAdapter);
        XMPPConnection con = ChatServerConnection.getConnection();
        final ChatManager cm = ChatManager.getInstanceFor(con);
        EntityBareJid jid = null;
        try {
            jid = JidCreate.entityBareFrom(userFrom);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        final Chat chat=cm.createChat(jid, new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {
//                            message.setBody(msg_content);
//                        System.out.println(message.getFrom() + "say:" + message.getBody());
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


}