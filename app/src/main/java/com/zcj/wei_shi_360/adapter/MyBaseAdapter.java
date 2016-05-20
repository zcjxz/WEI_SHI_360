package com.zcj.wei_shi_360.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 曾灿杰 on 2016/4/27.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    public List<T> lists;
    public Context mContext;

    public MyBaseAdapter(List<T> lists, Context mContext) {
        this.lists = lists;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;
//    }
}
