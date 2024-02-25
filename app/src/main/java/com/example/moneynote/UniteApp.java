package com.example.moneynote;

import android.app.Application;

import com.example.moneynote.db.DBManager;

//表示全局应用的类
public class UniteApp extends Application {

    public void onCreate(){
        super.onCreate();
        //初始化数据库
        DBManager.initDB(getApplicationContext());
    }
}
