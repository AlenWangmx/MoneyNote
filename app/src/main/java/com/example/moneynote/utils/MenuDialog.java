package com.example.moneynote.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.moneynote.AboutActivity;
import com.example.moneynote.HistoryActivity;
import com.example.moneynote.R;
import com.example.moneynote.SettingActivity;

public class MenuDialog extends Dialog implements View.OnClickListener {
    Button aboutBtn,settingBtn,historyBtn,infoBtn;
    ImageView cancelIV;
    public MenuDialog(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_menu);
        aboutBtn=findViewById(R.id.dialog_menu_btn_about);
        settingBtn=findViewById(R.id.dialog_menu_btn_setting);
        historyBtn=findViewById(R.id.dialog_menu_btn_record);
//        infoBtn=findViewById(R.id.dialog_menu_btn_info);
        cancelIV=findViewById(R.id.dialog_menu_iv);
        
        aboutBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
//        infoBtn.setOnClickListener(this);
        cancelIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (v.getId()==R.id.dialog_menu_btn_about) {
            intent.setClass(getContext(), AboutActivity.class);
            getContext().startActivity(intent);
        } else if (v.getId()==R.id.dialog_menu_btn_setting) {
            intent.setClass(getContext(), SettingActivity.class);
            getContext().startActivity(intent);
        } else if (v.getId() == R.id.dialog_menu_btn_record) {
            intent.setClass(getContext(), HistoryActivity.class);
            getContext().startActivity(intent);
        } else if (v.getId()==R.id.dialog_menu_iv) {

        }
        cancel();
    }


    //设置dialog尺寸与屏幕尺寸一致
    public void setDialogSize(){
        //获取当前窗口对象
        Window window=getWindow();
        //获取窗口对象参数
        WindowManager.LayoutParams wlp=window.getAttributes();
        //获取屏幕宽度
        Display d=window.getWindowManager().getDefaultDisplay();
        wlp.width=(int)(d.getWidth());
        wlp.gravity= Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
