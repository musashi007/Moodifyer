package com.example.moodifyer.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.example.moodifyer.R;
import com.example.moodifyer.pojo.MusicDetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicListAdapter extends BaseAdapter {

	ArrayList<MusicDetails> dataList = new ArrayList<MusicDetails>();
	LayoutInflater inflater;
	Context context;
	// ImageLoader imageLoader;
	Bitmap bmp;
	ImageView artistimage;
	int pos;
	URL urls = null;
	View view;

	Bitmap bm;
	// public MediaAdapter(Context context, ArrayList<MusicDetails> dataList){
	// this.dataList = dataList;
	// this.context = context;
	// inflater = LayoutInflater.from(this.context);
	// }

	public MusicListAdapter(Context context, ArrayList<MusicDetails> dataList) {
		this.dataList = dataList;
		this.context = context;
		// imageLoader = new ImageLoader(context);
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public MusicDetails getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MyViewHolder myViewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item, null);
			myViewHolder = new MyViewHolder();
			convertView.setTag(myViewHolder);
		}

		else {
			myViewHolder = (MyViewHolder) convertView.getTag();
		}
		view = convertView;
		pos = position;

		//new DownloadImageTask().execute();
		myViewHolder.textSong = detail(convertView, R.id.songtitle_item,
				dataList.get(position).getTitle());
		myViewHolder.textArtist = detail(convertView, R.id.songartist_item,
				dataList.get(position).getArtist());		
		myViewHolder.textGenre = detail(convertView,R.id.playcountsongs,dataList.get(position).getValence());
		
		//display cover
		
		//artistimage = (ImageView) convertView.findViewById(R.id.imageView1);
		//artistimage.setImageBitmap(dataList.get(position).getImage());
		
		return convertView;
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

		protected Bitmap doInBackground(String... urls) {
			return loadImageFromNetwork(dataList.get(pos).getCover_url());
		}

		protected void onPostExecute(Bitmap result) {
			// Do something with bitmap eg:

			bm = result;
			
			

		}
	}

	private Bitmap loadImageFromNetwork(String url) {
		try {
			URL urls = null;
			try {
				urls = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			bmp = null;
			try {
				bmp = BitmapFactory.decodeStream(urls.openConnection()
						.getInputStream());

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	private TextView detail(View v, int resId, String text) {
		TextView tv = (TextView) v.findViewById(resId);
		tv.setText(text);
		return tv;
	}

	private ImageView detail(View v, int resId, int icon) {
		ImageView iv = (ImageView) v.findViewById(resId);
		iv.setImageResource(icon); //

		return iv;
	}

	private class MyViewHolder {
		TextView textArtist, textSong, textGenre;
		ImageView iv;
	}

}
