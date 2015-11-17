package com.example.moodifyer.drawers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.moodifyer.HomePageActivity;
import com.example.moodifyer.LoginPage;
import com.example.moodifyer.R;
import com.example.moodifyer.util.Base64;

public class ProfileFragment extends Fragment {

	//mood
	
	public static final String PREFS_SONG_DETAILS = "SongPrefsDetail";
	
	public static final String PREFS_ANGRY = "Angrymood";
	public static final String PREFS_SAD = "Sadmood";
	public static final String PREFS_RELAXED = "Relaxedmood";
	public static final String PREFS_HAPPY = "Happymood";
	public static final String PREFS_ENERGETIC = "Energeticmood";
	public static final String PREFS_DET = "DeterminePos";
	
	public static String hello = "ANGRY";
	public static SharedPreferences settings2;
	public final static String PREFS_NAME_AUTH = "Auth_PREFS";
	public static LoginPage pg;
	public MenuDrawer mMenuDrawer;

	HomePageActivity homepage_connect;
	HomeFragment homefrag_connect;

	public ViewGroup root;
	private Uri fileUri;
	String picturePath;
	Uri selectedImage;
	Bitmap photo;
	public static Dialog dialog;
	public static String user_id, username, password, email, dof, gender,
			genre;
	public static ListView lv;
	public ImageView iv;
	public static EditText epass, eemail, edof;
	public static EditText puser, ppass, pemail, pdate, pmeLength;
	public static Spinner pgender, pgenre;
	public AlertDialog.Builder builder;
	public LayoutInflater inflater;
	public static String url = "http://de.fio.re/MP/default.php", moodLength;
	public static ArrayList profile;
	public static ArrayList newProfile;
	public static ProgressDialog mProgressDialog;
	public String FURL = "http://de.fio.re/MP/avatar/test.php";
	public static Button saveButton;
	public static Context context;
	public String ba1;
	public static Spinner spin_gender, spin_genre;
	public Button b;
	public static Button psave;
	public Handler handler;
	public static final String PREFS_MOOD_LENGTH = "PrefsMoodLength";
	public Runnable r;
	public Intent bb;
	public static String updateProfileLink = "http://de.fio.re/MP2/update.php";
	public static ArrayAdapter<String> arrayAdapter;
	public static ArrayAdapter<String> newArrayAdapter;
	// public static ArrayList<ProfileDetails> myList = new
	// ArrayList<ProfileDetails>();

	public static int[] imageProfile = new int[] { R.drawable.logo };
	public String[] genders = new String[] { "Male", "Female" };
	public String[] genres = new String[] { "Pop", "Acoustic", "Swing",
			"Soundtrack", "Soul", "Rock", "R&B", "New Age", "Indie", "Folk",
			"Electronic", "Alternative", "Other" };
	
	// detprefs
	public static final String PREFS_DETA = "DeterminePosAngry";
	public static final String PREFS_DETS = "DeterminePosSad";
	public static final String PREFS_DETR = "DeterminePosRelaxed";
	public static final String PREFS_DETH = "DeterminePosHappy";
	public static final String PREFS_DETE = "DeterminePosEnergetic";
	
	

