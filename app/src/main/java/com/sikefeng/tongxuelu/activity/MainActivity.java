package com.sikefeng.tongxuelu.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nineoldandroids.view.ViewHelper;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.fragment.ContactsFragment;
import com.sikefeng.tongxuelu.activity.fragment.HomeFragment;
import com.sikefeng.tongxuelu.activity.fragment.MineFragment;
import com.sikefeng.tongxuelu.activity.user.User;
import com.sikefeng.tongxuelu.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * http://blog.csdn.net/lmj623565791/article/details/41531475
 *
 * @author zhy
 */
public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {


    private DrawerLayout mDrawerLayout;
    private TabHost tabHost;
    private SlidingMenu menu;
    public static int indexTab = 0;
    public static boolean isOpenedmenu = false;
    private MediaPlayer player;
    private RelativeLayout relativeLayout = null;
    private float startX;
    private int current = 0;
    private User loginUser = null;
    private Animation left_in, left_out;
    private Animation right_in, right_out;
    private RadioGroup radioGroup;
    private RadioButton radioButton = null;
    private List<Fragment> fragmentList;
    private ViewPager mViewPager;
    private int currenttab = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_layout);
        initView();
        initEvents();
        CloseActivityClass.activities.add(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        radioGroup = (RadioGroup) findViewById(R.id.main_tab_group);
        radioGroup.setOnCheckedChangeListener(this);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        initFragment();
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        current = AppUtils.getCurrentVersion(MainActivity.this);
//		MySlidingMenu(); // 左边侧滑菜单
		IntentFilter intentFilter = new IntentFilter("actionkkk");
		registerReceiver(broadcastReceiver, intentFilter);

    }

    public void OpenRightMenu(View view) {
        mDrawerLayout.openDrawer(Gravity.RIGHT);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
    }
    public void OpenLeftMenu() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
    }


    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.RIGHT);
    }

    private void initFragment() {
        fragmentList = new ArrayList<Fragment>();
        HomeFragment homeFragment = new HomeFragment();
        ContactsFragment contactsFragment = new ContactsFragment();
        MineFragment mineFragment = new MineFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(contactsFragment);
        fragmentList.add(mineFragment);
        mViewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_tab_addExam:
                changeView(0);
                break;
            case R.id.main_tab_myExam:
                changeView(1);
                break;
            case R.id.main_tab_settings:
                changeView(2);
                break;

        }
    }

    /**
     * 定义自己的ViewPager适配器。
     * 也可以使用FragmentPagerAdapter。关于这两者之间的区别，可以自己去搜一下。
     */
    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {

        public MyFrageStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        /**
         * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
         */
        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);//这句话要放在最前面，否则会报错
            //获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
            int currentItem = mViewPager.getCurrentItem();
            if (currentItem == currenttab) {
                return;
            }
            currenttab = mViewPager.getCurrentItem();
            radioButton = (RadioButton) radioGroup.getChildAt(currenttab);
            radioButton.setChecked(true);
        }

    }

    //手动设置ViewPager要显示的视图
    private void changeView(int desTab) {
        mViewPager.setCurrentItem(desTab, true);
    }



    private void initEvents() {
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;
                if (drawerView.getTag().equals("LEFT")) {
                    float leftScale = 1 - 0.3f * scale;
                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                } else {
                    ViewHelper.setTranslationX(mContent, -mMenu.getMeasuredWidth() * slideOffset);
                    ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
                    ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isOpenedmenu = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }


//    private void MySlidingMenu() {
//
//        View view = this.getLayoutInflater().inflate(R.layout.left_menu_layout,
//                null);
//        menu = new SlidingMenu(MainActivity.this);
//        menu.setMode(SlidingMenu.LEFT);// 设置左滑菜单 
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//        menu.setShadowDrawable(R.drawable.shadow);// 设置阴影图片 
//        menu.setShadowWidthRes(R.dimen.shadow_width);// 设置阴影图片的宽度 
//        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu划出时主页面显示的剩余宽度
//        int screenwidth = ScreenUtils.getScreenWidth(MainActivity.this);
//        menu.setBehindWidth(screenwidth - screenwidth / 4);// 设置SlidingMenu菜单的宽度 
//        menu.setFadeDegree(0.35f);// SlidingMenu滑动时的渐变程度 
//        menu.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_CONTENT);// 使SlidingMenu附加在Activity上 
//        menu.setMenu(view);// 设置menu的布局文件 
//        // menu.toggle();//动态判断自动关闭或开启SlidingMenu 
//        menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
//            @Override
//            public void onOpened() {
//                // TODO Auto-generated method stub
//                isOpenedmenu = true;
//                relativeLayout.setAlpha(0.5f);
//            }
//        });
//        // menu.setOnClosedListener(new OnClosedListener() {
//        // @Override
//        // public void onClosed() {
//        // // TODO Auto-generated method stub
//        // linearLayout.setAlpha(1f);
//        // }
//        // });
//        menu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
//
//            @Override
//            public void onClose() {
//                // TODO Auto-generated method stub
//                relativeLayout.setAlpha(1f);
//            }
//        });
//        Button bt_diray = (Button) view.findViewById(R.id.bt_diray);
//        Button bt_refresh = (Button) view.findViewById(R.id.bt_refresh);
//        Button bt_share = (Button) view.findViewById(R.id.bt_share);
//        Button bt_exit = (Button) view.findViewById(R.id.bt_exit);
//        Button bt_baidu = (Button) view.findViewById(R.id.bt_baidu);
//
//        bt_share.setOnClickListener(this);
//        bt_exit.setOnClickListener(this);
//        bt_refresh.setOnClickListener(this);
//        bt_baidu.setOnClickListener(this);
//        bt_diray.setOnClickListener(this);
//
//    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if ("actionkkk".equals(action)) {
                if (isOpenedmenu) {
//                    menu.showMenu();
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
                } else {
//                    menu.showContent();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }



    // 退出应用程序代码
    private long exittime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (MainActivity.isOpenedmenu) {
                MainActivity.isOpenedmenu = false;
                sendBroadcast(new Intent("actionkkk"));
                return false;
            }
            if ((System.currentTimeMillis() - exittime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exittime = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
