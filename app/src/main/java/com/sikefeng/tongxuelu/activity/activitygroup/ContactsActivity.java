package com.sikefeng.tongxuelu.activity.activitygroup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.EditPersonActivity;
import com.sikefeng.tongxuelu.activity.MainActivity;
import com.sikefeng.tongxuelu.activity.MessageActivity;
import com.sikefeng.tongxuelu.activity.RegisterActivity;
import com.sikefeng.tongxuelu.dialog.CommonDialogView;
import com.sikefeng.tongxuelu.dialog.ListDialog;
import com.sikefeng.tongxuelu.diray.DataPersonImpl;
import com.sikefeng.tongxuelu.entity.Person;
import com.sikefeng.tongxuelu.utils.ToastUtils;
import com.sikefeng.tongxuelu.widgets.MyPopupWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;

public class ContactsActivity extends Activity implements AbsListView.OnScrollListener,SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeLayout;
    private static final int REFRESH_COMPLETE = 0X110;
    private ImageButton imageButton2;
    private TextView tv_title;
    private List<String> group_list;
    private List<List<Person>> item_list;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contacts);
        dataPersonImpl = new DataPersonImpl(ContactsActivity.this);
        bitmapUtils = new BitmapUtils(ContactsActivity.this, imagesPath);
        bitmapUtils.configDefaultBitmapMaxSize(100, 100);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (MainActivity.isOpenedmenu) {
                    sendBroadcast(new Intent("actionkkk"));
                } else {
                    MainActivity.isOpenedmenu = true;
                    sendBroadcast(new Intent("actionkkk"));
                }
            }
        });

        final CommonDialogView commonDialogView = new CommonDialogView(ContactsActivity.this);

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
//                openPopupWindow(view);
                startActivity(new Intent(ContactsActivity.this, MainActivity.class));

            }
        });
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
            initView2();
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int screenWidth = dm.widthPixels;
        final int screenHeight = dm.heightPixels-50;


    }// *****************************************************************************************************
    private void showChangeIPView(View view){
        //创建自定义的PopupWindow
        MyPopupWindow ipView = new MyPopupWindow(this);
        //居中显示
        ipView.showAtLocation(view, Gravity.CENTER,0,0);
    }
    private void initListDialog(final String flag, final int index) {
        final ListDialog listDialog = new ListDialog(ContactsActivity.this);
        listDialog.show();
        final List<String> list = Arrays.asList(new String[]{"添加联系人", "编辑联系人", "删除联系人"});
        listDialog.initDialog(ContactsActivity.this, list, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        listDialog.cancel();
                        Intent intent1 = new Intent(ContactsActivity.this, RegisterActivity.class);
                        intent1.putExtra("flag", flag);
                        startActivityForResult(intent1, 100);
                        ToastUtils.showShort(ContactsActivity.this, flag);
                        break;
                    case 1:
                        listDialog.cancel();
                        Intent editiIntent = new Intent(ContactsActivity.this, EditPersonActivity.class);
                        editiIntent.putExtra("id", index);
                        startActivity(editiIntent);
                        break;
                    case 2:
                        listDialog.cancel();
                        DeleteDialog(index);
                        break;
                }
            }
        });

    }
    //删除联系人
    private void DeleteDialog(final int id){
          CommonDialogView commonDialogView=new CommonDialogView(ContactsActivity.this);
          String time=new SimpleDateFormat("MM-dd hh:mm").format(System.currentTimeMillis());
          commonDialogView.showDialog(ContactsActivity.this,true,null,"提示:",time,"是否删除该联系人?","确定","取消");
          commonDialogView.setmOnDialogClickLister(new CommonDialogView.OnDialogClickLister() {
            @Override
            public void onOk() {
                String url = dataPersonImpl.findPerson(id).getTouxiang();
                if (!TextUtils.isEmpty(url)) {
                    BmobFile.deleteBatch(ContactsActivity.this, new String[]{url}, new DeleteBatchListener() {
                        @Override
                        public void done(String[] strings, BmobException e) {

                        }
                    });
                }
                dataPersonImpl.deletePerson(id);
                reLoad();//重新加载数据
                Toast.makeText(getApplicationContext(), "成功移除!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancal() {

            }
        });
    }

    private void initView2() {
        expandAdapter = new MyExpandableAdapter(ContactsActivity.this, group_list, item_list);
        expandableList = (ExpandableListView) findViewById(R.id.expandableListView);
        View v = new View(this);
        expandableList.addHeaderView(v);
        expandableList.setAdapter(expandAdapter);
        expandableList.setGroupIndicator(null);  //取消默认指示图标
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

        view_flotage = (RelativeLayout) findViewById(R.id.group_item_layout);
        view_flotage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_flotage.setVisibility(View.GONE);
                expandableList.collapseGroup(the_group_expand_position);
                expandableList.setSelectedGroup(the_group_expand_position);
            }
        });
        group_content = (TextView) findViewById(R.id.group_name);
        tv_count = (TextView) findViewById(R.id.tv_count);
        group_indicator = (ImageView) findViewById(R.id.group_indicator);
        group_indicator.setImageResource(R.drawable.indicator_expanded);
        // 设置滚动事件
        expandableList.setOnScrollListener(this);
        /**
         * 监听子节点点击事件
         */
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Person person = (Person) expandAdapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(ContactsActivity.this, MessageActivity.class);
                intent.putExtra("id", person.getId());
                startActivity(intent);
                return false;
            }
        });


    }
    @Override
    public void onRefresh() {
        reLoad(); //重新加载数据
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
    @Override
    protected void onRestart() {
        super.onRestart();
        if (isReLoad) {
            reLoad(); //重新加载数据
            isReLoad = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReLoad) {
            reLoad(); //重新加载数据
            isReLoad = false;
        }
    }

    //重新加载数据
    private void reLoad() {
        dataPersonImpl.findAllPersons();
        item_list.clear();
        item_list.add(dataPersonImpl.findTeacher());
        item_list.add(dataPersonImpl.findClassmate());
        item_list.add(dataPersonImpl.findFriends());
        expandAdapter.notifyDataSetChanged();
    }

    private void openPopupWindow(View v) {

        View view = getLayoutInflater().inflate(R.layout.popupwindow, null);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(new ColorDrawable(0000000000));
        popupWindow.showAsDropDown(v);

        Button button1, button2, button3;
        button1 = (Button) view.findViewById(R.id.button1);
        button2 = (Button) view.findViewById(R.id.button2);
        button3 = (Button) view.findViewById(R.id.button3);

        button1.setOnClickListener(new MyOnClickListener());
        button2.setOnClickListener(new MyOnClickListener());
        button3.setOnClickListener(new MyOnClickListener());


    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.button1:
                    popupWindow.dismiss();
                    Intent intent1 = new Intent(ContactsActivity.this, RegisterActivity.class);
                    intent1.putExtra("flag", "teacher");
                    startActivityForResult(intent1, 100);
                    break;
                case R.id.button2:
                    popupWindow.dismiss();
                    Intent intent2 = new Intent(ContactsActivity.this, RegisterActivity.class);
                    intent2.putExtra("flag", "classmate");
                    startActivityForResult(intent2, 100);
                    break;
                case R.id.button3:
                    popupWindow.dismiss();
                    Intent intent3 = new Intent(ContactsActivity.this, RegisterActivity.class);
                    intent3.putExtra("flag", "friends");
                    startActivityForResult(intent3, 100);
                    break;

            }
        }

    }


    class MyExpandableAdapter extends BaseExpandableListAdapter {
        private Context mContext;
        private List<String> group_list;
        private List<List<Person>> item_list;
        private int mHideGroupPos = -1;


        public MyExpandableAdapter(Context mContext, List<String> group_list,
                                   List<List<Person>> item_list) {
            super();
            this.mContext = mContext;
            this.group_list = group_list;
            this.item_list = item_list;
        }

        public Object getChild(int groupPosition, int childPosition) {
            return item_list.get(groupPosition).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return item_list.get(groupPosition).size();
        }

        public View getChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ChileViewHolder vHolderchild = null;
            if (convertView == null) {
                convertView = (View) getLayoutInflater().from(mContext).inflate(
                        R.layout.list_item_view, null);
                vHolderchild = new ChileViewHolder();
                vHolderchild.imageView = (ImageView) convertView
                        .findViewById(R.id.icon);
                vHolderchild.tv_name = (TextView) convertView
                        .findViewById(R.id.contact_list_item_name);
                vHolderchild.tv_word = (TextView) convertView
                        .findViewById(R.id.cpntact_list_item_state);
                vHolderchild.relativelayout_item = (RelativeLayout) convertView.findViewById(R.id.relativelayout_item);
                convertView.setTag(vHolderchild);
            } else {
                vHolderchild = (ChileViewHolder) convertView.getTag();
            }
            vHolderchild.tv_name.setText(String.valueOf(item_list
                    .get(groupPosition).get(childPosition).getName()));
            vHolderchild.tv_word.setText(String.valueOf(item_list
                    .get(groupPosition).get(childPosition).getWords()));

            String path = item_list.get(groupPosition).get(childPosition).getTouxiang();
            if (path != null && !TextUtils.isEmpty(path)) {
                bitmapUtils.display(vHolderchild.imageView, String.valueOf(item_list.get(groupPosition).get(childPosition).getTouxiang()));
            } else {
                vHolderchild.imageView.setImageResource(R.drawable.touxiang);
            }
            vHolderchild.relativelayout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Person person = (Person) expandAdapter.getChild(groupPosition, childPosition);
                    Intent intent = new Intent(ContactsActivity.this, MessageActivity.class);
                    intent.putExtra("id", person.getId());
                    startActivity(intent);
                }
            });
            vHolderchild.relativelayout_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    initListDialog(item_list.get(groupPosition).get(childPosition).getFlag(), item_list.get(groupPosition).get(childPosition).getId());
                    return false;
                }
            });

            return convertView;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflaterGroup = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflaterGroup.inflate(R.layout.list_group_view, null);
            }
            TextView title = (TextView) view.findViewById(R.id.group_name);
            TextView tv_counts = (TextView) view.findViewById(R.id.tv_counts);
            tv_counts.setText(getChildrenCount(groupPosition) + "/" + getChildrenCount(groupPosition));
            title.setText(getGroup(groupPosition).toString());
            ImageView image = (ImageView) view.findViewById(R.id.group_indicator);
            if (isExpanded) {
                image.setImageResource(R.drawable.indicator_expanded);
            } else {
                image.setImageResource(R.drawable.indicator_unexpanded);
            }
            return view;
        }

        class GroupViewHolder {
            TextView textView;
            ImageView image;
            TextView tv_counts;
        }

        class ChileViewHolder {
            ImageView imageView;
            TextView tv_name;
            TextView tv_word;
            RelativeLayout relativelayout_item;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public Object getGroup(int groupPosition) {
            return group_list.get(groupPosition);
        }

        public int getGroupCount() {
            return group_list.size();

        }

        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

        public void hideGroup(int groupPos) {
            mHideGroupPos = groupPos;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
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
                View groupView = expandableList.getChildAt(npos
                        - expandableList.getFirstVisiblePosition());
                indicatorGroupHeight = groupView.getHeight();
            }

            if (indicatorGroupHeight == 0) {
                return;
            }

            if (count_expand > 0) {
                the_group_expand_position = groupPos;
//88888888888888888888888888888888888888888888888888888888888888888888888888
                group_content.setText(group_list.get(the_group_expand_position));
                //88888888888888888888888888888888888888888888888888888888888888888888888888

                if (the_group_expand_position != groupPos
                        || !expandableList.isGroupExpanded(groupPos)) {
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
    // 退出应用程序代码
    private long exittime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
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
