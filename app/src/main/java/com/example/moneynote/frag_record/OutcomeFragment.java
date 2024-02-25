package com.example.moneynote.frag_record;

import androidx.fragment.app.Fragment;

import com.example.moneynote.R;
import com.example.moneynote.db.DBManager;
import com.example.moneynote.db.TypeBean;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class OutcomeFragment extends BaseRecordFragment {
    //重写父类对象，子类存在与父类相同的方法时，优先执行子类方法
    // 利用以上特性在不同的fragment调取数据库中不同的数据，实现收入与支出记录页面的显示
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库当中的数据
        List<TypeBean> outlist= DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        DBManager.insertItem_ToAccounttb(accountBean);
    }
}