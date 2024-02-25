package com.example.moneynote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moneynote.R;
import com.example.moneynote.db.AccountBean;

import java.util.Calendar;
import java.util.List;

public class AccountAdapter extends BaseAdapter {
    Context context;
    List<AccountBean>mDatas;
    LayoutInflater inflater;
    int year,month,day;

    public AccountAdapter(Context context, List<AccountBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater=LayoutInflater.from(context);
        Calendar calendar = Calendar.getInstance();
        year=calendar.get(calendar.YEAR);
        month=calendar.get(calendar.MONTH)+1;
        day=calendar.get(calendar.DAY_OF_MONTH);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null) {
            convertView=inflater.inflate(R.layout.item_mainlv,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        AccountBean bean = mDatas.get(position);
        holder.typeIv.setImageResource(bean.getsImgId());
        holder.typeTv.setText(bean.getTypename());
        holder.tipTv.setText(bean.getTip());
        holder.moneyTv.setText("$"+bean.getMoney());
        if (bean.getYear()==year&&bean.getMonth()==month&&bean.getDay()==day) {
            String time = bean.getTime().split("")[1];
            holder.timeTv.setText("今天"+time);
        }else {
            holder.timeTv.setText(bean.getTime());
        }
        holder.timeTv.setText(bean.getTime());
        return convertView;
    }

    class ViewHolder {
        ImageView typeIv;
        TextView typeTv,tipTv,timeTv,moneyTv;
        public ViewHolder(View view){
            typeIv=view.findViewById(R.id.item_mainLv_iv);
            typeTv=view.findViewById(R.id.item_mainLv_tv_title);
            tipTv=view.findViewById(R.id.item_mainLv_tv_tip);
            timeTv=view.findViewById(R.id.item_mainLv_tv_Time);
            moneyTv=view.findViewById(R.id.item_mainLv_tv_money);
        }
    }
}
