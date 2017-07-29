package com.sikefeng.tongxuelu.activity.activitygroup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.BaiduActivity;
import com.sikefeng.tongxuelu.activity.CloseActivityClass;
import com.sikefeng.tongxuelu.activity.user.ForgetPasswdActicity;
import com.sikefeng.tongxuelu.activity.user.User;
import com.sikefeng.tongxuelu.baseactivity.BaseActivity;
import com.sikefeng.tongxuelu.bmobutils.BmobUtils;
import com.sikefeng.tongxuelu.dialog.CommonDialogView;
import com.sikefeng.tongxuelu.download.DownloadManager;
import com.sikefeng.tongxuelu.download.DownloadService;
import com.sikefeng.tongxuelu.screenlock.PasswordSwitcher;
import com.sikefeng.tongxuelu.utils.AppUtils;
import com.sikefeng.tongxuelu.utils.NetworkUtils;

import java.io.File;
import java.text.SimpleDateFormat;

import cn.bmob.v3.BmobUser;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_lock, btn_update, btn_refresh, btn_baidu, btn_fankui, btn_diaocha;
    private TextView btn_back;
    private User loginUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting_layout);
        CloseActivityClass.activities.add(this);
        loginUser = BmobUser.getCurrentUser(this, User.class); // 获取已登录对象和信息
        initView();//初始化视图

		/* * 下载进度条的设置 */
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setTitle("提示");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在下载，请耐心等候！");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        Button bt_exit = (Button) findViewById(R.id.bt_exit);
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitDialog();
            }
        });
    }// *****************************

    //退出提示对话框
    private void ExitDialog() {
        CommonDialogView commonDialogView = new CommonDialogView(SettingActivity.this);
        String time = new SimpleDateFormat("MM-dd hh:mm").format(System.currentTimeMillis());
        commonDialogView.showDialog(SettingActivity.this, true, null, "提示:", time, "是否退出当前账号?", "确定", "取消");
        commonDialogView.setmOnDialogClickLister(new CommonDialogView.OnDialogClickLister() {
            @Override
            public void onOk() {
                BmobUtils.exitLoginUser(SettingActivity.this);
            }
            @Override
            public void onCancal() {

            }
        });
    }

    public void downloadFile(String downUrl){
        final String sdcardDir = Environment.getExternalStorageDirectory() + "/TongXueLu/Download/";
        File dir = new File(sdcardDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        HttpUtils http = new HttpUtils();
        HttpHandler handler = http.download(downUrl,
                sdcardDir+"同学录.apk",
                true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {
                    @Override
                    public void onStart() {
                        progressDialog.show();
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        progressDialog.setProgress((int)(current*100/total));
                        System.out.println("progress="+ current*100/total);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        System.out.println("downloaded:" + responseInfo.result.getPath());
                        progressDialog.dismiss();
                        Toast.makeText(SettingActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                        installApk(responseInfo.result.getPath());
                    }


                    @Override
                    public void onFailure(HttpException error, String msg) {
                        System.out.println("fail msg="+msg);
                        progressDialog.dismiss();
                        if (msg.contains("maybe the file has downloaded completely")){
                            installApk(sdcardDir+"同学录.apk");
                        }
                    }
                });
    }
    //更新完成后安装新版app
    private void installApk(String path){
        File apkfile = new File(path);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        startActivity(intent);
    }
    public void initView() {
        btn_back = (TextView) findViewById(R.id.activity_setting_back);
        btn_fankui = (Button) findViewById(R.id.btn_fankui);
        btn_diaocha = (Button) findViewById(R.id.btn_diaocha);
        btn_baidu = (Button) findViewById(R.id.btn_baidu);
        btn_update = (Button) findViewById(R.id.bt_update);
        btn_refresh = (Button) findViewById(R.id.bt_refresh);
        btn_lock = (Button) findViewById(R.id.bt_lock);

        btn_fankui.setOnClickListener(this);
        btn_diaocha.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_baidu.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);
        btn_lock.setOnClickListener(this);
        SharedPreferences sp = SettingActivity.this.getSharedPreferences("appmsg", MODE_PRIVATE);
        int version = sp.getInt("appVersion", 1);
        if (version > AppUtils.getCurrentVersion(SettingActivity.this)) {
            btn_refresh.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_diaocha:
                Intent intent = new Intent(SettingActivity.this, BaiduActivity.class);
                String url = "https://www.wenjuan.com/s/beuyya/?share=1&newpublish=1";
                intent.putExtra("wenjuan", true);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.btn_fankui:
                Toast.makeText(getApplicationContext(), "请联系开发者QQ:2300586308", Toast.LENGTH_LONG).show();
                break;
            case R.id.activity_setting_back:
                SettingActivity.this.finish();
                break;
            case R.id.btn_baidu:
                startActivity(new Intent(this, BaiduActivity.class));
                break;
            case R.id.bt_refresh:
                if (NetworkUtils.isNetworkConnected(SettingActivity.this)) {
                    SharedPreferences sp = SettingActivity.this.getSharedPreferences("appmsg", MODE_PRIVATE);
                    int version = sp.getInt("appVersion", 1);
                    String appUrl = sp.getString("appUrl", "");
                    if (version > AppUtils.getCurrentVersion(SettingActivity.this)) {
                        showNoticeDialog(appUrl);
                    } else {
                        Toast.makeText(SettingActivity.this, "当前已是最新版本！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SettingActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_lock:
                startActivity(new Intent(this, PasswordSwitcher.class));
                break;
            case R.id.bt_update:
                Intent intent_update = new Intent(this, ForgetPasswdActicity.class);
                intent_update.putExtra("index", 0);
                startActivity(intent_update);
                break;
        }
    }

    private void showNoticeDialog(final String url) {
        AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).setTitle("软件更新提示：")
                .setMessage("软件已更新至最新版本，请下载更新！").setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        download(url);
                        downloadFile(url);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void download(String url) {
        String sdcardDir = Environment.getExternalStorageDirectory()
                + "/TongXueLu/Download/";
        File dir = new File(sdcardDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        DownloadManager downloadManager = DownloadService.getDownloadManager(SettingActivity.this);
        try {
            downloadManager.addNewDownload(url,
                    "同学录.apk",
                    sdcardDir + "同学录.apk",
                    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                    false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                    null);
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
    }


}//*****************************************************************
