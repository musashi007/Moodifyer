package com.example.moodifyer.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.example.moodifyer.drawers.HomeFragment;
import com.triggertrap.seekarc.SeekArc;

public class StreamSongService extends Service implements OnCompletionListener {
	public static final String ACTION_PLAY = "com.example.moodifyer.services.action.PLAY";
	public static final String ACTION_PAUSE = "com.example.moodifyer.services.action.PAUSE";
	public static final String ACTION_PLAYPAUSE = "com.example.moodifyer.services.action.PLAYPAUSE";
	public static final String ACTION_PLAYPAUSEBROWSE = "com.example.moodifyer.services.action.PLAYPAUSEBROWSE";
	public static final String ACTION_MOODLIST = "com.example.moodifyer.services.action.MOODLIST";
	public static final String ACTION_LOGOUTSTOP = "com.example.moodifyer.services.action.LOGOUTSTOP";

	public Handler handler;
	public Runnable r;
	public SeekArc seekArc;
	public static MediaPlayer mediaPlayer;
	private String song_url = "";

	private TextView durationTwo;
	HomeFragment homepage_connect;
	public String songtitle, artistname, songtitletwo, artistnametwo,
			coverphotourl;
	public boolean grant_access = false;
	int currentDuration;

	Runnable durationRun;
	public Bitmap bmp;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub

		return null;
	}

	@SuppressWarnings("static-access")
	@Override
	public void onCreate() {
		mediaPlayer = homepage_connect.mediaPlayer;

		durationTwo = homepage_connect.durationtwo;

	}

	@Override
	public int onStartCommand(Intent intent, int flag, int startId) {
		String action = intent.getAction();

		song_url = intent.getStringExtra("value_url");
		artistname = intent.getStringExtra("value_artist");
		songtitle = intent.getStringExtra("value_title");
		coverphotourl = intent.getStringExtra("value_coverURL");

		if (action.equals(ACTION_PLAY))
			processPlayRequest();
		else if (action.equals(ACTION_PAUSE))
			processPauseRequest();
		else if (action.equals(ACTION_PLAYPAUSE))
			processPLAYPauseRequest();
		else if (action.equals(ACTION_PLAYPAUSEBROWSE))
			processPLAYPauseBrowseRequest();
		else if (action.equals(ACTION_MOODLIST))
			processSONGDETAILS();
		else if (action.equals(ACTION_LOGOUTSTOP))
			processLOGOUTSTOP();

		handler = new Handler();

		final Runnable r = new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				homepage_connect.primarySeekBarProgressUpdater();
				showDuration(mediaPlayer.getCurrentPosition());
				handler.postDelayed(this, 1000);
			}
		};

		handler.postDelayed(r, 1000);

		return START_NOT_STICKY;

	}

	private void processLOGOUTSTOP() {
		mediaPlayer.reset();
		mediaPlayer.release();
	}

	private void processSONGDETAILS() {
		HomeFragment.artistname.setText(artistname);
		HomeFragment.artistnametwo.setText(artistname);
		HomeFragment.songtitle.setText(songtitle);
		HomeFragment.songtitletwo.setText(songtitle);
	}

	private void processPLAYPauseBrowseRequest() {

		try {

			if (mediaPlayer.isPlaying())
				mediaPlayer.reset();

			mediaPlayer.setDataSource(song_url);
			mediaPlayer.prepare();

			// showDuration(mediaPlayer.getCurrentPosition());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mediaPlayer.start();
		grant_access = true;

	}

	private void processPlayRequest() {
		if (!mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(currentDuration);
			mediaPlayer.start();
		}
		showDuration(mediaPlayer.getCurrentPosition());

	}

	private void processPauseRequest() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			currentDuration = mediaPlayer.getCurrentPosition();
		}
	}

	private void processPLAYPauseRequest() {

		try {

			if (mediaPlayer.isPlaying())
				mediaPlayer.reset();

			mediaPlayer.setDataSource(song_url);
			mediaPlayer.prepare();

			// showDuration(mediaPlayer.getCurrentPosition());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mediaPlayer.start();
		grant_access = false;

	}

	@SuppressWarnings({ "deprecation", "static-access" })
	private void showDuration(long milliseconds) {
		StringBuffer buf = new StringBuffer();
		int minutes = (int) ((milliseconds % (1000 * 60 * 60)) / (1000 * 60));
		int seconds = (int) (((milliseconds % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

		buf.append(String.format("%02d", minutes)).append(":")
				.append(String.format("%02d", seconds));

		homepage_connect.durationtwo.setText(buf.toString());
		homepage_connect.duration.setText(buf.toString());
		if (grant_access) {
			HomeFragment.artistname.setText(artistname);
			HomeFragment.artistnametwo.setText(artistname);
			HomeFragment.songtitle.setText(songtitle);
			HomeFragment.songtitletwo.setText(songtitle);
			HomeFragment.drawableBitmap = new BitmapDrawable(
					loadImageFromNetwork(coverphotourl));

			HomeFragment.dragView2
					.setBackgroundDrawable(HomeFragment.drawableBitmap);
			HomeFragment.img_coverphoto
					.setImageBitmap(loadImageFromNetwork(coverphotourl));

			HomeFragment.playtwo.setVisibility(View.GONE);
			HomeFragment.follow.setVisibility(View.GONE);
			HomeFragment.followme.setVisibility(View.VISIBLE);
			HomeFragment.pausetwo.setVisibility(View.VISIBLE);

			HomeFragment.followme
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							processPauseRequest();
							HomeFragment.playtwo.setVisibility(View.VISIBLE);
							HomeFragment.follow.setVisibility(View.VISIBLE);
							HomeFragment.followme.setVisibility(View.GONE);
							HomeFragment.pausetwo.setVisibility(View.GONE);
						}
					});
			HomeFragment.pausetwo
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							processPauseRequest();
							HomeFragment.playtwo.setVisibility(View.VISIBLE);
							HomeFragment.follow.setVisibility(View.VISIBLE);
							HomeFragment.followme.setVisibility(View.GONE);
							HomeFragment.pausetwo.setVisibility(View.GONE);
						}
					});
			HomeFragment.follow.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					processPlayRequest();
					HomeFragment.playtwo.setVisibility(View.GONE);
					HomeFragment.follow.setVisibility(View.GONE);
					HomeFragment.followme.setVisibility(View.VISIBLE);
					HomeFragment.pausetwo.setVisibility(View.VISIBLE);
				}
			});
			HomeFragment.playtwo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					processPlayRequest();
					HomeFragment.playtwo.setVisibility(View.GONE);
					HomeFragment.follow.setVisibility(View.GONE);
					HomeFragment.followme.setVisibility(View.VISIBLE);
					HomeFragment.pausetwo.setVisibility(View.VISIBLE);
				}
			});
		}
		grant_access = false;

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

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

}
