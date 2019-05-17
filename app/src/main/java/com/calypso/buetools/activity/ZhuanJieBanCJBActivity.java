package com.calypso.buetools.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.calypso.bluelib.manage.BlueManager;
import com.calypso.buetools.R;
import com.calypso.buetools.adapter.ZhuanJieCJBAdapter;
import com.calypso.buetools.utils.SendMessageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZhuanJieBanCJBActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<Map<String,String>> dataList;
    private ZhuanJieCJBAdapter mAdapter;
    private AlertDialog.Builder customizeDialog;
    private SendMessageUtils sendMessageUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendMessageUtils = SendMessageUtils.getInstance();
        setContentView(R.layout.activity_zhuan_jie_ban_cjb);
        mRecyclerView = findViewById(R.id.zhuanJieBan_recyclerView);
        dataList = new ArrayList<>();
        mAdapter = new ZhuanJieCJBAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void zhuanJieBanSetting(View view){
        List<Map<String,String>> dataList = mAdapter.getDatalist();
        List<Byte> sendDate = new ArrayList<>();
        sendDate.add((byte)((dataList.size()*2) & 0xff));
        for(Map<String,String> map : dataList){
           byte[] bytes = sendMessageUtils.int2hex(Integer.parseInt(map.get("device_id")));
           sendDate.add(bytes[0]);
           sendDate.add(bytes[1]);
        }
        BlueManager.getInstance(this.getApplicationContext()).dataSend(sendMessageUtils.setTYSettingMsg(sendDate));
    }
    //添加设备
    public void addSettingDevice(View view){
      if(mAdapter.getDatalist().size()>=16){
          Toast.makeText(this,"最多添加16个设备！",Toast.LENGTH_LONG).show();
      }else{
          showCustomizeDialog();
      }

    }

    private void showCustomizeDialog() {
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        customizeDialog =
                new AlertDialog.Builder(ZhuanJieBanCJBActivity.this);
        final View dialogView = LayoutInflater.from(ZhuanJieBanCJBActivity.this)
                .inflate(R.layout.zhuanjieban_setting_dialog_item,null);
        final EditText input_Name =
                (EditText) dialogView.findViewById(R.id.input_xs_deviceName_Edt);
        final EditText input_ID =
                (EditText) dialogView.findViewById(R.id.input_xs_deviceId_Edt);
        customizeDialog.setTitle("请输入设备信息");
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                      String device_Id = input_ID.getText().toString();
                      if(device_Id!=null){
                          device_Id.trim();
                      }else{
                          Toast.makeText(ZhuanJieBanCJBActivity.this,"请输入设备ID！",
                                  Toast.LENGTH_LONG).show();
                          return;
                        }
                        Map<String,String> deviceMap = new HashMap<>();
//                        deviceMap.put("device_name",device_Name);
                        deviceMap.put("device_id",device_Id);
                        dataList.add(deviceMap);
                        mAdapter.setDate(dataList);
                    }
                });
        customizeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        customizeDialog.show();
    }
}
