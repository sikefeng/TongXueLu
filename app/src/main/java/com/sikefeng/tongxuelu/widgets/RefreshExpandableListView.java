package com.sikefeng.tongxuelu.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;

import java.util.Date;

public class RefreshExpandableListView extends ExpandableListView  {

    private final static int RELEASE_To_REFRESH = 0;// 下拉过程的状态值
    private final static int PULL_To_REFRESH = 1; // 从下拉返回到不刷新的状态值
    private final static int REFRESHING = 2;// 正在刷新的状态值
    private final static int DONE = 3;
    private final static int LOADING = 4;

    // 实际的padding的距离与界面上偏移距离的比例
    private final static int RATIO = 3;
    private LayoutInflater inflater;

    // ListView头部下拉刷新的布局
    private LinearLayout headerView;
    private TextView lvHeaderTipsTv;
    private TextView lvHeaderLastUpdatedTv;
    private ImageView lvHeaderArrowIv;
    private ProgressBar lvHeaderProgressBar;

    // 定义头部下拉刷新的布局的高度
    private int headerContentHeight;

    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;

    private int startY;
    private int state;
    private boolean isBack;

    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean isRecored;

    private OnRefreshListener refreshListener;

    private boolean isRefreshable;

    public boolean isRefreshable() {
        return isRefreshable;
    }

    public void setRefreshable(boolean refreshable) {
        isRefreshable = refreshable;
    }

    public RefreshExpandableListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setCacheColorHint(context.getResources().getColor(R.color.transparent));
        inflater = LayoutInflater.from(context);
        headerView = (LinearLayout) inflater.inflate(R.layout.refresh_head, null);
        lvHeaderTipsTv = (TextView) headerView
                .findViewById(R.id.head_tipsTextView);
        lvHeaderLastUpdatedTv = (TextView) headerView
                .findViewById(R.id.head_lastUpdatedTextView);

        lvHeaderArrowIv = (ImageView) headerView
                .findViewById(R.id.head_arrowImageView);
        // 设置下拉刷新图标的最小高度和宽度
        lvHeaderArrowIv.setMinimumWidth(70);
        lvHeaderArrowIv.setMinimumHeight(50);

        lvHeaderProgressBar = (ProgressBar) headerView
                .findViewById(R.id.head_progressBar);
        measureView(headerView);
        headerContentHeight = headerView.getMeasuredHeight();
        // 设置内边距，正好距离顶部为一个负的整个布局的高度，正好把头部隐藏
        headerView.setPadding(0, -1 * headerContentHeight, 0, 0);
        // 重绘一下
        headerView.invalidate();
        // 将下拉刷新的布局加入ListView的顶部
        addHeaderView(headerView, null, false);


