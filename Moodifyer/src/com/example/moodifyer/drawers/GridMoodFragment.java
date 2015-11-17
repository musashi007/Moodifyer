package com.example.moodifyer.drawers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.moodifyer.HomePageActivity;
import com.example.moodifyer.R;
import com.example.moodifyer.adapter.MoodAdapter;
import com.example.moodifyer.adapter.MoodGridViewAdapter;
import com.example.moodifyer.adapter.MusicListAdapter;
import com.example.moodifyer.pojo.MoodDetails;
import com.example.moodifyer.pojo.MusicDetails;
import com.google.gson.Gson;



public class GridMoodFragment extends Fragment {

	public static int GlobalPos;
	private static final int SELECT_PICTURE = 1;
	public static int flagger = 0;
	public boolean ctrlsw = false;
	public static String localmood;
	public static Boolean flagClick = true;
	Uri selectedImageUri;
	String selectedPath;
	Drawable drawable;
	public static ArrayList<MoodDetails> myList;
	List<Float> valenceTotal;
	public Handler handler;
	public static int atotal, stotal, htotal, rtotal, retotal;
	public static int p = 0;
	public static String sv = "";

	public static MoodDetails md;

	public int counter = 0, counts = 0, finalCounter = 0;
	public Context xx;

	public static Context context;
	public static MoodGridViewAdapter adapter;
	public static ArrayList<MusicDetails> mood_listAngry = new ArrayList<MusicDetails>();
	public static ArrayList<MusicDetails> mood_listSad = new ArrayList<MusicDetails>();
	public static ArrayList<MusicDetails> mood_listHappy = new ArrayList<MusicDetails>();
	public static ArrayList<MusicDetails> mood_listRelax = new ArrayList<MusicDetails>();
	public static ArrayList<MusicDetails> mood_listEnergetic = new ArrayList<MusicDetails>();
	public static float itemtotal;
	public float angrysum, happysum, relaxsum;
	public static float sadsum, energeticsum;
	public static float angrytotal, sadtotal, happytotal, relaxtotal,
			energetictotal,totals;
	public Dialog dialog;
	public static String mud;
	public ListView lv;
	static String urls = "http://de.fio.re/MP2/raw_valence.php";
	static String url_genre = "http://de.fio.re/MP/default.php";

	public int ANGRY = 0, SAD = 1, HAPPY = 2, RELAX = 3, ENERGETIC = 4,
			moodele = 5;

	public int pos = 0;
	public static ArrayList<MusicDetails> mood_list = new ArrayList<MusicDetails>();
	public static String[] valence = { "VERY LOW", "LOW", "AVERAGE", "HIGH",
			"VERY HIGH" };
	public static int[] pics = { R.drawable.angrymood, R.drawable.sadmood,
			R.drawable.happymood, R.drawable.relaxedmood,
			R.drawable.energeticmood };

	public float[] valenceT = new float[5];
	public String[] moods = new String[] { "Angry", "Sad", "Happy", "Relax",
			"Energetic" };
	public int currentPos;
	public Bitmap bmp;
	public boolean elevate = false;
	public ProgressDialog mProgressDialog;
	public MenuDrawer mMenuDrawer;
	HomeFragment homefrag_connect;
	HomePageActivity homepage_connect;
	public ViewGroup root;
	public Button goBtn;
	public static ListView listview2;
	public static GridView gridView;
	public String moodtapped = "";
	public String genre = "";
	public String usernametwo;
	public ViewPager pager;
	public Bitmap yourSelectedImage;
	public static int dd;

	public static SharedPreferences settings;
	public static final String PREFS_MOOD_LENGTH = "PrefsMoodLength";
	public static final String PREFS_NAME = "Auth_PREFS";
	
	public static final String PREFS_DET = "DeterminePos";	
	
	//moodprefs
		public static final String PREFS_ANGRY = "Angrymood";
		public static final String PREFS_SAD = "Sadmood";
		public static final String PREFS_RELAXED = "Relaxedmood";
		public static final String PREFS_HAPPY = "Happymood";
		public static final String PREFS_ENERGETIC = "Energeticmood";
		
		public static final String PREFS_LIST =  "mood";
		public static SharedPreferences sx;
		private String selectedImagePath;
		
