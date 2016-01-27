package com.zcj.wei_shi_360.activity;

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

    private ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ListView listView = (ListView) findViewById(R.id.lv_list);
        list = readContact();
        listView.setAdapter(new SimpleAdapter(this, list, R.layout.contact_list_item, new String[]{"name", "phone"}, new int[]{R.id.tv_name, R.id.tv_phone}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone=list.get(position).get("phone");
                Intent intent = new Intent();
                intent.putExtra("phone",phone);
                setResult(0,intent);
                finish();
            }
        });
    }

    private ArrayList<HashMap<String, String>> readContact() {
        //首先，从raw_contacts中读取联系人的id（contact_id)
        //其次，根据contact_id从data中读取联系人的电话和名称
        //然后，根据mimetype来区分哪个是联系人，哪个是电话号码
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        Uri rawContactUri = Uri.parse("context://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("context://com.android.contacts/data");
        Cursor rawContactCursor = getContentResolver().query(rawContactUri, new String[]{"contact_id"}, null, null, null);
        if (rawContactCursor != null) {
            while (rawContactCursor.moveToNext()) {
                String contactId = rawContactCursor.getString(0);//查询出联系人id
                Cursor dataCursor = getContentResolver().query(dataUri, new String[]{"data1", "mimetype"}, "contact_id=?", new String[]{contactId}, null);
                if (dataCursor != null) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    while (dataCursor.moveToNext()) {
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);
                        if (mimetype.equals("vnd.android.cursor.items/phone_v2")) {
                            map.put("phone", data1);
                        }
                        if (mimetype.equals("vnd.android.cursor.items/name")) {
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
