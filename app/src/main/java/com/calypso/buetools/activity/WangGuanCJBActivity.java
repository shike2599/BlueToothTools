package com.calypso.buetools.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.calypso.bluelib.manage.BlueManager;
import com.calypso.buetools.MyApp;
import com.calypso.buetools.R;
import com.calypso.buetools.WangGuanBean;
import com.calypso.buetools.adapter.ZhuanJieCJBAdapter;
import com.calypso.buetools.utils.SendMessageUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WangGuanCJBActivity extends AppCompatActivity {
    private String[] time_arr = {"15分钟","30分钟","45分钟","60分钟",};
    private String URL = "http://116.62.186.91:8080/gywyext/collector/getCollectorByGateway.do?gatewayId=00000001";
    private Spinner time_Spinner;
    private RecyclerView device_recycle;
    private int select_position;
    private SendMessageUtils sendMessageUtils;
//    private RequestQueue requestQueue;
    private MyHander myHander;
    private ZhuanJieCJBAdapter zhuanJieCJBAdapter;
    private class MyHander extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg!=null){
                if(msg.what == 1){

                }
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        myHander = new MyHander();
        sendMessageUtils = SendMessageUtils.getInstance();
        volleyGet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wang_guan_cjb);
//        requestQueue = new MyApp().getRequestQueue();
        time_Spinner = findViewById(R.id.time_Spinner);
        device_recycle = findViewById(R.id.wangguan_RecyclerView);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,time_arr);
        time_Spinner.setAdapter(adapter);

        time_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                select_position = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        zhuanJieCJBAdapter = new ZhuanJieCJBAdapter(true);
        device_recycle.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        device_recycle.setAdapter(zhuanJieCJBAdapter);
    }
    public void wangGuanSetting(View view){
        //先发送IP地址信息
        int time = 0;
        if(select_position == 0){
            time = 15;
        }else if(select_position == 1){
            time = 30;
        }else if(select_position == 2){
            time = 45;
        }else if(select_position == 3){
            time = 60;
        }
        byte[] time_byte = sendMessageUtils.int2hexTo4(time*6000);
        List<Byte> time_list = new ArrayList<>();
        time_list.add((byte) 0x04);
        for(int i = 0; i < time_byte.length; i++){
            time_list.add(time_byte[i]);
        }
        //发送IP设置
        BlueManager.getInstance(this.getApplicationContext()).dataSend(sendMessageUtils.getIpList());
        //发送RTSP设置
        BlueManager.getInstance(this.getApplicationContext()).dataSend(sendMessageUtils.getRTSPList());
        //发送巡检时间设置
        BlueManager.getInstance(this.getApplicationContext()).dataSend(sendMessageUtils.setTYSettingMsg(time_list));
        //发送下属设备设置
        List<Map<String,String>> dataList = zhuanJieCJBAdapter.getDatalist();
        List<Byte> sendDate = new ArrayList<>();
        sendDate.add((byte)((dataList.size()*2) & 0xff));
        for(Map<String,String> map : dataList){
            byte[] bytes = sendMessageUtils.int2hex(Integer.parseInt(map.get("device_id")));
            sendDate.add(bytes[0]);
            sendDate.add(bytes[1]);
        }
        BlueManager.getInstance(this.getApplicationContext()).dataSend(sendMessageUtils.setTYSettingMsg(sendDate));
    }

    /**
     *  new StringRequest(int method,String url,Listener listener,ErrorListener errorListener)
     *  method：请求方式，Get请求为Method.GET，Post请求为Method.POST
     *  url：请求地址
     *  listener：请求成功后的回调
     *  errorListener：请求失败的回调
     */
    private void volleyGet() {
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {//s为请求返回的字符串数据
//                        Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        WangGuanBean wangGuanBean = gson.fromJson(s, WangGuanBean.class);
                       if(wangGuanBean!=null && wangGuanBean.getList()!=null && wangGuanBean.getList().size()!=0){
                           Log.d("WangGuan","onResponse===="+s);
                           List<String> list =  wangGuanBean.getList();
                           List<Map<String,String>> dataList = new ArrayList<>();
                           for(String id : list){
                               Map<String,String> map = new HashMap<>();
                               map.put("device_id",id+"");
                               dataList.add(map);
                           }
                           zhuanJieCJBAdapter.setDate(dataList);
                       }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(WangGuanCJBActivity.this,volleyError.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("QUEST_COMPAN");
        //将请求加入全局队列中
        MyApp.getRequestQueue().add(request);
        MyApp.getRequestQueue().start();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