		//
		public static final String PREFS_PIC = "pic";
		public static final String PREFS_ANGRYPIC = "Angrypic";
		public static final String PREFS_SADPIC = "Sadpic";
		public static final String PREFS_HAPPYPIC = "Happypic";
		public static final String PREFS_RELAXEDPIC = "Relaxedpic";
		public static final String PREFS_ENERGETICPIC = "Energeticpic";
		public static final String PREFS_ELEVATEPIC = "Elevatepic";
	public static GridMoodFragment newInstance(String text) {

		GridMoodFragment f = new GridMoodFragment();
		Bundle b = new Bundle();
		b.putString("msg", text);

		f.setArguments(b);

		return f;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// xx = getActivity().getApplicationContext();
		
		context = getActivity();
		root = (ViewGroup) inflater.inflate(
				R.layout.activity_weeklytoptracks_fragment, null);

		listview2 = (ListView) root.findViewById(R.id.listview2);

		new ReadUserGenreTask().execute(url_genre);
		
		pager = HomeFragment.pager;
		gridView = (GridView) root.findViewById(R.id.gridview);
		adapter = new MoodGridViewAdapter(getActivity());
		gridView.setAdapter(adapter);
		
		//updated
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
//				settings = getActivity().getSharedPreferences(PREFS_PIC, 0);
//				SharedPreferences.Editor ed = settings.edit();
//				ed.putString("new", "yes");
//				ed.commit();
				
				switch (position) {
				case 0:
					HomePageActivity.StringGlobal = "Angry";
					break;
				case 1:
					HomePageActivity.StringGlobal = "Sad";
					break;
				case 2:
					HomePageActivity.StringGlobal = "Happy";
					break;
				case 3:
					HomePageActivity.StringGlobal = "Relaxed";
					break;
				case 4:
					HomePageActivity.StringGlobal = "Energetic";
					break;
				case 5:
					HomePageActivity.StringGlobal = "Mood Elevate";
					break;
				}
				GlobalPos = position;
				HomePageActivity.PosGlobal = position;
				Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
//                MoodGridViewAdapter.mItems.remove(0);
//    			adapter.notifyDataSetChanged();
				return true;
			}
			
			//toprove

			
			//toprove
			
			
			
			
		});
		//updated
		
		
		
		//outdated
