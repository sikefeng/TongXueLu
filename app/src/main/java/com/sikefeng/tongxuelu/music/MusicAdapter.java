package com.sikefeng.tongxuelu.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class MusicAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private Context mContext;
	private List<String> list;
	private MediaPlayer mediaPlayer;
	
	public MusicAdapter(Context mContext,List<String> list) {
		this.mContext = mContext;
		inflater=LayoutInflater.from(mContext);
		this.list=list;
		mediaPlayer=new MediaPlayer();
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.music_item, null);
			holder.filename = (TextView) convertView.findViewById(R.id.filename);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {  
			mediaPlayer.reset();// 重置MediaPlayer
			mediaPlayer.setDataSource(list.get(position));// 指定要播放的音频文件
			mediaPlayer.prepare(); // 预加载音频文�?
			holder.time.setText(formatTime(mediaPlayer.getDuration()) + "   "+ getFileSize(list.get(position)));
		} catch (Exception e) {
			// TODO: handle exception
		}
        //显示录音文件名称
		String url=list.get(position);
		String name=url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));//获取文件�?
		holder.filename.setText(name);
		return convertView;
	}

	public class ViewHolder {
		TextView filename;
		TextView time;
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
 	
	//我写的获取文件大�?
  	public String getFileSize(String path){
  		DecimalFormat df = new DecimalFormat("#.00");
  		String filesizesString="";
  		File file=new File(path);
  		long filesize=file.length();
  		if (filesize <1024) {
  			filesizesString = df.format((double) filesize) + "B";
  		} else if (filesize<(1024*1024)) {
  			filesizesString = df.format((double) filesize / 1024) + "K";
  		} else if (filesize < (1024*1024*1024)) {
  			filesizesString = df.format((double) filesize / (1024*1024)) + "M";
  		} else {
  			filesizesString = df.format((double) filesize / (1024*1024*1024)) + "G";
  		}
  		return filesizesString;
  	}
}