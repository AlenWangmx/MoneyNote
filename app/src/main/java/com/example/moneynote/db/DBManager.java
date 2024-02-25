package com.example.moneynote.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//负责管理数据库的类，对表当中的内容进行操作
public class DBManager {
    private static SQLiteDatabase db;

    public static void initDB(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context);//得到帮助类对象
        db = helper.getWritableDatabase();//得到数据库对象
    }

    //    读取数据库当中的数据，写入内存集合
//    kind表示收入或支出
    public static List<TypeBean> getTypeList(int kind) {
        List<TypeBean> list = new ArrayList<>();

        String sql = "select*from typetb where kind=" + kind;
        Cursor cursor = db.rawQuery(sql, null);
//        循环读取图标内容，存储到对象之中
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndexOrThrow("typeName"));
            int imageId = cursor.getInt(cursor.getColumnIndexOrThrow("ImgID"));
            int simageId = cursor.getInt(cursor.getColumnIndexOrThrow("sImgID"));
            int kind1 = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, simageId, kind1);
            list.add(typeBean);
        }
        return list;
    }

    //向记账表中插入元素
    public static void insertItem_ToAccounttb(AccountBean bean) {
        ContentValues values = new ContentValues();
        values.put("typeName", bean.getTypename());
        values.put("sImgID", bean.getsImgId());
        values.put("tip", bean.getTip());
        values.put("money", bean.getMoney());
        values.put("time", bean.getTime());
        values.put("year", bean.getYear());
        values.put("month", bean.getMonth());
        values.put("day", bean.getDay());
        values.put("kind", bean.getKind());
        db.insert("accounttb", null, values);
        Log.i("animee", "insertItemToAccounttb:ok!");
    }

    /*
    * 获取记账表中某一天的所有收支情况
    * */
    public static  List<AccountBean>getAccountListOneDayFromAccounttb(int year,int month,int day){
        List<AccountBean>list=new ArrayList<>();
        String sql="select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        //遍历符合要求的每一行数据
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typeName = cursor.getString(cursor.getColumnIndexOrThrow("typeName"));
            String tip = cursor.getString(cursor.getColumnIndexOrThrow("tip"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImgID = cursor.getInt(cursor.getColumnIndexOrThrow("sImgID"));

            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            AccountBean accountBean = new AccountBean(id, typeName, tip, sImgID, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return  list;
    }

    /**
     *获取记账表中某一个月的所有收支情况
     */
    public static  List<AccountBean>getAccountListOneMonthFromAccounttb(int year,int month){
        List<AccountBean>list=new ArrayList<>();
        String sql="select * from accounttb where year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        //遍历符合要求的每一行数据
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typeName = cursor.getString(cursor.getColumnIndexOrThrow("typeName"));
            String tip = cursor.getString(cursor.getColumnIndexOrThrow("tip"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImgID = cursor.getInt(cursor.getColumnIndexOrThrow("sImgID"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            int day=cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            AccountBean accountBean = new AccountBean(id, typeName, tip, sImgID, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return  list;
    }

    //获取某一天的收支总金额
    public static  float getSumMoneyOneDay(int year,int month,int day,int kind){
        float total=0.0f;
        String sql="select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        //遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total=money;
        }
        return total;
    }

    //获取某一月的收支总金额
    public static  float getSumMoneyOneMonth(int year,int month,int kind){
        float total=0.0f;
        String sql="select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        //遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total=money;
        }
        return total;
    }

    //获取某一年的收支总金额
    public static  float getSumMoneyOneYear(int year,int kind){
        float total=0.0f;
        String sql="select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        //遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("sum(money)"));
            total=money;
        }
        return total;
    }

    /*
    *根据传入的id，删除accounttb表当中的一条数据
    */
    public static  int deleteItemFromAccounttbById(int id){
        int i = db.delete("accounttb", "id=?", new String[]{id + ""});
        return i;
    }

    /*
    *根据备注搜索收入或者支出的情况列表
    */
    public static List<AccountBean>getAccountListByRemarkFromAccounttb(String remark){
        List<AccountBean>list=new ArrayList<>();
        String sql="select * from accounttb where tip like '%"+remark+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String typeName = cursor.getString(cursor.getColumnIndexOrThrow("typeName"));
            String bz = cursor.getString(cursor.getColumnIndexOrThrow("tip"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            int sImgID = cursor.getInt(cursor.getColumnIndexOrThrow("sImgID"));
            int kind = cursor.getInt(cursor.getColumnIndexOrThrow("kind"));
            float money = cursor.getFloat(cursor.getColumnIndexOrThrow("money"));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
            int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
            AccountBean accountBean = new AccountBean(id, typeName, bz, sImgID, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /*
    查询记账的表中有几个年份信息
     */
    public static List<Integer>getYearListFromAccounttb(){
        List<Integer> list=new ArrayList<>();
        String sql="select distinct(year) from accounttb order by year asc";
        Cursor cursor= db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
            list.add(year);
        }
        return list;
    }

    /*
    *删除accounttb表中所有数据
     */
    public static void deleteAllAccount(){
        String sql="delete from accounttb";
        db.execSQL(sql);
    }
}
