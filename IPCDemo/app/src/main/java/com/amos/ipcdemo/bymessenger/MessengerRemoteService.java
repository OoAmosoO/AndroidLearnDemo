package com.amos.ipcdemo.bymessenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.amos.ipcdemo.util.LogUtil;

/**
 * author: amos
 * date: 18/5/1 18:48
 */
public class MessengerRemoteService extends Service {
    private boolean isServiceBind = false;
    private Messenger handleClientMsgMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //处理客户端发来的消息
            if (msg != null) {
                switch (msg.what) {
                    case MessengerConstants.TYPE_QUERY_NAME:
                        LogUtil.logMessenger("remote reply client name, isServiceBind:" + isServiceBind);
                        if (isServiceBind) {
                            replyName(msg);
                        }
                        break;
                    case MessengerConstants.TYPE_QUERY_AGE:
                        LogUtil.logMessenger("remote reply client age, isServiceBind:" + isServiceBind);
                        if (isServiceBind) {
                            replyAge(msg);
                        }
                        break;
                }
            }
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.logMessenger("onBind");
        isServiceBind = true;
        return handleClientMsgMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.logMessenger("onUnbind");
        isServiceBind = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        LogUtil.logMessenger("onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.logMessenger("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.logMessenger("onDestroy");
        isServiceBind = false;
        super.onDestroy();
    }

    /**
     * 返回名字
     * @author: amos
     * @date: 18/5/1 19:22
     */
    private void replyName(Message receiveMsg) {
        Message replyMsg = Message.obtain();
        replyMsg.what = MessengerConstants.TYPE_QUERY_NAME;
        Bundle bundle = new Bundle();
        bundle.putString(MessengerConstants.PARAM_NAME, "张三");
        replyMsg.setData(bundle);
        sendMsg2Client(receiveMsg, replyMsg);
    }

    /**
     * 返回年龄
     * @author: amos
     * @date: 18/5/1 19:26
     */
    private void replyAge(Message receiveMsg) {
        Message replyMsg = Message.obtain();
        replyMsg.what = MessengerConstants.TYPE_QUERY_AGE;
        Bundle bundle = new Bundle();
        bundle.putString(MessengerConstants.PARAM_AGE, "22");
        replyMsg.setData(bundle);
        sendMsg2Client(receiveMsg, replyMsg);
    }

    /**
     * 向客户端发送消息
     * @author: amos
     * @date: 18/5/1 19:22
     */
    private void sendMsg2Client(Message receiveMsg, Message replyMsg) {
        try {
            receiveMsg.replyTo.send(replyMsg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
