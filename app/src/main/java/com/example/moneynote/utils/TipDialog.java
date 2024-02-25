package com.example.moneynote.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.moneynote.R;

public class TipDialog extends Dialog implements View.OnClickListener {
    EditText et;
    Button cancelBtn;
    Button ensureBtn;
    OnEnsureListener onEnsureListener;
//设定回调接口的方法
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public TipDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tip);
        et=findViewById(R.id.dialog_tip_et);
        cancelBtn=findViewById(R.id.dialog_tip_btn_cancel);
        ensureBtn=findViewById(R.id.dialog_tip_btn_ensure);
        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    public interface OnEnsureListener{
        public void onEnsure();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.dialog_tip_btn_cancel) {
            cancel();
        } else if (v.getId()==R.id.dialog_tip_btn_ensure) {
            if (onEnsureListener!=null) {
                onEnsureListener.onEnsure();
            }
        }
    }

    //获取输入数据的方法
    public  String getEditText(){
        return  et.getText().toString().trim();
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
        handler.sendEmptyMessageDelayed(1,50);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
//            自动弹出软键盘的方法
            InputMethodManager inputMethodManager= (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
