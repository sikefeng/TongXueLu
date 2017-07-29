package com.sikefeng.tongxuelu.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/** 
 * @ClassName: MyListView 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @Author: JOE
 * @Date: 2015-10-12 上午12:16:51
 */

public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(  Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	

}
