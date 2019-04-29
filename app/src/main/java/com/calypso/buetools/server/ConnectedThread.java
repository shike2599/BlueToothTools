package com.calypso.buetools.server;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.calypso.buetools.utils.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lichee on 2019/3/29.
 *
 */

public class ConnectedThread extends Thread  {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final Handler mHandler;

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        mHandler = handler;
        //获得输入输出流
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    //在子线程中运行的代码
    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // 不断循环以读取数据
        while (true) {
            try {

                // 读取数据
                bytes = mmInStream.read(buffer);
                // 将读取的数据信息发送至UI线程并显示数据
                if( bytes >0) {
                    Message message = mHandler.obtainMessage(Constant.MSG_GOT_DATA, new String(buffer, 0, bytes, "utf-8"));
                    mHandler.sendMessage(message);
                }
                Log.d("GOTMSG", "message size" + bytes);
            } catch (IOException e) {
                mHandler.sendMessage(mHandler.obtainMessage(Constant.MSG_ERROR, e));
                break;
            }
        }
    }

    //写入数据
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }

    //取消连接
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
