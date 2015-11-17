package com.example.moodifyer.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moodifyer.R;

public class MoodGridViewAdapter extends BaseAdapter {
	public static Boolean hello = false;
	public static List<ItemFor> mItems = new ArrayList<ItemFor>();
	private final LayoutInflater mInflater;
	public static ImageView picture;
	
	public static final String PREFS_ANGRYPIC = "Angrypic";
	public static final String PREFS_SADPIC = "Sadpic";
	public static final String PREFS_HAPPYPIC = "Happypic";
	public static final String PREFS_RELAXEDPIC = "Relaxedpic";
	public static final String PREFS_ENERGETICPIC = "Energeticpic";
	public static final String PREFS_ELEVATEPIC = "Elevatepic";
	SharedPreferences angry,sad,happy,relax,energetic,mood;
	SharedPreferences.Editor ed_angry, ed_sad, ed_happy, ed_relax, ed_energetic, ed_mood;
	Bitmap bAngry,bSad,bHappy,bRelax,bEnergetic,bMood;
	public String sd_angry, sd_sad, sd_happy, sd_relax, sd_energetic,sd_mood;

	//
	public Uri uri;
	//public String[]filePath

	public static Context context;

	public MoodGridViewAdapter(Context context) {
		hello = false;
		mInflater = LayoutInflater.from(context);
		this.context = context;
//		mItems.add(new ItemFor("Angry", R.drawable.angrymood));
//		mItems.add(new ItemFor("Sad", R.drawable.sadmood));
//		mItems.add(new ItemFor("Happy", R.drawable.happymood));
//		mItems.add(new ItemFor("Relaxed", R.drawable.relaxedmood));
//		mItems.add(new ItemFor("Energetic", R.drawable.energeticmood));
//		mItems.add(new ItemFor("Mood Elevate", R.drawable.elevate));
		
		//loading sharedprefs
		//angry
		angry = context.getSharedPreferences(PREFS_ANGRYPIC, 0);
		sd_angry = angry.getString("path", "");
		bAngry = decodeSampledBitmapFromUri(sd_angry, 400, 400);
		mItems.add(new ItemFor("Angry", bAngry));
		
		//sad
		sad = context.getSharedPreferences(PREFS_SADPIC, 0);
		sd_sad = sad.getString("path", "");
		bSad = decodeSampledBitmapFromUri(sd_sad, 400, 400);
		mItems.add(new ItemFor("Sad", bSad));
		
		//happy
		happy = context.getSharedPreferences(PREFS_HAPPYPIC, 0);
		sd_happy = happy.getString("path", "");
		bHappy = decodeSampledBitmapFromUri(sd_happy, 400, 400);
		mItems.add(new ItemFor("Happy", bHappy));
		
		//relax
		relax = context.getSharedPreferences(PREFS_RELAXEDPIC, 0);
		sd_relax = relax.getString("path", "");
		bRelax = decodeSampledBitmapFromUri(sd_relax, 400, 400);
		mItems.add(new ItemFor("Relaxed", bRelax));
		
		//energetic
		energetic = context.getSharedPreferences(PREFS_ENERGETICPIC, 0);
		sd_energetic = energetic.getString("path", "");
		bEnergetic = decodeSampledBitmapFromUri(sd_energetic, 400, 400);
		mItems.add(new ItemFor("Energetic", bEnergetic));
		
		//mood
		mood = context.getSharedPreferences(PREFS_ELEVATEPIC, 0);
		sd_mood = mood.getString("path", "");
		bMood = decodeSampledBitmapFromUri(sd_mood, 400, 400);
		mItems.add(new ItemFor("Mood Elevate", bMood));
	}

	/*public MoodGridViewAdapter(Context context,String text, Bitmap bp) {
		hello = true;
		mInflater = LayoutInflater.from(context);
		this.context = context;
		mItems.add(new ItemFor(text, bp));
	}*/

	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
			int reqHeight) {

		Bitmap bm = null;
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);

		return bm;
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

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public ItemFor getItem(int i) {
		return mItems.get(i);
	}

	@Override
	public long getItemId(int i) {
		return mItems.get(i).drawableId;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		View v = view;

		TextView name;

		if (v == null) {
			v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
			v.setTag(R.id.picture, v.findViewById(R.id.picture));
			v.setTag(R.id.text, v.findViewById(R.id.text));
		}

		picture = (ImageView) v.getTag(R.id.picture);
		name = (TextView) v.getTag(R.id.text);

		ItemFor item = getItem(i);
		// picture.setImageResource(item.drawableId);
	//	if (!hello) {
	//		picture.setImageBitmap(decodeSampledBitmapFromResource(
	//				context.getResources(), item.drawableId, 400, 400));
	//		name.setText(item.name);
	//	}
//		else{
			picture.setImageBitmap(item.bp);
			name.setText(item.name);
//		}
			
			
		return v;
	}
	
	

	public static class ItemFor {
		public String name;
		public int drawableId;
		public Bitmap bp;
		public Drawable d;

		public ItemFor(String name, int drawableId) {
			this.name = name;
			this.drawableId = drawableId;
			hello = false;
		}

		public ItemFor(String name, Bitmap bp) {
			hello = true;
			this.name = name;
			this.bp = bp;
		}
		
		public ItemFor(String name, Drawable d){
			this.name = name;
			this.d = d;
		}
	}
}
