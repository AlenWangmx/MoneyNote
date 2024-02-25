package com.example.moneynote.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moneynote.R;
import com.example.moneynote.db.AccountBean;
import com.example.moneynote.db.DBManager;
import com.example.moneynote.db.TypeBean;
import com.example.moneynote.utils.KeyBoardUtils;
import com.example.moneynote.utils.SelectTimeDialog;
import com.example.moneynote.utils.TipDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,tipTv,timeTv;
    GridView typeGv;
    List<TypeBean> typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean;//将需要插入到记账本中的数据保存成对象的形式

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean=new AccountBean();//创建对象
        accountBean.setTypename("其他");
        accountBean.setsImgId(R.mipmap.ic_qita_fs);
    }

    private  void initView(View view){
        keyboardView=view.findViewById(R.id.frag_record_keyboard);
        moneyEt=view.findViewById(R.id.frag_recordEt_money);
        typeIv=view.findViewById(R.id.frag_record_iv);
        typeGv=view.findViewById(R.id.frag_record_gv);
        typeTv=view.findViewById(R.id.frag_recordTv_type);
        tipTv=view.findViewById(R.id.frag_recordTv_tip);
        timeTv=view.findViewById(R.id.frag_recordTv_time);
        tipTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
//        使自定义键盘显示出来
        KeyBoardUtils boardUtils=new KeyBoardUtils(keyboardView,moneyEt);
        boardUtils.showKeyboard();
//        设置接口，监听确定按钮被点击
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //获取输入钱数
                String moneyStr=moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                //获取记录的信息，保存在数据库中
                saveAccountToDB();
                //返回上一级页面
                getActivity().finish();
            }
        });
    }
    /*抽象方法：让子类一定要重写方法*/
    public abstract void saveAccountToDB();

    //    记录页面当中的支出模块
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        setInitTime();
        //给GridView填充数据的方法
        loadDataToGV();
        setGVListener();
        return view;
    }
    //获取当前时间显示在timeTV上
    private void setInitTime() {
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time=sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar= Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    //GridView中每一项的点击事件
    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos=position;
                adapter.notifyDataSetChanged();
                TypeBean typeBean=typeList.get(position);
                String typename=typeBean.getTypeName();
                typeTv.setText(typename);
                accountBean.setTypename(typename);

                int simageId=typeBean.getsImgID();
                typeIv.setImageResource(simageId);
                accountBean.setsImgId(simageId);
            }
        });
    }

    // 加载GridView内容
    public void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.frag_recordTv_time) {
            showTimeDialog();
        } else if (v.getId()==R.id.frag_recordTv_tip) {
            showBZdialog();
        }

    }
//弹出显示时间的对话框
    private void showTimeDialog() {
        SelectTimeDialog dialog=new SelectTimeDialog(getContext());
        dialog.show();
        //设定按钮被点击的监听
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }

    //弹出备注对话框
    public void showBZdialog() {
        TipDialog dialog=new TipDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new TipDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
               String msg=dialog.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    tipTv.setText(msg);
                    accountBean.setTip(msg);
                }
                dialog.cancel();
            }
        });
    }
}