package com.zcj.wei_shi_360.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.zcj.wei_shi_360.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactActivity extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> resulelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ListView listView = (ListView) findViewById(R.id.lv_list);
        resulelist = readContact();
        listView.setAdapter(new SimpleAdapter(this, resulelist, R.layout.contact_list_item, new String[]{"name", "phone"}, new int[]{R.id.tv_name, R.id.tv_phone}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone=resulelist.get(position).get("phone");
                phone.replaceAll("-"," ");
                phone.replaceAll(" ","");
                Intent intent = new Intent();
                intent.putExtra("phone",phone);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    private ArrayList<HashMap<String, String>> readContact() {
        //首先，从raw_contacts中读取联系人的id（contact_id)
        //其次，根据contact_id从data中读取联系人的电话和名称
        //然后，根据mimetype来区分哪个是联系人，哪个是电话号码
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        Uri rawContactUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        //getContentResolver() 通过这个查询实际上都是通过查询view
        Cursor rawContactCursor = getContentResolver().query(rawContactUri, new String[]{"contact_id"},"contact_id is not null", null, null);//记得加"contact_id is not null"
        if (rawContactCursor != null) {
            while (rawContactCursor.moveToNext()) {
                String contactId = rawContactCursor.getString(0);//查询出联系人id
                Cursor dataCursor = getContentResolver().query(dataUri, new String[]{"data1", "mimetype"}, "contact_id=?", new String[]{contactId}, null);
                if (dataCursor.getCount() != 0) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    while (dataCursor.moveToNext()) {
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);
                        if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                            map.put("phone", data1);
                        }
                        if (mimetype.equals("vnd.android.cursor.item/name")) {
                            map.put("name", data1);
                        }
                    }

                    list.add(map);
                }
                dataCursor.close();
            }
            rawContactCursor.close();
        }

        return list;
    }

}
