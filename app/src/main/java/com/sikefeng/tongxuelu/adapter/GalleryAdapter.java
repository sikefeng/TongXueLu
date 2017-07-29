package com.sikefeng.tongxuelu.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.sikefeng.tongxuelu.R;

import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> list;

	
	public GalleryAdapter(Context context, ArrayList<String> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(180, 120));

			TypedArray typedArray = context
					.obtainStyledAttributes(R.styleable.Gallery);
			imageView.setBackgroundResource(typedArray.getResourceId(
					R.styleable.Gallery_android_galleryItemBackground, 0));
			imageView.setPadding(0, 0, 0, 0);

		} else {
			imageView = (ImageView) convertView;
		}
		Bitmap bitmap=BitmapFactory.decodeFile(list.get(position));
		Bitmap bitmap1=ThumbnailUtils.extractThumbnail(bitmap, 150, 100);
		imageView.setImageBitmap(bitmap1);
		return imageView;
	}

}
