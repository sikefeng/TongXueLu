package com.sikefeng.tongxuelu.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.fragment.MineFragment;
import com.sikefeng.tongxuelu.baseactivity.BaseActivity;
import com.sikefeng.tongxuelu.bmobutils.BmobUtils;
import com.sikefeng.tongxuelu.dialog.LoadingDialog;
import com.smile.filechoose.api.ChooserType;
import com.smile.filechoose.api.ChosenFile;
import com.smile.filechoose.api.FileChooserListener;
import com.smile.filechoose.api.FileChooserManager;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserMsgActivity extends BaseActivity implements FileChooserListener {
	private ImageView imageView;
	private TextView tv_update_touxiang,tv_nickname,tv_truename,tv_phone,tv_email,tv_sex,tv_age,tv_birthday,tv_work,
			tv_company,tv_school;
	private TextView tv_qq,tv_xingzuo;
	private FileChooserManager fm;
	private User loginUser;
	private LoadingDialog loadingDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mine_layout);
		this.key=5;
		setEnterSwichLayout();
		initListView();
		loadingDialog=LoadingDialog.getInstance();
	}//******************************************************
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initListView();
	}
	@Override
	public void onError(String s) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onFileChosen( final ChosenFile chosenfile) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	uploadFile(chosenfile.getFilePath());
            }
        });
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChooserType.REQUEST_PICK_FILE && resultCode == RESULT_OK) {
            if (fm == null) {
                fm = new FileChooserManager(this);
                fm.setFileChooserListener(this);
            }
          
            fm.submit(requestCode, data);
        }
    }
	
	public void pickFile() {
        fm = new FileChooserManager(this);
        fm.setFileChooserListener(this);
        try {
            fm.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	private void initListView(){
		tv_update_touxiang=(TextView)findViewById(R.id.update_touxiang);
		tv_age=(TextView)findViewById(R.id.tv_age);
		tv_email=(TextView)findViewById(R.id.tv_email);
		tv_nickname=(TextView)findViewById(R.id.tv_nickname);
		tv_truename=(TextView)findViewById(R.id.tv_truename);
		tv_phone=(TextView)findViewById(R.id.tv_phone);
		tv_sex=(TextView)findViewById(R.id.tv_sex);
		imageView=(ImageView)findViewById(R.id.user_image);
		tv_company=(TextView)findViewById(R.id.tv_company);
		tv_birthday=(TextView)findViewById(R.id.tv_birthday);
		tv_school=(TextView)findViewById(R.id.tv_school);
		tv_work=(TextView)findViewById(R.id.tv_work);
		tv_qq=(TextView)findViewById(R.id.tv_qq);
		tv_xingzuo=(TextView)findViewById(R.id.tv_collection);
		TextView tv_back=(TextView)findViewById(R.id.tv_back);
		Button edit_Button=(Button)findViewById(R.id.edit_btn);
		edit_Button.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			startActivity(new Intent(UserMsgActivity.this,UpdateUserActivity.class));
			}
		});
		tv_back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			setExitSwichLayout();
			}
		});
		
    	loginUser= BmobUser.getCurrentUser(getApplicationContext(), User.class);
		tv_nickname.setText(loginUser.getUsername());
		tv_truename.setText(loginUser.getTruename());
		tv_phone.setText(loginUser.getPhoneno());
		tv_age.setText(loginUser.getAge());
		tv_email.setText(loginUser.getEmail());
		tv_sex.setText(loginUser.getSex());
		tv_birthday.setText(loginUser.getBirthday());
		tv_qq.setText(loginUser.getQQ());
		tv_company.setText(loginUser.getCompany());
		tv_work.setText(loginUser.getWork());
		tv_school.setText(loginUser.getSchool());
		tv_xingzuo.setText(loginUser.getXingzuo());

 	
		BmobFile bmobFile=loginUser.getIcon();
		if (bmobFile!=null) {
            ImageLoader.getInstance().displayImage(bmobFile.getFileUrl(getApplicationContext()),imageView);
		}
		tv_update_touxiang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showToast("请选择图片文件");
				pickFile();
			}
		});
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showToast("请选择图片文件");
				pickFile();
			}
		});

	}


	private boolean isImageFile(String path){
		String[] formats=new String[]{".png",".jpeg",".jpg",".gif",".PNG",".JPEG",".JPG",".GIF"};
		for (String format:formats){
			if (path.contains(format)){
				return true;
			}
		}
		return false;
	}
	private void uploadFile(String path){
		if (isImageFile(path)){
			Toast.makeText(getApplicationContext(),path, Toast.LENGTH_SHORT).show();
			final BmobFile bmobFile=new BmobFile(new File(path));
			bmobFile.upload(UserMsgActivity.this, new UploadFileListener() {
				@Override
				public void onSuccess() {
					User user=new User();
					user.setIcon(bmobFile);
					user.update(getApplicationContext(), loginUser.getObjectId(), new UpdateListener() {
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(UserMsgActivity.this));
							ImageLoader.getInstance().displayImage(bmobFile.getFileUrl(getApplicationContext()),imageView);
							if (loginUser.getIcon()!=null)
							deletefile(loginUser.getIcon().getUrl());
							MineFragment.isReLoad=true;
						}

						@Override
						public void onFailure(int arg0, String s) {
							// TODO Auto-generated method stub
							showToast(BmobUtils.showErrorMsg(arg0));
						}
					});
				}

				@Override
				public void onStart() {
					super.onStart();
					loadingDialog.showDialogForLoading(UserMsgActivity.this,true,"正在保存头像...");
					loadingDialog.setOnDialogClosedListener(new LoadingDialog.OnDialogClosedListener() {
						@Override
						public void closeDialog() {

						}
					});
				}

				@Override
				public void onProgress(Integer value) {
					super.onProgress(value);
					loadingDialog.setProgress(String.valueOf(value)+"%");
				}

				@Override
				public void onFinish() {
					super.onFinish();
					loadingDialog.hideDialogForLoading();
				}

				@Override
				public void onFailure(int i, String s) {
					showToast("图片上传失败"+BmobUtils.showErrorMsg(i));
				}
			});
		}else {
			toast("图片格式不正确");
			toast("请选择图片文件");
		}
	}
	public void deletefile(String url){

		String[] urls=new String[]{url};
		BmobFile.deleteBatch(UserMsgActivity.this, urls, new DeleteBatchListener() {
			@Override
			public void done(String[] strings, BmobException e) {
				if (e == null) {
//					toast("全部删除成功");
				} else {
					if (strings != null) {
//						toast("删除失败个数：" + strings.length + "," + e.toString());
					} else {
//						toast("全部文件删除失败：" + e.getErrorCode() + "," + e.toString());
					}
				}
			}
		});
	}


			public boolean onKeyDown(int keyCode, KeyEvent event) {// 按返回键时退出Activity的Activity特效动画

				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					setExitSwichLayout();
					return true;
				}
				return super.onKeyDown(keyCode, event);
			}


}
