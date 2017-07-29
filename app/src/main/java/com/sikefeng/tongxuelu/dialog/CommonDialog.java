package com.sikefeng.tongxuelu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;


public class CommonDialog extends Dialog {

	private TextView textTitle;
	private TextView text_left;
	private TextView text_right;
	private View.OnClickListener listener;

	public CommonDialog(Context context) {
		super(context);
	}

	public CommonDialog(Context context, int theme,View.OnClickListener listener) {
		super(context, theme);
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_dialog_layout);
		initView();
	}

	private void initView() {
		textTitle = (TextView) findViewById(R.id.dialog_content);
		text_left = (TextView) findViewById(R.id.txt_no);
		text_right = (TextView) findViewById(R.id.txt_yes);
		text_left.setOnClickListener(listener);
		text_right.setOnClickListener(listener);
	}

	/**
	 * 对话框的标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		textTitle.setText(title);
	}

	public void setLeft(String leftText) {
		text_left.setText(leftText);
	}

	public void setRight(String rightText) {
		text_right.setText(rightText);
	}

}
