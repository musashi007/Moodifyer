package com.example.moodifyer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.moodifyer.pojo.MoodDetails;
import com.example.moodifyer.adapter.MoodAdapter;

public class ListmoodActivity extends Activity {
	public Intent i;
	public static String mude;
	public ListView lv;
	public String[] moods = new String[] { "ANGRY", "SAD", "HAPPY", "RELAX",
			"ENERGETIC" };
	ArrayList<MoodDetails> myList = new ArrayList<MoodDetails>();
	Context context = ListmoodActivity.this;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		i = new Intent();
		
		setContentView(R.layout.listmood);
		lv = (ListView) findViewById(R.id.listView_listmood);
		
		for(int i = 0; i < 5 ; i++){
			MoodDetails md = new MoodDetails();
			md.setMood(moods[i]);
			//md.setValence(moods[i]);
			//md.setImage(R.drawable.logo);
			myList.add(md);
			lv.setAdapter(new MoodAdapter(context, myList));
		}
		
//		lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
	//			android.R.layout.simple_list_item_1, moods));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					mude = "ANGRY";
					i.putExtra("mood", "ANGRY");
					break;
				case 1:
					mude = "SAD";
					i.putExtra("mood", "SAD");
					break;
				case 2:
					mude = "HAPPY";
					i.putExtra("mood", "HAPPY");
					break;
				case 3:
					mude = "RELAX";
					i.putExtra("mood", "RELAX");
					break;
				case 4:
					mude = "ENERGETIC";
					i.putExtra("mood", "ENERGETIC");
					break;
				}
				setResult(111,i);
				//GridMoodFragment.runfile();
				finish();
			}
		});
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listmood, menu);
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
