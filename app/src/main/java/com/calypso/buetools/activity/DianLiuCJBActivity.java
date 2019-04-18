package com.calypso.buetools.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.calypso.buetools.R;

public class DianLiuCJBActivity extends AppCompatActivity {
    private EditText input_id,input_blbb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dian_liu_cjb);
        input_id = findViewById(R.id.dianliu_idEdit);
        input_blbb = findViewById(R.id.dianliuBianBi_idEdit);
    }

    public void dianLiuSetting(View view){
       String id = input_id.getText().toString();
       String dlbb = input_blbb.getText().toString();
       if(id==null){
           Toast.makeText(this,"请输入ID",Toast.LENGTH_LONG).show();
           return;
       }else {
           if(Integer.parseInt(id.trim())>65535){
               Toast.makeText(this,"请输入0-65535之间",Toast.LENGTH_LONG).show();
               return;
           }
       }
        if(dlbb == null){
            Toast.makeText(this,"请输入电流变比",Toast.LENGTH_LONG).show();
            return;
        }else{
            if(Integer.parseInt(dlbb.trim())>65535){
                Toast.makeText(this,"请输入0-65535之间",Toast.LENGTH_LONG).show();
                return;
            }
        }
        int int_id = Integer.parseInt(id.trim());
        int int_dlbb = Integer.parseInt(dlbb.trim());

    }
}