	public static ProfileFragment newInstance(String text) {

		ProfileFragment f = new ProfileFragment();
		Bundle b = new Bundle();
		b.putString("msg", text);

		f.setArguments(b);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		bb = getActivity().getIntent();
		context = getActivity();
		builder = new AlertDialog.Builder(context);

		root = (ViewGroup) inflater.inflate(R.layout.activity_profile_fragment,
				null);
		puser = (EditText) root.findViewById(R.id.profile_username);
		ppass = (EditText) root.findViewById(R.id.profile_password);
		pmeLength = (EditText) root.findViewById(R.id.profile_melength);
		pemail = (EditText) root.findViewById(R.id.profile_email);
		pdate = (EditText) root.findViewById(R.id.profile_date);
		pgender = (Spinner) root.findViewById(R.id.profile_gender);
		pgenre = (Spinner) root.findViewById(R.id.profile_genre);
		psave = (Button) root.findViewById(R.id.profile_save);
		
		
		
		//HomeFragment.mediaPlayer.release();
		
		psave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				displayEdited();
				
				//mood
//				SharedPreferences settings4 = getActivity().getSharedPreferences(
//						PREFS_ANGRY, 0);
//				SharedPreferences.Editor editor4 = settings4.edit();
//				editor4.clear();
//				editor4.commit();
//				
//				SharedPreferences settings5 = getActivity().getSharedPreferences(
//						PREFS_SAD, 0);
//				SharedPreferences.Editor editor5 = settings5.edit();
//				editor5.clear();
//				editor5.commit();
//				
//				SharedPreferences settings6 = getActivity().getSharedPreferences(
//						PREFS_RELAXED, 0);
//				SharedPreferences.Editor editor6 = settings6.edit();
//				editor6.clear();
//				editor6.commit();
//				
//				SharedPreferences settings7 = getActivity().getSharedPreferences(
//						PREFS_HAPPY, 0);
//				SharedPreferences.Editor editor7 = settings7.edit();
//				editor7.clear();
//				editor7.commit();
//				
//				SharedPreferences settings8 = getActivity().getSharedPreferences(
//						PREFS_ENERGETIC, 0);
//				SharedPreferences.Editor editor8 = settings8.edit();
//				editor8.clear();
//				editor8.commit();
				
				//start
				SharedPreferences settings9 = getActivity().getSharedPreferences(
						PREFS_ANGRY, 0);
				SharedPreferences.Editor editor9 = settings9.edit();
				editor9.putString("Start", "Start");
				//editor9.putInt("position", 0);
				//MoodListFragment.currentPos = 0;
				editor9.commit();
				
				SharedPreferences settings10 = getActivity().getSharedPreferences(
						PREFS_SAD, 0);
				SharedPreferences.Editor editor10 = settings10.edit();
				editor10.putString("Start", "Start");
				//editor10.putInt("position", 0);
				//MoodListFragment.currentPos = 0;
				editor10.commit();
				
				SharedPreferences settings11 = getActivity().getSharedPreferences(
						PREFS_RELAXED, 0);
				SharedPreferences.Editor editor11 = settings11.edit();
				editor11.putString("Start", "Start");
				//editor11.putInt("position", 0);
				//MoodListFragment.currentPos = 0;
				editor11.commit();
				
				SharedPreferences settings12 = getActivity().getSharedPreferences(
						PREFS_HAPPY, 0);
				SharedPreferences.Editor editor12 = settings12.edit();
				editor12.putString("Start", "Start");
				//editor12.putInt("position", 0);
				//MoodListFragment.currentPos = 0;
				editor12.commit();
				
				SharedPreferences settings13 = getActivity().getSharedPreferences(
						PREFS_ENERGETIC, 0);
				SharedPreferences.Editor editor13 = settings13.edit();
				editor13.putString("Start", "Start");
				//editor13.putInt("position", 0);
				//MoodListFragment.currentPos = 0;
				editor13.commit();
				
//				MediaPlayer ss = new MediaPlayer();
//				try {
//					ss.prepare();
//				} catch (IllegalStateException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				HomeFragment.mediaPlayer = ss;
				
				Intent intent = new Intent(getActivity(),
						HomePageActivity.class);
				startActivity(intent);
			}
		});

		new ReadJSONFeedTask().execute(url);

		ArrayAdapter<String> adapter_genre = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, genres);

		ArrayAdapter<String> adapter_gender = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, genders);

		pgenre.setAdapter(adapter_genre);
		pgender.setAdapter(adapter_gender);

		return root;
	}

	protected void makeDialog() {
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dialog.setTitle("Edit Profile");
		dialog.setContentView(R.layout.activity_edit_profile);
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				android.R.drawable.ic_menu_edit);

		epass = (EditText) dialog.findViewById(R.id.editProfile_password);
		eemail = (EditText) dialog.findViewById(R.id.editProfile_email);
		edof = (EditText) dialog.findViewById(R.id.editProfile_dof);
		spin_gender = (Spinner) dialog.findViewById(R.id.spinner_gender);
		spin_genre = (Spinner) dialog.findViewById(R.id.spinner_genre);
		saveButton = (Button) dialog.findViewById(R.id.editProfile_save);

		epass.setText(password);
		eemail.setText(email);
		edof.setText(dof);
		if (gender.equals("Male")) {
			spin_genre.setSelection(0);
		} else
			spin_genre.setSelection(1);

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				displayEdited();
				dialog.dismiss();
			}
		});

		ArrayAdapter<String> adapter_genre = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, genres);
		ArrayAdapter<String> adapter_gender = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, genders);

		adapter_genre.setDropDownViewResource(R.layout.text_list);
		adapter_gender.setDropDownViewResource(R.layout.text_list);
		spin_gender.setAdapter(adapter_gender);
		spin_genre.setAdapter(adapter_genre);

		dialog.show();

	}

	public void displayEdited() {
		new ReadNewJSONFeedTask().execute();
		new ReadJSONFeedTask().execute(url);
	}

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

	public static class ReadNewJSONFeedTask extends
			AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String s = postData(params);

			return s;
		}

		private String postData(String[] params) {

			String jsonResult = "";
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost updateprofilepost = new HttpPost(updateProfileLink);

			try {
				List<NameValuePair> updateparams = new ArrayList<NameValuePair>();
				updateparams.add(new BasicNameValuePair("message_type",
						"ImageName"));
				updateparams.add(new BasicNameValuePair("userid", user_id));
				updateparams.add(new BasicNameValuePair("email", pemail
						.getText().toString()));
				updateparams.add(new BasicNameValuePair("username", puser
						.getText().toString()));
				updateparams.add(new BasicNameValuePair("password", ppass
						.getText().toString()));
				updateparams.add(new BasicNameValuePair("birthday", pdate
						.getText().toString()));
				updateparams.add(new BasicNameValuePair("gender", pgender
						.getSelectedItem().toString()));
				updateparams.add(new BasicNameValuePair("genre", pgenre
						.getSelectedItem().toString()));
				// accountparams.add(new BasicNameValuePair("avatar", gender));

				SharedPreferences settings3 = context.getSharedPreferences(
						PREFS_MOOD_LENGTH, 0);
				SharedPreferences.Editor editor2 = settings3.edit();
				moodLength = pmeLength.getText().toString();
				editor2.putString("meLength", moodLength);
				editor2.commit();

				updateprofilepost.setEntity(new UrlEncodedFormEntity(
						updateparams));
				String haha = updateparams.toString();

				HttpParams httpParameters = updateprofilepost.getParams();
				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);
				// Set the default socket timeout (SO_TIMEOUT)
				// in milliseconds which is the timeout for waiting for data.
				int timeoutSocket = 10000;
				HttpConnectionParams
						.setSoTimeout(httpParameters, timeoutSocket);
				HttpResponse httpResponse = httpclient
						.execute(updateprofilepost);
				HttpEntity httpEntity = httpResponse.getEntity();
				jsonResult = inputStreamToString(
						httpResponse.getEntity().getContent()).toString();

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String result) {

			Toast.makeText(context, "Account updated.", Toast.LENGTH_LONG)
					.show();

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

	public class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(context);
			// // Set progressdialog title
			mProgressDialog.setTitle(null);
			// // Set progressdialog message
			mProgressDialog.setMessage("Loading profile...");

			mProgressDialog.setIndeterminate(false);
			// // Show progressdialog
			mProgressDialog.show();

		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return readJSONFeed(arg0[0]);
		}

		protected void onPostExecute(String result) {
			// musicList = new ArrayList<MusicDetails>();
			// artistSong = new ArrayList<String>();
			// profile = new ArrayList<String>();

			String genre, mood, url, cover_url;
			try {
				// JSONArray jar = new JSONArray(result);
				// ProfileDetails pd = new ProfileDetails();
				JSONObject user = new JSONObject(result);
				JSONArray jar = user.getJSONArray("users");
				for (int i = 0; i < jar.length(); i++) {

					// ProfileDetails pd = new ProfileDetails();
					JSONObject song = jar.getJSONObject(i);
					SharedPreferences settings = getActivity()
							.getSharedPreferences(PREFS_NAME_AUTH, 0);
					String finalUser = settings.getString("username_prefs", "");
					String finalPass = settings.getString("password_prefs", "");
					if (finalUser.equals(song.getString("username"))
							&& finalPass.equals(song.getString("password"))) {
						profile = new ArrayList<String>();
						profile.add(song.getString("user_id"));
						profile.add(song.getString("username"));
						profile.add(song.getString("password"));
						profile.add(song.getString("email"));
						profile.add(song.getString("date_of_birth"));
						profile.add(song.getString("gender"));
						profile.add(song.getString("genre"));

						user_id = song.getString("user_id");
						puser.setText(song.getString("username"));
						ppass.setText(song.getString("password"));
						pemail.setText(song.getString("email"));
						pdate.setText(song.getString("date_of_birth"));

						settings2 = context.getSharedPreferences(
								PREFS_MOOD_LENGTH, 0);
						String finalSettings = settings2.getString("meLength",
								"4");
						pmeLength.setText(finalSettings);
						
						
						
						//detprefs
//						SharedPreferences settings10 = getActivity().getSharedPreferences(
//								PREFS_DETA, 0);
//						SharedPreferences.Editor editor10 = settings10.edit();
//						editor10.clear();
//						editor10.commit();
//						
//						SharedPreferences settings11 = getActivity().getSharedPreferences(
//								PREFS_DETS, 0);
//						SharedPreferences.Editor editor11 = settings11.edit();
//						editor11.clear();
//						editor11.commit();
//						
//						SharedPreferences settings12 = getActivity().getSharedPreferences(
//								PREFS_DETR, 0);
//						SharedPreferences.Editor editor12 = settings12.edit();
//						editor12.clear();
//						editor12.commit();
//						
//						SharedPreferences settings13 = getActivity().getSharedPreferences(
//								PREFS_DETH, 0);
//						SharedPreferences.Editor editor13 = settings13.edit();
//						editor13.clear();
//						editor13.commit();
//						
//						SharedPreferences settings14 = getActivity().getSharedPreferences(
//								PREFS_DETE, 0);
//						SharedPreferences.Editor editor14 = settings14.edit();
//						editor14.clear();
//						editor14.commit();
						
						//tttt

//						SharedPreferences settings2 = getActivity().getSharedPreferences(
//								PREFS_SONG_DETAILS, 0);
//						SharedPreferences.Editor editor2 = settings2.edit();
//						editor2.clear();
//						editor2.commit();
						
						
//						SharedPreferences settings10 = getActivity(). 
//								getSharedPreferences(PREFS_DETA, 0);
//						SharedPreferences.Editor editor10 = settings10.edit();
//						editor10.putString("Start", "Start");
//						editor10.commit();
//						
//						SharedPreferences settings11 = getActivity().
//								getSharedPreferences(PREFS_DETS, 0);
//						SharedPreferences.Editor editor11 = settings11.edit();
//						editor11.putString("Start", "Start");
//						editor11.commit();
//						
//						SharedPreferences settings12 = getActivity().
//								getSharedPreferences(PREFS_DETR, 0);
//						SharedPreferences.Editor editor12 = settings12.edit();
//						editor12.putString("Start", "Start");
//						editor12.commit();
//						
//						SharedPreferences settings13 = getActivity().
//								getSharedPreferences(PREFS_DETH, 0);
//						SharedPreferences.Editor editor13 = settings13.edit();
//						editor13.putString("Start", "Start");
//						editor13.commit();
//						
//						SharedPreferences settings14 = getActivity().
//								getSharedPreferences(PREFS_DETE, 0);
//						SharedPreferences.Editor editor14 = settings14.edit();
//						editor14.putString("Start", "Start");
//						editor14.commit();
						
//						HomeFragment.mediaPlayer.stop();
//						HomeFragment.mediaPlayer.release();


						if (song.getString("gender").equals("Male")) {
							pgender.setSelection(0);
						} else {
							pgender.setSelection(1);
						}

						if (song.getString("genre").equals("Pop"))
							pgenre.setSelection(0);
						else if (song.getString("genre").equals("Acoustic"))
							pgenre.setSelection(1);
						else if (song.getString("genre").equals("Swing"))
							pgenre.setSelection(2);
						else if (song.getString("genre").equals("Soundtrack"))
							pgenre.setSelection(3);
						else if (song.getString("genre").equals("Soul"))
							pgenre.setSelection(4);
						else if (song.getString("genre").equals("Rock"))
							pgenre.setSelection(5);
						else if (song.getString("genre").equals("R&B"))
							pgenre.setSelection(6);
						else if (song.getString("genre").equals("New Age"))
							pgenre.setSelection(7);
						else if (song.getString("genre").equals("Indie"))
							pgenre.setSelection(8);
						else if (song.getString("genre").equals("Folk"))
							pgenre.setSelection(9);
						else if (song.getString("genre").equals("Electronic"))
							pgenre.setSelection(10);
						else if (song.getString("genre").equals("Alternative"))
							pgenre.setSelection(11);
						else if (song.getString("genre").equals("Other"))
							pgenre.setSelection(12);
						else
							pgenre.setSelection(0);
					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			mProgressDialog.dismiss();

		}

	}

	public static String readJSONFeed(String url) {

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

	private void upload() {
		// Image location URL
		Log.e("path", "----------------" + picturePath);

		// Image
		Bitmap bm = BitmapFactory.decodeFile(picturePath);
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte[] ba = bao.toByteArray();
		ba1 = Base64.encodeBytes(ba);

		Log.e("base64", "-----" + ba1);

		// Upload image to server
		new uploadToServer().execute();
		b.setVisibility(View.GONE);
	}

	private void clickpic() {

		// Check Camera
		if (getActivity().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// Open default camera
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

			// start the image capture Intent
			startActivityForResult(intent, 100);

		} else {
			Toast.makeText(getActivity(), "Camera not supported",
					Toast.LENGTH_LONG).show();
		}

		b.setVisibility(View.VISIBLE);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100 && resultCode == getActivity().RESULT_OK) {

			selectedImage = data.getData();
			photo = (Bitmap) data.getExtras().get("data");

			// Cursor to get image uri to display

			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getActivity().getContentResolver().query(
					selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			Bitmap photo = (Bitmap) data.getExtras().get("data");
			// ImageView imageView = (ImageView) root
			// .findViewById(R.id.image_profile);
			// imageView.setImageBitmap(photo);
		}
	}

	public class uploadToServer extends AsyncTask<Void, Void, String> {

		// private ProgressDialog pd = new ProgressDialog(getActivity());

		protected void onPreExecute() {
			super.onPreExecute();
			// pd.setMessage("Wait image uploading!");
			// pd.show();
		}

		@Override
		protected String doInBackground(Void... params) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("base64", ba1));
			nameValuePairs.add(new BasicNameValuePair("ImageName", System
					.currentTimeMillis() + ".jpg"));
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(FURL);
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
			// pd.hide();
			// pd.dismiss();
		}
	}
}
