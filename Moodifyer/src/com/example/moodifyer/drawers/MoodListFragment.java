package com.example.moodifyer.drawers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.moodifyer.HomePageActivity;
import com.example.moodifyer.R;
import com.example.moodifyer.adapter.MusicListAdapter;
import com.example.moodifyer.pojo.MusicDetails;
import com.example.moodifyer.services.StreamSongService;
import com.google.gson.Gson;

public class MoodListFragment extends Fragment implements OnCompletionListener {

	public static boolean det = false;
	public static int moment = 0;
	public static String tough;
	public static ArrayList<MusicDetails> mood_list = new ArrayList<MusicDetails>();
	public static final String PREFS_LIST =  "mood";
	public static SharedPreferences sx;
	public ViewPager pager;
	public ViewGroup root;
	static String url = "http://de.fio.re/MP2/stream.php";
	public MenuDrawer mMenuDrawer;
	public String firsturl;
	HomePageActivity homepage_connect;
	HomeFragment homefrag_connect;
	public static ListView listview3;
	public static int currentPos;
	public Bitmap bmp, loadImage;
	public URL url_image;
	public String cover_url;
	public String songtitle;
	public String songurl_prefs;
	public static final String PREFS_NAME = "SongPrefsDetail";
	public SharedPreferences settings;
	
	//moodprefs
	public static final String PREFS_ANGRY = "Angrymood";
	public static final String PREFS_SAD = "Sadmood";
	public static final String PREFS_RELAXED = "Relaxedmood";
	public static final String PREFS_HAPPY = "Happymood";
	public static final String PREFS_ENERGETIC = "Energeticmood";
	public static final String PREFS_DET = "DeterminePos";
	public SharedPreferences moodpos;
	
	
	// detprefs
	public static final String PREFS_DETA = "DeterminePosAngry";
	public static final String PREFS_DETS = "DeterminePosSad";
	public static final String PREFS_DETR = "DeterminePosRelaxed";
	public static final String PREFS_DETH = "DeterminePosHappy";
	public static final String PREFS_DETE = "DeterminePosEnergetic";

	
	


