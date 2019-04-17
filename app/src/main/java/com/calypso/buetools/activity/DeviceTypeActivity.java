package com.calypso.buetools.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.calypso.buetools.R;

public class DeviceTypeActivity extends AppCompatActivity {
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_type);
        intent = new Intent();
    }
    // 通过采集板设置
    public void toTongYongCJBSetting(View view){
      intent.setClass(DeviceTypeActivity.this,TongYongCJBActivity.class);
      startActivity(intent);
    }

    // 电流采集板设置
    public void toDianLiuCJBSetting(View view){
        intent.setClass(DeviceTypeActivity.this,DianLiuCJBActivity.class);
        startActivity(intent);
    }

    // 转接板采集板设置
    public void toZhuanJieBanCJBSetting(View view){
        intent.setClass(DeviceTypeActivity.this,ZhuanJieBanCJBActivity.class);
        startActivity(intent);
    }

    // 网关采集板设置
    public void toWangGuanCJBSetting(View view){
        intent.setClass(DeviceTypeActivity.this,WangGuanCJBActivity.class);
        startActivity(intent);
    }
}