        // 设置旋转动画事件
        animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);

        // 一开始的状态就是下拉刷新完的状态，所以为DONE
        state = DONE;
        // 是否正在刷新
        isRefreshable = false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isRefreshable) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!isRecored) {
                        isRecored = true;
                        startY = (int) ev.getY();// 手指按下时记录当前位置
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (state != REFRESHING && state != LOADING) {
                        if (state == PULL_To_REFRESH) {
                            state = DONE;
                            changeHeaderViewByState();
                        }
                        if (state == RELEASE_To_REFRESH) {
                            state = REFRESHING;
                            changeHeaderViewByState();
                            onLvRefresh();
                        }
                    }
                    isRecored = false;
                    isBack = false;

                    break;

                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) ev.getY();
                    if (!isRecored) {
                        isRecored = true;
                        startY = tempY;
                    }
                    if (state != REFRESHING && isRecored && state != LOADING) {
                        // 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
                        // 可以松手去刷新了
                        if (state == RELEASE_To_REFRESH) {
                            setSelection(0);
                            // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                            if (((tempY - startY) / RATIO < headerContentHeight)// 由松开刷新状态转变到下拉刷新状态
                                    && (tempY - startY) > 0) {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            }
                            // 一下子推到顶了
                            else if (tempY - startY <= 0) {// 由松开刷新状态转变到done状态
                                state = DONE;
                                changeHeaderViewByState();
                            }
                        }
                        // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                        if (state == PULL_To_REFRESH) {
                            setSelection(0);
                            // 下拉到可以进入RELEASE_TO_REFRESH的状态
                            if ((tempY - startY) / RATIO >= headerContentHeight) {// 由done或者下拉刷新状态转变到松开刷新
                                state = RELEASE_To_REFRESH;
                                isBack = true;
                                changeHeaderViewByState();
                            }
                            // 上推到顶了
                            else if (tempY - startY <= 0) {// 由DOne或者下拉刷新状态转变到done状态
                                state = DONE;
                                changeHeaderViewByState();
                            }
                        }
                        // done状态下
                        if (state == DONE) {
                            if (tempY - startY > 0) {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            }
                        }
                        // 更新headView的size
                        if (state == PULL_To_REFRESH) {
                            headerView.setPadding(0, -1 * headerContentHeight
                                    + (tempY - startY) / RATIO, 0, 0);

                        }
                        // 更新headView的paddingTop
                        if (state == RELEASE_To_REFRESH) {
                            headerView.setPadding(0, (tempY - startY) / RATIO
                                    - headerContentHeight, 0, 0);
                        }

                    }
                    break;

                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState() {
        switch (state) {
            case RELEASE_To_REFRESH:
                lvHeaderArrowIv.setVisibility(View.VISIBLE);
                lvHeaderProgressBar.setVisibility(View.GONE);
                lvHeaderTipsTv.setVisibility(View.VISIBLE);
                lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);

                lvHeaderArrowIv.clearAnimation();// 清除动画
                lvHeaderArrowIv.startAnimation(animation);// 开始动画效果

                lvHeaderTipsTv.setText("松开刷新");
                break;
            case PULL_To_REFRESH:
                lvHeaderProgressBar.setVisibility(View.GONE);
                lvHeaderTipsTv.setVisibility(View.VISIBLE);
                lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
                lvHeaderArrowIv.clearAnimation();
                lvHeaderArrowIv.setVisibility(View.VISIBLE);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack) {
                    isBack = false;
                    lvHeaderArrowIv.clearAnimation();
                    lvHeaderArrowIv.startAnimation(reverseAnimation);

                    lvHeaderTipsTv.setText("下拉刷新");
                } else {
                    lvHeaderTipsTv.setText("下拉刷新");
                }
                break;

            case REFRESHING:

                headerView.setPadding(0, 0, 0, 0);

                lvHeaderProgressBar.setVisibility(View.VISIBLE);
                lvHeaderArrowIv.clearAnimation();
                lvHeaderArrowIv.setVisibility(View.GONE);
                lvHeaderTipsTv.setText("正在刷新...");
                lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
                break;
            case DONE:
                headerView.setPadding(0, -1 * headerContentHeight, 0, 0);

                lvHeaderProgressBar.setVisibility(View.GONE);
                lvHeaderArrowIv.clearAnimation();
                lvHeaderArrowIv.setImageResource(R.drawable.default_ptr_flip);
                lvHeaderTipsTv.setText("下拉刷新");
                lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
                break;
        }
    }

    // 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
    private void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,
                params.width);
        int lpHeight = params.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setonRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        isRefreshable = true;
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    public void onRefreshComplete() {
        state = DONE;
        lvHeaderLastUpdatedTv.setText("最近更新:" + new Date().toLocaleString());
        changeHeaderViewByState();
    }

    private void onLvRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }


}
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Handler;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.animation.LinearInterpolator;
//import android.view.animation.RotateAnimation;
//import android.widget.ExpandableListView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListAdapter;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.android.tongxuelu.R;
//import com.tongxuelu.utils.SPUtils;
//import com.tongxuelu.utils.TimeUtil;
//
//
//public class RefreshExpandableListView extends ExpandableListView  {
//    private static final int REFRESH_COMPLETE = 0X110;
//    private Context mcontext;
//    @Override
//    public void setAdapter(ListAdapter adapter) {
//        // TODO Auto-generated method stub
//        super.setAdapter(adapter);
//    }
//
//    //松开刷新
//    private final static int RELEASE_TO_REFRESH=0;//
//    private final static int PULL_TO_REFRESH=1;//下拉刷新
//    private final static int REFRESHING =2;//正在刷新
//    private final static int DONE = 3;
//    private final static int RATIO=3;//实际的padding的距离与界面 上偏移距离 的比例
//    private LayoutInflater inflater;
//    private LinearLayout headLayout;//头linearlayout
//    private TextView tipsTextview;
//    private TextView lastUpdatedTextView;
//    private ImageView arrowImageView;//箭头的图标
//
//    private ProgressBar progressBar;
//
//    private RotateAnimation animation;
//    // 反转动画
//    private RotateAnimation reverseAnimation;
//
//    private int headContentWidth;//头部的宽度
//
//    private LinearLayout headView;
//    private int headContentHeight;
//
//    /** 手势按下的起点位置 */
//    private int startY;
//    private int firstItemIndex;
//
//    private int state;
//
//    private boolean isBack;
//
//    private boolean isRefreshable;
//
//
//    public RefreshExpandableListView(Context context) {
//        super(context);
//        // TODO Auto-generated constructor stub
//        init(context);
//    }
//    public RefreshExpandableListView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        // TODO Auto-generated constructor stub
//        init(context);
//    }
//
//    public void onClick(DialogInterface dialog, int which) {
//        // TODO Auto-generated method stub
//
//    }
//    public void init(Context context)
//    {
//        mcontext=context;
//        inflater =LayoutInflater.from(context);
//        headView =(LinearLayout) inflater.inflate(R.layout.refresh_head, null);
//        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);//箭头
//        arrowImageView.setMinimumWidth(70);
//        arrowImageView.setMinimumHeight(50);
//        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
//        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
//        lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);
//
//        headView.measure(0, 0);
//        headContentHeight=headView.getMeasuredHeight();
//        headContentWidth=headView.getMeasuredWidth();
//
//        headView.setPadding(0, -headContentHeight, 0, 0);//把headview隐藏到顶部
//        headView.invalidate();//刷新界面
//
//        addHeaderView(headView,null,false);
//
//        animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//        animation.setInterpolator(new LinearInterpolator());
//        animation.setDuration(250);
//        animation.setFillAfter(true);
//
//        reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//        reverseAnimation.setInterpolator(new LinearInterpolator());
//        reverseAnimation.setDuration(200);
//        reverseAnimation.setFillAfter(true);
//
//        state = DONE;
//        isRefreshable = false;
//    }
//
//    /**
//     * 设置触摸事件 总的思路就是
//     *
//     * 1 ACTION_DOWN：记录起始位置
//     *
//     * 2 ACTION_MOVE：计算当前位置与起始位置的距离，来设置state的状态
//     *
//     * 3 ACTION_UP：根据state的状态来判断是否下载
//     */
//    public boolean onTouchEvent(MotionEvent event)
//    {
//        isRefreshable = true;
//        if (isRefreshable) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN://按下屏幕
//                    System.out.println("按下屏");
//                    if (firstItemIndex==0) {
//                        startY=(int)event.getY();
//                        System.out.println("记录down下当前的位置");
//                    }
//                    break;
//
//                case MotionEvent.ACTION_MOVE: //移动屏幕
//                    System.out.println("移动下屏");
//                    int tempY=(int)event.getY();
//                    if (state ==PULL_TO_REFRESH) {
//                        setSelection(0);//很重要
//                        //下拉到可以release_to_refresh的状态
//                        if ((tempY-startY)/RATIO>=headContentHeight) {
//                            state=RELEASE_TO_REFRESH;
//                            isBack=true;
//                            changeHeaderViewByState();
//                        }
//                        //上推到顶了
//                        else if(tempY-startY<=0){
//                            state =DONE;
//                            changeHeaderViewByState();
//                        }
//                        headView.setPadding(0, -headContentHeight + (tempY - startY) / RATIO, 0, 0);
//                    }
//                    if (state == RELEASE_TO_REFRESH) {
//                        setSelection(0);
//                        // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
//                        if (((tempY - startY) / RATIO < headContentHeight) && (tempY - startY) > 0) {
//                            state = PULL_TO_REFRESH;
//                            changeHeaderViewByState();
////                        Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
//                        }
//                        headView.setPadding(0, -headContentHeight + (tempY - startY) / RATIO, 0, 0);
//                    }
//                    // done状态下
//                    if (state == DONE) {
//                        if (tempY - startY > 0) {
//                            state = PULL_TO_REFRESH;
//                            changeHeaderViewByState();
//                        }
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                    System.out.println("ACTION_UP");
//                    if (state != REFRESHING) {
//                        // 不在刷新状态
//                        if (state == PULL_TO_REFRESH) {
//                            state = DONE;
//                            changeHeaderViewByState();
////                        Log.v(TAG, "下拉刷新状态，到done状态");
//                        }
//                        if (state == RELEASE_TO_REFRESH) {
//                            state = REFRESHING;
//                            changeHeaderViewByState();
//                            isRefreshable = true;
////                        Log.v(TAG, "松开刷新状态，到done状态");
//                        }
//                    }
//                    isBack = false;
//                    break;
//
//            }
//        }
//        return super.onTouchEvent(event);
//
//    }
//    //当状态改变时候，调用 该方法，以更新界面
//    private void changeHeaderViewByState()
//    {
//        switch (state) {
//            case RELEASE_TO_REFRESH:
//                arrowImageView.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
//                tipsTextview.setVisibility(View.VISIBLE);
//
//                arrowImageView.clearAnimation();
//                arrowImageView.startAnimation(animation);
//
//                tipsTextview.setText("松开刷新");
//
////            Log.v(TAG, "当前状态，松开刷新");
//                break;
//            case PULL_TO_REFRESH:
//                progressBar.setVisibility(View.GONE);
//                tipsTextview.setVisibility(View.VISIBLE);
//                arrowImageView.clearAnimation();
//                arrowImageView.setVisibility(View.VISIBLE);
//                tipsTextview.setText("下拉刷新");
//                // 是RELEASE_To_REFRESH状态转变来的
//                if (isBack) {
//                    isBack = false;
//                    arrowImageView.startAnimation(reverseAnimation);
//                }
//                break;
//
//            case REFRESHING:
//                headView.setPadding(0, 0, 0, 0);
//                progressBar.setVisibility(View.VISIBLE);
//                arrowImageView.clearAnimation();
//                arrowImageView.setVisibility(View.GONE);
//                tipsTextview.setText("正在刷新...");
//                if (onRefreshListener!=null){
//                    onRefreshListener.onRefreshing();
//                    mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
//                }
//
//                break;
//            case DONE:
//                headView.setPadding(0, -headContentHeight, 0, 0);
//                progressBar.setVisibility(View.GONE);
//                arrowImageView.clearAnimation();
//                arrowImageView.setImageResource(R.drawable.default_ptr_flip);
//                tipsTextview.setText("下拉刷新");
//                Long time=(Long)SPUtils.get(mcontext,"time",Long.valueOf(System.currentTimeMillis()));
//                if (time!=null&&time>0){
//                    lastUpdatedTextView.setText(TimeUtil.converTime(time));
//                }
//                break;
//        }
//    }
//    //刷新完成
//    public  void onRefreshComplete() {
//        state = DONE;
//        changeHeaderViewByState();
//    }
//    //自定义下拉刷新监听接口
//    private PullToOnRefreshListener onRefreshListener;
//
//    public void setOnRefreshListener(PullToOnRefreshListener onRefreshListener) {
//        this.onRefreshListener = onRefreshListener;
//    }
//    public interface PullToOnRefreshListener{
//        void onRefreshing();
//        void onRefreshFinished();
//    }
//    private Handler mHandler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case REFRESH_COMPLETE:
//                    if (onRefreshListener!=null){
//                        onRefreshListener.onRefreshFinished();
//                        onRefreshComplete();
//                    }
//                    break;
//            }
//        }
//    };
//}