	public static MoodListFragment newInstance(String text) {

		MoodListFragment f = new MoodListFragment();
		Bundle b = new Bundle();
		b.putString("msg", text);

		f.setArguments(b);

		return f;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		
		root = (ViewGroup) inflater.inflate(
				R.layout.activity_playlist_fragment, null);
		pager = HomeFragment.pager;
//		sx = getActivity().getSharedPreferences(PREFS_LIST, 0);
//		if (sx.getString("yess", "").equals("yess") && moment == 0) {
//			pager.setCurrentItem(1);
//		}

		HomeFragment.mediaPlayer.setOnCompletionListener(this);

		if (HomeFragment.mediaPlayer.isPlaying()) {
			HomeFragment.follow.setVisibility(View.GONE);
			HomeFragment.followme.setVisibility(View.VISIBLE);
		} else {

		}
		new ImageRunBackgroundtask().execute();

		HomeFragment.next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new NextTask().execute();
			}
		});

		HomeFragment.previous.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new PreviousTask().execute();
			}
		});

		new retrieveDataPrefsTask().execute();

		HomeFragment.follow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HomeFragment.mediaFileLengthInMilliseconds = HomeFragment.mediaPlayer
						.getDuration();

				

				getActivity().startService(
						new Intent(StreamSongService.ACTION_PLAYPAUSE)
								.putExtra("value_url", songurl_prefs));
				HomeFragment.primarySeekBarProgressUpdater();
				HomeFragment.follow.setVisibility(View.GONE);
				HomeFragment.followme.setVisibility(View.VISIBLE);
				HomeFragment.playtwo.setVisibility(View.GONE);
				HomeFragment.pausetwo.setVisibility(View.VISIBLE);
				//Toast.makeText(getActivity(), "SONG URL IS: " + songurl_prefs,
				//		Toast.LENGTH_SHORT).show();
			}
		});

		HomeFragment.playtwo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HomeFragment.mediaFileLengthInMilliseconds = HomeFragment.mediaPlayer
						.getDuration();

				

				getActivity().startService(
						new Intent(StreamSongService.ACTION_PLAYPAUSE)
								.putExtra("value_url", songurl_prefs));
				HomeFragment.primarySeekBarProgressUpdater();
				HomeFragment.follow.setVisibility(View.GONE);
				HomeFragment.followme.setVisibility(View.VISIBLE);
				HomeFragment.playtwo.setVisibility(View.GONE);
				HomeFragment.pausetwo.setVisibility(View.VISIBLE);
			}
		});

		return root;
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

	private class retrieveDataPrefsTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String s = postData(params);
			return s;
		}

		private String postData(String[] params) {
			return null;
		}

		protected void onPostExecute(String result) {
			settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
			String artist_prefs = settings.getString("artistnameprefs",
					"----------");
			String songtitle_prefs = settings.getString("songtitleprefs",
					"---------------");
			String coverphoto_prefs = settings.getString("coverphotoprefs", "");
			songurl_prefs = settings.getString("songurlprefs", "");

			try {
				url_image = new URL(coverphoto_prefs);
				loadImage = BitmapFactory.decodeStream(url_image
						.openConnection().getInputStream());
				HomeFragment.drawableBitmap = new BitmapDrawable(loadImage);

				HomeFragment.dragView2
						.setBackgroundDrawable(HomeFragment.drawableBitmap);
				HomeFragment.img_coverphoto.setImageBitmap(loadImage);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HomeFragment.artistname.setText(artist_prefs);
			HomeFragment.artistnametwo.setText(artist_prefs);
			HomeFragment.songtitle.setText(songtitle_prefs);
			HomeFragment.songtitletwo.setText(songtitle_prefs);
			artist_prefs = "";
			songtitle_prefs = "";
			coverphoto_prefs = "";

			HomeFragment.follow.setVisibility(View.VISIBLE);
			HomeFragment.playtwo.setVisibility(View.VISIBLE);

		}
	}

	private class PreviousTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String s = postData(params);

			return s;
		}

		private String postData(String[] params) {

			return null;
		}

		protected void onPostExecute(String result) {
			Toast.makeText(getActivity(), "Previous", Toast.LENGTH_SHORT)
					.show();
			try {
				currentPos -= 1;

				if (currentPos == GridMoodFragment.mood_list.size()) {
					currentPos = 0;
				}

				HomeFragment.mediaPlayer.reset();
				cover_url = "";
				new ReadImageCovTask().execute(url);

				firsturl = GridMoodFragment.mood_list.get(currentPos)
						.getUrl();

				songtitle = GridMoodFragment.mood_list.get(currentPos)
						.getTitle();

				HomeFragment.songtitletwo
						.setText(GridMoodFragment.mood_list.get(
								currentPos).getTitle());
				HomeFragment.songtitle
						.setText(GridMoodFragment.mood_list.get(
								currentPos).getTitle());
				HomeFragment.artistname
						.setText(GridMoodFragment.mood_list.get(
								currentPos).getArtist());
				HomeFragment.artistnametwo
						.setText(GridMoodFragment.mood_list.get(
								currentPos).getArtist());

				try {

					if (HomeFragment.mediaPlayer.isPlaying())
						HomeFragment.mediaPlayer.reset();

					HomeFragment.mediaPlayer.setDataSource(firsturl);
					HomeFragment.mediaPlayer.prepare();
				} catch (Exception e) {
					Toast.makeText(getActivity(), e.getMessage(),
							Toast.LENGTH_LONG).show();
				}

				HomeFragment.mediaFileLengthInMilliseconds = HomeFragment.mediaPlayer
						.getDuration();

				// mediaPlayer.start();
				getActivity().startService(
						new Intent(StreamSongService.ACTION_PLAYPAUSE)
								.putExtra("value_url", firsturl));
				HomeFragment.primarySeekBarProgressUpdater();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}

	}

	private class NextTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String s = postData(params);
			return s;
		}

		private String postData(String[] params) {
			return null;

		}

		protected void onPostExecute(String result) {
			Toast.makeText(getActivity(), "Next", Toast.LENGTH_SHORT).show();
			try {
				currentPos += 1;

				if (currentPos == GridMoodFragment.mood_list.size()) {
					currentPos = 0;
				}

				HomeFragment.mediaPlayer.reset();
				cover_url = "";
				new ReadImageCovTask().execute(url);

				firsturl = GridMoodFragment.mood_list.get(currentPos)
						.getUrl();

				songtitle = GridMoodFragment.mood_list.get(currentPos)
						.getTitle();

				HomeFragment.songtitletwo
						.setText(GridMoodFragment.mood_list.get(
								currentPos).getTitle());
				HomeFragment.songtitle
						.setText(GridMoodFragment.mood_list.get(
								currentPos).getTitle());
				HomeFragment.artistname
						.setText(GridMoodFragment.mood_list.get(
								currentPos).getArtist());
				HomeFragment.artistnametwo
						.setText(GridMoodFragment.mood_list.get(
								currentPos).getArtist());

				try {

					if (HomeFragment.mediaPlayer.isPlaying())
						HomeFragment.mediaPlayer.reset();

					HomeFragment.mediaPlayer.setDataSource(firsturl);
					HomeFragment.mediaPlayer.prepare();
				} catch (Exception e) {
					Toast.makeText(getActivity(), e.getMessage(),
							Toast.LENGTH_LONG).show();
				}

				HomeFragment.mediaFileLengthInMilliseconds = HomeFragment.mediaPlayer
						.getDuration();

				// mediaPlayer.start();
				getActivity().startService(
						new Intent(StreamSongService.ACTION_PLAYPAUSE)
								.putExtra("value_url", firsturl));
				HomeFragment.primarySeekBarProgressUpdater();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}

	}

	private class ReadImageCovTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return readJSONFeed(arg0[0]);
		}

		@SuppressWarnings("deprecation")
		protected void onPostExecute(String result) {

			try {

				JSONArray jar = new JSONArray(result);
				for (int i = 0; i < jar.length(); i++) {
					JSONObject song = jar.getJSONObject(i);
					String title = song.getString("title");
					if (title.equals(songtitle)) {
						cover_url = song.getString("cover_url");
					}

				}
				SharedPreferences settings = getActivity()
						.getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("coverphotoprefs", cover_url);
				editor.commit();

				url_image = new URL(cover_url);
				loadImage = BitmapFactory.decodeStream(url_image
						.openConnection().getInputStream());

				HomeFragment.drawableBitmap = new BitmapDrawable(loadImage);

				HomeFragment.dragView2
						.setBackgroundDrawable(HomeFragment.drawableBitmap);
				HomeFragment.img_coverphoto.setImageBitmap(loadImage);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private class ImageRunBackgroundtask extends
			AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}

		protected void onPostExecute(String result) {
			
			listview3 = (ListView) root.findViewById(R.id.listview3);	
			//beginline
//			SharedPreferences mm;
//			sx = getActivity().getSharedPreferences(PREFS_LIST, 0);
//			if(sx.getString("yess","").equals("yess") && moment == 0){
//				det = true;
//				pager.setCurrentItem(1);
//				String hello = sx.getString("mud", "");
//				tough = hello;
//				if(hello.equals("Angry")){
//					mm = getActivity().getSharedPreferences(PREFS_ANGRY, 0);
//					String itemlist = mm.getString("itemlist", "");
//					List<MusicDetails> moodf;
//					Gson gson = new Gson();
//					MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
//					moodf = Arrays.asList(mitems);
//					moodf = new ArrayList<MusicDetails>(moodf);
//					mood_list =  (ArrayList<MusicDetails>) moodf;
//				}
//				
//				else if(hello.equals("Sad")){
//					mm = getActivity().getSharedPreferences(PREFS_SAD, 0);
//					String itemlist = mm.getString("itemlist", "");
//					List<MusicDetails> moodf;
//					Gson gson = new Gson();
//					MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
//					moodf = Arrays.asList(mitems);
//					moodf = new ArrayList<MusicDetails>(moodf);
//					mood_list =  (ArrayList<MusicDetails>) moodf;
//				}
//				
//				else if(hello.equals("Relax")){
//					mm = getActivity().getSharedPreferences(PREFS_RELAXED, 0);
//					String itemlist = mm.getString("itemlist", "");
//					List<MusicDetails> moodf;
//					Gson gson = new Gson();
//					MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
//					moodf = Arrays.asList(mitems);
//					moodf = new ArrayList<MusicDetails>(moodf);
//					mood_list =  (ArrayList<MusicDetails>) moodf;
//				}
//				else if(hello.equals("Happy")){
//					mm = getActivity().getSharedPreferences(PREFS_HAPPY, 0);
//					String itemlist = mm.getString("itemlist", "");
//					List<MusicDetails> moodf;
//					Gson gson = new Gson();
//					MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
//					moodf = Arrays.asList(mitems);
//					moodf = new ArrayList<MusicDetails>(moodf);
//					mood_list =  (ArrayList<MusicDetails>) moodf;
//				}
//				else if(hello.equals("Energetic")){
//					mm = getActivity().getSharedPreferences(PREFS_ENERGETIC, 0);
//					String itemlist = mm.getString("itemlist", "");
//					List<MusicDetails> moodf;
//					Gson gson = new Gson();
//					MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
//					moodf = Arrays.asList(mitems);
//					moodf = new ArrayList<MusicDetails>(moodf);
//					mood_list =  (ArrayList<MusicDetails>) moodf;
//				}	
//			listview3.setAdapter(new MusicListAdapter(
//						getActivity(), mood_list));
//			moment++;
//			}
			//lastline
			
			
			listview3.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long arg3) {
					String adet,sdet,rdet,hdet,edet;
					SharedPreferences shareadet,sharesdet,sharerdet,sharehdet,shareedet;
					shareadet = getActivity().getSharedPreferences(PREFS_ANGRY, 0);
					sharesdet = getActivity().getSharedPreferences(PREFS_SAD, 0);
					sharerdet = getActivity().getSharedPreferences(PREFS_RELAXED, 0);
					sharehdet = getActivity().getSharedPreferences(PREFS_HAPPY, 0);
					shareedet = getActivity().getSharedPreferences(PREFS_ENERGETIC, 0);
					
					adet = shareadet.getString("Start", "not");
					sdet = sharesdet.getString("Start", "not");
					rdet = sharerdet.getString("Start", "not");
					hdet = sharehdet.getString("Start", "not");
					edet = shareedet.getString("Start", "not");

					
					//flagClick
					if (!GridMoodFragment.flagClick) {
						//HomeFragment.mediaPlayer.reset();
						// position = 0;
						// settings = getActivity().getSharedPreferences(
						// PREFS_NAME, 0);
						// int todo = settings.getInt("position", position);
						// position = todo;
						
//						moodpos = getActivity().getSharedPreferences(PREFS_DET,
//								0);
//						String det = moodpos.getString("Start", "not");
//						if (det.equals("Start")) {
//							position = 0;
//							settings = getActivity().getSharedPreferences(
//									PREFS_DET, 0);
//							SharedPreferences.Editor ee = settings.edit();
//							ee.putString("Start", "not");
//							ee.commit();
//							
//						}
						
						
						if((adet.equals("Start") && GridMoodFragment.mud.equals("Angry"))){
							position = 0;
							settings = getActivity().getSharedPreferences(
									PREFS_ANGRY, 0);
							SharedPreferences.Editor ee = settings.edit();
							ee.putString("Start", "not");
							ee.putInt("position", position);
							ee.commit();
						}
						
						else if((sdet.equals("Start") && GridMoodFragment.mud.equals("Sad"))){
							position = 0;
							settings = getActivity().getSharedPreferences(
									PREFS_SAD, 0);
							SharedPreferences.Editor ee = settings.edit();
							ee.putString("Start", "not");
							ee.putInt("position", position);
							ee.commit();
						}
						
						else if((rdet.equals("Start") && GridMoodFragment.mud.equals("Relax"))){
							position = 0;
							settings = getActivity().getSharedPreferences(
									PREFS_RELAXED, 0);
							SharedPreferences.Editor ee = settings.edit();
							ee.putString("Start", "not");
							ee.putInt("position", position);
							ee.commit();
						}
						
						else if((hdet.equals("Start") && GridMoodFragment.mud.equals("Happy"))){
							position = 0;
							settings = getActivity().getSharedPreferences(
									PREFS_HAPPY, 0);
							SharedPreferences.Editor ee = settings.edit();
							ee.putString("Start", "not");
							ee.putInt("position", position);
							ee.commit();
						}
						
						else if((edet.equals("Start") && GridMoodFragment.mud.equals("Energetic"))){
							position = 0;
							settings = getActivity().getSharedPreferences(
									PREFS_ENERGETIC, 0);
							SharedPreferences.Editor ee = settings.edit();
							ee.putString("Start", "not");
							ee.putInt("position", position);
							ee.commit();
						}
						
						
						else {

							// to comment
							if (GridMoodFragment.mud.equals("Angry") ) {
								settings = getActivity().getSharedPreferences(
										PREFS_ANGRY, 0);
								int todo = settings
										.getInt("position", position);
								position = todo;
							} else if (GridMoodFragment.mud.equals("Sad")) {
								settings = getActivity().getSharedPreferences(
										PREFS_SAD, 0);
								int todo = settings
										.getInt("position", position);
								position = todo;
							} else if (GridMoodFragment.mud.equals("Relax") ) {
								settings = getActivity().getSharedPreferences(
										PREFS_RELAXED, 0);
								int todo = settings
										.getInt("position", position);
								position = todo;
							} else if (GridMoodFragment.mud.equals("Happy")) {
								settings = getActivity().getSharedPreferences(
										PREFS_HAPPY, 0);
								int todo = settings
										.getInt("position", position);
								position = todo;
							} else if (GridMoodFragment.mud.equals("Energetic")) {
								settings = getActivity().getSharedPreferences(
										PREFS_ENERGETIC, 0);
								int todo = settings
										.getInt("position", position);
								position = todo;
								Toast.makeText(getActivity(), "entered energetic", Toast.LENGTH_LONG).show();
							}

							else {
								SharedPreferences settings = getActivity()
										.getSharedPreferences(PREFS_NAME, 0);
								SharedPreferences.Editor editor = settings
										.edit();
								editor.putInt("position", currentPos);
								editor.commit();
							}
						}

					}
					//currentlysaved
					else if(sx.getString("yess","").equals("yess")){
						if((adet.equals("Start") && tough.equals("Angry"))){
							position = 0;
							settings = getActivity().getSharedPreferences(
									PREFS_ANGRY, 0);
							SharedPreferences.Editor ee = settings.edit();
							ee.putString("Start", "not");
							ee.putInt("position", position);
							ee.commit();
						}
						
						else if((sdet.equals("Start") && tough.equals("Sad"))){
							position = 0;
							settings = getActivity().getSharedPreferences(
									PREFS_SAD, 0);
							SharedPreferences.Editor ee = settings.edit();
							ee.putString("Start", "not");
							ee.putInt("position", position);
							ee.commit();
						}
						
						else if((rdet.equals("Start") &&tough.equals("Relax"))){
							position = 0;
							settings = getActivity().getSharedPreferences(
									PREFS_RELAXED, 0);
							SharedPreferences.Editor ee = settings.edit();
							ee.putString("Start", "not");
							ee.putInt("position", position);
							ee.commit();
						}
						
						else if((hdet.equals("Start") && tough.equals("Happy"))){
							position = 0;
							settings = getActivity().getSharedPreferences(
									PREFS_HAPPY, 0);
							SharedPreferences.Editor ee = settings.edit();
							ee.putString("Start", "not");
							ee.putInt("position", position);
							ee.commit();
						}
						
						else if((edet.equals("Start") && tough.equals("Energetic"))){
							position = 0;
							settings = getActivity().getSharedPreferences(
									PREFS_ENERGETIC, 0);
							SharedPreferences.Editor ee = settings.edit();
							ee.putString("Start", "not");
							ee.putInt("position", position);
							ee.commit();
						}
					else{
						if (tough.equals("Angry") ) {
							settings = getActivity().getSharedPreferences(
									PREFS_ANGRY, 0);
							int todo = settings
									.getInt("position", 0);
							position = todo;
						} else if (tough.equals("Sad")) {
							settings = getActivity().getSharedPreferences(
									PREFS_SAD, 0);
							int todo = settings
									.getInt("position", 0);
							position = todo;
						} else if (tough.equals("Relax") ) {
							settings = getActivity().getSharedPreferences(
									PREFS_RELAXED, 0);
							int todo = settings
									.getInt("position", 0);
							position = todo;
						} else if (tough.equals("Happy")) {
							settings = getActivity().getSharedPreferences(
									PREFS_HAPPY, 0);
							int todo = settings
									.getInt("position", 0);
							position = todo;
						} else if (tough.equals("Energetic")) {
							settings = getActivity().getSharedPreferences(
									PREFS_ENERGETIC, 0);
							int todo = settings
									.getInt("position", 0);
							position = todo;
							Toast.makeText(getActivity(), "entered energetic" + position, Toast.LENGTH_LONG).show();
						}
					}
						//bb

						
						firsturl = mood_list.get(position)
								.getUrl();
						HomeFragment.mediaPlayer.reset();
						// cover_url = "";
						new ReadImageCovTask().execute(url);
						songtitle = mood_list.get(position)
								.getTitle();
						HomeFragment.songtitletwo
								.setText(mood_list.get(
										position).getTitle());
						HomeFragment.songtitle
								.setText(mood_list.get(
										position).getTitle());
						HomeFragment.artistname
								.setText(mood_list.get(
										position).getArtist());

						HomeFragment.artistnametwo
								.setText(mood_list.get(
										position).getArtist());
						
						Log.d("HELLO", "position: "+position+"\nFirsturl: "+firsturl);

						try {

							if (HomeFragment.mediaPlayer.isPlaying())
								HomeFragment.mediaPlayer.reset();

							//
							HomeFragment.mediaPlayer.setDataSource(firsturl); // setup
							// song

							// from
							// http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3
							// URL to
							// mediaplayer
							// data
							// source
							HomeFragment.mediaPlayer.prepare(); // you must call
																// this
							// method

							// after
							// setup
							// the datasource in
							// setDataSource
							// method. After calling
							// prepare()
							// the
							// instance of MediaPlayer
							// starts
							// load
							// data from URL to internal
							// buffer.
						} catch (Exception e) {
							Toast.makeText(getActivity(), e.getMessage(),
									Toast.LENGTH_LONG).show();
						}

						HomeFragment.mediaFileLengthInMilliseconds = HomeFragment.mediaPlayer
								.getDuration();

						// mediaPlayer.start();
						
						getActivity().startService(
								new Intent(StreamSongService.ACTION_MOODLIST)
										.putExtra(
												"value_artist",
												mood_list
														.get(position).getArtist())
										.putExtra(
												"value_title",
												mood_list
														.get(position).getTitle()));
						HomeFragment.primarySeekBarProgressUpdater();

						// SHARED PREFERENCES SAVING
						SharedPreferences settings = getActivity()
								.getSharedPreferences(PREFS_NAME, 0);
						SharedPreferences.Editor editor = settings.edit();

						editor.putString("artistnameprefs",
								mood_list.get(position)
										.getArtist());
						editor.putString("songtitleprefs",
								mood_list.get(position)
										.getTitle());
						editor.putString("songurlprefs",
								mood_list.get(position)
										.getUrl());
						
						//editor.putInt("position",currentPos);

						editor.commit();
						// SHARED PREFERENCES SAVING

						getActivity().startService(
								new Intent(StreamSongService.ACTION_PLAYPAUSE)
										.putExtra("value_url", firsturl));

						HomeFragment.follow.setVisibility(View.GONE);
						HomeFragment.followme.setVisibility(View.VISIBLE);

						HomeFragment.playtwo.setVisibility(View.GONE);
						HomeFragment.pausetwo.setVisibility(View.VISIBLE);
						
						if(sx.getString("yess","").equals("yess")){
							HomeFragment.previous.setVisibility(View.INVISIBLE);
							HomeFragment.next.setVisibility(View.INVISIBLE);
						}
						
						else{
							HomeFragment.previous.setVisibility(View.VISIBLE);
							HomeFragment.next.setVisibility(View.VISIBLE);
						}
						
						
						//ss
					}
					
					
									
					currentPos = position;
//					if (!GridMoodFragment.flagClick) {
//						settings = getActivity().getSharedPreferences(
//								PREFS_NAME, 0);
//						int todo = settings.getInt("position", 0);
//						currentPos = todo;
//					}
				
					//currentPos = 0;
					
					firsturl = GridMoodFragment.mood_list.get(position)
							.getUrl();
					HomeFragment.mediaPlayer.reset();
					// cover_url = "";
					new ReadImageCovTask().execute(url);
					songtitle = GridMoodFragment.mood_list.get(position)
							.getTitle();
					HomeFragment.songtitletwo
							.setText(GridMoodFragment.mood_list.get(
									position).getTitle());
					HomeFragment.songtitle
							.setText(GridMoodFragment.mood_list.get(
									position).getTitle());
					HomeFragment.artistname
							.setText(GridMoodFragment.mood_list.get(
									position).getArtist());

					HomeFragment.artistnametwo
							.setText(GridMoodFragment.mood_list.get(
									position).getArtist());
					
					Log.d("HELLO", "position: "+position+"\nFirsturl: "+firsturl);

					try {

						if (HomeFragment.mediaPlayer.isPlaying())
							HomeFragment.mediaPlayer.reset();

						//
						HomeFragment.mediaPlayer.setDataSource(firsturl); // setup
						// song

						// from
						// http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3
						// URL to
						// mediaplayer
						// data
						// source
						HomeFragment.mediaPlayer.prepare(); // you must call
															// this
						// method

						// after
						// setup
						// the datasource in
						// setDataSource
						// method. After calling
						// prepare()
						// the
						// instance of MediaPlayer
						// starts
						// load
						// data from URL to internal
						// buffer.
					} catch (Exception e) {
						Toast.makeText(getActivity(), e.getMessage(),
								Toast.LENGTH_LONG).show();
					}

					HomeFragment.mediaFileLengthInMilliseconds = HomeFragment.mediaPlayer
							.getDuration();

					// mediaPlayer.start();
					
					getActivity().startService(
							new Intent(StreamSongService.ACTION_MOODLIST)
									.putExtra(
											"value_artist",
											GridMoodFragment.mood_list
													.get(position).getArtist())
									.putExtra(
											"value_title",
											GridMoodFragment.mood_list
													.get(position).getTitle()));
					HomeFragment.primarySeekBarProgressUpdater();

					// SHARED PREFERENCES SAVING
					SharedPreferences settings = getActivity()
							.getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();

					editor.putString("artistnameprefs",
							GridMoodFragment.mood_list.get(position)
									.getArtist());
					editor.putString("songtitleprefs",
							GridMoodFragment.mood_list.get(position)
									.getTitle());
					editor.putString("songurlprefs",
							GridMoodFragment.mood_list.get(position)
									.getUrl());
					
					//editor.putInt("position",currentPos);

					editor.commit();
					// SHARED PREFERENCES SAVING

					getActivity().startService(
							new Intent(StreamSongService.ACTION_PLAYPAUSE)
									.putExtra("value_url", firsturl));

					HomeFragment.follow.setVisibility(View.GONE);
					HomeFragment.followme.setVisibility(View.VISIBLE);

					HomeFragment.playtwo.setVisibility(View.GONE);
					HomeFragment.pausetwo.setVisibility(View.VISIBLE);
					
					if(!GridMoodFragment.flagClick){
						HomeFragment.previous.setVisibility(View.INVISIBLE);
						HomeFragment.next.setVisibility(View.INVISIBLE);
					}
					
					else{
						HomeFragment.previous.setVisibility(View.VISIBLE);
						HomeFragment.next.setVisibility(View.VISIBLE);
					}

				}

			});
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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

				if (event.getAction() == KeyEvent.ACTION_UP
						&& keyCode == KeyEvent.KEYCODE_BACK) {

					// handle back button

					final int drawerState = mMenuDrawer.getDrawerState();

					if (drawerState == MenuDrawer.STATE_OPEN
							|| drawerState == MenuDrawer.STATE_OPENING) {
						mMenuDrawer.closeMenu();

					} else if (homefrag_connect.mLayout.collapsePanel()) {
						homefrag_connect.mLayout.collapsePanel();
					} else if (homefrag_connect.mLayout != null
							&& homefrag_connect.mLayout.isPanelExpanded()
							|| homefrag_connect.mLayout.isPanelAnchored()) {
						homefrag_connect.mLayout.collapsePanel();
					}

					return true;

				}

				return false;
			}
		});

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		try {
			currentPos += 1;	
			//Toast.makeText(getActivity(),"currentPos: "+currentPos, Toast.LENGTH_LONG).show();
			sx = getActivity().getSharedPreferences(PREFS_LIST, 0);
			if(det == true){
				Toast.makeText(getActivity(),"currentPos: "+currentPos, Toast.LENGTH_LONG).show();
				if (currentPos == mood_list.size()) {
					HomeFragment.mediaPlayer.stop();
					Toast.makeText(getActivity(), "You have finished Mood Elevate!", Toast.LENGTH_LONG).show();
					if(tough.equals("Angry")){
						SharedPreferences angrys = getActivity().getSharedPreferences(PREFS_ANGRY, 0);
						SharedPreferences.Editor angrye = angrys.edit();
						angrye.putString("random", "yes");
						angrye.putString("itemlist", "");
						angrye.putString("Start", "Start");
						angrye.commit();
					}
					else if(tough.equals("Sad")){
						SharedPreferences sads = getActivity().getSharedPreferences(PREFS_SAD, 0);
						SharedPreferences.Editor sade = sads.edit();
						sade.putString("random", "yes");
						sade.putString("itemlist", "");
						sade.putString("Start", "Start");
						sade.commit();
					}
					else if(tough.equals("Relax")){
						SharedPreferences relaxs = getActivity().getSharedPreferences(PREFS_RELAXED, 0);
						SharedPreferences.Editor relaxe = relaxs.edit();
						relaxe.putString("random", "yes");
						relaxe.putString("itemlist", "");
						relaxe.putString("Start", "Start");
						relaxe.commit();
					}
					else if(tough.equals("Happy")){
						SharedPreferences happys = getActivity().getSharedPreferences(PREFS_HAPPY, 0);
						SharedPreferences.Editor happye = happys.edit();
						happye.putString("random", "yes");
						happye.putString("itemlist", "");
						happye.putString("Start", "Start");
						happye.commit();
					}
					else if(tough.equals("Energetic")){
						SharedPreferences energetics = getActivity().getSharedPreferences(PREFS_ENERGETIC, 0);
						SharedPreferences.Editor energetice = energetics.edit();
						energetice.putString("random", "yes");
						energetice.putString("itemlist", "");
						energetice.putString("Start", "Start");
						energetice.commit();
					}
					SharedPreferences mn = getActivity().getSharedPreferences(PREFS_LIST, 0);
					SharedPreferences.Editor sn = mn.edit();
					sn.clear();
					sn.commit();
					
					det = false;
					
				}
				
				if(tough.equals("Angry")){
					SharedPreferences angrys = getActivity().getSharedPreferences(PREFS_ANGRY, 0);
					SharedPreferences.Editor angrye = angrys.edit();
					angrye.putInt("position", currentPos);
					angrye.commit();
				}
				else if(tough.equals("Sad")){
					SharedPreferences sads = getActivity().getSharedPreferences(PREFS_SAD, 0);
					SharedPreferences.Editor sade = sads.edit();
					sade.putInt("position", currentPos);
					sade.commit();
				}
				else if(tough.equals("Relax")){
					SharedPreferences relaxs = getActivity().getSharedPreferences(PREFS_RELAXED, 0);
					SharedPreferences.Editor relaxe = relaxs.edit();
					relaxe.putInt("position", currentPos);
					relaxe.commit();
				}
				else if(tough.equals("Happy")){
					SharedPreferences happys = getActivity().getSharedPreferences(PREFS_HAPPY, 0);
					SharedPreferences.Editor happye = happys.edit();
					happye.putInt("position", currentPos);
					happye.commit();
				}
				else if(tough.equals("Energetic")){
					SharedPreferences energetics = getActivity().getSharedPreferences(PREFS_ENERGETIC, 0);
					SharedPreferences.Editor energetice = energetics.edit();
					energetice.putInt("position", currentPos);
					energetice.commit();
				}
				
				else{
					SharedPreferences settings = getActivity()
							.getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("position", currentPos);
					editor.commit();
				}
				
				//begin
				//newstart
				firsturl = mood_list.get(currentPos)
						.getUrl();
				HomeFragment.mediaPlayer.reset();
				// cover_url = "";
				new ReadImageCovTask().execute(url);
				songtitle = mood_list.get(currentPos)
						.getTitle();
				HomeFragment.songtitletwo
						.setText(mood_list.get(
								currentPos).getTitle());
				HomeFragment.songtitle
						.setText(mood_list.get(
								currentPos).getTitle());
				HomeFragment.artistname
						.setText(mood_list.get(
								currentPos).getArtist());

				HomeFragment.artistnametwo
						.setText(mood_list.get(
								currentPos).getArtist());
				Toast.makeText(getActivity(), "position: "+ currentPos + " firsturl: "+ firsturl, Toast.LENGTH_LONG).show();
				Log.d("HELLO", "position: "+currentPos+"\nFirsturl: "+firsturl);

				try {

					if (HomeFragment.mediaPlayer.isPlaying())
						HomeFragment.mediaPlayer.reset();

					//
					HomeFragment.mediaPlayer.setDataSource(firsturl); // setup
					// song

					// from
					// http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3
					// URL to
					// mediaplayer
					// data
					// source
					HomeFragment.mediaPlayer.prepare(); // you must call
														// this
					// method

					// after
					// setup
					// the datasource in
					// setDataSource
					// method. After calling
					// prepare()
					// the
					// instance of MediaPlayer
					// starts
					// load
					// data from URL to internal
					// buffer.
				} catch (Exception e) {
					Toast.makeText(getActivity(), e.getMessage(),
							Toast.LENGTH_LONG).show();
				}

				HomeFragment.mediaFileLengthInMilliseconds = HomeFragment.mediaPlayer
						.getDuration();

				// mediaPlayer.start();
				
				getActivity().startService(
						new Intent(StreamSongService.ACTION_MOODLIST)
								.putExtra(
										"value_artist",
										mood_list
												.get(currentPos).getArtist())
								.putExtra(
										"value_title",
										mood_list
												.get(currentPos).getTitle()));
				HomeFragment.primarySeekBarProgressUpdater();

				// SHARED PREFERENCES SAVING
				SharedPreferences settings = getActivity()
						.getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();

				editor.putString("artistnameprefs",
						mood_list.get(currentPos)
								.getArtist());
				editor.putString("songtitleprefs",
						mood_list.get(currentPos)
								.getTitle());
				editor.putString("songurlprefs",
						mood_list.get(currentPos)
								.getUrl());
				
				//editor.putInt("position",currentPos);

				editor.commit();
				// SHARED PREFERENCES SAVING

				getActivity().startService(
						new Intent(StreamSongService.ACTION_PLAYPAUSE)
								.putExtra("value_url", firsturl));

				HomeFragment.follow.setVisibility(View.GONE);
				HomeFragment.followme.setVisibility(View.VISIBLE);

				HomeFragment.playtwo.setVisibility(View.GONE);
				HomeFragment.pausetwo.setVisibility(View.VISIBLE);
				
				if(sx.getString("yess","").equals("yess")){
					HomeFragment.previous.setVisibility(View.INVISIBLE);
					HomeFragment.next.setVisibility(View.INVISIBLE);
				}
				
				else{
					HomeFragment.previous.setVisibility(View.VISIBLE);
					HomeFragment.next.setVisibility(View.VISIBLE);
				}
				
				//end
				
				
			}
			

			else if (currentPos == GridMoodFragment.mood_list.size()) {
				if(!GridMoodFragment.flagClick){
					HomeFragment.mediaPlayer.stop();
					Toast.makeText(getActivity(), "You have finished Mood Elevate!", Toast.LENGTH_LONG).show();
					if(GridMoodFragment.mud.equals("Angry")){
						SharedPreferences angrys = getActivity().getSharedPreferences(PREFS_ANGRY, 0);
						SharedPreferences.Editor angrye = angrys.edit();
						angrye.putString("random", "yes");
						angrye.putString("itemlist", "");
						angrye.putString("Start", "Start");
						angrye.commit();
					}
					else if(GridMoodFragment.mud.equals("Sad")){
						SharedPreferences sads = getActivity().getSharedPreferences(PREFS_SAD, 0);
						SharedPreferences.Editor sade = sads.edit();
						sade.putString("random", "yes");
						sade.putString("itemlist", "");
						sade.putString("Start", "Start");
						sade.commit();
					}
					else if(GridMoodFragment.mud.equals("Relax")){
						SharedPreferences relaxs = getActivity().getSharedPreferences(PREFS_RELAXED, 0);
						SharedPreferences.Editor relaxe = relaxs.edit();
						relaxe.putString("random", "yes");
						relaxe.putString("itemlist", "");
						relaxe.putString("Start", "Start");
						relaxe.commit();
					}
					else if(GridMoodFragment.mud.equals("Happy")){
						SharedPreferences happys = getActivity().getSharedPreferences(PREFS_HAPPY, 0);
						SharedPreferences.Editor happye = happys.edit();
						happye.putString("random", "yes");
						happye.putString("itemlist", "");
						happye.putString("Start", "Start");
						happye.commit();
					}
					else if(GridMoodFragment.mud.equals("Energetic")){
						SharedPreferences energetics = getActivity().getSharedPreferences(PREFS_ENERGETIC, 0);
						SharedPreferences.Editor energetice = energetics.edit();
						energetice.putString("random", "yes");
						energetice.putString("itemlist", "");
						energetice.putString("Start", "Start");
						energetice.commit();
					}
					
					
					GridMoodFragment.flagger = 0;
					
					
				}
				else
				currentPos = 0;
			}
			
			if (!GridMoodFragment.flagClick) {
//				SharedPreferences settings = getActivity()
//						.getSharedPreferences(PREFS_NAME, 0);
//				SharedPreferences.Editor editor = settings.edit();
//				editor.putInt("position", currentPos);
//				editor.commit();
				
				//to commit
				if(GridMoodFragment.mud.equals("Angry")){
					SharedPreferences angrys = getActivity().getSharedPreferences(PREFS_ANGRY, 0);
					SharedPreferences.Editor angrye = angrys.edit();
					angrye.putInt("position", currentPos);
					angrye.commit();
				}
				else if(GridMoodFragment.mud.equals("Sad")){
					SharedPreferences sads = getActivity().getSharedPreferences(PREFS_SAD, 0);
					SharedPreferences.Editor sade = sads.edit();
					sade.putInt("position", currentPos);
					sade.commit();
				}
				else if(GridMoodFragment.mud.equals("Relax")){
					SharedPreferences relaxs = getActivity().getSharedPreferences(PREFS_RELAXED, 0);
					SharedPreferences.Editor relaxe = relaxs.edit();
					relaxe.putInt("position", currentPos);
					relaxe.commit();
				}
				else if(GridMoodFragment.mud.equals("Happy")){
					SharedPreferences happys = getActivity().getSharedPreferences(PREFS_HAPPY, 0);
					SharedPreferences.Editor happye = happys.edit();
					happye.putInt("position", currentPos);
					happye.commit();
				}
				else if(GridMoodFragment.mud.equals("Energetic")){
					SharedPreferences energetics = getActivity().getSharedPreferences(PREFS_ENERGETIC, 0);
					SharedPreferences.Editor energetice = energetics.edit();
					energetice.putInt("position", currentPos);
					energetice.commit();
				}
				
				else{
					SharedPreferences settings = getActivity()
							.getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("position", currentPos);
					editor.commit();
				}
				
				
				HomeFragment.mediaPlayer.reset();
				// cover_url = "";
				new ReadImageCovTask().execute(url);

				firsturl = GridMoodFragment.mood_list.get(currentPos)
						.getUrl();
				songtitle = GridMoodFragment.mood_list.get(currentPos)
						.getTitle();

				HomeFragment.songtitletwo.setText(GridMoodFragment.mood_list
						.get(currentPos).getTitle());
				HomeFragment.songtitle.setText(GridMoodFragment.mood_list
						.get(currentPos).getTitle());
				HomeFragment.artistname.setText(GridMoodFragment.mood_list
						.get(currentPos).getArtist());
				HomeFragment.artistnametwo
						.setText(GridMoodFragment.mood_list.get(currentPos)
								.getArtist());

				try {

					if (HomeFragment.mediaPlayer.isPlaying())
						HomeFragment.mediaPlayer.reset();

					HomeFragment.mediaPlayer.setDataSource(firsturl);
					HomeFragment.mediaPlayer.prepare();
				} catch (Exception e) {
					Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
							.show();
				}

				HomeFragment.mediaFileLengthInMilliseconds = HomeFragment.mediaPlayer
						.getDuration();

				// mediaPlayer.start();
				getActivity().startService(
						new Intent(StreamSongService.ACTION_PLAYPAUSE).putExtra(
								"value_url", firsturl));
				HomeFragment.primarySeekBarProgressUpdater();
				
			}	
			
//			//begin
//			//newstart
//			if(tough.equals("Angry")){
//				SharedPreferences angrys = getActivity().getSharedPreferences(PREFS_ANGRY, 0);
//				SharedPreferences.Editor angrye = angrys.edit();
//				angrye.putInt("position", currentPos);
//				angrye.commit();
//			}
//			else if(tough.equals("Sad")){
//				SharedPreferences sads = getActivity().getSharedPreferences(PREFS_SAD, 0);
//				SharedPreferences.Editor sade = sads.edit();
//				sade.putInt("position", currentPos);
//				sade.commit();
//			}
//			else if(tough.equals("Relax")){
//				SharedPreferences relaxs = getActivity().getSharedPreferences(PREFS_RELAXED, 0);
//				SharedPreferences.Editor relaxe = relaxs.edit();
//				relaxe.putInt("position", currentPos);
//				relaxe.commit();
//			}
//			else if(tough.equals("Happy")){
//				SharedPreferences happys = getActivity().getSharedPreferences(PREFS_HAPPY, 0);
//				SharedPreferences.Editor happye = happys.edit();
//				happye.putInt("position", currentPos);
//				happye.commit();
//			}
//			else if(tough.equals("Energetic")){
//				SharedPreferences energetics = getActivity().getSharedPreferences(PREFS_ENERGETIC, 0);
//				SharedPreferences.Editor energetice = energetics.edit();
//				energetice.putInt("position", currentPos);
//				energetice.commit();
//			}
//			
//			else{
//				SharedPreferences settings = getActivity()
//						.getSharedPreferences(PREFS_NAME, 0);
//				SharedPreferences.Editor editor = settings.edit();
//				editor.putInt("position", currentPos);
//				editor.commit();
//			}
//			
//			
//			//playing
//			HomeFragment.mediaPlayer.reset();
//			// cover_url = "";
//			new ReadImageCovTask().execute(url);
//
//			firsturl = mood_list.get(currentPos)
//					.getUrl();
//			songtitle = mood_list.get(currentPos)
//					.getTitle();
//
//			HomeFragment.songtitletwo.setText(mood_list
//					.get(currentPos).getTitle());
//			HomeFragment.songtitle.setText(mood_list
//					.get(currentPos).getTitle());
//			HomeFragment.artistname.setText(mood_list
//					.get(currentPos).getArtist());
//			HomeFragment.artistnametwo
//					.setText(mood_list.get(currentPos)
//							.getArtist());
//
//			try {
//
//				if (HomeFragment.mediaPlayer.isPlaying())
//					HomeFragment.mediaPlayer.reset();
//
//				HomeFragment.mediaPlayer.setDataSource(firsturl);
//				HomeFragment.mediaPlayer.prepare();
//			} catch (Exception e) {
//				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
//						.show();
//			}
//
//			HomeFragment.mediaFileLengthInMilliseconds = HomeFragment.mediaPlayer
//					.getDuration();
//
//			// mediaPlayer.start();
//			getActivity().startService(
//					new Intent(StreamSongService.ACTION_PLAYPAUSE).putExtra(
//							"value_url", firsturl));
//			HomeFragment.primarySeekBarProgressUpdater();
//			//end

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

}
