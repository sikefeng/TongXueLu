package com.sikefeng.tongxuelu.test;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.dialog.ListItemDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActivity extends Activity{

    private int[] imagesId=new int[]{R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
    private String[] titles=new String[]{"添加","添加","添加","添加","添加"};
    private List<Map<String,Object>> lists=new ArrayList<Map<String,Object>>();
    private SimpleAdapter simpleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        for (int i = 0; i <imagesId.length ; i++) {
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("image",imagesId[i]);
            map.put("title",titles[i]);
            lists.add(map);
        }
        simpleAdapter=new SimpleAdapter(TestActivity.this,lists,R.layout.item_layout,new String[]{"title","image"},new int[]{R.id.textView,R.id.imageView});
       Button button=(Button)findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItemDialog listItemDialog=new ListItemDialog(TestActivity.this);
                listItemDialog.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setTime();
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }
    private int hourOfDay, minute;
    private boolean isTime = false;
    private void setTime() {
        Calendar calendar = Calendar.getInstance();
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(TestActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int min) {
                if (isTime) {
                    hourOfDay = hour;
                    minute = min;
                    System.out.println("1Time: " + hourOfDay + ":" + minute);
                    isTime = false;
                }
                System.out.println("2Time: " + hourOfDay + ":" + minute);
            }
        }, hourOfDay, minute, true);
        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isTime = true;
                        dialog.dismiss();
                    }
                });

        timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        timePickerDialog.show();

    }

}