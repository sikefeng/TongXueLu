package com.sikefeng.tongxuelu.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.dialog.DialogView;
import com.sikefeng.tongxuelu.download.DownloadListActivity;
import com.sikefeng.tongxuelu.download.DownloadManager;
import com.sikefeng.tongxuelu.download.DownloadService;
import com.sikefeng.tongxuelu.utils.FileUtils;

import java.io.File;

public class BaiduActivity extends Activity implements View.OnClickListener {
    private String fileName = "";
    private WebView webView;
    private String url;
    private ProgressBar bar;
    private ImageButton go, back, zhuye, menu;
    private DownloadManager downloadManager;
    private PopupWindow popWindow;
    private DialogView dialogView;
    private TextView tv_refresh, tv_share, tv_download,tv_exit,tv_back;
    private LinearLayout linearlayout;
//    private CustomProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_baidu);
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        bar = (ProgressBar) findViewById(R.id.bar);
        go = (ImageButton) findViewById(R.id.go);
        zhuye = (ImageButton) findViewById(R.id.zhuye);
        back = (ImageButton) findViewById(R.id.back);
        menu = (ImageButton) findViewById(R.id.caidan);
        linearlayout=(LinearLayout)findViewById(R.id.linearlayout);

        go.setOnClickListener(this);
        menu.setOnClickListener(this);
        zhuye.setOnClickListener(this);
        back.setOnClickListener(this);
        MyWeb();

        downloadManager = DownloadService.getDownloadManager(BaiduActivity.this);
        dialogView = new DialogView(BaiduActivity.this);
        dialogView.setmOnDialogClickLister(new DialogView.OnDialogClickLister() {
            @Override
            public void onOk() {
                // 调用自己的下载方式
                download(url);
                dialogView.hideDialogForLoading();
            }
            @Override
            public void onCancal() {
                dialogView.hideDialogForLoading();
            }
        });
        setWebViewType();//app问卷调查
    }// &&&&&&&&&************************************************************

    private void openPopupWindow(View v) {
        if (popWindow == null) {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_menu, null);
            tv_exit = (TextView) view.findViewById(R.id.tv_exit);
            tv_share = (TextView) view.findViewById(R.id.tv_share);
            tv_download = (TextView) view.findViewById(R.id.tv_download);
            tv_refresh = (TextView) view.findViewById(R.id.tv_refresh);
            tv_exit.setOnClickListener(this);
            tv_refresh.setOnClickListener(this);
            tv_share.setOnClickListener(this);
            tv_download.setOnClickListener(this);
            popWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
            popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
            popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popWindow.setOutsideTouchable(true);
            popWindow.setTouchable(true);
            popWindow.update();
            popWindow.setBackgroundDrawable(new ColorDrawable(0000000000));
        }
        popWindow.showAtLocation(findViewById(R.id.line), Gravity.BOTTOM, 0, v.getHeight());
    }



    private void download(String url) {
        String sdcardDir = Environment.getExternalStorageDirectory()
                + "/TongXueLu/Download/";
        File dir = new File(sdcardDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            downloadManager.addNewDownload(url,
                    FileUtils.getFileNameByURL(url),
                    sdcardDir + FileUtils.getFileNameByURL(url),
                    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                    false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                    null);
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    private void setWebViewType() {
        Intent intent = this.getIntent();
        if (intent != null) {
            final String msg = intent.getStringExtra("url");
            if (intent.getBooleanExtra("wenjuan",false)){
                webView.loadUrl(msg);
                linearlayout.setVisibility(View.GONE);
            }else if (intent.getBooleanExtra("indexinfo",false)){
                webView.loadUrl(msg);
                linearlayout.setVisibility(View.GONE);
            }
            Button btn_open_brower=(Button)findViewById(R.id.button_open);
            btn_open_brower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (msg==null || msg.equals("")){
                        openBrower("http://www.hao123.com/");
                    }else{
                        openBrower(msg);
                    }
                }
            });

        }
    }
    private void openBrower(String url) {
        Uri uri = Uri.parse(url);
        Intent intent =new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(Menu.NONE, Menu.FIRST + 2, 2, "刷新");
        menu.add(Menu.NONE, Menu.FIRST + 5, 5, "分享");
        menu.add(Menu.NONE, Menu.FIRST + 6, 6, "退出");
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST + 1:
                webView.loadUrl("http://connect.qq.com");
                break;
            case Menu.FIRST + 2:
                webView.reload();// 刷新
                break;
            case Menu.FIRST + 3:
                Toast.makeText(this, "帮助菜单被点击了", Toast.LENGTH_LONG).show();
                break;
            case Menu.FIRST + 4:
                Toast.makeText(this, "添加菜单被点击了", Toast.LENGTH_LONG).show();
                break;
            case Menu.FIRST + 5:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); // 纯文本
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, url);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                break;
            case Menu.FIRST + 6:
                BaiduActivity.this.finish();
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.back:
                if (webView.canGoBack()) {
                    webView.goBack();
                }
                break;
            case R.id.go:
                if (webView.canGoForward()) {
                    webView.goForward();
                }
                break;
            case R.id.caidan:
