package com.zcj.wei_shi_360.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcj.wei_shi_360.R;

public class SettingClickItemView extends RelativeLayout{

    private TextView Title;
    private TextView Desc;
    private String NAMESPACE="http://schemas.android.com/apk/res-auto";

    public SettingClickItemView(Context context) {
        super(context);
        initView();
    }

    public SettingClickItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public SettingClickItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        View.inflate(getContext(), R.layout.view_setting_click_item,this);
        Title = (TextView) findViewById(R.id.tv_title);
        Desc = (TextView) findViewById(R.id.tv_decs);
    }
    public void setTitle(String title){
        Title.setText(title);
    }
    public void setDesc(String desc){
        Desc.setText(desc);
    }

}
