package com.zcj.wei_shi_360.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcj.wei_shi_360.R;

/**
 * Created by 曾灿杰 on 2016/1/23.
 */
public class SettingItemView extends RelativeLayout{

    private TextView Title;
    private TextView Desc;
    private CheckBox Status;
    private String NAMESPACE="http://schemas.android.com/apk/res-auto";
    private String title="000";
    private String descOn="111";
    private String descOff="222";

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        int attributeCount = attrs.getAttributeCount();
//        for (int i = 0; i <attributeCount ; i++) {
//            String attributeValue = attrs.getAttributeValue(i);
//            String attributeName = attrs.getAttributeName(i);
//            System.out.println(attributeName+"="+attributeValue);
//        }
        title = attrs.getAttributeValue(NAMESPACE, "item_title");
        descOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        descOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
        initView();
        setTitle(title);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        View.inflate(getContext(), R.layout.view_setting_item,this);
        Title = (TextView) findViewById(R.id.tv_title);
        Desc = (TextView) findViewById(R.id.tv_decs);
        Status = (CheckBox) findViewById(R.id.cb_status);
    }
    public void setTitle(String title){
        Title.setText(title);
    }
    public void setDesc(String desc){
        Desc.setText(desc);
    }
    public void setCheck(boolean status){
        Status.setChecked(status);
        if (status){
            setDesc(descOn);
        }else{
            setDesc(descOff);
        }
    }
    public boolean isChecked(){
        return Status.isChecked();
    }
}
