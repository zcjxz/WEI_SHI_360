package com.zcj.wei_shi_360.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.bean.AppInfo;
import com.zcj.wei_shi_360.dbUtils.AppLockDao;
import com.zcj.wei_shi_360.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZCJ on 2016/7/28.
 */
public class UnLockFragment extends Fragment {

    private TextView tv_unlock_size;
    private ListView listView;
    private List<AppInfo> appInfos;
    private List<AppInfo> unLockLists;
    private AppLockDao dao;
    private UnlockAdapter unlockAdapter;
    private TranslateAnimation translateAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        translateAnimation.setDuration(300);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_unlock, container, false);
        tv_unlock_size = (TextView) rootView.findViewById(R.id.tv_unlock_size);
        listView = (ListView) rootView.findViewById(R.id.list_view);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        appInfos = AppInfoProvider.getAppInfos(getActivity());
        dao = new AppLockDao(getContext());
        unLockLists = new ArrayList<>();
        for (AppInfo info:appInfos){
            if (dao.find(info.getPackName())){

            }else{
                unLockLists.add(info);
            }
        }
        unlockAdapter = new UnlockAdapter();
        listView.setAdapter(unlockAdapter);
    }

    private class UnlockAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            tv_unlock_size.setText("未加锁应用："+unLockLists.size());
            return unLockLists.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final AppInfo info=unLockLists.get(position);
            ViewHolder holder=null;
            if (convertView==null){
                convertView = View.inflate(getActivity(), R.layout.item_unlock, null);
                holder=new ViewHolder();
                holder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
                holder.iv_unLock= (ImageView) convertView.findViewById(R.id.iv_unLock);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv_icon.setImageDrawable(info.getIcon());
            holder.tv_name.setText(info.getName());
            final View finalConvertView = convertView;
            holder.iv_unLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalConvertView.startAnimation(translateAnimation);
                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            dao.add(info.getPackName());
                            unLockLists.remove(position);
                            unlockAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
            });
            return convertView;
        }
    }
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        ImageView iv_unLock;
    }
}
