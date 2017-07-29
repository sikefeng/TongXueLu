package com.sikefeng.tongxuelu.multipleimages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;

import java.util.List;

public class ShowImageActivity extends Activity {
	private GridView mGridView;
	private ChildAdapter adapter;
	private List<String> list;
	private TextView tv_return;
	private Button saveButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_image_activity);
		tv_return = (TextView) findViewById(R.id.back);
		saveButton = (Button) findViewById(R.id.sava);
		mGridView = (GridView) findViewById(R.id.child_grid);
		list = getIntent().getStringArrayListExtra("data");
		adapter = new ChildAdapter(this, list, mGridView);
		mGridView.setAdapter(adapter);
		tv_return.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				finish();
				return false;
			}
		});
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								Intent intent = new Intent(ShowImageActivity.this,ScanActivity.class);
				intent.putExtra("path", list.get(position));
				startActivity(intent);
			}
		});
//		saveButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if (adapter.getArrayList().size() > 0) {
//					setResult(101);
//					finish();
//				}
//
//			}
//		});
	}// ******************************************

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

		}

		return super.onKeyDown(keyCode, event);
	}
}
