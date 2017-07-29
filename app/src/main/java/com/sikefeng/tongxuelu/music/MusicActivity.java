package com.sikefeng.tongxuelu.music;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.appliction.MyApplication;
import com.sikefeng.tongxuelu.baseactivity.BaseActivity;

import java.util.List;

public class MusicActivity extends BaseActivity {
    private ImageButton preButton,playButton,nextButton;
    private ListView listView;
    private ProgressBar progressBar;
    private TextView tv_curcentTime,tv_allTime;
    private SeekBar seekBar;
    private TextView textView;
    private List<String> musicList;
    private MusicService musicService;
    private boolean bound=false;
    private MusicAdapter musicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.music_layout);
        initView();//初始化视图
        MyApplication application = (MyApplication) getApplication();
        musicService = application.getmService();
        musicList=musicService.getMusicFile();// 获取音乐文件路径
        if (musicList!=null){
            musicAdapter=new MusicAdapter(getApplicationContext(),musicList);
            listView.setAdapter(musicAdapter);
        }else{
            System.out.println("null");
        }
        //判断是否首次安装
        if (MyApplication.isFirst) {

        }else {
            handler.postDelayed(updateThread,500);
        }

        if (musicService.isPlaying()) {
            if (musicService.isPlaying()) {
                progressBar.setVisibility(View.VISIBLE);
                playButton.setImageResource(R.drawable.play);
            }else {
                playButton.setImageResource(R.drawable.stop);
                progressBar.setVisibility(View.GONE);
            }
        }
        musicService.setmCompletionListener(new MusicService.OnMusicCompletionListener() {
            @Override
            public void onCompletion() {
                // TODO Auto-generated method stub
                textView.setText("   "+musicService.getSingerName()+"   ");
            }
        });
    }



    public void initView() {
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        tv_allTime=(TextView)findViewById(R.id.tv_allTime);
        tv_curcentTime=(TextView)findViewById(R.id.tv_curcentTime);
        textView=(TextView)findViewById(R.id.songname);
        playButton=(ImageButton)findViewById(R.id.playButton);
        preButton=(ImageButton)findViewById(R.id.player_previous);
        nextButton=(ImageButton)findViewById(R.id.player_next);
        listView=(ListView)findViewById(R.id.listview);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                // TODO Auto-generated method stub
                progressBar.setVisibility(View.VISIBLE);
                MyApplication.isFirst=false;
                musicService.currentitem=position;
                textView.setText("   "+musicService.getSingerName()+"   ");
                musicService.playMusic(position);
                handler.postDelayed(updateThread,500);
                playButton.setImageResource(R.drawable.play);
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (MyApplication.isFirst) {
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setText("   "+musicService.getSingerName()+"   ");
                    musicService.playMusic(musicService.currentitem);
                    playButton.setImageResource(R.drawable.play);
                }else {
                    textView.setText("   "+musicService.getSingerName()+"   ");
                    musicService.playOrpause();
                    if (musicService.isPlaying()) {
                        ((ImageButton)v).setImageResource(R.drawable.play);
                        progressBar.setVisibility(View.VISIBLE);
                    }else {
                        ((ImageButton)v).setImageResource(R.drawable.stop);
                        progressBar.setVisibility(View.GONE);
                    }
                }
                handler.postDelayed(updateThread,500);
                MyApplication.isFirst=false;
            }
        });
        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                MyApplication.isFirst=false;
                musicService.preMusic();
                progressBar.setVisibility(View.VISIBLE);
                textView.setText("   "+musicService.getSingerName()+"   ");
                handler.postDelayed(updateThread,500);
                playButton.setImageResource(R.drawable.play);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                MyApplication.isFirst=false;
                musicService.nextMusic();
                textView.setText("   "+musicService.getSingerName()+"   ");
                handler.postDelayed(updateThread,500);
                playButton.setImageResource(R.drawable.play);
                progressBar.setVisibility(View.VISIBLE);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // fromUser判断是用户改变的滑块的�??
                if (fromUser) {
                    musicService.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }




    Handler handler = new Handler();
    Runnable updateThread = new Runnable() {
        public void run() {
            textView.setText("   "+musicService.getSingerName()+"   ");
            // 获得歌曲的长度并设置成播放进度条的最大�??
            seekBar.setMax(musicService.getDuration());
            // 获得歌曲现在播放位置并设置成播放进度条的�?
            seekBar.setProgress(musicService.getCurrentPosition());
            tv_allTime.setText(musicService.getMusicTimeLength());
            tv_curcentTime.setText(musicService.getCurrentPositionString());
            // 每次延迟500毫秒再启动线�?
            if (musicService.isPlaying()) {
                handler.postDelayed(updateThread,500);
            }
        }
    };

}
