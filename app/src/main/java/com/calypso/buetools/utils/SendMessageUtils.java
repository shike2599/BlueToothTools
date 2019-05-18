package com.calypso.buetools.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liting on 2019/4/17.
 */

public class SendMessageUtils {
    private  byte[] function_Head = {(byte)0x05,(byte)0x0A};
    private  byte[] function_End = {(byte)0x12,(byte)0x34};
    private  byte[]  ip_byte = {(byte)0x11,(byte)0x00,(byte)0x1f,(byte)0x41,(byte)0x54,(byte)0x2b,(byte)0x2b,(byte)0x53,(byte)0x4f,
            (byte)0x43,(byte)0x4b,(byte)0x41,(byte)0x3d,(byte)0x54,(byte)0x43,(byte)0x50,(byte)0x2c,(byte)0x31,(byte)0x31,(byte)0x26,
            (byte)0x2e,(byte)0x36,(byte)0x32,(byte)0x2e,(byte)0x31,(byte)0x38,(byte)0x36,(byte)0x21,(byte)0x39,(byte)0x31,(byte)0x2c,
            (byte)0x38,(byte)0x30,(byte)0x39,(byte)0x31,(byte)0x12,(byte)0x34};
    private byte[] RPRS_byte = {(byte)0x07,(byte)0x04,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x12,(byte)0x34};
    private byte[] time_byte_head = {(byte)0x09,(byte)0x04};
    public volatile static SendMessageUtils sendMsgUtils = null;
    private SendMessageUtils(){

    }
    public static synchronized SendMessageUtils getInstance(){
        if(sendMsgUtils == null){
            synchronized (SendMessageUtils.class){
                if(sendMsgUtils == null){
                    sendMsgUtils = new SendMessageUtils();
                }
            }

        }
        return sendMsgUtils;
    }
    public List<Byte> setTYSettingMsg(List<Byte> list){
         //添加包头
         List<Byte> msglist = new ArrayList<>();
         addHeadMsg(msglist);
         //添加MSG
         for(byte msg : list){
             msglist.add(msg);
         }
         addEndMsg(msglist);
         return msglist;
    }
    //添加包头
    private  void addHeadMsg(List<Byte> msgList){
        for(int i = 0; i < function_Head.length; i++){
            msgList.add(function_Head[i]);
        }
    }

    //添加包尾
    private  void addEndMsg(List<Byte> msgList){
        for(int i = 0; i<function_End.length; i++){
            msgList.add(function_End[i]);
        }
    }

    public byte[] int2hex(int number){
        String hex = Integer.toHexString(number);
        Log.d("hex","hex==="+hex);
        byte[] byte_arr = new byte[2];
        byte_arr[1] = (byte)(number & 0xFF);
        byte_arr[0] = (byte)(number >> 8 & 0xFF);
        return byte_arr;
    }

    public byte[] int2hexTo4(int number){
        String hex = Integer.toHexString(number);
        Log.d("hex","hex==="+hex);
        byte[] byte_arr = new byte[4];
        byte_arr[3] = (byte)(number & 0xFF);
        byte_arr[2] = (byte)(number >> 8 & 0xFF);
        byte_arr[1] = (byte)(number >> 16 & 0xFF);
        byte_arr[0] = (byte)(number >> 24 & 0xFF);
        return byte_arr;
    }

    /**
     * 发送IP的数组
     * @return
     */
    public List<Byte> getIpList(){
        List<Byte> list = new ArrayList<>();
        for(int i = 0; i < ip_byte.length; i++){
            list.add(ip_byte[i]);
        }
        return list;
    }

    /**
     * 发送IP的数组
     * @return
     */
    public List<Byte> getRTSPList(){
        List<Byte> list = new ArrayList<>();
        for(int i = 0; i < RPRS_byte.length; i++){
            list.add(RPRS_byte[i]);
        }
        return list;
    }
}
