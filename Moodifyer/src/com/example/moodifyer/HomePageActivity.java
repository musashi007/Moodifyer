package com.example.moodifyer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodifyer.adapter.MoodGridViewAdapter;
import com.example.moodifyer.adapter.MoodGridViewAdapter.ItemFor;
import com.example.moodifyer.drawers.GridMoodFragment;
import com.example.moodifyer.pojo.MusicDetails;
import com.example.moodifyer.services.StreamSongService;
import com.example.moodifyer.util.Base64;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.triggertrap.seekarc.SeekArc;
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener;

public class HomePageActivity extends FragmentActivity implements
		ViewTreeObserver.OnScrollChangedListener, View.OnClickListener,
		OnTouchListener, OnCompletionListener, OnBufferingUpdateListener,
		OnLongClickListener {

	// imageupload
	//
	public static SharedPreferences settings;
	public static final String PREFS_PIC = "pic";
	public static final String PREFS_ANGRYPIC = "Angrypic";
	public static final String PREFS_SADPIC = "Sadpic";
	public static final String PREFS_HAPPYPIC = "Happypic";
	public static final String PREFS_RELAXEDPIC = "Relaxedpic";
	public static final String PREFS_ENERGETICPIC = "Energeticpic";
	public static final String PREFS_ELEVATEPIC = "Elevatepic";
	
	public static String StringGlobal;
	Drawable drawable;
	public static int PosGlobal;
	public static GridView gridView;
	private static final int SELECT_PICTURE = 1;
	public MoodGridViewAdapter adapter;
	private String selectedImagePath;
	public String ba1;
	public String URL = "http://de.fio.re/MP2/avatar/avatar.php";
	String picturePath;
	Uri selectedImage;
	Bitmap photo;
	private Uri fileUri;

	private static final String STATE_MENUDRAWER = "net.simonvt.menudrawer.samples.WindowSample.menuDrawer";
	private static final String STATE_ACTIVE_VIEW_ID = "net.simonvt.menudrawer.samples.WindowSample.activeViewId";
	public static MenuDrawer mMenuDrawer;
	private TextView mContentTextView;

	public static SeekArc mSeekArc;
	public static MediaPlayer mediaPlayer;
	private static int mediaFileLengthInMilliseconds;

	public Button previous, next;
	public int prog;

	public static TextView durationtwo, duration;
	private final static Handler handler = new Handler();

	private int mActiveViewId;
	private ActionBar actionBar;
	// mainLayout is the child of the HorizontalScrollView ...

	public Bitmap bmp;

	private Typeface typeFacetwo;

	private ActionBar mActionBar;
	private float mActionBarHeight;

	// SlidngPanelLayout
	private SlidingUpPanelLayout mLayout;
	private static final String TAG = "SLIDING UP";

	public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";

	public boolean actionBarHidden;
	private Button follow, followme, pausetwo, playtwo;

	public ImageView img_coverphoto, img_cover;
	int currentDuration;

	public ViewPager pager;

	// staticlistview
	public ListView list;

	final String[] tab_array = { "com.example.moodifyer.drawers.HomeFragment",
			"com.example.moodifyer.drawers.BrowseFragment",
			"com.example.moodifyer.drawers.ProfileFragment",
			"com.example.moodifyer.drawers.LogoutFragment" };

	ProgressDialog mProgressDialog;
	public static ArrayList<MusicDetails> musicList = new ArrayList<MusicDetails>();
	static String url = "http://de.fio.re/MP/upload.php";
	Context context = HomePageActivity.this;
	String strName;
	JSONObject jsonobject;
	JSONArray jsonarray;
	int op2, currentPos;
	String finalurl, firsturl;
	boolean elevate = false;
	Runnable durationRun;
	public static TextView profName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		// setContentView(R.layout.activity_home_page);

		getWindow().requestFeature(Window.FEATURE_ACTION_MODE_OVERLAY);

		super.onCreate(savedInstanceState);
		// 330000ff
		// 550000ff
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#FFFFFF")));
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#FFFFFF")));
		if (savedInstanceState != null) {
			mActiveViewId = savedInstanceState.getInt(STATE_ACTIVE_VIEW_ID);
		}

		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW);
		mMenuDrawer.setContentView(R.layout.activity_home_page);
		mMenuDrawer.setMenuView(R.layout.menutwo_scrollview);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		// mContentTextView = (TextView) findViewById(R.id.contentText);

		findViewById(R.id.item1).setOnClickListener(this);
		findViewById(R.id.item2).setOnClickListener(this);
		findViewById(R.id.item3).setOnClickListener(this);
		findViewById(R.id.profile).setOnLongClickListener(this);
		findViewById(R.id.item4).setOnClickListener(this);

		follow = (Button) findViewById(R.id.follow);
		followme = (Button) findViewById(R.id.followme);

		duration = (TextView) findViewById(R.id.duration);
		durationtwo = (TextView) findViewById(R.id.durationtwo);

		pausetwo = (Button) findViewById(R.id.pausetwo);
		playtwo = (Button) findViewById(R.id.playtwo);

		// whatsnew = (TextView) findViewById(R.id.whatsnew);
		// whatshot = (TextView) findViewById(R.id.whatshot);
		// recommended = (TextView) findViewById(R.id.toprecommended);

		img_coverphoto = (ImageView) findViewById(R.id.imgcoverphoto);
		// img_cover = (ImageView) findViewById(R.id.cover_img);

		profName = (TextView) findViewById(R.id.profName);

		typeFacetwo = Typeface.createFromAsset(getAssets(), "lvnmbd.ttf");
		TextView activeView = (TextView) findViewById(mActiveViewId);
		if (activeView != null) {
			mMenuDrawer.setActiveView(activeView);
			mContentTextView.setText("Active item: " + activeView.getText());
		}

		// This will animate the drawer open and closed until the user
		// manually
		// drags it. Usually this would only be
		// called on first launch.
		mMenuDrawer.peekDrawer();

		final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
				new int[] { android.R.attr.actionBarSize });
		mActionBarHeight = styledAttributes.getDimension(0, 0);

		styledAttributes.recycle();

		// getActionBar().setBackgroundDrawable(
		// new ColorDrawable(Color.parseColor("#9a50b8")));

		mActionBar = getActionBar();

		// new HorizontalViewTask().execute();

		profName.setTypeface(typeFacetwo);

		duration.setVisibility(View.VISIBLE);

		follow.setVisibility(View.GONE);
		followme.setVisibility(View.GONE);
		playtwo.setVisibility(View.GONE);
		pausetwo.setVisibility(View.GONE);

		// staticlistview

		// list = (ListView) findViewById(R.id.lisview);
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, tab_array);
		// list.setAdapter(adapter);

		follow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				follow.setVisibility(View.GONE);
				followme.setVisibility(View.VISIBLE);
				playtwo.setVisibility(View.GONE);
				pausetwo.setVisibility(View.VISIBLE);

				startService(new Intent(StreamSongService.ACTION_PLAY));
				/*
				 * if (!mediaPlayer.isPlaying()) {
				 * mediaPlayer.seekTo(currentDuration); // mediaPlayer.start();
				 * startService(new Intent(StreamSongService.ACTION_PLAY));
				 * primarySeekBarProgressUpdater(); }
				 */
			}
		});

		followme.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				follow.setVisibility(View.VISIBLE);
				followme.setVisibility(View.GONE);
				playtwo.setVisibility(View.VISIBLE);
				pausetwo.setVisibility(View.GONE);
				startService(new Intent(StreamSongService.ACTION_PAUSE));
				/*
				 * if (mediaPlayer.isPlaying()) { //mediaPlayer.pause();
				 * startService(new Intent(StreamSongService.ACTION_PAUSE));
				 * currentDuration = mediaPlayer.getCurrentPosition(); }
				 */
			}
		});

		playtwo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playtwo.setVisibility(View.GONE);
				pausetwo.setVisibility(View.VISIBLE);
				follow.setVisibility(View.GONE);
				followme.setVisibility(View.VISIBLE);

				startService(new Intent(StreamSongService.ACTION_PLAY));
				/*
				 * if (!mediaPlayer.isPlaying()) {
				 * mediaPlayer.seekTo(currentDuration); //mediaPlayer.start();
				 * primarySeekBarProgressUpdater(); }
				 */
			}
		});

		pausetwo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				follow.setVisibility(View.VISIBLE);
				followme.setVisibility(View.GONE);
				playtwo.setVisibility(View.VISIBLE);
				pausetwo.setVisibility(View.GONE);

				startService(new Intent(StreamSongService.ACTION_PAUSE));
				/*
				 * if (mediaPlayer.isPlaying()) { //mediaPlayer.pause();
				 * currentDuration = mediaPlayer.getCurrentPosition(); }
				 */
			}
		});

		mSeekArc = (SeekArc) findViewById(R.id.seekArc);
		initView();
		mSeekArc.setOnSeekArcChangeListener(new OnSeekArcChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekArc seekArc) {
			}

			@Override
			public void onStartTrackingTouch(SeekArc seekArc) {
			}

			@Override
			public void onProgressChanged(SeekArc seekArc, int progress,
					boolean fromUser) {

				prog = progress;

			}
		});

		actionBarHidden = savedInstanceState != null
				&& savedInstanceState.getBoolean(SAVED_STATE_ACTION_BAR_HIDDEN,
						false);
		if (actionBarHidden) {
			int actionBarHeight = getActionBarHeight();
			setActionBarTranslation(-actionBarHeight);// will "hide" an
														// ActionBar
		}

		durationRun = new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (mediaPlayer.isPlaying()) {
						showDuration(mediaPlayer.getCurrentPosition());
					}
					try {
						Thread.sleep(400);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		pager = (ViewPager) findViewById(R.id.viewPager);

		setContentView(R.layout.fragment);

		FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
		tx.replace(R.id.content_frame,
				Fragment.instantiate(HomePageActivity.this, tab_array[0]));
		tx.commit();
		

	}
	
	

	@SuppressLint("ClickableViewAccessibility")
	private void initView() {

		mSeekArc.setOnTouchListener(this);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);

	}

	public static void primarySeekBarProgressUpdater() {
		mSeekArc.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This
																														// //"was playing"/"song length"
		if (mediaPlayer.isPlaying()) {
			showDuration(mediaPlayer.getCurrentPosition());
			Runnable notification = new Runnable() {
				public void run() {
					primarySeekBarProgressUpdater();
				}
			};
			handler.postDelayed(notification, 1000);
		}
	}

	private int getActionBarHeight() {
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
					getResources().getDisplayMetrics());
		}
		return actionBarHeight;
	}

	public void setActionBarTranslation(float y) {
		// Figure out the actionbar height
		int actionBarHeight = getActionBarHeight();
		// A hack to add the translation to the action bar
		ViewGroup content = ((ViewGroup) findViewById(android.R.id.content)
				.getParent());
		int children = content.getChildCount();
		for (int i = 0; i < children; i++) {
			View child = content.getChildAt(i);
			if (child.getId() != android.R.id.content) {
				if (y <= -actionBarHeight) {
					child.setVisibility(View.GONE);
				} else {
					child.setVisibility(View.VISIBLE);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						child.setTranslationY(y);
					} else {
						// AnimatorProxy.wrap(child).setTranslationY(y);
					}
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		final int drawerState = mMenuDrawer.getDrawerState();

		// Toast.makeText(context, "hey", Toast.LENGTH_SHORT).show();
		if (pager != null && pager.isShown()) {

			pager.setVisibility(View.GONE);
		} else if (drawerState == MenuDrawer.STATE_OPEN
				|| drawerState == MenuDrawer.STATE_OPENING) {
			mMenuDrawer.closeMenu();
			return;
		} else if (mLayout != null && mLayout.isPanelExpanded()
				|| mLayout.isPanelAnchored()) {
			mLayout.collapsePanel();
		} else if (mLayout.collapsePanel()) {
			mLayout.collapsePanel();
		} else {
			super.onBackPressed();
		}
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// menu.add(0, 0, 0, "ElevateOne").setIcon(R.drawable.moodelevate)
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		// getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onRestoreInstanceState(inState);
		mMenuDrawer.restoreState(inState.getParcelable(STATE_MENUDRAWER));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(STATE_MENUDRAWER, mMenuDrawer.saveState());
		outState.putInt(STATE_ACTIVE_VIEW_ID, mActiveViewId);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			mMenuDrawer.toggleMenu();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onScrollChanged() {

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mMenuDrawer.setActiveView(v);
		// mContentTextView.setText("Active item: " + ((TextView) v).getText());
		mMenuDrawer.closeMenu();
		mActiveViewId = v.getId();

		setContentView(R.layout.fragment);
		FragmentTransaction tx = getSupportFragmentManager().beginTransaction();

		switch (v.getId()) {

		case R.id.item1: {
			tx.replace(R.id.content_frame,
					Fragment.instantiate(HomePageActivity.this, tab_array[0]));
			tx.commit();
			break;
		}
		case R.id.item2: {
			tx.replace(R.id.content_frame,
					Fragment.instantiate(HomePageActivity.this, tab_array[1]));
			tx.commit();
			break;
		}
		case R.id.item3: {
			tx.replace(R.id.content_frame,
					Fragment.instantiate(HomePageActivity.this, tab_array[2]));
			tx.commit();
			break;
		}
		case R.id.item4: {
			tx.replace(R.id.content_frame,
					Fragment.instantiate(HomePageActivity.this, tab_array[3]));
			tx.commit();
			break;
		}

		}

	}

	public String readJSONFeed(String url) {

		StringBuilder sb = new StringBuilder();
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(httpget);

			StatusLine statusline = response.getStatusLine();
			int statuscode = statusline.getStatusCode();

			// check if code is equal to 200

			if (statuscode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream inputstream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputstream));

				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				inputstream.close();

			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}

	private static void showDuration(long milliseconds) {
		StringBuffer buf = new StringBuffer();
		int minutes = (int) ((milliseconds % (1000 * 60 * 60)) / (1000 * 60));
		int seconds = (int) (((milliseconds % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

		buf.append(String.format("%02d", minutes)).append(":")
				.append(String.format("%02d", seconds));
		// duration.setText(buf.toString());
		// durationtwo.setText(buf.toString());
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (v.getId() == R.id.seekArc) {
			/**
			 * Seekbar onTouch event handler. Method which seeks MediaPlayer to
			 * seekBar primary progress position
			 */
			if (mediaPlayer.isPlaying()) {

				int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100)
						* prog;
				mediaPlayer.seekTo(playPositionInMillisecconds);
			}
		}
		return false;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		/*
		 * currentPos += 1; if (currentPos == musicList.size()) { currentPos =
		 * 0; } mediaPlayer.reset(); firsturl =
		 * musicList.get(currentPos).getUrl();
		 * songtitletwo.setText(musicList.get(currentPos).getTitle());
		 * songtitle.setText(musicList.get(currentPos).getTitle());
		 * artistname.setText(musicList.get(currentPos).getArtist());
		 * artistnametwo.setText(musicList.get(currentPos).getArtist());
		 * img_coverphoto.setImageBitmap(musicList.get(currentPos).getImage());
		 * img_cover.setImageBitmap(musicList.get(currentPos).getImage()); try {
		 * 
		 * if (mediaPlayer.isPlaying()) mediaPlayer.reset();
		 * 
		 * mediaPlayer.setDataSource(firsturl); mediaPlayer.prepare(); } catch
		 * (Exception e) { Toast.makeText(getApplicationContext(),
		 * e.getMessage(), Toast.LENGTH_LONG).show(); }
		 * 
		 * mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
		 * 
		 * mediaPlayer.start(); primarySeekBarProgressUpdater();
		 */
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.profile: {
			new AlertDialog.Builder(this)
					.setTitle("Update Image")
					.setMessage("Do you want to change your profile picture?")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with delete
									clickpic();
									// upload();
								}
							})
					.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing

									// upload();
									// Toast.makeText(getApplicationContext(),
									// "hello", Toast.LENGTH_LONG).show();
								}
							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();

			break;
		}
		}
		return false;

	}

	public void upload() {
		// Image location URL
		Toast.makeText(getApplicationContext(), "nisud sa upload",
				Toast.LENGTH_LONG).show();
		// Log.e("path", "----------------" + picturePath);
		//
		// // Image
		Bitmap bm = BitmapFactory.decodeFile(picturePath);
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte[] ba = bao.toByteArray();
		ba1 = Base64.encodeBytes(ba);
		//
		Log.e("base64", "-----" + ba1);

		// Upload image to server
		new uploadToServer().execute();
	}

	private void clickpic() {

		// Check Camera
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// Open default camera
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

			// start the image capture Intent
			startActivityForResult(intent, 100);

		} else {
			Toast.makeText(getApplicationContext(), "Camera not supported",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && resultCode == RESULT_OK) {

			selectedImage = data.getData();
			photo = (Bitmap) data.getExtras().get("data");

			// Cursor to get image uri to display

			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			Bitmap photo = (Bitmap) data.getExtras().get("data");
			ImageView imageView = (ImageView) findViewById(R.id.profile);
			imageView.setImageBitmap(photo);
			// Toast.makeText(getApplicationContext(), picturePath,
			// Toast.LENGTH_LONG).show();
			// upload();

		}

		else{ 
			InputStream is;
			Bitmap bm;
			Uri selectedImageUri = data.getData();
			selectedImagePath = getPath(selectedImageUri);
			
//			try {
//				is = getContentResolver().openInputStream(selectedImageUri);
//				drawable = Drawable.createFromStream(is,selectedImageUri.toString());
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			bm = decodeSampledBitmapFromUri(selectedImagePath, 400, 400);
//			Toast.makeText(getApplicationContext(), ""+selectedImagePath,
//					Toast.LENGTH_LONG).show();
			
			
			if(PosGlobal == 0){
				settings = getSharedPreferences(PREFS_ANGRYPIC, 0);
				SharedPreferences.Editor edit = settings.edit();
				edit.putString("path", selectedImagePath);
				edit.commit();
			}
			
			else if(PosGlobal == 1){
				settings = getSharedPreferences(PREFS_SADPIC, 0);
				SharedPreferences.Editor edit = settings.edit();
				edit.putString("path", selectedImagePath);
				edit.commit();
			}
			
			else if(PosGlobal == 2){
				settings = getSharedPreferences(PREFS_HAPPYPIC, 0);
				SharedPreferences.Editor edit = settings.edit();
				edit.putString("path", selectedImagePath);
				edit.commit();
			}
			
			else if(PosGlobal == 3){
				settings = getSharedPreferences(PREFS_RELAXEDPIC, 0);
				SharedPreferences.Editor edit = settings.edit();
				edit.putString("path", selectedImagePath);
				edit.commit();
			}
			
			else if(PosGlobal == 4){
				settings = getSharedPreferences(PREFS_ENERGETICPIC, 0);
				SharedPreferences.Editor edit = settings.edit();
				edit.putString("path", selectedImagePath);
				edit.commit();
			}
			
			if(PosGlobal == 5){
				settings = getSharedPreferences(PREFS_ELEVATEPIC, 0);
				SharedPreferences.Editor edit = settings.edit();
				edit.putString("path", selectedImagePath);
				edit.commit();
			}
			
			
			//MoodGridViewAdapter.mItems.remove(0);
			MoodGridViewAdapter.mItems.set(PosGlobal, new ItemFor(StringGlobal, bm));
			GridMoodFragment.adapter.notifyDataSetChanged();
			
			

		}
	}
	
	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
		   
		   Bitmap bm = null;
		   // First decode with inJustDecodeBounds=true to check dimensions
		   final BitmapFactory.Options options = new BitmapFactory.Options();
		   options.inJustDecodeBounds = true;
		   BitmapFactory.decodeFile(path, options);
		       
		   // Calculate inSampleSize
		   options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		       
		   // Decode bitmap with inSampleSize set
		   options.inJustDecodeBounds = false;
		   bm = BitmapFactory.decodeFile(path, options); 
		       
		   return bm;   
		  }
	
	public String getPath(Uri uri) {
	    String[] projection = { MediaStore.Images.Media.DATA };
	    Cursor cursor = managedQuery(uri, projection, null, null, null);
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    Log.d("HELLO", "HELLOPATH");
	    return cursor.getString(column_index);
	    
	}

	public class uploadToServer extends AsyncTask<Void, Void, String> {

		private ProgressDialog pd = new ProgressDialog(getApplicationContext());

		protected void onPreExecute() {
			super.onPreExecute();
			pd.setMessage("Wait image uploading!");
			pd.show();
		}

		@Override
		protected String doInBackground(Void... params) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("base64", ba1));
			nameValuePairs.add(new BasicNameValuePair("ImageName", System
					.currentTimeMillis() + ".jpg"));
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(URL);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				String st = EntityUtils.toString(response.getEntity());
				Log.v("log_tag", "In the try Loop" + st);

			} catch (Exception e) {
				Log.v("log_tag", "Error in http connection " + e.toString());
			}
			return "Success";

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pd.hide();
			pd.dismiss();
		}
	}

}
