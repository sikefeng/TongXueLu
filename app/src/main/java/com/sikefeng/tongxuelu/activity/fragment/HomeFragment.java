package com.sikefeng.tongxuelu.activity.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sikefeng.tongxuelu.DefaultConfig;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.BaiduActivity;
import com.sikefeng.tongxuelu.activity.MainActivity;
import com.sikefeng.tongxuelu.activity.user.User;
import com.sikefeng.tongxuelu.activity.user.UserUtils;
import com.sikefeng.tongxuelu.adapter.CommonAdapter;
import com.sikefeng.tongxuelu.adapter.ViewHolder;
import com.sikefeng.tongxuelu.entity.ImageInfo;
import com.sikefeng.tongxuelu.entity.IndexInfo;
import com.sikefeng.tongxuelu.utils.NetworkUtils;
import com.sikefeng.tongxuelu.utils.SPUtils;
import com.sikefeng.tongxuelu.widgets.ImageCycleView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by sikefeng on 2016/9/4.
 */


public class HomeFragment extends BaseFragment {

    private ImageView roundImageView;
    private Activity activity;
    private PullToRefreshListView listView;
    private MyAdapter myAdapter;
    private ImageCycleView mImageCycleView;
    private static final int REFRESH_COMPLETE = 0X110;
    private static final int REFRESH_FAILED = 0X100;
    private List<IndexInfo> indexInfoList;
    private List<IndexInfo> collections=new ArrayList<IndexInfo>();
    private User localUser=null;
    private Gson gson;
    public static boolean isReLoad = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        gson =new Gson();
        localUser= UserUtils.getLoginUser(activity);
        collections=gson.fromJson(localUser.getColletions(), new TypeToken<List<IndexInfo>>(){}.getType());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);
        initView(view);
        initData();
        initCycleView();
