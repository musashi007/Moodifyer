package com.example.moodifyer.drawers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodifyer.HomePageActivity;
import com.example.moodifyer.R;
import com.example.moodifyer.services.StreamSongService;

public class BrowseFragment extends Fragment {

	ProgressDialog mProgressDialog;
	public ViewGroup root;
	public ArrayList<String> artistSong = new ArrayList<String>();
	public String url = "http://de.fio.re/MP/upload.php";
	public String input;
	private String artist = "", title = "";
	ArrayAdapter<String> adapter;

	public String url2 = "", coverphoto_url = "";
	ArrayList<HashMap<String, String>> productList;
	public JSONArray jar2;
	HomeFragment home_connect;
	public MediaPlayer mediaPlayer2;
	public TextView songtitle2, songtitle, artistname, artistname2;

	private AutoCompleteTextView actv;

	HomePageActivity homepage_connect;
	HomeFragment homefrag_connect;

	public MenuDrawer mMenuDrawer;

	public String title2 = "";
	public String artist2 = "";

	final String[] tab_array = { "com.example.moodifyer.drawers.HomeFragment",
			"com.example.moodifyer.drawers.BrowseFragment",
			"com.example.moodifyer.drawers.ProfileFragment" };

	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = (ViewGroup) inflater.inflate(R.layout.activity_browse_fragment,
				null);
		actv = (AutoCompleteTextView) root.findViewById(R.id.search);
		// input = actv.getText().toString();

		new ReadJSONFeedTask().execute(url);
		ArrayAdapter adapter = new ArrayAdapter(getActivity(),
				android.R.layout.simple_list_item_1, artistSong);

		actv.setAdapter(adapter);

		mediaPlayer2 = home_connect.mediaPlayer;
		songtitle2 = home_connect.songtitletwo;
		songtitle = home_connect.songtitle;
		artistname = home_connect.artistname;
		artistname2 = home_connect.artistnametwo;

		return root;
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

	private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(getActivity());
			// // Set progressdialog title
			mProgressDialog.setTitle(null);
			// // Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// // Show progressdialog

		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return readJSONFeed(arg0[0]);
		}

		protected void onPostExecute(String result) {
			// musicList = new ArrayList<MusicDetails>();
			// artistSong = new ArrayList<String>();

			String genre, mood, url, cover_url;
			try {
				JSONArray jar = new JSONArray(result);

				for (int i = 0; i < jar.length(); i++) {

					JSONObject song = jar.getJSONObject(i);
					artist = song.getString("artist");
					title = song.getString("title");
					genre = song.getString("genre");
					mood = song.getString("mood");
					url = song.getString("url");
					cover_url = song.getString("cover_url");
					// if(artist.contains)
					artistSong.add(artist + " - " + title);
				}

				jar2 = new JSONArray(result);
				actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub

						int starting = 0;
						String searched = actv.getText().toString();
						for (; starting < searched.length(); starting++) {

							if (searched.charAt(starting) != '-') {
								artist2 += searched.charAt(starting);
							} else {
								break;
							}
						}
						starting++;
						for (; starting < searched.length(); starting++) {
							title2 += searched.charAt(starting);
						}

						title2 = title2.trim();
						artist2 = artist2.trim();

					//	Toast.makeText(getActivity(), "Title is: " + title2,
					//			Toast.LENGTH_SHORT).show();
					//	Toast.makeText(getActivity(), "Artist is: " + artist2,
					//			Toast.LENGTH_SHORT).show();

						try {

							String artist3 = "", title3 = "";
							JSONObject song2;
							for (int i = 0; i < jar2.length(); i++) {

								song2 = jar2.getJSONObject(i);
								artist3 = song2.getString("artist");
								title3 = song2.getString("title");
								if (artist2.equals(artist3)
										&& title2.equals(title3)) {
									url2 = song2.getString("url");
									coverphoto_url = song2
											.getString("cover_url");
									/*
									Toast.makeText(
											getActivity(),
											"Artist: " + artist2 + "="
													+ artist3,
											Toast.LENGTH_SHORT).show();
									Toast.makeText(getActivity(),
											"Artist: " + title2 + "=" + title3,
											Toast.LENGTH_SHORT).show();
											*/
								}
							}

							//Toast.makeText(getActivity(), "URL is: " + url2,
							//		Toast.LENGTH_SHORT).show();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// genre = song.getString("genre");
						// mood = song.getString("mood");
						// url = song.getString("url");
						// cover_url = song.getString("cover_url");
						// if(artist.contains)

						HomeFragment.artistname.setText(artist);
						HomeFragment.artistnametwo.setText(artist);
						HomeFragment.songtitle.setText(title);
						HomeFragment.songtitletwo.setText(title);
						GridMoodFragment.mood_list.clear();
						getActivity()
								.startService(
										new Intent(
												StreamSongService.ACTION_PLAYPAUSEBROWSE)
												.putExtra("value_url", url2)
												.putExtra("value_artist",
														artist2)
												.putExtra("value_title", title2)
												.putExtra("value_coverURL",
														coverphoto_url));

						getActivity().setContentView(R.layout.fragment);

						FragmentTransaction tx = getActivity()
								.getSupportFragmentManager().beginTransaction();
						tx.replace(R.id.content_frame, Fragment.instantiate(
								getActivity(), tab_array[0]));
						tx.commit();
						HomeFragment.mLayout.expandPanel();
						// startServi

					}
				});

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			// mLayout.expandPanel();
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

}
