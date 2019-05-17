package com.calypso.buetools.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.calypso.bluelib.manage.BlueManager;
import com.calypso.buetools.R;
import com.calypso.buetools.utils.SendMessageUtils;

import java.util.ArrayList;
import java.util.List;

public class TongYongCJBActivity extends AppCompatActivity {
    private String[] settingType = {"请选择要设置的功能","干湿变压器","压力变送器","温湿度变送器","液位变送器"};
    private EditText input_id;
    private Spinner one_spinner,two_spinner,three_spinner,four_spinner,
            five_spinner,six_spinner,seven_spinner,eight_spinner;
    private int[] choseArr = new int[8];
    private int device_id;
//    private List<Byte> typeList = new ArrayList<>();
    private SendMessageUtils sendMessageUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tong_yong_cjb);

        sendMessageUtils = SendMessageUtils.getInstance();

        input_id = findViewById(R.id.tongyong_idEdit);
        one_spinner = findViewById(R.id.one_Spinner);
        two_spinner = findViewById(R.id.two_Spinner);
        three_spinner = findViewById(R.id.three_Spinner);
        four_spinner = findViewById(R.id.four_Spinner);
        five_spinner = findViewById(R.id.five_Spinner);
        six_spinner = findViewById(R.id.six_Spinner);
        seven_spinner = findViewById(R.id.seven_Spinner);
        eight_spinner = findViewById(R.id.eight_Spinner);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,settingType);

        one_spinner.setAdapter(adapter);
        two_spinner.setAdapter(adapter);
        three_spinner.setAdapter(adapter);
        four_spinner.setAdapter(adapter);
        five_spinner.setAdapter(adapter);
        six_spinner.setAdapter(adapter);
        seven_spinner.setAdapter(adapter);
        eight_spinner.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        one_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                choseArr[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        two_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                choseArr[1] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        three_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                choseArr[2] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        four_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                choseArr[3] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        five_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                choseArr[4] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        six_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                choseArr[5] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        seven_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                choseArr[6] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        eight_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                choseArr[7] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void tongYongSetting(View view){
        List<Byte> dataList = new ArrayList<>();
        String id = input_id.getText().toString();
        if(id!=null){
            if(Integer.parseInt(id)<=65535){
                device_id = Integer.parseInt(id);
            }else{
                Toast.makeText(this,"id不能超过65535",Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            Toast.makeText(this,"请新输入ID",Toast.LENGTH_SHORT).show();
            return;
        }
        byte[] id_byte = sendMessageUtils.int2hex(device_id);
        dataList.add(id_byte[0]);
        dataList.add(id_byte[1]);
        for(int i=0;i<choseArr.length;i++){
            if(choseArr[i]==0){
                dataList.add((byte)0x00);
            }else if(choseArr[i] == 1){
                dataList.add((byte)0x01);
            }else if(choseArr[i] == 2){
                dataList.add((byte)0x02);
            }else if(choseArr[i] == 3){
                dataList.add((byte)0x03);
            }else if(choseArr[i] == 4){
                dataList.add((byte)0x04);
            }
       }

        BlueManager.getInstance(this.getApplicationContext()).dataSend(sendMessageUtils.setTYSettingMsg(dataList));
    }
}
