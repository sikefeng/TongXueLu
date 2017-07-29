package com.sikefeng.tongxuelu.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.adapter.CommonAdapter;
import com.sikefeng.tongxuelu.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ColorActivity extends Activity {
    private ListView listView;
    private Myadapter myadapter;
    private List<String[]> lists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        listView=(ListView)findViewById(R.id.listView);
        lists=new ArrayList<String[]>();
        initColor();
        myadapter=new Myadapter(ColorActivity.this,lists,R.layout.color_item);
        listView.setAdapter(myadapter);
    }


    public class Myadapter extends CommonAdapter<String[]> {

        public Myadapter(Context mContext, List<String[]> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, String[] s) {
            Button button1=holder.getView(R.id.button1);
            Button button2=holder.getView(R.id.button2);
            Button button3=holder.getView(R.id.button3);
            holder.setText(R.id.textView1,s[0]);
            holder.setText(R.id.textView2,s[1]);
            holder.setText(R.id.textView3,s[2]);
            button1.setBackgroundColor(Color.parseColor(s[0]));
            button2.setBackgroundColor(Color.parseColor(s[1]));
            button3.setBackgroundColor(Color.parseColor(s[2]));
        }
    }
         private void initColor(){
             lists.add(new String[]{"#FFB6C1","#FFC0CB","#DC143C"});
             lists.add(new String[]{"#FFF0F5","#DB7093","#FF69B4"});
             lists.add(new String[]{"#C71585","#DA70D6","#D8BFD8"});
             lists.add(new String[]{"#DDA0DD","#EE82EE","#FF00FF"});
             lists.add(new String[]{"#FF00FF","#8B008B","#800080"});
             lists.add(new String[]{"#BA55D3","#9400D3","#9932CC"});
             lists.add(new String[]{"#4B0082","#8A2BE2","#9370DB"});
             lists.add(new String[]{"#7B68EE","#6A5ACD","#483D8B"});
             lists.add(new String[]{"#E6E6FA","#F8F8FF","#0000FF"});
             lists.add(new String[]{"#0000CD","#191970","#00008B"});
             lists.add(new String[]{"#000080","#4169E1","#6495ED"});
             lists.add(new String[]{"#B0C4DE","#778899","#708090"});
             lists.add(new String[]{"#1E90FF","#F0F8FF","#4682B4"});
             lists.add(new String[]{"#87CEFA","#87CEEB","#00BFFF"});
             lists.add(new String[]{"#ADD8E6","#B0E0E6","#AFEEEE"});
             lists.add(new String[]{"#5F9EA0","#F0FFFF","#E0FFFF"});
             lists.add(new String[]{"#00FFFF","#00FFFF","#00CED1"});
             lists.add(new String[]{"#2F4F4F","#008B8B","#008080"});
             lists.add(new String[]{"#48D1CC","#20B2AA","#40E0D0"});
             lists.add(new String[]{"#7FFFD4","#66CDAA","#00FA9A"});
             lists.add(new String[]{"#F5FFFA","#00FF7F","#3CB371"});
             lists.add(new String[]{"#2E8B57","#F0FFF0","#90EE90"});
             lists.add(new String[]{"#98FB98","#8FBC8F","#32CD32"});
             lists.add(new String[]{"#00FF00","#228B22","#008000"});
             lists.add(new String[]{"#006400","#7FFF00","#7CFC00"});
             lists.add(new String[]{"#ADFF2F","#556B2F","#9ACD32"});
             lists.add(new String[]{"#6B8E23","#F5F5DC","#FAFAD2"});
             lists.add(new String[]{"#FFFFF0","#FFFFE0","#FFFF00"});
             lists.add(new String[]{"#808000","#BDB76B","#FFFACD"});
             lists.add(new String[]{"#EEE8AA","#F0E68C","#FFD700"});
             lists.add(new String[]{"#FFF8DC","#DAA520","#B8860B"});
             lists.add(new String[]{"#FFFAF0","#FDF5E6","#F5DEB3"});
             lists.add(new String[]{"#FFE4B5","#FFA500","#FFEFD5"});
             lists.add(new String[]{"#FFEBCD","#FFDEAD","#FAEBD7"});
             lists.add(new String[]{"#D2B48C","#DEB887","#FFE4C4"});
             lists.add(new String[]{"#FF8C00","#FAF0E6","#CD853F"});
             lists.add(new String[]{"#FFDAB9","#F4A460","#D2691E"});
             lists.add(new String[]{"#8B4513","#FFF5EE","#A0522D"});
             lists.add(new String[]{"#FFA07A","#FF7F50","#FF4500"});
             lists.add(new String[]{"#E9967A","#FF6347","#F08080"});
             lists.add(new String[]{"#FFE4E1","#FA8072","#FFFAFA"});
             lists.add(new String[]{"#BC8F8F","#CD5C5C","#FF0000"});
             lists.add(new String[]{"#A52A2A","#B22222","#8B0000"});
             lists.add(new String[]{"#800000","#FFFFFF","#F5F5F5"});
             lists.add(new String[]{"#DCDCDC","#D3D3D3","#C0C0C0"});
             lists.add(new String[]{"#A9A9A9","#808080","#696969"});
         }

}
