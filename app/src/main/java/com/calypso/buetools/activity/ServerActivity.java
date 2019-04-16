package com.calypso.buetools.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.calypso.buetools.utils.Constant;
import com.calypso.buetools.R;
import com.calypso.buetools.server.AcceptThread;

public class ServerActivity extends Activity {

    private Button btnStart;

    //创建一个新线程，用于处理会发生阻塞的监听网络的方法accept
    private AcceptThread mAcceptThread;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //开始监听时，接收由子线程发来的消息
                case Constant.MSG_START_LISTENING:
                    //更新UI
                    setProgressBarIndeterminateVisibility(true);
                    break;
                //结束监听时，接收由子线程发来的消息
                case Constant.MSG_FINISH_LISTENING:
                    //更新UI
                    setProgressBarIndeterminateVisibility(false);
                    break;
                //接收到数据后，接收由子线程发来的消息及其附加信息
                case Constant.MSG_GOT_DATA:
                    //在UI线程中显示
                    showToast("data: "+String.valueOf(msg.obj));
                    break;
                //当发生错误时，接收由子线程发来的消息
                case Constant.MSG_ERROR:
                    //显示错误原因
                    showToast("error: "+String.valueOf(msg.obj));
                    break;
                //当程序作为客户端连接到服务器后，接收接收由子线程发来的消息
                case Constant.MSG_CONNECTED_TO_SERVER:
                    //通知用户连接成功
                    showToast("Connected to Server");
                    break;
                //当程序作为服务器端绑定了一个客户端后，接收接收由子线程发来的消息
                case Constant.MSG_GOT_A_CLINET:
                    //通知用户绑定成功
                    showToast("Got a Client");
                    break;
            }
        }
    }

    private void showToast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mAcceptThread != null) {
                    mAcceptThread.cancel();
                }
                //创建一个新线程，传入BlueToothAdapter对象实例和Handler对象实例
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                    mAcceptThread = new AcceptThread(BluetoothAdapter.getDefaultAdapter(), new MyHandler());
                }
                //启动该线程
                mAcceptThread.start();
            }
        });
    }

}
