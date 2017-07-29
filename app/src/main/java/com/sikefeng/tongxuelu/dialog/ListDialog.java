package com.sikefeng.tongxuelu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.adapter.CommonAdapter;
import com.sikefeng.tongxuelu.adapter.ViewHolder;

import java.util.List;

/**
 * Created by sikefeng on 2016/8/29.
 */
public class ListDialog extends Dialog {
    private ListView listView;
    private MyAdapter myAdapter;
    private AdapterView.OnItemClickListener onItemClickListener;
    public ListDialog(Context context) {
        super(context, R.style.loading_dialog_style);
    }
    public  void initDialog(Context context,List<String> list,AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener=onItemClickListener;
        myAdapter=new MyAdapter(context,list,R.layout.dialog_list_item);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list_layout);
        listView=(ListView)findViewById(R.id.listView);
    }

    private class MyAdapter extends CommonAdapter<String> {

        private List<String> lists;
        public MyAdapter(Context mContext, List<String> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            lists=mDatas;
        }

        @Override
        public void convert(ViewHolder holder, String s) {
            TextView textView=holder.getView(R.id.tv_title);
            if (holder.getPosition()==0){
                textView.setBackgroundResource(R.drawable.bg_above);
            }else if(holder.getPosition()==lists.size()-1){
                textView.setBackgroundResource(R.drawable.bg_bottom);
            }else{
                textView.setBackgroundResource(R.drawable.bg_center);
            }
            textView.setText(s);
//            holder.setText(R.id.tv_title,s);
        }
    }



}
