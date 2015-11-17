package com.example.moodifyer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.moodifyer.adapter.MusicListAdapter;
import com.example.moodifyer.drawers.MoodListFragment;
import com.example.moodifyer.pojo.MusicDetails;
import com.google.gson.Gson;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	public Button login, signup;
	private Typeface typeFace;
	public TextView mod;
	private SharedPreferences sharedPrefs;
	private Editor editor;
	public SharedPreferences settings;
	public static final String PREFS_ANGRY = "Angrymood";
	public static final String PREFS_SAD = "Sadmood";
	public static final String PREFS_RELAXED = "Relaxedmood";
	public static final String PREFS_HAPPY = "Happymood";
	public static final String PREFS_ENERGETIC = "Energeticmood";
	public static ArrayList<MusicDetails> mood_list;
	public static final String PREFS_LIST =  "mood";
	public static final String PREFS_NAME = "Auth_PREFS";
	public static SharedPreferences sx;
	public Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = getApplicationContext();
		setContentView(R.layout.activity_main);
		ActionBar mActionBar = getActionBar();
		mActionBar.hide();
		mood_list = new ArrayList<MusicDetails>();
		mod = (TextView) findViewById(R.id.moodifyer);
		typeFace = Typeface.createFromAsset(getAssets(), "DecoNeue-Light.otf");
		mod.setTypeface(typeFace);

		settings = getSharedPreferences(PREFS_NAME, 0);
		boolean artist_prefs = settings.getBoolean("grant_access", false);

		if (artist_prefs) {
//			SharedPreferences mm;
//			sx = getSharedPreferences(PREFS_LIST, 0);
//			if(sx.getString("yess","").equals("yess")){
//				String hello = sx.getString("mud", "");
//				
//				if(hello.equals("Angry")){
//					mm = getSharedPreferences(PREFS_ANGRY, 0);
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
//					mm = getSharedPreferences(PREFS_SAD, 0);
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
//					mm = getSharedPreferences(PREFS_ANGRY, 0);
//					String itemlist = mm.getString("itemlist", "");
//					List<MusicDetails> moodf;
//					Gson gson = new Gson();
//					MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
//					moodf = Arrays.asList(mitems);
//					moodf = new ArrayList<MusicDetails>(moodf);
//					mood_list =  (ArrayList<MusicDetails>) moodf;
//				}
//				else if(hello.equals("Happy")){
//					mm = getSharedPreferences(PREFS_ANGRY, 0);
//					String itemlist = mm.getString("itemlist", "");
//					List<MusicDetails> moodf;
//					Gson gson = new Gson();
//					MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
//					moodf = Arrays.asList(mitems);
//					moodf = new ArrayList<MusicDetails>(moodf);
//					mood_list =  (ArrayList<MusicDetails>) moodf;
//				}
//				else if(hello.equals("Energetic")){
//					mm = getSharedPreferences(PREFS_ANGRY, 0);
//					String itemlist = mm.getString("itemlist", "");
//					List<MusicDetails> moodf;
//					Gson gson = new Gson();
//					MusicDetails[] mitems = gson.fromJson(itemlist, MusicDetails[].class);
//					moodf = Arrays.asList(mitems);
//					moodf = new ArrayList<MusicDetails>(moodf);
//					mood_list =  (ArrayList<MusicDetails>) moodf;
//				}
//				Toast.makeText(getApplicationContext(), ""+mood_list.get(1).getArtist(), Toast.LENGTH_LONG).show();
//				//try{
//				
//				MoodListFragment.listview3 = (ListView) findViewById(R.id.listview3);	
//				
//				MoodListFragment.listview3
//				.setAdapter(new MusicListAdapter(getApplicationContext(),
//						mood_list));
//				//}catch(RuntimeException e){}
//				//catch(Exception e){}
//				
//			}
			//	else{
			Intent intent = new Intent(getApplicationContext(),
					HomePageActivity.class);
			startActivity(intent);
			//	}
		}

		login = (Button) findViewById(R.id.login);
		signup = (Button) findViewById(R.id.signup);
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						LoginPage.class);
				startActivity(intent);
			}
		});
		signup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						SignUpPage.class);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		sharedPrefs = getSharedPreferences("MYREFERENCES", Context.MODE_PRIVATE);
		super.onResume();
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
}
