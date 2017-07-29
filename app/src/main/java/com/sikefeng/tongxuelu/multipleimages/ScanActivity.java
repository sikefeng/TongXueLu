package com.sikefeng.tongxuelu.multipleimages;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.baseactivity.BaseActivity;

import uk.co.senab.photoview.PhotoView;

public class ScanActivity extends BaseActivity {
    private TextView tv_back;
    private PhotoView photoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        tv_back=(TextView) findViewById(R.id.tv_back);
        photoView=(PhotoView)findViewById(R.id.photoView);
        Intent intent = this.getIntent();
        String path = intent.getStringExtra("path");
        System.out.println("path="+path);
        photoView.setImageBitmap(BitmapFactory.decodeFile(path));
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
