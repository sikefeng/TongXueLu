package com.sikefeng.tongxuelu.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.MainActivity;
import com.sikefeng.tongxuelu.activity.MessageActivity;
import com.sikefeng.tongxuelu.activity.RegisterActivity;
import com.sikefeng.tongxuelu.activity.user.User;
import com.sikefeng.tongxuelu.adapter.MyExpandableAdapter;
import com.sikefeng.tongxuelu.diray.DataPersonImpl;
import com.sikefeng.tongxuelu.entity.Person;
import com.sikefeng.tongxuelu.scan.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by sikefeng on 2016/9/4.
 */
public class ContactsFragment extends BaseFragment implements AbsListView.OnScrollListener,SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeLayout;
    private static final int REFRESH_COMPLETE = 0X110;
    private ImageButton imageButton2;
    private ImageView roundImageView;
    private List<String> group_list;
    private List<List<Person>> item_list;
    private int lastItem = -1;
    private PopupWindow popupWindow;
    private int index = 0;
    private String person_name;
    public static boolean isReLoad = false;
    private String imagesPath = Environment.getExternalStorageDirectory() + "/TongXueLu/HeadImages/";
    private BitmapUtils bitmapUtils;
    private DataPersonImpl dataPersonImpl;
    private List<Person> personList;
    private MyExpandableAdapter expandAdapter;
    private ExpandableListView expandableList;
    private int indicatorGroupHeight;
    private int the_group_expand_position = -1;
    private RelativeLayout view_flotage = null;
    private TextView group_content = null;
    private TextView tv_count;
    private ImageView group_indicator;
    private int count_expand = 0;
    private Map<Integer, Integer> ids = new HashMap<Integer, Integer>();
    public static Person personl;
    private Activity activity;
    private User loginUser;
    private View view_layout;
    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            // remove掉保存的Fragment
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        activity = getActivity();
        loginUser = BmobUser.getCurrentUser(activity, User.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_layout= inflater.inflate(R.layout.activity_contacts, container, false);
        dataPersonImpl = new DataPersonImpl(activity);
        initView(view_layout);
        initData();
        return view_layout;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        expandableList = (ExpandableListView) view.findViewById(R.id.expandableListView);
        roundImageView = (ImageView) view.findViewById(R.id.roundImageView);
        mSwipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.id_swipe_ly);
        imageButton2 = (ImageButton) view.findViewById(R.id.imageButton2);
        group_list = new ArrayList<String>();
        group_list.add("老师");
        group_list.add("同学");
        group_list.add("朋友");
        personList = dataPersonImpl.findAllPersons();
        if (personList != null && personList.size() > 0) {
            item_list = new ArrayList<List<Person>>();
            item_list.add(dataPersonImpl.findTeacher());
            item_list.add(dataPersonImpl.findClassmate());
            item_list.add(dataPersonImpl.findFriends());
        }
        expandAdapter = new MyExpandableAdapter(activity, group_list, item_list);
        View v = new View(activity);
        expandableList.addHeaderView(v);
        if (item_list!=null && item_list.size()>0){
            expandableList.setAdapter(expandAdapter);
            expandableList.setGroupIndicator(null);  //取消默认指示图标
        }
        /**
         * 监听父节点打开的事件
         */
        expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                the_group_expand_position = groupPosition;
                ids.put(groupPosition, groupPosition);
                count_expand = ids.size();
            }
        });
        /**
         * 监听父节点关闭的事件
         */
        expandableList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                ids.remove(groupPosition);
                expandableList.setSelectedGroup(groupPosition);
                count_expand = ids.size();
            }
        });

        view_flotage = (RelativeLayout) view.findViewById(R.id.group_item_layout);
        view_flotage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_flotage.setVisibility(View.GONE);
                expandableList.collapseGroup(the_group_expand_position);
                expandableList.setSelectedGroup(the_group_expand_position);
            }
        });
        group_content = (TextView) view.findViewById(R.id.group_name);
        tv_count = (TextView) view.findViewById(R.id.tv_count);
        group_indicator = (ImageView) view.findViewById(R.id.group_indicator);
        group_indicator.setImageResource(R.drawable.indicator_expanded);
        // 设置滚动事件
        expandableList.setOnScrollListener(ContactsFragment.this);
        /**
         * 监听子节点点击事件
         */
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Person person = (Person) expandAdapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(activity, MessageActivity.class);
                intent.putExtra("id", person.getId());
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        dataPersonImpl = new DataPersonImpl(activity);
        bitmapUtils = new BitmapUtils(activity, imagesPath);
        bitmapUtils.configDefaultBitmapMaxSize(100, 100);
        mSwipeLayout.setOnRefreshListener(ContactsFragment.this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        final BmobFile bmobFile = loginUser.getIcon();
        if (bmobFile != null) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
        }
        roundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (MainActivity.isOpenedmenu) {
                    activity.sendBroadcast(new Intent("actionkkk"));
                } else {
                    MainActivity.isOpenedmenu = true;
                    activity.sendBroadcast(new Intent("actionkkk"));
                }
            }
        });


        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                openPopupWindow(view);

            }
        });

    }


    @Override
    public void onRefresh() {
        dataPersonImpl = new DataPersonImpl(activity);
        initView(view_layout);
        initData();
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    mSwipeLayout.setRefreshing(false);
                    break;
            }
        }
    };

    //重新加载数据
    private void reLoad() {
        dataPersonImpl.findAllPersons();
        if (item_list==null){
            return;
        }
        item_list.clear();
        item_list.add(dataPersonImpl.findTeacher());
        item_list.add(dataPersonImpl.findClassmate());
        item_list.add(dataPersonImpl.findFriends());
        expandAdapter.notifyDataSetChanged();
    }

    private void openPopupWindow(View v) {

        View view = LayoutInflater.from(activity).inflate(R.layout.popupwindow, null);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(new ColorDrawable(0000000000));
        popupWindow.showAsDropDown(v);

        Button button1, button2, button3,button4;
        button1 = (Button) view.findViewById(R.id.button1);
        button2 = (Button) view.findViewById(R.id.button2);
        button3 = (Button) view.findViewById(R.id.button3);
        button4 = (Button) view.findViewById(R.id.button4);

        button1.setOnClickListener(new MyOnClickListener());
        button2.setOnClickListener(new MyOnClickListener());
        button3.setOnClickListener(new MyOnClickListener());
        button4.setOnClickListener(new MyOnClickListener());


    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.button1:
                    popupWindow.dismiss();
                    Intent intent1 = new Intent(activity, RegisterActivity.class);
                    intent1.putExtra("flag", "teacher");
                    startActivityForResult(intent1, 100);
                    break;
                case R.id.button2:
                    popupWindow.dismiss();
                    Intent intent2 = new Intent(activity, RegisterActivity.class);
                    intent2.putExtra("flag", "classmate");
                    startActivityForResult(intent2, 100);
                    break;
                case R.id.button3:
                    popupWindow.dismiss();
                    Intent intent3 = new Intent(activity, RegisterActivity.class);
                    intent3.putExtra("flag", "friends");
                    startActivityForResult(intent3, 100);
                    break;
                case R.id.button4:
                    popupWindow.dismiss();
                    startActivity(new Intent(activity, CaptureActivity.class));
                    break;
            }
        }

    }


    private int getHeight() {
        int showHeight = indicatorGroupHeight;
        int nEndPos = expandableList.pointToPosition(0, indicatorGroupHeight);
        if (nEndPos != AdapterView.INVALID_POSITION) {
            long pos = expandableList.getExpandableListPosition(nEndPos);
            int groupPos = ExpandableListView.getPackedPositionGroup(pos);
            if (groupPos != the_group_expand_position) {
                View viewNext = expandableList.getChildAt(nEndPos
                        - expandableList.getFirstVisiblePosition());
                showHeight = viewNext.getTop();
            }
        }
        return showHeight;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        // 防止三星,魅族等手机第一个条目可以一直往下拉,父条目和悬浮同时出现的问题
        if (firstVisibleItem == 0) {
            view_flotage.setVisibility(View.GONE);
            mSwipeLayout.setEnabled(true);
        }else{
            mSwipeLayout.setEnabled(false);
        }
        // 控制滑动时TextView的显示与隐藏
        int npos = view.pointToPosition(0, 0);
        if (npos != AdapterView.INVALID_POSITION) {
            long pos = expandableList.getExpandableListPosition(npos);
            int childPos = ExpandableListView.getPackedPositionChild(pos);
            final int groupPos = ExpandableListView.getPackedPositionGroup(pos);
            if (childPos == AdapterView.INVALID_POSITION) {
                View groupView = expandableList.getChildAt(npos - expandableList.getFirstVisiblePosition());
                indicatorGroupHeight = groupView.getHeight();
            }

            if (indicatorGroupHeight == 0) {
                return;
            }

            if (count_expand > 0) {
                the_group_expand_position = groupPos;
                group_content.setText(group_list.get(the_group_expand_position));

                if (the_group_expand_position != groupPos || !expandableList.isGroupExpanded(groupPos)) {
                    view_flotage.setVisibility(View.GONE);
                } else {
                    view_flotage.setVisibility(View.VISIBLE);
                    tv_count.setText(expandAdapter.getChildrenCount(groupPos) + "/" + expandAdapter.getChildrenCount(groupPos));
                }
            }
            if (count_expand == 0) {
                view_flotage.setVisibility(View.GONE);
            }
        }

        if (the_group_expand_position == -1) {
            return;
        }
        /**
         * calculate point (0,indicatorGroupHeight)
         */
        int showHeight = getHeight();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view_flotage.getLayoutParams();
        // 得到悬浮的条滑出屏幕多少
        layoutParams.topMargin = -(indicatorGroupHeight - showHeight);
        view_flotage.setLayoutParams(layoutParams);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("---------onAttach()");
    }


    @Override
    public void onStart() {
        super.onStart();
        System.out.println("---------onStart()");
    }
    @Override
    public void onResume() {
        super.onResume();
        loginUser = BmobUser.getCurrentUser(activity, User.class);
        if (isReLoad) {
            isReLoad = false;
            dataPersonImpl = new DataPersonImpl(activity);
            initView(view_layout);
        }
        initData();
        System.out.println("onResume");
    }
    @Override
    public void onPause() {
        super.onPause();
        System.out.println("---------onPause()");
    }
    @Override
    public void onStop() {
        super.onStop();
        System.out.println("---------onStop()");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("---------onDestroyView()");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("---------onDestroy()");
    }


}