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
public class IncomeFragment extends BaseRecordFragment {
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库当中的数据
        List<TypeBean> inlist= DBManager.getTypeList(1);
        typeList.addAll(inlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.in_qt_fs);
    }
    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
        DBManager.insertItem_ToAccounttb(accountBean);
    }
}