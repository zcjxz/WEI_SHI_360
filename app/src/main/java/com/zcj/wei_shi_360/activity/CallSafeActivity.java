package com.zcj.wei_shi_360.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.adapter.MyBaseAdapter;
import com.zcj.wei_shi_360.bean.BlackNumberInfo;
import com.zcj.wei_shi_360.dbUtils.BlackNumberDao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class CallSafeActivity extends AppCompatActivity {

    private ListView list_view;
    private List<BlackNumberInfo> data=new ArrayList<BlackNumberInfo>();
    private LinearLayout ll_pd;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ll_pd.setVisibility(View.INVISIBLE);
//            tv_page_number.setText(mCurrentNumber+"/"+totalPage);
            adapter.notifyDataSetChanged();
            loaded=true;
        }
    };

    //当前页面
//    private int mCurrentNumber=1;
    //每一页显示20条数据
    private int mPageSize=20;
//    private TextView tv_page_number;
    private BlackNumberDao dao;
    //一共有多少页
    private int totalPer;
    private int totalNumber;
//    private EditText et_page_number;

    private int startIndex=0;
    private CellSafeAdapter adapter;
    private boolean loaded=true;
    private ImageView iv_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe_new);
        dao = new BlackNumberDao(CallSafeActivity.this);
        //获取总条数
        totalNumber = dao.getTotalNumber();
        totalPer=dao.getTotalNumber()%mPageSize==0?dao.getTotalNumber()/mPageSize:dao.getTotalNumber()/mPageSize+1;
        initUI();
        initData();
    }

    private void initData() {

        new Thread(){
            @Override
            public void run() {

                data.addAll(dao.findPi(startIndex,mPageSize));

                handler.sendMessage(Message.obtain());
            }
        }.start();
    }

    private void initUI() {
        ll_pd = (LinearLayout) findViewById(R.id.ll_pd);
        ll_pd.setVisibility(View.VISIBLE);
        list_view = (ListView) findViewById(R.id.list_view);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(CallSafeActivity.this);
                View view = View.inflate(CallSafeActivity.this, R.layout.dialog_add_black, null);
                final AlertDialog dialog = builder.create();
                 final EditText et_blackNumber;
                 Button bt_add_confirm;
                 Button bt_add_cancel;
                final CheckBox cb_phone = (CheckBox) view.findViewById(R.id.cb_phone);
                final CheckBox cb_sms = (CheckBox) view.findViewById(R.id.cb_sms);

                et_blackNumber = (EditText) view.findViewById(R.id.et_blackNumber);
                bt_add_confirm = (Button) view.findViewById(R.id.bt_add_confirm);
                bt_add_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String blackNumber = et_blackNumber.getText().toString().trim();
                        if (TextUtils.isEmpty(blackNumber)){
                            Toast.makeText(CallSafeActivity.this, "号码不能为空", Toast.LENGTH_SHORT).show();
                        }else{
                            String mode="";
                            boolean phone_checked = cb_phone.isChecked();
                            boolean sms_checked = cb_sms.isChecked();
                            if (phone_checked||sms_checked){
                                if (phone_checked&&sms_checked){
                                    mode="1";
                                }else if(phone_checked){
                                    mode="2";
                                }else if (sms_checked){
                                    mode="3";
                                }
                                BlackNumberInfo blackNumberInfo=new BlackNumberInfo();
                                blackNumberInfo.setNumber(blackNumber);
                                blackNumberInfo.setMode(mode);
                                data.add(0,blackNumberInfo);
                                dao.add(blackNumber,mode);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(CallSafeActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(CallSafeActivity.this, "请选择拦截类型", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                bt_add_cancel = (Button) view.findViewById(R.id.bt_add_cancel);
                bt_add_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setView(view);
                dialog.show();
            }
        });
//        tv_page_number = (TextView) findViewById(R.id.tv_page_number);
//        et_page_number = (EditText) findViewById(R.id.et_page_number);
        adapter = new CellSafeAdapter(data,CallSafeActivity.this);

        list_view.setAdapter(adapter);

        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (loaded) {
                    int loadtotal = totalItemCount;
                    int lastItem = list_view.getLastVisiblePosition();
                    if ((lastItem + 1) == totalItemCount) {
                        if (totalItemCount > 0) {
                            int currentpage = totalItemCount % mPageSize == 0 ? totalItemCount / mPageSize : totalItemCount / mPageSize + 1;
                            if (currentpage < totalPer) {
                                startIndex = totalItemCount;
                                loaded=false;
                                initData();
                            }
                        }
                    }
                }
            }
        });


    }

    private class CellSafeAdapter extends MyBaseAdapter<BlackNumberInfo>{

        private LayoutInflater inflater;


        public CellSafeAdapter(List lists, Context mContext) {
            super(lists, mContext);
            inflater=  LayoutInflater.from(CallSafeActivity.this);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView=inflater.inflate(R.layout.item_cell_safe,null);
                viewHolder.tv_number= (TextView) convertView.findViewById(R.id.tv_number);
                viewHolder.tv_mode= (TextView) convertView.findViewById(R.id.tv_mode);
                viewHolder.iv_delete= (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_number.setText(lists.get(position).getNumber());
            viewHolder.tv_mode.setText(getModeText(lists.get(position).getMode()));
            viewHolder.iv_delete.setImageResource(R.drawable.iv_black_number);
            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {

                private AlertDialog alertDialog;

                
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(CallSafeActivity.this);
                    builder.setTitle("移除黑名单？");
                    builder.setMessage("确定把"+data.get(position).getNumber()+"从黑名单中移除？");
                    builder.setPositiveButton("移除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dao.delete(data.get(position).getNumber());
                            data.remove(position);
                            adapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                }
            });
            return convertView;
        }
        public class ViewHolder{
            public TextView tv_number;
            public TextView tv_mode;
            public ImageView iv_delete;
        }
        private String getModeText(String mode){
            String text="";
            switch (mode){
                case "1":
                    text="拦截电话+短信";
                    break;
                case "2":
                    text="拦截电话";
                    break;
                case "3":
                    text="拦截短信";
                    break;
            }
            return text;
        }
    }
    //上一页
//    public void prePage(View view){
//        if (mCurrentNumber<=1){
//            Toast.makeText(CallSafeActivity.this, "已经是第一页了", Toast.LENGTH_SHORT).show();
//        }else{
//            mCurrentNumber--;
//            initData();
//        }
//    }
    //下一页
//    public void nextPage(View view){
//        if (mCurrentNumber>=totalPage){
//            Toast.makeText(CallSafeActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
//        }else{
//            mCurrentNumber++;
//            initData();
//        }
//    }
    //跳转
//    public void jump(View view){
//       String str_page_number = et_page_number.getText().toString().trim();//trim（）是去除前后空白的函数
//        if(TextUtils.isEmpty(str_page_number)){
//            Toast.makeText(CallSafeActivity.this, "请输入跳转页码", Toast.LENGTH_SHORT).show();
//        }else{
//            int jump_page_number = Integer.parseInt(str_page_number);
//            if(jump_page_number>0 && jump_page_number<totalPage){
//                mCurrentNumber=jump_page_number;
//                initData();
//            }else{
//                Toast.makeText(CallSafeActivity.this, "请输入正确的页码", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

}
