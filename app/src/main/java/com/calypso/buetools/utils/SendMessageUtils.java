package com.calypso.buetools.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liting on 2019/4/17.
 */

public class SendMessageUtils {
    private  int[] function_Head = {0x05,0x0A};
    private  int[] function_End = {0x12,0x34};
    public static SendMessageUtils sendMsgUtils = null;
    private SendMessageUtils(){

    }
    public  synchronized SendMessageUtils getInstance(){
        if(sendMsgUtils == null){
             sendMsgUtils = new SendMessageUtils();
        }
        return sendMsgUtils;
    }
    public void setTYSettingMsg(List<Integer> list){
         //添加包头
         List<Integer> msglist = new ArrayList<>();
         addHeadMsg(msglist);
         //添加MSG
         for(int msg : list){
             msglist.add(msg);
         }
         addEndMsg(msglist);
    }
    //添加包头
    private  void addHeadMsg(List<Integer> msgList){
        for(int i = 0; i < function_Head.length; i++){
            msgList.add(function_End[i]);
        }
    }

    //添加包尾
    private  void addEndMsg(List<Integer> msgList){
        for(int i = 0; i<function_End.length; i++){
            msgList.add(function_End[i]);
        }
    }
}
