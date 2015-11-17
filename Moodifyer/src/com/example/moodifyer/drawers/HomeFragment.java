package com.example.moodifyer.drawers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.moodifyer.FixedSpeedScroller;
import com.example.moodifyer.HomePageActivity;
import com.example.moodifyer.R;
import com.example.moodifyer.pojo.MusicDetails;
import com.example.moodifyer.services.StreamSongService;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.triggertrap.seekarc.SeekArc;
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener;

public class HomeFragment extends Fragment implements
		ViewTreeObserver.OnScrollChangedListener, View.OnClickListener,
		OnTouchListener, OnBufferingUpdateListener {

	public ViewGroup root, root2;

	private static final String STATE_MENUDRAWER = "net.simonvt.menudrawer.samples.WindowSample.menuDrawer";
	private static final String STATE_ACTIVE_VIEW_ID = "net.simonvt.menudrawer.samples.WindowSample.activeViewId";
	public static final String PREFS_NAME_AUTH = "Auth_PREFS";
	public static MenuDrawer mMenuDrawer;

	public static SeekArc mSeekArc;
	public static MediaPlayer mediaPlayer;
	public static int mediaFileLengthInMilliseconds;

	public static Button previous, next;
	public int prog;

	public static TextView durationtwo, duration;
	public static Handler handler = new Handler();

	private int mActiveViewId;

	public Bitmap bmp;

	// private ViewPager viewPager;
	private Typeface typeFace;

	public static ActionBar mActionBar;
	private float mActionBarHeight;

	// SlidngPanelLayout
	public static SlidingUpPanelLayout mLayout;
	private static final String TAG = "SLIDING UP";

	public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";

	public boolean actionBarHidden;
	public static Button follow, followme, pausetwo, playtwo;
	public static TextView songtitletwo, artistnametwo, songtitle, artistname;

	public static ImageView img_coverphoto;
	int currentDuration;

	// staticlistview
	public ListView list;
	public ListView mood_listview;

	final String[] tab_array = { "com.example.moodifyer.drawers.HomeFragment",
			"com.example.moodifyer.drawers.BrowseFragment",
			"com.example.moodifyer.drawers.ProfileFragment",
			"com.example.moodifyer.drawers.LogoutFragment" };

	ProgressDialog mProgressDialog;
	public static ArrayList<MusicDetails> mood_list = new ArrayList<MusicDetails>();
	public static ArrayList<MusicDetails> top_list = new ArrayList<MusicDetails>();

	static String url = "http://de.fio.re/MP/upload.php";

	static String COUNT_URL = "";

	Context context = getActivity();
	String strName;
	JSONObject jsonobject;
	JSONArray jsonarray;
	int op2, currentPos;
	String finalurl, firsturl;
	boolean elevate = false;
	Runnable durationRun;

	HomePageActivity homepage_connect;
	GridMoodFragment weeklyFragment_connect;
	MoodListFragment moodFragment_connect;
	public GridView gridview;

	public static LinearLayout dragView2;

	public static ViewPager pager;

	public static BitmapDrawable drawableBitmap;

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = (ViewGroup) inflater.inflate(R.layout.activity_home_page, null);
		root2 = (ViewGroup) inflater.inflate(R.layout.menutwo_scrollview, null);

		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		// getActivity().requestWindowFeature(
		// Window.FEATURE_ACTION_BAR_OVERLAY);
		// setContentView(R.layout.activity_home_page);

		if (savedInstanceState != null) {
			mActiveViewId = savedInstanceState.getInt(STATE_ACTIVE_VIEW_ID);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		// mContentTextView = (TextView) root.findViewById(R.id.contentText);
		root2.findViewById(R.id.item1).setOnClickListener(this);
		root2.findViewById(R.id.item2).setOnClickListener(this);
		root2.findViewById(R.id.item3).setOnClickListener(this);
		root2.findViewById(R.id.item4).setOnClickListener(this);

		follow = (Button) root.findViewById(R.id.follow);
		followme = (Button) root.findViewById(R.id.followme);

		duration = (TextView) root.findViewById(R.id.duration);
		durationtwo = (TextView) root.findViewById(R.id.durationtwo);
		songtitletwo = (TextView) root.findViewById(R.id.songtitletwo);
		pausetwo = (Button) root.findViewById(R.id.pausetwo);
		playtwo = (Button) root.findViewById(R.id.playtwo);
		next = (Button) root.findViewById(R.id.next);
		previous = (Button) root.findViewById(R.id.previous);

		dragView2 = (LinearLayout) root.findViewById(R.id.dragViewtwo);

		artistnametwo = (TextView) root.findViewById(R.id.artistnametwo);
		img_coverphoto = (ImageView) root.findViewById(R.id.imgcoverphoto);

		artistname = (TextView) root.findViewById(R.id.artistname);
		songtitle = (TextView) root.findViewById(R.id.songtitle);

		typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"DecoNeue-Light.otf");

		songtitletwo.setTextColor(Color.WHITE);
		artistnametwo.setTextColor(Color.WHITE);
		durationtwo.setTextColor(Color.WHITE);

		// TextView activeView = (TextView) root.findViewById(mActiveViewId);

		// This will animate the drawer open and closed until the user
		// manually
		// drags it. Usually this would only be
		// called on first launch.

		final TypedArray styledAttributes = getActivity().getTheme()
				.obtainStyledAttributes(
						new int[] { android.R.attr.actionBarSize });
		mActionBarHeight = styledAttributes.getDimension(0, 0);

		styledAttributes.recycle();

		mActionBar = getActivity().getActionBar();

		// viewPager = (ViewPager) root.findViewById(R.id.view);

		// new HorizontalViewTask().execute();

		// ((ParallaxScrollView) root.findViewById(R.id.parent))
		// .getViewTreeObserver().addOnScrollChangedListener(this);

		final LinearLayout dragView = (LinearLayout) root
				.findViewById(R.id.dragView);

		// final LinearLayout viewHide = (LinearLayout)
		// root.findViewById(R.id.viewHide);

		// final RelativeLayout hideAgain = (RelativeLayout)
		// root.findViewById(R.id.hideAgain);
		// hideAgain.setVisibility(View.GONE);

		mLayout = (SlidingUpPanelLayout) root.findViewById(R.id.sliding_layout);

		// viewHide.setVisibility(View.GONE);
		// dragView.setVisibility(View.GONE);

		mLayout.setPanelSlideListener(new PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				Log.i(TAG, "onPanelSlide, offset " + slideOffset);
				setActionBarTranslation(mLayout.getCurrentParalaxOffset());
				// mActionBar.hide();
			}

			@Override
			public void onPanelExpanded(View panel) {
				Log.i(TAG, "onPanelExpanded");

				duration.setVisibility(View.GONE);

				dragView.setVisibility(View.GONE);
			}

			@Override
			public void onPanelCollapsed(View panel) {
				Log.i(TAG, "onPanelCollapsed");
				duration.setVisibility(View.VISIBLE);

				dragView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPanelAnchored(View panel) {
				Log.i(TAG, "onPanelAnchored");

			}

			@Override
			public void onPanelHidden(View panel) {
				Log.i(TAG, "onPanelHidden");
			}
		});

		songtitletwo.setTypeface(typeFace);

		duration.setVisibility(View.VISIBLE);

		follow.setVisibility(View.GONE);
		followme.setVisibility(View.GONE);
		playtwo.setVisibility(View.GONE);
		pausetwo.setVisibility(View.GONE);

		// list = (ListView) root.findViewById(R.id.lisview);

		list = GridMoodFragment.listview2;
		mood_listview = MoodListFragment.listview3;

		follow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				follow.setVisibility(View.GONE);
				followme.setVisibility(View.VISIBLE);
				playtwo.setVisibility(View.GONE);
				pausetwo.setVisibility(View.VISIBLE);

				mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
				mediaPlayer.seekTo(currentDuration);
				primarySeekBarProgressUpdater();
				getActivity().startService(
						new Intent(StreamSongService.ACTION_PLAY));

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
				getActivity().startService(
						new Intent(StreamSongService.ACTION_PAUSE));
				currentDuration = mediaPlayer.getCurrentPosition();
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

				mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
				mediaPlayer.seekTo(currentDuration);
				primarySeekBarProgressUpdater();
				getActivity().startService(
						new Intent(StreamSongService.ACTION_PLAY));

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

				getActivity().startService(
						new Intent(StreamSongService.ACTION_PAUSE));
				currentDuration = mediaPlayer.getCurrentPosition();
			}
		});

		mSeekArc = (SeekArc) root.findViewById(R.id.seekArc);

		initView();

		mSeekArc.setOnSeekArcChangeListener(new OnSeekArcChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekArc seekArc) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekArc seekArc) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekArc seekArc, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
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

		pager = (ViewPager) root.findViewById(R.id.viewPager);
		pager.setAdapter(new MyPagerAdapter(getActivity()
				.getSupportFragmentManager()));

		Interpolator sInterpolator = new AccelerateInterpolator();
		try {
			Field mScroller;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(
					pager.getContext(), sInterpolator);
			// scroller.setFixedDuration(5000);
			mScroller.set(pager, scroller);

		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}

		RelativeLayout tile = (RelativeLayout) root.findViewById(R.id.tile);
		Bitmap backgroundImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.tactilenoise);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
				backgroundImage);
		bitmapDrawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		tile.setBackgroundDrawable(bitmapDrawable);
		//
		Bitmap topimage = BitmapFactory.decodeResource(getResources(),
				R.drawable.topshadow);
		BitmapDrawable topshadow = new BitmapDrawable(getResources(), topimage);
		topshadow.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		songtitletwo.setBackgroundDrawable(topshadow);

		GridMoodFragment.mood_list.clear();

		SharedPreferences settings = getActivity()
				.getSharedPreferences(PREFS_NAME_AUTH, 0);
		String finalUser = settings.getString("username_prefs", "");
		HomePageActivity.profName.setText(finalUser);
		
		
		return root;
	}

	private class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			switch (pos) {

			case 0: {

				return GridMoodFragment
						.newInstance("WeeklyTopTracksFragment, Instance 1");
			}
			case 1: {
				return MoodListFragment
						.newInstance("PlaylistFragment, Instance 1");
			}

			default:
				return GridMoodFragment
						.newInstance("WeeklyTopTracksFragment, Default");
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	}

	public static void primarySeekBarProgressUpdater() {
		mSeekArc.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This
																														// math
																														// construction
																														// give
																														// a
																														// percentage
																														// of
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
	private void initView() {

		mSeekArc.setOnTouchListener(this);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnBufferingUpdateListener(this);

	}

	private int getActionBarHeight() {
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		if (getActivity().getTheme().resolveAttribute(
				android.R.attr.actionBarSize, tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
					getResources().getDisplayMetrics());
		}
		return actionBarHeight;
	}

	public void setActionBarTranslation(float y) {
		// Figure out the actionbar height
		int actionBarHeight = getActionBarHeight();
		// A hack to add the translation to the action bar
		ViewGroup content = ((ViewGroup) getActivity().findViewById(
				android.R.id.content).getParent());
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

	@SuppressWarnings("static-access")
	@Override
	public void onResume() {

		super.onResume();

		mMenuDrawer = homepage_connect.mMenuDrawer;

		getView().setFocusableInTouchMode(true);
		getView().requestFocus();
		getView().setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				// Toast.makeText(context, "hey", Toast.LENGTH_SHORT).show();
				if (event.getAction() == KeyEvent.ACTION_UP
						&& keyCode == KeyEvent.KEYCODE_BACK) {

					// handle back button

					final int drawerState = mMenuDrawer.getDrawerState();

					if (drawerState == MenuDrawer.STATE_OPEN
							|| drawerState == MenuDrawer.STATE_OPENING) {
						mMenuDrawer.closeMenu();

					} else if (mLayout.collapsePanel()) {
						mLayout.collapsePanel();
					} else if (mLayout != null && mLayout.isPanelExpanded()
							|| mLayout.isPanelAnchored()) {
						mLayout.collapsePanel();
					}

					return true;

				}

				return false;
			}
		});

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// menu.add(0, 0, 0, "Elevate").setIcon(R.drawable.moodelevate)
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		// inflater.inflate(R.menu.home, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
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
		// float y = ((ParallaxScrollView) root.findViewById(R.id.parent))
		// .getScrollY();
		// if (y >= mActionBarHeight && mActionBar.isShowing()) {
		// mActionBar.hide();
		// } else if (y == 0 && !mActionBar.isShowing()) {
		// mActionBar.show();
		// }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		getActivity().setContentView(R.layout.fragment);

		FragmentTransaction tx = getActivity().getSupportFragmentManager()
				.beginTransaction();
		switch (v.getId()) {

		case R.id.item1: {
			tx.replace(R.id.content_frame,
					Fragment.instantiate(getActivity(), tab_array[0]));
			tx.commit();
			break;
		}
		case R.id.item2: {
			tx.replace(R.id.content_frame,
					Fragment.instantiate(getActivity(), tab_array[1]));
			tx.commit();
			break;
		}
		case R.id.item3: {
			tx.replace(R.id.content_frame,
					Fragment.instantiate(getActivity(), tab_array[2]));
			tx.commit();
			break;
		}
		case R.id.item4: {
			tx.replace(R.id.content_frame,
					Fragment.instantiate(getActivity(), tab_array[3]));
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

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub

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

}
