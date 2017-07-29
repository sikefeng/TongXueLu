package com.sikefeng.tongxuelu.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.entity.IndexInfo;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class ManagerActivity extends Activity {

    private List<IndexInfo> bookList;
    private BookAdapter bookAdapter;
    private ListView listView;
    private Button btn_add;
    public  static boolean isRefresh=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        listView=(ListView)findViewById(R.id.listview);
        btn_add=(Button)findViewById(R.id.btn_add);

        loadData();//加载数据
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerActivity.this,AddActivity.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(ManagerActivity.this,UpdateActivity.class);
                intent.putExtra("book",(IndexInfo)bookAdapter.getItem(position));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRefresh){
            loadData();
        }
    }

    private void loadData() {
        BmobQuery<IndexInfo> query=new BmobQuery<IndexInfo>();
        query.findObjects(this, new FindListener<IndexInfo>() {
            @Override
            public void onSuccess(List<IndexInfo> list) {
                bookList=  list;
                bookAdapter=new BookAdapter(ManagerActivity.this,bookList);
                listView.setAdapter(bookAdapter);
            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }

}