//		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				//MoodGridViewAdapter.mItems.remove(position);
//				//MoodGridViewAdapter.mItems.set(position, );
//				//adapter.notifyDataSetChanged();
//				// MoodGridViewAdapter.mItems.add(0,new
//				// MoodGridViewAdapter.ItemFor("hello", R.drawable.logo));
//				changeImage(position);
//
//				return false;
//			}
//
//			public void changeImage(int position) {
//				Intent intent = new Intent();
//
//				intent.setType("image/*");
//
//				intent.setAction(Intent.ACTION_GET_CONTENT);
//				intent.putExtra("position", position);
//				Log.d("LAPTOP", "HELLO");
//
//				startActivityForResult(
//						Intent.createChooser(intent, "Select file to upload "),
//						10);
//
//			}
//
//			public void onActivityResult(int requestCode, int resultCode,
//					Intent data) {
//
//				if (resultCode == getActivity().RESULT_OK) {
//					if (data.getData() != null) {
//						selectedImageUri = data.getData();
//						Toast.makeText(getActivity(), "hello2", Toast.LENGTH_LONG).show();
//					} else {
//						Log.d("selectedPath1 : ", "Came here its null !");
//						Toast.makeText(getActivity(), "failed to get Image!",
//								500).show();
//					}
//
//					if (requestCode == 100
//							&& resultCode == getActivity().RESULT_OK) {
//						Bitmap photo = (Bitmap) data.getExtras().get("data");
//						selectedPath = getPath(selectedImageUri);
//						// drawable = new BitmapDrawable(selectedPath);
//						// preview.setImageURI(selectedImageUri);
//						// MoodGridViewAdapter.mItems.add(data.getIntExtra("position",
//						// 0),new MoodGridViewAdapter.Baba("hahahahaah",
//						// selectedImageUri));
//
//						Log.d("selectedPath1 : ", selectedPath);
//						Toast.makeText(getActivity(), "100", Toast.LENGTH_LONG).show();
//
//					}
//
//					if (requestCode == 10)
//
//					{
//
//						selectedPath = getPath(selectedImageUri);
//
//						// drawable = new BitmapDrawable(selectedPath);
//
//						InputStream is;
//						try {
//							is = getActivity().getContentResolver()
//									.openInputStream(selectedImageUri);
//							drawable = Drawable.createFromStream(is,
//									selectedImageUri.toString());
//
//						} catch (FileNotFoundException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
//						Log.d("selectedPath1 : ", selectedPath);
//						Toast.makeText(getActivity(), "10", Toast.LENGTH_LONG).show();
//
//					}
//
//				}
//				
//				
//
//			}
//
//			public String getPath(Uri uri) {
//				String[] projection = { MediaStore.Images.Media.DATA };
//
//				Cursor cursor = getActivity().managedQuery(uri, projection,
//						null, null, null);
//
//				int column_index = cursor
//						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//				cursor.moveToFirst();
//
//				return cursor.getString(column_index);
//			}
//
//		});

		//outdated
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (moodele == position) {
					// new ReadJSONFeedTask().execute(url);
					// HomeFragment.mActionBar.setTitle("MOOD ELEVATE");
					elevate = true;
				} else {
					elevate = false;
					if (ANGRY == position) {
						HomeFragment.mActionBar.setTitle("ANGRY");
						moodtapped = "ANGRY";
						pos = position;
					} else if (SAD == position) {
						HomeFragment.mActionBar.setTitle("SAD");
						moodtapped = "SAD";
					} else if (HAPPY == position) {
						HomeFragment.mActionBar.setTitle("HAPPY");
						moodtapped = "HAPPY";
					} else if (RELAX == position) {
						HomeFragment.mActionBar.setTitle("RELAXED");
						moodtapped = "RELAXED";
					} else if (ENERGETIC == position) {
						HomeFragment.mActionBar.setTitle("ENERGETIC");
						moodtapped = "ENERGETIC";
					} else {
						HomeFragment.mActionBar.setTitle("HOME");
					}
				}
				new ReadJSONFeedTask().execute(urls);

			}
		});

		return root;
	}
	


	private class ReadUserGenreTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String s = postData(params);

			return s;
		}

		private String postData(String[] params) {

			String jsonResult = "";

			JSONArray jsonArray;

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet verifyAccountget = new HttpGet(url_genre);

			try {
				HttpResponse response = httpclient.execute(verifyAccountget);
				jsonResult = inputStreamToString(
						response.getEntity().getContent()).toString();
				JSONObject obj1 = new JSONObject(jsonResult);
				jsonArray = obj1.getJSONArray("users");

				settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
				String user_pref = settings.getString("username_prefs", "");
				// String pass_pref = settings.getString("password_prefs", "");

				usernametwo = user_pref;
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj2 = jsonArray.getJSONObject(i);

					if (obj2.getString("username").equals(user_pref)) {

						genre = obj2.getString("genre");
					}
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		private StringBuilder inputStreamToString(InputStream is) {
			String rLine = "";
			StringBuilder answer = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				while ((rLine = rd.readLine()) != null) {
					answer.append(rLine);
				}
			}

			catch (IOException e) {
				e.printStackTrace();
			}
			return answer;
		}

		protected void onPostExecute(String result) {
		}

	}

	private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(getActivity());
			// Set progressdialog title
			mProgressDialog.setTitle(null);
			// Set progressdialog message
			mProgressDialog.setMessage("Loading playlist...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

			// Show progressdialog
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return readJSONFeed(arg0[0]);
		}

		protected void onPostExecute(String result) {
			mood_list = new ArrayList<MusicDetails>();

			String artist, title, songGenre, mood, url, songValence, cover_url;
			try {
				JSONArray jar = new JSONArray(result);
				for (int i = 0; i < jar.length(); i++) {
					itemtotal = jar.length() - 1;
					JSONObject song = jar.getJSONObject(i);
					artist = song.getString("artist");
					title = song.getString("title");
					songGenre = song.getString("genre");
					cover_url = song.getString("cover_url");
					url = song.getString("url");
					songValence = song.getString("valence");

					mood = song.getString("mood");

					// if (counter == finalCounter) {
					// if (mood.equals("ANGRY")) {
					// angrysum += Float.parseFloat(songValence);
					// } else if (mood.equals("SAD")) {
					// sadsum += Float.parseFloat(songValence);
					// } else if (mood.equals("HAPPY")) {
					// happysum += Float.parseFloat(songValence);
					// } else if (mood.equals("RELAX")) {
					// relaxsum += Float.parseFloat(songValence);
					// } else if (mood.equals("ENERGETIC")) {
					// energeticsum += Float.parseFloat(songValence);
					// }
					// counter++;
					// }

					if (!elevate) {

						if (mood.equals(moodtapped) && songGenre.equals(genre)) {

							mood_list.add(new MusicDetails(artist, title,
									songGenre, mood, url));
							localmood = "DEFAULT";

						} else if (mood.equals(moodtapped)
								&& !songGenre.equals(genre)) {
							mood_list.add(new MusicDetails(artist, title,
									songGenre, mood, url));
							localmood = "DEFAULT";
						}

					} else {

						// ELEVATEE
						if (mood.equals("ANGRY")) {
							atotal++;
							localmood = "ANGRY";
							angrysum += Float.parseFloat(songValence);
							mood_listAngry.add(new MusicDetails(artist + "\n"
									+ mood, title, songGenre, mood, url,
									songValence));

						} else if (mood.equals("SAD")) {
							stotal++;
							localmood = "SAD";
							sadsum += Float.parseFloat(songValence);
							mood_listSad.add(new MusicDetails(artist + "\n"
									+ mood, title, songGenre, mood, url,
									songValence));
						} else if (mood.equals("HAPPY")) {
							htotal++;
							localmood = "HAPPY";
							happysum += Float.parseFloat(songValence);
							mood_listHappy.add(new MusicDetails(artist + "\n"
									+ mood, title, songGenre, mood, url,
									songValence));
						} else if (mood.equals("RELAXED")) {
							rtotal++;
							localmood = "RELAXED";
							relaxsum += Float.parseFloat(songValence);
							mood_listRelax.add(new MusicDetails(artist + "\n"
									+ mood, title, songGenre, mood, url,
									songValence));
						} else if (mood.equals("ENERGETIC")) {
							retotal++;
							localmood = "ENERGETIC";
							energeticsum += Float.parseFloat(songValence);
							mood_listEnergetic.add(new MusicDetails(artist + "\n"
									+ mood, title, songGenre, mood, url,
									songValence));
						}

					}

				}
				if (elevate) {
					flagClick = false;
					// Toast.makeText(context, "going to elevate",
					// Toast.LENGTH_LONG).show();
					if (counts == finalCounter) {

						angrytotal = angrysum / itemtotal;
						sadtotal = sadsum / itemtotal;
						happytotal = happysum / itemtotal;
						relaxtotal = relaxsum / itemtotal;
						energetictotal = energeticsum / itemtotal;
						
						dd = atotal+stotal+htotal+rtotal+retotal;
						
						totals = (atotal+stotal+htotal+rtotal+retotal)/itemtotal;

						valenceT[0] = angrytotal;
						valenceT[1] = sadtotal;
						valenceT[2] = happytotal;
						valenceT[3] = relaxtotal;
						valenceT[4] = energetictotal;
						counts++;
					}
//					Collections.shuffle(mood_listAngry);
//					Collections.shuffle(mood_listSad);
//					Collections.shuffle(mood_listRelax);
//					Collections.shuffle(mood_listHappy);
//					Collections.shuffle(mood_listEnergetic);
					
					
					
					whatsyourmood();

				} else {
					flagClick = true;
					mood_list = arrangeMoodGenre(mood_list);
				}

				Log.d("SIZE PO", mood_list.toString());

				// this
				// if (localmood.equals("ANGRY")) {
				// MoodListFragment.listview3.setAdapter(new MusicListAdapter(
				// getActivity(), mood_listAngry));
				// } else if (localmood.equals("SAD")) {
				// MoodListFragment.listview3.setAdapter(new MusicListAdapter(
				// getActivity(), mood_listSad));
				// } else if (localmood.equals("HAPPY")) {
				// MoodListFragment.listview3.setAdapter(new MusicListAdapter(
				// getActivity(), mood_listHappy));
				// } else if (localmood.equals("RELAXED")) {
				// MoodListFragment.listview3.setAdapter(new MusicListAdapter(
				// getActivity(), mood_listRelax));
				// } else if (localmood.equals("ENERGETIC")) {
				// MoodListFragment.listview3.setAdapter(new MusicListAdapter(
				// getActivity(), mood_listEnergetic));
				// } else {
				// MoodListFragment.listview3.setAdapter(new MusicListAdapter(
				// getActivity(), mood_list));
				// }
				MoodListFragment.listview3.setAdapter(new MusicListAdapter(
						getActivity(), mood_list));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}

			elevate = false;
			pager.setCurrentItem(1);

		}

		public void whatsyourmood() {
			dialog = new Dialog(getActivity());
			dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
			dialog.setTitle("What's your mood?");
			dialog.setContentView(R.layout.whatsyourmoodlist);
			dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
					android.R.drawable.ic_menu_edit);

			lv = (ListView) dialog.findViewById(R.id.listView_whatsyourmood);

			// populateData

			myList = new ArrayList<MoodDetails>();
			for (int v = 0; v < moods.length; v++) {
				md = new MoodDetails();
				md.setMood(moods[v]);
				md.setValence(String.valueOf(valenceT[v]));
				md.setImage(pics[v]);
				myList.add(md);

				lv.setAdapter(new MoodAdapter(context, myList));
			}

			// if(myList.isEmpty())
			// Toast.makeText(context, "myList is not empty",
			// Toast.LENGTH_LONG).show();
			// lv.setAdapter(new MoodAdapter(context, myList));

			// lv.setAdapter(new ArrayAdapter<String>(getActivity(),
			// android.R.layout.simple_list_item_1, moods));

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					switch (position) {
					case 0:
						settings = context.getSharedPreferences(PREFS_ANGRY,0);						
						mud = "Angry";
						if (settings.getString("random", "").equals("yes"))
							mood_list = arrangeElevate(mood_listAngry);
						else{
							settings = context.getSharedPreferences(PREFS_ANGRY, 0);
							String itemlist = settings.getString("itemlist", "");
							List<MusicDetails> moodf;
							Gson gson = new Gson();
							MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
							moodf = Arrays.asList(mitems);
							moodf = new ArrayList<MusicDetails>(moodf);
							mood_list =  (ArrayList<MusicDetails>) moodf;							
						}						
						MoodListFragment.listview3
								.setAdapter(new MusicListAdapter(context,
										mood_list));
						break;
					case 1:
						settings = context.getSharedPreferences(PREFS_SAD,0);	
						mud = "Sad";
						if (settings.getString("random", "").equals("yes"))
						mood_list = arrangeElevate(mood_listSad);
						else{
							settings = context.getSharedPreferences(PREFS_SAD, 0);
							String itemlist = settings.getString("itemlist", "");
							List<MusicDetails> moodf;
							Gson gson = new Gson();
							MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
							moodf = Arrays.asList(mitems);
							moodf = new ArrayList<MusicDetails>(moodf);
							mood_list =  (ArrayList<MusicDetails>) moodf;	
						}
						MoodListFragment.listview3
								.setAdapter(new MusicListAdapter(context,
										mood_list));
						break;
					case 2:
						settings = context.getSharedPreferences(PREFS_HAPPY,0);	
						mud = "Happy";
						if (settings.getString("random", "").equals("yes"))
						mood_list = arrangeElevate(mood_listHappy);
						else{
							settings = context.getSharedPreferences(PREFS_HAPPY, 0);
							String itemlist = settings.getString("itemlist", "");
							List<MusicDetails> moodf;
							Gson gson = new Gson();
							MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
							moodf = Arrays.asList(mitems);
							moodf = new ArrayList<MusicDetails>(moodf);
							mood_list =  (ArrayList<MusicDetails>) moodf;	
						}
						MoodListFragment.listview3
								.setAdapter(new MusicListAdapter(context,
										mood_list));
						break;
					case 3:
						settings = context.getSharedPreferences(PREFS_RELAXED,0);	
						mud = "Relax";
						if (settings.getString("random", "").equals("yes"))
						mood_list = arrangeElevate(mood_listRelax);
						else{
							settings = context.getSharedPreferences(PREFS_RELAXED, 0);
							String itemlist = settings.getString("itemlist", "");
							List<MusicDetails> moodf;
							Gson gson = new Gson();
							MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
							moodf = Arrays.asList(mitems);
							moodf = new ArrayList<MusicDetails>(moodf);
							mood_list =  (ArrayList<MusicDetails>) moodf;	
						}
						MoodListFragment.listview3
								.setAdapter(new MusicListAdapter(context,
										mood_list));
						break;
					case 4:
						settings = context.getSharedPreferences(PREFS_ENERGETIC,0);	
						mud = "Energetic";
						if (settings.getString("random", "").equals("yes"))
						mood_list = arrangeElevate(mood_listEnergetic);
						else{
							settings = context.getSharedPreferences(PREFS_ENERGETIC, 0);
							String itemlist = settings.getString("itemlist", "");
							List<MusicDetails> moodf;
							Gson gson = new Gson();
							MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
							moodf = Arrays.asList(mitems);
							moodf = new ArrayList<MusicDetails>(moodf);
							mood_list =  (ArrayList<MusicDetails>) moodf;	
						}
						MoodListFragment.listview3
								.setAdapter(new MusicListAdapter(context,
										mood_list));
						break;
					}
					
					SharedPreferences ss = getActivity().getSharedPreferences(PREFS_LIST, 0);
					SharedPreferences.Editor es = ss.edit();
					es.putString("mud", mud);
					es.putString("yess", "yess");
					es.commit();
					dialog.dismiss();

				}
			});

			// Intent i = new Intent(context, ListmoodActivity.class);
			// startActivity(i);
			dialog.show();
		}

	}

	@Override
	public void onAttach(Activity activity) {
		xx = activity;
		super.onAttach(activity);
	}

	private ArrayList<MusicDetails> arrangeMoodGenre(
			ArrayList<MusicDetails> moodList) {
		ArrayList<MusicDetails> combineMoodGenre = new ArrayList<MusicDetails>();
		ArrayList<MusicDetails> genreList = new ArrayList<MusicDetails>();
		ArrayList<MusicDetails> nongenreList = new ArrayList<MusicDetails>();

		for (int loop1 = 0; loop1 < moodList.size(); loop1++) {
			if (moodList.get(loop1).getGenre().equals(genre))
				genreList.add(moodList.get(loop1));

		}
		// SHUFFLE NIA SIYA
		Collections.shuffle(genreList);
		for (int loop2 = 0; loop2 < moodList.size(); loop2++) {
			if (!moodList.get(loop2).getGenre().equals(genre))
				nongenreList.add(moodList.get(loop2));
		}
		// SHUFFLE NIA SIYA
		Collections.shuffle(nongenreList);

		for (int loop3 = 0; loop3 < genreList.size(); loop3++) {
			combineMoodGenre.add(genreList.get(loop3));
		}
		for (int loop4 = 0; loop4 < nongenreList.size(); loop4++) {
			combineMoodGenre.add(nongenreList.get(loop4));
		}

		return combineMoodGenre;
	}

	public static ArrayList<MusicDetails> arrangeElevate(
			ArrayList<MusicDetails> elevateList) {
		int finalSize;
		ArrayList<MusicDetails> flag = new ArrayList<MusicDetails>();
		ArrayList<MusicDetails> testflag = new ArrayList<MusicDetails>();
		try {		
			int control;
			int supplement = 0;
			// Collections.shuffle(elevateList);
//			if (flagger == 0) {
//				Collections.sort(elevateList, MusicDetails.MuCompare);
//				Collections.sort(mood_listAngry, MusicDetails.MuCompare);
//				Collections.sort(mood_listSad, MusicDetails.MuCompare);
//				Collections.sort(mood_listRelax, MusicDetails.MuCompare);
//				Collections.sort(mood_listHappy, MusicDetails.MuCompare);
//				Collections.sort(mood_listEnergetic, MusicDetails.MuCompare);
//				flagger++;
//			}
			if(flagger == 0){
			Collections.shuffle(mood_listAngry);
			Collections.shuffle(mood_listSad);
			Collections.shuffle(mood_listRelax);
			Collections.shuffle(mood_listHappy);
			Collections.shuffle(mood_listEnergetic);
			flagger++;
			}
			p = 0;
			settings = context.getSharedPreferences(PREFS_MOOD_LENGTH, 0);
//			String finalSettings = settings.getString("meLength", "5");
//			finalSize = Integer.parseInt(finalSettings);
//			
//			if (finalSize <= 4)
//				finalSize = 5;
//			
//			if(finalSize >= 8 && finalSize <= 99)
//				finalSize = 7;
			//finalSize = (int)dd/5;
			//Toast.makeText(context, "itemtotal: "+itemtotal+"\ndd: "+dd, Toast.LENGTH_LONG).show();
			//int b = (int)finalSize;
			finalSize = 10;
			
			
			while (flag.size() < finalSize) {

				if (mud.equals("Angry")) {
					
					float temp = finalSize / 5;
					control = (int)temp;
					if(finalSize % 5 > 0){
						supplement = finalSize % 5;
						
					}
//					if (Float.parseFloat(elevateList.get(p).getValence()) >= angrytotal
//							&& !(flag.contains(elevateList.get(p)))) {
//						boolean semi = consider(p, flag, elevateList);
//						if (!semi) {
//							flag.add(elevateList.get(p));
//							p++;
//						} else {
//							p++;
//						}\
//					} else {
//						p++;
//					}
					for (int i = 0, q = 0; i < control; q++) {
						//if (Float.parseFloat(mood_listAngry.get(q).getValence()) >= angrytotal) {
						//boolean semi = consider(i, testflag, mood_listAngry);
						//if (!semi) {
							testflag.add(mood_listAngry.get(q));
							i++;
						//}
						
							sv = mood_listAngry.get(q).getValence();
							
					//	}
					}
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();
					
					for (int i = 0, q = 0; i < control; q++) {
						//if (Float.parseFloat(mood_listSad.get(q).getValence()) >= Float
						//		.parseFloat(sv)) {
						//boolean semi = consider(i, testflag, mood_listSad);
						//if(!semi){
							testflag.add(mood_listSad.get(q));
							i++;
					//	}
						
							sv = mood_listSad.get(q).getValence();
							
					//	}
					}
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();
					
					for (int i = 0, q = 0; i < control; q++) {
						//if (Float
						//		.parseFloat(mood_listRelax.get(q).getValence()) >= Float
						//		.parseFloat(sv)) {
						//boolean semi = consider(i, testflag, mood_listRelax);
						//if(!semi){
							testflag.add(mood_listRelax.get(q));
							i++;
					//	}
						
							sv = mood_listRelax.get(q).getValence();
							

						//}
					}
			Collections.sort(testflag, MusicDetails.MuCompare);
			flag.addAll(testflag);
			testflag.clear();
					for (int i = 0, q = 0; i < control; q++) {
						//if (Float
						//		.parseFloat(mood_listHappy.get(q).getValence()) >= Float
						//		.parseFloat(sv)) {
					//	boolean semi = consider(i, testflag, mood_listHappy);
					//	if(!semi){
							testflag.add(mood_listHappy.get(q));
							sv = mood_listHappy.get(q).getValence();
							i++;
					//		}
					//	else 
					//		continue;
						//}
					}
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();
					
					for (int i = 0, q = 0; i < control + supplement; q++) {
					//	if (Float.parseFloat(mood_listEnergetic.get(q).getValence()) >= Float
					//			.parseFloat(sv)) {
					//	boolean semi = consider(i, testflag, mood_listEnergetic);
					//	if(!semi){
							testflag.add(mood_listEnergetic.get(q));
							sv = mood_listEnergetic.get(q).getValence();
							i++;
					//	}
					//	else
					//		continue;
					//	}
					}
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();
					
					//putting angry
					settings = context.getSharedPreferences(PREFS_ANGRY, 0);
					Editor edit = settings.edit();
					Gson gson = new Gson();
					String itemlist = gson.toJson(flag);
					edit.putString("itemlist", itemlist);
					edit.putString("random", "no");
					edit.commit();
					
					
					

					// SAD SONGS
				} else if (mud.equals("Sad")) {
					
					float temp = finalSize / 4;
					control = (int)temp;
					if(finalSize % 4 > 0){
						supplement = finalSize % 4;
					}
					
					/*if (Float.parseFloat(elevateList.get(p).getValence()) >= sadtotal
							&& !(flag.contains(elevateList.get(p)))) {
						boolean semi = consider(p, flag, elevateList);
						if (!semi) {
							flag.add(elevateList.get(p));
							p++;
						} else {
							p++;
						}
					} else {
						p++;
					}*/
					for (int i = 0, q = 0; i < control; q++) {
						//if (Float.parseFloat(mood_listSad.get(q).getValence()) >= sadtotal) {
					//	boolean semi = consider(i, testflag, mood_listSad);
					//	if(!semi){
							testflag.add(mood_listSad.get(q));
							sv = mood_listSad.get(q).getValence();
							i++;
					//	}
					//	else
					//		continue;
						//}
					}					
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();
					
					for (int i = 0, q = 0; i < control; q++) {
						//if (Float
						//		.parseFloat(mood_listRelax.get(q).getValence()) >= Float
						//		.parseFloat(sv)) {
					//	boolean semi = consider(i, testflag, mood_listRelax);
					//	if(!semi){
							testflag.add(mood_listRelax.get(q));
							sv = mood_listRelax.get(q).getValence();
							i++;
					//	}
					//	else
					//		continue;

						//}
					}
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();
					
					for (int i = 0, q = 0; i < control; q++) {
						//if (Float
						//		.parseFloat(mood_listHappy.get(q).getValence()) >= Float
						//		.parseFloat(sv)) {
					//	boolean semi = consider(i, testflag, mood_listHappy);
					//	if(!semi){
							testflag.add(mood_listHappy.get(q));
							sv = mood_listHappy.get(q).getValence();
							i++;
					//	}
					//	else
					//		continue;
						//}
					}
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();
					
					
					for (int i = 0, q = 0; i < control+supplement; q++) {
						//if (Float.parseFloat(mood_listEnergetic.get(q).getValence()) >= Float
						//		.parseFloat(sv)) {
					//	boolean semi = consider(i, testflag, mood_listEnergetic);
					//	if(!semi){
							testflag.add(mood_listEnergetic.get(q));
							sv = mood_listEnergetic.get(q).getValence();
							i++;
					//		}
					//	else
					//		continue;
						//}
					}
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();
					
					//putting sad
					settings = context.getSharedPreferences(PREFS_SAD, 0);
					Editor edit = settings.edit();
					Gson gson = new Gson();
					String itemlist = gson.toJson(flag);
					edit.putString("itemlist", itemlist);
					edit.putString("random", "no");
					edit.commit();


				} else if (mud.equals("Happy")) {
					
					float temp = finalSize / 2;
					control = (int)temp;
					if(finalSize % 2 > 0){
						supplement = finalSize % 2;
					}
					
//					if (Float.parseFloat(elevateList.get(p).getValence()) >= happytotal
//							&& !(flag.contains(elevateList.get(p)))) {
//						boolean semi = consider(p, flag, elevateList);
//						if (!semi) {
//							flag.add(elevateList.get(p));
//							p++;
//						} else {
//							p++;
//						}
//					} else {
//						p++;
//					}
					
					for (int i = 0, q = 0; i < control; q++) {
						//if (Float
						//		.parseFloat(mood_listHappy.get(q).getValence()) >= happytotal) {
					//	boolean semi = consider(i, testflag, mood_listHappy);
					//	if(!semi){
							testflag.add(mood_listHappy.get(q));
							sv = mood_listHappy.get(q).getValence();
							i++;
					//	}
					//	else
					//		continue;
					//	}
					}
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();
										
					for (int i = 0, q = 0; i < control + supplement; q++) {
						//if (Float.parseFloat(mood_listEnergetic.get(q).getValence()) >= Float
						//		.parseFloat(sv)) {
					//	boolean semi = consider(i, testflag, mood_listEnergetic);
					//	if(!semi){
							testflag.add(mood_listEnergetic.get(q));
							sv = mood_listEnergetic.get(q).getValence();
							i++;
					//	}
					//	else
					//		continue;
						//}

					}
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();
					
					//putting happy
					settings = context.getSharedPreferences(PREFS_HAPPY, 0);
					Editor edit = settings.edit();
					Gson gson = new Gson();
					String itemlist = gson.toJson(flag);
					edit.putString("itemlist", itemlist);
					edit.putString("random", "no");
					edit.commit();
					
				} else if (mud.equals("Relax")) {
					float temp = finalSize / 3;
					control = (int)temp;
					if(finalSize % 3 > 0){
						supplement = finalSize % 3;
					}
					
//					if (Float.parseFloat(elevateList.get(p).getValence()) >= relaxtotal
//							&& !(flag.contains(elevateList.get(p)))) {
//						boolean semi = consider(p, flag, elevateList);
//						if (!semi) {
//							flag.add(elevateList.get(p));
//							p++;
//						} else {
//							p++;
//						}
//					} else {
//						p++;
//					}
					
					for (int i = 0, q = 0; i < control; q++) {
						//if (Float
						//		.parseFloat(mood_listRelax.get(q).getValence()) >= relaxtotal) {
					//	boolean semi = consider(i, testflag, mood_listRelax);
					//	if(!semi){
							testflag.add(mood_listRelax.get(q));
							sv = mood_listRelax.get(q).getValence();
							i++;
					//	}
					//	else
					//		continue;

						//}
					}
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();

					for (int i = 0, q = 0; i < control; q++) {
						//if (Float
						//		.parseFloat(mood_listHappy.get(q).getValence()) >= Float
						//		.parseFloat(sv)) {
					//	boolean semi = consider(i, testflag, mood_listHappy);
					//	if(!semi){
							testflag.add(mood_listHappy.get(q));
							sv = mood_listHappy.get(q).getValence();
							i++;
					//	}
					//	else
					//		continue;
						//}
					}
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();
					
					for (int i = 0, q = 0; i < control+supplement; q++) {
						//if (Float.parseFloat(mood_listEnergetic.get(q).getValence()) >= Float
						//		.parseFloat(sv)) {
					//	boolean semi = consider(i, testflag, mood_listEnergetic);
					//	if(!semi){
							testflag.add(mood_listEnergetic.get(q));
							sv = mood_listEnergetic.get(q).getValence();
							i++;
					//	}
					//	else
					//		continue;
						//}
					}
					Collections.sort(testflag, MusicDetails.MuCompare);
					flag.addAll(testflag);
					testflag.clear();
					
					//putting relax
					settings = context.getSharedPreferences(PREFS_RELAXED, 0);
					Editor edit = settings.edit();
					Gson gson = new Gson();
					String itemlist = gson.toJson(flag);
					edit.putString("itemlist", itemlist);
					edit.putString("random", "no");
					edit.commit();


				} else if (mud.equals("Energetic")) {
					//if (//Float.parseFloat(elevateList.get(p).getValence()) >= energetictotal
							//&& 
						//	!(flag.contains(elevateList.get(p)))) {
//						boolean semi = consider(p, testflag, mood_listEnergetic);
//						if (!semi) {
//							testflag.add(mood_listEnergetic.get(p));
//							p++;
//						} else {
//							p++;
//						}
					//} else {
					//	p++;
					//}
					
					for(int i = 0, q = 0; i < finalSize; q++){
						testflag.add(mood_listEnergetic.get(q));
						sv = mood_listEnergetic.get(q).getValence();
						i++;
					}
				Collections.sort(testflag, MusicDetails.MuCompare);
				flag.addAll(testflag);
				testflag.clear();
				
				
				
				//putting energetic
				settings = context.getSharedPreferences(PREFS_ENERGETIC, 0);
				Editor edit = settings.edit();
				Gson gson = new Gson();
				String itemlist = gson.toJson(flag);
				edit.putString("itemlist", itemlist);
				edit.putString("random", "no");
				edit.commit();
				}

			}
			
			p = 0;
			
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		// Collections.sort(flag, MusicDetails.MuCompare);
		return flag;
	}

	private static boolean consider(int p2, ArrayList<MusicDetails> flag,
			ArrayList<MusicDetails> elevateList) {
		boolean ss = false;

		for (int s = 0; s < flag.size(); s++) {
			if (flag.get(s).getTitle().equals(elevateList.get(p2).getTitle())) {
				ss = true;
				break;
			} else {
				continue;

			}

		}
		return ss;

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
	
	
	

}