//        initGridView(view);
        return view;
    }
    /*private void initGridView(View view){
      LineGridView gridView=(LineGridView)view.findViewById(R.id.gridView);
       int[] imagesId=new int[]{R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
       final String[] titles=new String[]{"患者信息","开处方","在线咨询","咨询记录","临时处方"};
       List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
       for (int i = 0; i < imagesId.length; i++) {
           Map<String,Object> map=new HashMap<String,Object>();
           map.put("image",imagesId[i]);
           map.put("title",titles[i]);
           mapList.add(map);
       }
       SimpleAdapter simpleAdapter=new SimpleAdapter(activity,mapList,R.layout.grid_item,new String[]{"title","image"},new int[]{R.id.title,R.id.image});
       gridView.setAdapter(simpleAdapter);
       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Toast.makeText(activity, titles[position], Toast.LENGTH_SHORT).show();
               System.out.println("title="+titles[position]);
           }
       });
   }*/
    public void initView(View view) {

        mImageCycleView = (ImageCycleView) view.findViewById(R.id.icv_topView);
        roundImageView = (ImageView) view.findViewById(R.id.roundImageView);
        listView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        listView.setMode(PullToRefreshBase.Mode.BOTH);//两端刷新
        String time = new SimpleDateFormat("hh:mm:ss").format(System.currentTimeMillis());
        ILoadingLayout startLayout = listView.getLoadingLayoutProxy(true, false);
        startLayout.setPullLabel("下拉刷新...");
        startLayout.setRefreshingLabel("正在努力刷新中...");
        startLayout.setReleaseLabel("松开刷新...");
        startLayout.setLastUpdatedLabel("上次刷新时间：" + time);
        ILoadingLayout endLayout = listView.getLoadingLayoutProxy(false, true);
        endLayout.setPullLabel("上拉加载更多...");
        endLayout.setRefreshingLabel("正在加载中...");
        endLayout.setReleaseLabel("松开加载...");
        final BmobFile bmobFile = localUser.getIcon();
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

        listView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(activity, BaiduActivity.class);
                String url = myAdapter.getItem(position - 1).getWebsite();
                intent.putExtra("indexinfo", true);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                refreshData();
            }

        });


    }

    private void refreshData() {
        if (!NetworkUtils.isNetworkConnected(activity)) {
            mHandler.sendEmptyMessage(REFRESH_FAILED);
            Toast.makeText(activity, "网络未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobQuery<IndexInfo> queryIndexInfo = new BmobQuery<IndexInfo>();
        queryIndexInfo.findObjects(activity, new FindListener<IndexInfo>() {
            @Override
            public void onSuccess(List<IndexInfo> list) {
                SPUtils.saveObject(activity, "indexinfos", list);
                BmobQuery<ImageInfo> queryImageInfo = new BmobQuery<ImageInfo>();
                queryImageInfo.findObjects(activity, new FindListener<ImageInfo>() {
                    @Override
                    public void onSuccess(List<ImageInfo> list) {
                        SPUtils.saveObject(activity, "imageinfos", list);
                        mHandler.sendEmptyMessage(REFRESH_COMPLETE);
                    }

                    @Override
                    public void onError(int i, String s) {
                        mHandler.sendEmptyMessage(REFRESH_FAILED);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                mHandler.sendEmptyMessage(REFRESH_FAILED);
            }

        });
    }

    @Override
    public void initData() {
        super.initData();
        List<IndexInfo> indexs = (List<IndexInfo>) SPUtils.readObject(activity, "indexinfos");
        if (indexs != null && indexs.size() > 0) {
            indexInfoList = new ArrayList<IndexInfo>();
            for (int i = indexs.size() - 1; i >= 0; i--) {
                indexInfoList.add(indexs.get(i));
            }
            myAdapter = new MyAdapter(activity, indexInfoList, R.layout.index_item_layout);
            listView.setAdapter(myAdapter);
            initCycleView();
        } else {
            refreshData();
        }
        final BmobFile bmobFile = localUser.getIcon();
        if (bmobFile != null) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
        }

    }



    private void initCycleView() {
        List<ImageInfo> images = (List<ImageInfo>) SPUtils.readObject(activity, "imageinfos");
        if (images != null && images.size() > 0) {
            mImageCycleView.loadData(images, new ImageCycleView.LoadImageCallBack() {
                @Override
                public ImageView loadAndDisplay(ImageInfo imageInfo) {
                    //使用BitmapUtils,只能使用网络图片
                    BitmapUtils bitmapUtils = new BitmapUtils(activity);
                    ImageView imageView = new ImageView(activity);
                    bitmapUtils.display(imageView, imageInfo.getImageUrl());
                    return imageView;
                }
            });

        } else {
            List<ImageInfo> list = new ArrayList<ImageInfo>();
            list.add(new ImageInfo("http://img2.imgtn.bdimg.com/it/u=1559138062,3924282920&fm=206&gp=0.jpg", "学会放下，人生才会更精彩！", "http://m.201980.com/lizhi/rsganwu/9675.html"));
            list.add(new ImageInfo("http://img1.imgtn.bdimg.com/it/u=2623481332,1427024523&fm=21&gp=0.jpg", "致我们终将逝去的青春!!!", "http://m.tongxiehui.net/by/6078.html"));
            list.add(new ImageInfo("http://img3.imgtn.bdimg.com/it/u=2868886379,3143514800&fm=21&gp=0.jpg", "我们是青春同路人！", "http://m.sohu.com/n/444180748/?=1&v=3"));
            mImageCycleView.loadData(list, new ImageCycleView.LoadImageCallBack() {
                @Override
                public ImageView loadAndDisplay(ImageInfo imageInfo) {
                    //使用BitmapUtils,只能使用网络图片
                    BitmapUtils bitmapUtils = new BitmapUtils(activity);
                    ImageView imageView = new ImageView(activity);
                    bitmapUtils.display(imageView, imageInfo.getImageUrl());
                    return imageView;
                }
            });
        }
        mImageCycleView.setOnPageClickListener(new ImageCycleView.OnPageClickListener() {
            @Override
            public void onClick(View imageView, ImageInfo imageInfo) {
                Intent intent = new Intent(activity, BaiduActivity.class);
                intent.putExtra("indexinfo", true);
                intent.putExtra("url", imageInfo.getWebsite());
                startActivity(intent);
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    List<IndexInfo> indexs = (List<IndexInfo>) SPUtils.readObject(activity, "indexinfos");
                    indexInfoList = new ArrayList<IndexInfo>();
                    for (int i = indexs.size() - 1; i >= 0; i--) {
                        indexInfoList.add(indexs.get(i));
                    }
                    myAdapter = new MyAdapter(activity, indexInfoList, R.layout.index_item_layout);
                    listView.setAdapter(myAdapter);
                    listView.onRefreshComplete();
                    listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("上次刷新时间：" + new SimpleDateFormat("hh:mm:ss").format(System.currentTimeMillis()));
                    break;
                case REFRESH_FAILED:
                    listView.onRefreshComplete();
                    break;
            }
        }
    };



    private class MyAdapter extends CommonAdapter<IndexInfo> {

        BitmapUtils bitmapUtils;
        private List<IndexInfo> mDatas;
        public MyAdapter(Context mContext, List<IndexInfo> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            this.mDatas=mDatas;
            bitmapUtils = new BitmapUtils(mContext, DefaultConfig.IMAGE_FILE_PATH);
            bitmapUtils.configDefaultBitmapMaxSize(100, 100);
        }

        @Override
        public void convert(final ViewHolder holder, final IndexInfo indexInfo) {
            holder.setText(R.id.tv_content, indexInfo.getContent());
            holder.setText(R.id.tv_text, indexInfo.getTitle());
            final CheckBox checkBox = holder.getView(R.id.checkbox);
            bitmapUtils.display(holder.getView(R.id.index_img), indexInfo.getImgUrl());
            if (collections!=null){
                for (int i = 0; i < collections.size(); i++) {
                    IndexInfo index=collections.get(i);
                     if (indexInfo.getObjectId().equals(index.getObjectId())){
                         checkBox.setChecked(true);
                         break;
                     }else{
                         checkBox.setChecked(false);
                     }
                }
            }else{
                checkBox.setChecked(false);
            }

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAnimation(checkBox);
                    indexInfo.setChecked(checkBox.isChecked());
                    String objectId=indexInfo.getObjectId();
                    if (collections!=null && collections.size()>0){
                        collections.clear();
                    }
                    for (int i = 0; i <mDatas.size(); i++) {
                        if (mDatas.get(i).isChecked()){
                            collections.add(mDatas.get(i));
                        }
                    }
                    updateUserMsg();
                }
            });
        }
    }
    private void updateUserMsg(){

        localUser.setColletions(gson.toJson(collections));
        localUser.update(activity, new UpdateListener() {
            @Override
            public void onSuccess() {

            }
            @Override
            public void onFailure(int i, String s) {
                System.out.println("----------"+i+s);
            }
        });
    }
    private void addAnimation(View view) {
        float[] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), ObjectAnimator.ofFloat(view, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isReLoad){
            isReLoad=false;
            localUser= BmobUser.getCurrentUser(activity, User.class);
            initData();
        }
    }
}
