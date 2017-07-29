package com.sikefeng.tongxuelu.music;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {
	private MediaPlayer mediaPlayer;
	public boolean flag = true;
	public static int currentitem=0;
	private List<String> musicList;
	private final IBinder mBinder = new LocalBinder();
    private Context context;

	public MusicService() {
	}
	/**
	 *	自定义绑定Service类，通过这里的getService得到Service，之后就可调用Service这里的方法了
	 */
	public class LocalBinder extends Binder {
		public MusicService getService() {
			return MusicService.this;
		}
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("onDestroy()");
	}



	public void setContext(Context context) {
		this.context = context;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	    if (mediaPlayer==null){
			mediaPlayer=new MediaPlayer();;
		}
		musicList=getMusicFile();
	}

	



	private List<String> getSongsList(){
		return musicList;
	}

	public void seekTo(int progress){
		mediaPlayer.seekTo(progress);
	}


	public void playOrpause(){
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}else {
			mediaPlayer.start();
		}
	}
	public boolean isPlaying(){
		return mediaPlayer.isPlaying();
	}

	public void playMusic(int position) {
		// TODO Auto-generated method stub
		final String Musicpath=musicList.get(position);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.stop();// 停止当前的音频播�?
					}
					mediaPlayer.reset();// 重置MediaPlayer
					mediaPlayer.setDataSource(Musicpath);// 指定要播放的音频文件
					mediaPlayer.prepare(); // 预加载音频文�?
					mediaPlayer.start();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start();
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				if (++currentitem >= musicList.size()) {// 当对currentitem进行�?1操作后，如果其�?�大于等于音频文件的总数
					currentitem = 0;
				}
				playMusic(currentitem);
				if (mCompletionListener!=null) {
					mCompletionListener.onCompletion();
				}
			}
		});
	}
	public void preMusic(){
		if (--currentitem >= 0) {// 当对currentitem进行�?1操作后，如果其�?�大于等�?0
			if (currentitem >= musicList.size()) { // 如果currentitem的�?�大于等于音频文件的总数
				currentitem = 0;
			}
		} else {
			currentitem = musicList.size() - 1;
		}
		playMusic(currentitem);

	}
	public void nextMusic(){
		if (++currentitem >= musicList.size()) {// 当对currentitem进行�?1操作后，如果其�?�大于等于音频文件的总数
			currentitem = 0;
		}
		playMusic(currentitem);


	}
	//添加音频文件  到list
	public List<String> getMusicFile(){
		List<String> list=new ArrayList<String>();
		ContentResolver mContentResolver=this.getContentResolver();
		Cursor mCursor = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,null,null,null);
		while (mCursor.moveToNext()) {
			String url = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
			list.add(url);
		}
		return list;
	}
	//获取正在播放的音乐名�?
	public String getSingerName(){
		String url=musicList.get(currentitem);
		//显示正在播放文件名称
		String name=url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));//获取文件�?
		String singerName = null;
		String songName= null;
		if (name.indexOf("-")>-1){
			singerName=name.substring(name.lastIndexOf("-")+1,name.lastIndexOf(""));//获取歌曲名称
			songName=name.substring(0,name.lastIndexOf("-"));//获取歌手名称
			if (songName.indexOf("-")>-1) {
				singerName=name.substring(songName.lastIndexOf("-")+1,songName.lastIndexOf(""));//获取歌曲名称
			}
			return singerName;
		}
		return name;
	}

	private OnMusicCompletionListener mCompletionListener;
	public interface OnMusicCompletionListener{
		void onCompletion();
	}

	public void setmCompletionListener(OnMusicCompletionListener mCompletionListener) {
		this.mCompletionListener = mCompletionListener;
	}

	public int getDuration(){
		return mediaPlayer.getDuration();
	}
	public int getCurrentPosition(){
		return mediaPlayer.getCurrentPosition();
	}
	public String getMusicTimeLength(){
		return formatTime(mediaPlayer.getDuration());
	}
	public String getCurrentPositionString(){
		return formatTime(mediaPlayer.getCurrentPosition());
	}

	//格式化时间，将其变成00:00的形�?
	public String formatTime(int time) {
		int secondSum = time / 1000;
		int minute = secondSum / 60;
		int second = secondSum % 60;
		String result = "";
		if (minute < 10)
			result = "0";
		result = result + minute + ":";
		if (second < 10)
			result = result + "0";
		result = result + second;
		return result;
	}

}
