package com.example.moneynote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.moneynote.adapter.AccountAdapter;
import com.example.moneynote.db.AccountBean;
import com.example.moneynote.db.DBManager;
import com.example.moneynote.utils.BudgetDialog;
import com.example.moneynote.utils.MenuDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv;//展示今日收支情况的ListView
    //首页按钮
    ImageView searchIv;
    Button editBtn;
    ImageButton menuBtn;

    //声明数据源
    List<AccountBean> mDatas;
    AccountAdapter adapter;
    int year,month,day;

    //头布局相关控件
    View headerView;
    TextView topOutTv,topInTv,topTv3,topBudgetTv,topConTv;
    ImageView topShowIv;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences=getSharedPreferences("budget",MODE_PRIVATE);
        //添加ListView的头布局
        addLVHeaderView();
        mDatas=new ArrayList<>();
        //设置适配器：加载每一行数据到列表当中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }
    //初始化自带的View的方法
    private void initView() {
        todayLv=findViewById(R.id.main_Lv);
        editBtn=findViewById(R.id.main_edit_Btn);
        menuBtn=findViewById(R.id.main_menu_imgBtn);
        searchIv=findViewById(R.id.search_img);
        editBtn.setOnClickListener(this);
        menuBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLVLongClickListener();
    }
    //设置listview的长按事件
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0) {//点击了头布局
                    return false;
                }
                int pos=position-1;
                AccountBean clickBean = mDatas.get(pos);//获取正在被点击的这条信息

                //弹出提示用户是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }
    
    //弹出是否删除某一条记录的对话框
    private void showDeleteItemDialog(AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("确定删除此记录吗？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //执行删除操作
                        DBManager.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);//实时刷新，移除集合当中的对象
                        adapter.notifyDataSetChanged();
                        setTopTvShow();//改变头布局TextView显示的内容
                    }
                });
        builder.create().show();//显示对话框
    }

    //添加ListView头布局的方法
    private void addLVHeaderView() {
        //将布局转换成View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //查找头布局可用控件
        topOutTv=headerView.findViewById(R.id.item_mainLv_top_tv1Out);
        topInTv=headerView.findViewById(R.id.item_mainLv_top_tv2In);
        topBudgetTv=headerView.findViewById(R.id.item_mainLv_top_tvBudget);
        topTv3=headerView.findViewById(R.id.item_mainLv_top_tv3);
        topConTv=headerView.findViewById(R.id.item_mainLv_top_tvDay);
        topShowIv=headerView.findViewById(R.id.item_mainLv_top_ivHide);

        topBudgetTv.setOnClickListener(this);
        topTv3.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);
    }
    //获取今日的具体时间
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year=calendar.get(calendar.YEAR);
        month=calendar.get(calendar.MONTH)+1;
        day=calendar.get(calendar.DAY_OF_MONTH);
    }

    //当Activity获取焦点时，会调用的方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }
    //设置头布局当中文本内容的显示
    private void setTopTvShow() {
        //获取今日支出和收入总金额，显示在view当中
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay="今日支出￥"+outcomeOneDay+"收入￥"+incomeOneDay;
        topConTv.setText(infoOneDay);

        //获取本月收支总金额
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("￥"+incomeOneMonth);
        topOutTv.setText("￥"+outcomeOneMonth);

        //设置显示预算剩余
        float bmoney = preferences.getFloat("bmoney", 0);
        if (bmoney==0) {
            topBudgetTv.setText("￥ 0");
        }else {
            float syMoney=bmoney-outcomeOneMonth;
            topBudgetTv.setText("￥"+syMoney);
        }
    }

    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }
    //首页Button相关点击事件
    public void onClick(View view) {
        if(view.getId()==R.id.search_img) {
            Intent it=new Intent(this, SearchActivity.class);
            startActivity(it);
        } else if (view.getId()==R.id.main_edit_Btn) {
            Intent it1=new Intent(this, RecordActivity.class);
            startActivity(it1);
        }else if (view.getId()==R.id.main_menu_imgBtn) {
            MenuDialog menuDialog = new MenuDialog(this);
            menuDialog.show();
            menuDialog.setDialogSize();
        } else if (view.getId()==R.id.item_mainLv_top_tvBudget||view.getId()==R.id.item_mainLv_top_tv3) {
            showBudgetDialog();
        } else if (view.getId()==R.id.item_mainLv_top_ivHide) {
            //切换TextView明文密文
            toggleShow();
        }
        if (view == headerView) {
            //头布局被点击了
        }
    }
    //    显示运输算设置对话框
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //将预算金额写入到共享参数当中，进行存储
                SharedPreferences.Editor editor= preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();

                //计算剩余金额
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float syMoney=money-outcomeOneMonth;//预算剩余=预算-支出
                topBudgetTv.setText("￥"+syMoney);
            }
        });
    }

    boolean isShow=true;
    //点击头布局眼睛图标时，互相切换明文密文
    private void toggleShow() {
        if (isShow) {//明文————>密文
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod);//设置隐藏
            topOutTv.setTransformationMethod(passwordMethod);//设置隐藏
            topBudgetTv.setTransformationMethod(passwordMethod);//设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow=false;
        }else {//密文————>明文
            HideReturnsTransformationMethod hideReturnsMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideReturnsMethod);//设置隐藏
            topOutTv.setTransformationMethod(hideReturnsMethod);//设置隐藏
            topBudgetTv.setTransformationMethod(hideReturnsMethod);//设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow=true;
        }
    }

}