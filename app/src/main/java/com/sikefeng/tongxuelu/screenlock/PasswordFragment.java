package com.sikefeng.tongxuelu.screenlock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.MainActivity;

public class PasswordFragment extends Fragment {
	private TextView tv_passwd;
	private LockPatternView lockPatternView;
	private RelativeLayout layout;
	private Button submit_btn;
	private String passwordStr;
	private static final String ARG_TYPE = "type";
	public static final String TYPE_SETTING = "setting";
	public static final String TYPE_CHECK = "check";
	private SharedPreferences sp;
	public PasswordFragment() {
		
	}

	public static PasswordFragment newInstance(String value) {
		PasswordFragment fragment = new PasswordFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_TYPE, value);
		fragment.setArguments(bundle);
		return fragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp=getActivity().getSharedPreferences("lock",getActivity().MODE_PRIVATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_password, container, false);
		
		tv_passwd = (TextView) view.findViewById(R.id.text);
		lockPatternView = (LockPatternView) view.findViewById(R.id.lock);
		layout = (RelativeLayout) view.findViewById(R.id.layout);
		submit_btn = (Button) view.findViewById(R.id.submit);
		if (getArguments() != null) {
			if (TYPE_SETTING.equals(getArguments().getString(ARG_TYPE))) {
				submit_btn.setVisibility(View.VISIBLE);
				layout.setVisibility(View.VISIBLE);
			}
		}
		submit_btn.setEnabled(false);
		submit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(passwordStr)) {
					Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
					submit_btn.setEnabled(false);
				} else {
					SharedPreferences.Editor editor=sp.edit();
					editor.putBoolean("isLock",true);
					editor.putString("password",passwordStr);
					editor.commit();
					Toast.makeText(getActivity(), "密码设置成功", Toast.LENGTH_SHORT).show();
					getActivity().finish();
				}

			}
		});
		lockPatternView.setOnPatterChangeListener(new LockPatternView.OnPatterChangeLister() {

			@Override
			public void onPatterChangeListener(String password) {
				// TODO Auto-generated method stub
				passwordStr = password;
				if (!TextUtils.isEmpty(passwordStr)) {
					if (getArguments() != null) {
						submit_btn.setEnabled(true);
						if (TYPE_CHECK.equals(getArguments().getString(ARG_TYPE))) {
							String psswd=sp.getString("password","");
							if (passwordStr.equals(psswd)) {
								//Toast.makeText(getActivity(), "密码正确", 1000).show();
								startActivity(new Intent(getActivity(), MainActivity.class));
								getActivity().finish();
							} else {
								tv_passwd.setText("图案绘制错误！");
								lockPatternView.resetPoint();
							}
						}
					}
				} else {
					tv_passwd.setText("至少输入5个图案");
				}

			}

			@Override
			public void onPatterStart(boolean isStart) {
				// TODO Auto-generated method stub
				if (isStart) {
					tv_passwd.setText("请绘制图案");

				}
			}

			@Override
			public void onReSetPoint(boolean isRet) {
				if (isRet) {
					submit_btn.setEnabled(false);
				}
			}
		});
		return view;
	}
}
