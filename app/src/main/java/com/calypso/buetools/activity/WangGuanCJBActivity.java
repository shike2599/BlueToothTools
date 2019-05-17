package com.calypso.buetools.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.calypso.buetools.R;

public class WangGuanCJBActivity extends AppCompatActivity {
    private String[] time_arr = {"15分钟","30分钟","45分钟","60分钟",};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wang_guan_cjb);
    }
}
