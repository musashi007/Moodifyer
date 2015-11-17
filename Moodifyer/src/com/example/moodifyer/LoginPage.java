package com.example.moodifyer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.moodifyer.adapter.MusicListAdapter;
import com.example.moodifyer.drawers.MoodListFragment;
import com.example.moodifyer.pojo.MusicDetails;
import com.google.gson.Gson;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends Activity implements OnClickListener {
	public static ArrayList<MusicDetails> mood_list = new ArrayList<MusicDetails>();
	public static final String PREFS_LIST =  "mood";
	public Button login;
	public static EditText username, password;
	public static String verify_user_account = "http://de.fio.re/MP/default.php";
	ArrayList<String> UserArray;
	public static String user, pass;
	public static boolean grant_access = false;
	public static final String PREFS_NAME_AUTH = "Auth_PREFS";
	
	public static final String PREFS_DET = "DeterminePos";
	
	//tocommentdetbymood
	public static final String PREFS_DETA = "DeterminePosAngry";
	public static final String PREFS_DETS = "DeterminePosSad";
	public static final String PREFS_DETR = "DeterminePosRelaxed";
	public static final String PREFS_DETH = "DeterminePosHappy";
	public static final String PREFS_DETE = "DeterminePosEnergetic";
	
	public static final String PREFS_ANGRY = "Angrymood";
	public static final String PREFS_SAD = "Sadmood";
	public static final String PREFS_RELAXED = "Relaxedmood";
	public static final String PREFS_HAPPY = "Happymood";
	public static final String PREFS_ENERGETIC = "Energeticmood";
	
	public static SharedPreferences sx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);

		ActionBar mActionBar = getActionBar();
		mActionBar.hide();

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		username.setHintTextColor(getResources().getColor(R.color.black));
		password.setHintTextColor(getResources().getColor(R.color.black));
		login = (Button) findViewById(R.id.login);

		login.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_page, menu);
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

	private class VerifyAccountTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String s = postData(params);

			return s;
		}

		private String postData(String[] params) {

			String jsonResult = "";

			JSONArray jsonArray;

			user = username.getText().toString();
			pass = password.getText().toString();

			HttpClient httpclient = new DefaultHttpClient();
			HttpGet verifyAccountget = new HttpGet(verify_user_account);
			try {
				HttpResponse response = httpclient.execute(verifyAccountget);
				jsonResult = inputStreamToString(
						response.getEntity().getContent()).toString();
				JSONObject obj1 = new JSONObject(jsonResult);
				Log.i("AGOY", jsonResult);
				jsonArray = obj1.getJSONArray("users");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj2 = jsonArray.getJSONObject(i);

					if (obj2.getString("username").equals(user)
							&& obj2.getString("password").equals(pass)) {

						// Toast.makeText(getApplicationContext(),
						// ""+sharedGenre, Toast.LENGTH_LONG).show();
						grant_access = true;
					}
				}
				SharedPreferences settings = getSharedPreferences(
						PREFS_NAME_AUTH, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("grant_access", grant_access);
				editor.commit();

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

		protected void onPostExecute(String result) {
			if (grant_access) {

					//Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_LONG).show();
				//}
				//else{
					
				Intent intent = new Intent(getApplicationContext(),
					HomePageActivity.class);

				startActivity(intent);
				login.setText("LOG IN");
				username.setText("");
				password.setText("");

				SharedPreferences settings = getSharedPreferences(
						PREFS_NAME_AUTH, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("username_prefs", user);
				editor.putString("password_prefs", pass);
				editor.commit();
				
				
				
				//det started
				SharedPreferences settings2 = 
						getSharedPreferences(PREFS_DET, 0);
				SharedPreferences.Editor editor2 = settings2.edit();
				editor2.putString("Start", "Start");
				editor2.commit();
				
				
				
				SharedPreferences settings3 = 
						getSharedPreferences(PREFS_ANGRY, 0);
				SharedPreferences.Editor editor3 = settings3.edit();
				editor3.putString("Start", "Start");
				editor3.putString("random", "yes");
				editor3.commit();
				
				SharedPreferences settings4 = 
						getSharedPreferences(PREFS_SAD, 0);
				SharedPreferences.Editor editor4 = settings4.edit();
				editor4.putString("Start", "Start");
				editor4.putString("random", "yes");
				editor4.commit();
				
				SharedPreferences settings5 = 
						getSharedPreferences(PREFS_RELAXED, 0);
				SharedPreferences.Editor editor5 = settings5.edit();
				editor5.putString("Start", "Start");
				editor5.putString("random", "yes");
				editor5.commit();
				
				SharedPreferences settings6 = 
						getSharedPreferences(PREFS_HAPPY, 0);
				SharedPreferences.Editor editor6 = settings6.edit();
				editor6.putString("Start", "Start");
				editor6.putString("random", "yes");
				editor6.commit();
				
				SharedPreferences settings7 = 
						getSharedPreferences(PREFS_ENERGETIC, 0);
				SharedPreferences.Editor editor7 = settings7.edit();
				editor7.putString("Start", "Start");
				editor7.putString("random", "yes");
				editor7.commit();
				

				grant_access = false;
			//}

			} else {
				login.setText("LOG IN");
				Toast.makeText(getBaseContext(),
						"Invalid account. Please Sign up.", Toast.LENGTH_LONG)
						.show();
			}

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

	}

	private boolean validate() {
		if (username.getText().toString().trim().equals(""))
			return false;
		else if (password.getText().toString().trim().equals(""))
			return false;
		else
			return true;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.login: {
			if (!validate()) {
				Toast.makeText(getBaseContext(), "Enter some data!",
						Toast.LENGTH_LONG).show();

			} else {
				login.setText("Logging in...");
				new VerifyAccountTask().execute();

			}
		}

			break;
		}
	}
}
