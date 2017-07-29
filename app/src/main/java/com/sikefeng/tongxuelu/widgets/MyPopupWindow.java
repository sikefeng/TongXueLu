package com.sikefeng.tongxuelu.widgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;


/**
 * Created by sikefeng on 2016/9/1.
 */
public class MyPopupWindow extends PopupWindow {

    private Context context;
    //弹出的视图对象
    private View popView;

    private TextView tvNei;
    private TextView tvWai;
    private TextView tvCancel;

    public MyPopupWindow(Context context){
    
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.popup_list_layout,null);

        //设置参数
        //加载视图
        this.setContentView(popView);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setAnimationStyle(android.R.style.Animation_Dialog);
        //设置点击消失
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //设置背景
        this.setBackgroundDrawable(new ColorDrawable(0000000000));

        initView();
    }



    private void initView(){
        tvNei = (TextView) popView.findViewById(R.id.tv_change_ip_nei);
        tvWai = (TextView) popView.findViewById(R.id.tv_change_ip_wai);
        tvCancel = (TextView) popView.findViewById(R.id.tv_change_ip_cancel);
        tvNei.setOnClickListener(new IpOnClickListener());
        tvWai.setOnClickListener(new IpOnClickListener());
        tvCancel.setOnClickListener(new IpOnClickListener());
    }

    class IpOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v == tvNei){//修改处内ip

            }else if(v == tvWai){//修改处外ip

            }else if(v == tvCancel){//取消
                dismiss();
            }
        }
    }

}