package com.amos.ipcdemo.bymessenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amos.ipcdemo.R;
import com.amos.ipcdemo.util.LogUtil;

/**
 * Messenger方式跨进程通信
 * author: amos
 * date: 18/5/1 18:06
 */
public class MessengerClientActivity extends Activity implements View.OnClickListener {
    private Button btnBind;
    private Button btnUnbind;

    private TextView tvName;
    private Button btnAskName;

    private TextView tvAge;
    private Button btnAskAge;

    private Messenger mClientMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                //处理服务端返回的消息
                switch (msg.what) {
                    case MessengerConstants.TYPE_QUERY_NAME:
                        LogUtil.logMessenger("receive remote reply name");
                        if (msg.getData() != null) {
                            tvName.setText("name:" + msg.getData().getString(MessengerConstants.PARAM_NAME));
                        }
                        break;
                    case MessengerConstants.TYPE_QUERY_AGE:
                        LogUtil.logMessenger("receive remote reply age");
                        if (msg.getData() != null) {
                            tvAge.setText("age:" + msg.getData().getString(MessengerConstants.PARAM_AGE));
                        }
                        break;
                }
            }
        }
    });

    private boolean isServiceConnected = false;
    private Messenger mServerMessenger;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServerMessenger = new Messenger(service);
            isServiceConnected = true;
            LogUtil.logMessenger("connect");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServerMessenger = null;
            isServiceConnected = false;
            LogUtil.logMessenger("disconnect");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    private void initView() {
        btnBind = (Button) findViewById(R.id.btn_bind);
        btnUnbind = (Button) findViewById(R.id.btn_unbind);

        tvName = (TextView) findViewById(R.id.tv_name);
        btnAskName = (Button) findViewById(R.id.btn_ask_name);

        tvAge = (TextView) findViewById(R.id.tv_age);
        btnAskAge = (Button) findViewById(R.id.btn_ask_age);

        btnBind.setOnClickListener(this);
        btnUnbind.setOnClickListener(this);
        btnAskName.setOnClickListener(this);
        btnAskAge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind:
                bindService();
                break;
            case R.id.btn_unbind:
                unbindService();
                break;
            case R.id.btn_ask_name:
                askName();
                break;
            case R.id.btn_ask_age:
                askAge();
                break;
        }
    }

    private void bindService() {
        LogUtil.logMessenger("bindService, isServiceConnected:" + isServiceConnected);
        if (!isServiceConnected) {
            Intent intent = new Intent(this, MessengerRemoteService.class);
            bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        }
    }

    private void unbindService() {
        LogUtil.logMessenger("unbindService, isServiceConnected:" + isServiceConnected);
        if (isServiceConnected) {
            isServiceConnected = false;
            unbindService(mServiceConnection);
        }
    }

    private void stopService() {
        LogUtil.logMessenger("stopService");
        Intent intent = new Intent(this, MessengerRemoteService.class);
        stopService(intent);
    }

    private void askName() {
        Message msg = Message.obtain();
        msg.what = MessengerConstants.TYPE_QUERY_NAME;
        msg.replyTo = mClientMessenger;
        sendMsg2Remote(msg);
    }

    private void askAge() {
        Message msg = Message.obtain();
        msg.what = MessengerConstants.TYPE_QUERY_AGE;
        msg.replyTo = mClientMessenger;
        sendMsg2Remote(msg);
    }

    /**
     * 向服务端发送消息
     * @author: amos
     * @date: 18/5/1 19:15
     */
    private void sendMsg2Remote(Message msg) {
        if (mServerMessenger != null) {
            try {
                mServerMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
