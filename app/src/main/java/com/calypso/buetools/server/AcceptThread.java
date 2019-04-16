package com.calypso.buetools.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Handler;

import com.calypso.buetools.utils.Constant;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by lichee on 2019/3/29.
 */

public class AcceptThread extends Thread {
    private static final String NAME = "BlueToothClass";
    //为了保证蓝牙连接，必须使用如下代码的唯一的UUID：
    public static final String CONNECTTION_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private static final UUID MY_UUID = UUID.fromString(CONNECTTION_UUID);

    private final BluetoothServerSocket mmServerSocket;
    private final BluetoothAdapter mBluetoothAdapter;
    private final Handler mHandler;
    private ConnectedThread mConnectedThread;

    public AcceptThread(BluetoothAdapter adapter, Handler handler) {

        mBluetoothAdapter = adapter;
        mHandler = handler;
        // 构造方法中创建一个临时的BluetoothServerSocket对象的引用
        BluetoothServerSocket tmp = null;
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            }
        } catch (IOException e) { }
        mmServerSocket = tmp;
    }

    //子线程中运行的代码
    public void run() {
        BluetoothSocket socket = null;
        // 不断监听来自客户端的连接请求
        while (true) {
            try {
//通知UI线程：“服务器端开始监听客户端的请求”
                mHandler.sendEmptyMessage(Constant.MSG_START_LISTENING);
//accept方法会阻塞线程
                socket = mmServerSocket.accept();
            } catch (IOException e) {
//若出现异常，退出监听
                mHandler.sendMessage(mHandler.obtainMessage(Constant.MSG_ERROR, e));
                break;
            }
            // 连接成功
            if (socket != null) {
                // 另开启一个子线程，新建一个socket用于传输数据
                manageConnectedSocket(socket);
                try {
                    //强制关闭监听
                    mmServerSocket.close();
                    mHandler.sendEmptyMessage(Constant.MSG_FINISH_LISTENING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        //只支持同时处理一个连接
        if( mConnectedThread != null) {
            mConnectedThread.cancel();
        }
        //通知UI线程连接到一个蓝牙设备（客户端）
        mHandler.sendEmptyMessage(Constant.MSG_GOT_A_CLINET);
        //另开启的新线程，用于传输数据，传入BluetoothSocket对象引用和Handler对象引用
        mConnectedThread = new ConnectedThread(socket, mHandler);
        //启动线程
        mConnectedThread.start();
    }

    //主动停止监听
    public void cancel() {
        try {
            mmServerSocket.close();
//通知UI线程：“已停止监听”
            mHandler.sendEmptyMessage(Constant.MSG_FINISH_LISTENING);
        } catch (IOException e) { }
    }
    //向客户端写入数据
    public void sendData(byte[] data) {
        if( mConnectedThread!=null){
            mConnectedThread.write(data);
        }
    }
}