//			openOptionsMenu();// 打开系统菜单
                openPopupWindow(v);
                break;
            case R.id.zhuye:
                webView.loadUrl("http://www.hao123.com/");
                break;
            case R.id.tv_refresh:
                webView.reload();// 刷新
                popWindow.dismiss();
                break;
            case R.id.tv_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); // 纯文本
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, url);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
                popWindow.dismiss();
                break;
            case R.id.tv_download:
                startActivity(new Intent(BaiduActivity.this,DownloadListActivity.class));
                popWindow.dismiss();
                break;
            case R.id.tv_exit:
                BaiduActivity.this.finish();
                popWindow.dismiss();
                break;
        }
    }

    private void MyWeb() {
//        dialog = CustomProgressDialog.createDialog(this);
        // TODO Auto-generated method stub
        webView = (WebView) findViewById(R.id.web);
        // 加载内部资源
        // webview.loadUrl("file:///android_asset/hao123.html");
        // 加载外部资源
        webView.loadUrl("http://www.hao123.com/");

        // 覆盖通过第三方浏览器或系统浏览器打开网页，使得网页在WebView中打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 如果返回值是true网页在WebView中打开，如果是false则在第三方浏览器或系统浏览器打开网页
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                dialog.dismiss();
            }
        });
        // 启用支持JavaScript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        // WebView加载页面优先使用缓存加载
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        // 显示网页加载进度
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // textView.setText(webView.getUrl());
                // textView.requestFocus();
                url = webView.getUrl();
                if (newProgress == 100) {
                    bar.setVisibility(View.GONE);
//                    dialog.dismiss();
                } else {
                    bar.setVisibility(View.VISIBLE);
                    bar.setProgress(newProgress);
                }
            }

        });
        webView.setDownloadListener(new MyDownloader());
    }

    // 网页的返回设置
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack(); // 返回上一页
                bar.setVisibility(View.INVISIBLE);
                return true;
            } else {
                BaiduActivity.this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // webview下载功能
    public class MyDownloader implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            // TODO Auto-generated method stub
            dialogView.showDialog(BaiduActivity.this, true, BitmapFactory.decodeResource(getResources(), R.mipmap.start), "下载提示:", "是否确定下载文件？", "确定", "取消");

        }
    }

    private void initWeb() {
//        setJavaScriptEnabled(true); //支持js脚步
//        setPluginsEnabled(true); //支持插件 目前新版本已不支持
//        setUseWideViewPort(false); //将图片调整到适合webview的大小
//        setSupportZoom(true); //支持缩放
//        setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
//        supportMultipleWindows(); //多窗口
//        setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
//        setAllowFileAccess(true); //设置可以访问文件
//        setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
//        setBuiltInZoomControls(true); //设置支持缩放
//        setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        setLoadsImagesAutomatically(true); //支持自动加载图片
//
//        WebViewClient 的方法总结:
//        doUpdateVisitedHistory(WebView view, String url, boolean isReload)(更新历史记录)
//        onFormResubmission(WebView view, Message dontResend, Message resend) (应用程序重新请求网页数据)
//        onLoadResource(WebView view, String url) 在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
//        onPageStarted(WebView view, String url, Bitmap favicon) 这个事件就是开始载入页面调用的，
//        通常我们可以在这设定一个loading的页面，告诉用户程序在等待网络响应。
//        onPageFinished(WebView view, String url) 在页面加载结束时调用。同样道理，我们知道一个页面载入完成，于是我们可以关闭loading 条，
//        切换程序动作。
//        onReceivedError(WebView view, int errorCode, String description, String failingUrl)(报告错误信息)
//        onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm)（获取返回信息授权请求）
//        onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
//        重写此方法可以让webview处理https请求。
//        onScaleChanged(WebView view, float oldScale, float newScale)(WebView发生改变时调用)
//        onUnhandledKeyEvent(WebView view, KeyEvent event)（Key事件未被加载时调用）
//        shouldOverrideKeyEvent(WebView view, KeyEvent event) 重写此方法才能够处理在浏览器中的按键事件。
//        shouldOverrideUrlLoading(WebView view, String url)
//        在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。这个函数我们可以做很多操作，
//        比如我们读取到某些特殊的URL，于是就可以不打开地址，取消这个操作，进行预先定义的其他操作，这对一个程序是非常必要的。
    }
}// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

