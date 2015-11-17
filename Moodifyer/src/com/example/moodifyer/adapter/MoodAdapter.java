package com.example.moodifyer.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moodifyer.R;
import com.example.moodifyer.pojo.*;

public class MoodAdapter extends BaseAdapter {
	ArrayList<MoodDetails> dataList = new ArrayList<MoodDetails>();
	LayoutInflater inflater;
	Context context;
	public ImageView picture;
	
	public MoodAdapter(Context context, ArrayList<MoodDetails> dataList){
		this.dataList = dataList;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public MoodDetails getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyViewHolder myViewHolder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.listmoodrow, null);
			myViewHolder = new MyViewHolder();
			convertView.setTag(myViewHolder);
//			convertView.setTag(R.id.listmoodrow_image, convertView.findViewById(R.id.listmoodrow_image));
		}
			
		else{
			myViewHolder = (MyViewHolder)convertView.getTag();
		}
		
	//	picture = (ImageView) convertView.getTag(R.id.listmoodrow_image);
		myViewHolder.tvMood = detail(convertView, R.id.listmoodrow_mood, dataList.get(position).getMood());
		myViewHolder.tvValence = detail(convertView, R.id.listmoodrow_valence, dataList.get(position).getValence());
		
		myViewHolder.iv = detail(convertView,R.id.listmoodrow_image,dataList.get(position).getImage());
		//picture.setImageBitmap(decodeSampledBitmapFromResource(
			//	context.getResources(), dataList.get(position).getImage(), 400, 400));
		return convertView;
	}
	
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
	
	private TextView detail(View v,int resId, String text){
		TextView tv =(TextView) v.findViewById(resId);
		tv.setText(text);
		return tv;
	}
	
	private ImageView detail(View v, int resId, int icon) {
		ImageView iv = (ImageView) v.findViewById(resId);
		iv.setImageResource(icon); // 
		
		return iv;
	}
	
	private class MyViewHolder{
		TextView tvMood, tvValence;
		ImageView iv;
	}

}
