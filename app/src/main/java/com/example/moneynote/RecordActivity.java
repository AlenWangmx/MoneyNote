package com.example.moneynote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.moneynote.adapter.RecordPagerAdapter;
import com.example.moneynote.frag_record.OutcomeFragment;
import com.example.moneynote.frag_record.IncomeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        tabLayout =findViewById(R.id.record_tabs);
        viewPager=findViewById(R.id.record_vp);

       initPaper();
    }

    public void initPaper(){
//        初始化ViewPager页面集合
        List<Fragment> fragmentList=new ArrayList<>();
//        创建收入和支出页面，放置在Fragment当中
        OutcomeFragment outFrag= new OutcomeFragment();//支出
        IncomeFragment inFrag=new IncomeFragment();//收入
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

//        创建适配器
        RecordPagerAdapter pagerAdapter=new RecordPagerAdapter(getSupportFragmentManager(),fragmentList);
//        设置适配器
        viewPager.setAdapter(pagerAdapter);
//        将TabLayout和ViewPager进行关联
        tabLayout.setupWithViewPager(viewPager);
    }
    /*点击事件*/
    public void onClick(View view) {
        if (view.getId() == R.id.record_iv_back) {
            finish();
        }
    }
}