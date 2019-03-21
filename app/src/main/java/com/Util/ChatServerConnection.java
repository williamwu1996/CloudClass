package com.Util;

import android.os.Message;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//import com.my.myapplication.UserFriendList;

public class ChatServerConnection {
    private static XMPPTCPConnectionConfiguration configuration;
    private static XMPPTCPConnection con;

    public static XMPPTCPConnection getConnection() {
        if (con == null || !con.isConnected()) {
            System.out.println("con == null");
            openConnection();
        }
        System.out.println("con != null");
        return con;
    }

    public static boolean openConnection() {
        try {
            configuration = configuration.builder()
                    .setXmppDomain("reinyo.cn")
                    .setCompressionEnabled(false).setSecurityMode(ConnectionConfiguration.SecurityMode.disabled).build();
            con = new XMPPTCPConnection(configuration);
//            con = new XMPPTCPConnection("129.204.207.18");
            con.connect();
            System.out.println("connect");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void closeConnection() {
        if(con!=null){
            con.disconnect();
        }
    }


    public static void registerUser(String username,String password){
            AccountManager am = AccountManager.getInstance(getConnection());
            try {
                Map<String, String> m = new HashMap<>();
                m.put("android", "test android");
//            am.createAccount(Localpart.from(username),password);
                am.sensitiveOperationOverInsecureConnection(true);
                am.createAccount(Localpart.from(username), password, m);
            } catch (SmackException.NoResponseException e) {
                e.printStackTrace();
            } catch (XMPPException.XMPPErrorException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }
    }

    public static void ChangePassword(String password){
        AccountManager am = AccountManager.getInstance(getConnection());
        try {
            am.sensitiveOperationOverInsecureConnection(true);
            am.changePassword(password);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static boolean login(String account, String password) {
        try {
            if (ChatServerConnection.getConnection() == null) {
                return false;
            }
//            SASLAuthentication.supportSASLMechanism("PLAIN");
            ChatServerConnection.getConnection().login(account, password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//    public static void findUsers() {
//        Roster roster = Roster.getInstanceFor(ChatServerConnection.getConnection());
//        Collection<RosterGroup> entriesGroup = roster.getGroups();
//        System.out.println("team:" + entriesGroup.size());
//        Collection<RosterEntry> entries = roster.getEntries();
//        for (RosterEntry item:
//                entries) {
//            Message m=new Message();
//            m.obj=item.getName();
//            UserFriendList.mhandler.sendMessage(m);
//            System.out.println(item.getName());
//            System.out.println(item.getUser());
//        }
//        ChatManager chatManager = ChatManager.getInstanceFor(ChatServerConnection.getConnection());
//        chatManager.addChatListener(new MyChatManagerListener());
//    }
}
