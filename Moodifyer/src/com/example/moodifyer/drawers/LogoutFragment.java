package com.example.moodifyer.drawers;

import net.simonvt.menudrawer.MenuDrawer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moodifyer.HomePageActivity;
import com.example.moodifyer.LoginPage;
import com.example.moodifyer.R;
import com.example.moodifyer.services.StreamSongService;

public class LogoutFragment extends Fragment {
	public ViewGroup root;
	public MenuDrawer mMenuDrawer;

	public static final String PREFS_NAME_AUTH = "Auth_PREFS";
	public static final String PREFS_SONG_DETAILS = "SongPrefsDetail";
	public static final String PREFS_MOOD_LENGTH = "PrefsMoodLength";
	
	public static final String PREFS_ANGRY = "Angrymood";
	public static final String PREFS_SAD = "Sadmood";
	public static final String PREFS_RELAXED = "Relaxedmood";
	public static final String PREFS_HAPPY = "Happymood";
	public static final String PREFS_ENERGETIC = "Energeticmood";
	public static final String PREFS_DET = "DeterminePos";
	
	// detprefs
	public static final String PREFS_DETA = "DeterminePosAngry";
	public static final String PREFS_DETS = "DeterminePosSad";
	public static final String PREFS_DETR = "DeterminePosRelaxed";
	public static final String PREFS_DETH = "DeterminePosHappy";
	public static final String PREFS_DETE = "DeterminePosEnergetic";
	
	public static final String PREFS_ANGRYPIC = "Angrypic";
	public static final String PREFS_SADPIC = "Sadpic";
	public static final String PREFS_HAPPYPIC = "Happypic";
	public static final String PREFS_RELAXEDPIC = "Relaxedpic";
	public static final String PREFS_ENERGETICPIC = "Energeticpic";
	public static final String PREFS_ELEVATEPIC = "Elevatepic";
	
	public static final String PREFS_LIST =  "mood";

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = (ViewGroup) inflater.inflate(R.layout.activity_logout_fragment,
				null);

		SharedPreferences settings = getActivity().getSharedPreferences(
				PREFS_NAME_AUTH, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();

		SharedPreferences settings2 = getActivity().getSharedPreferences(
				PREFS_SONG_DETAILS, 0);
		SharedPreferences.Editor editor2 = settings2.edit();
		editor2.clear();
		editor2.commit();

		SharedPreferences settings3 = getActivity().getSharedPreferences(
				PREFS_MOOD_LENGTH, 0);
		SharedPreferences.Editor editor3 = settings3.edit();
		editor3.clear();
		editor3.commit();
		
		SharedPreferences settings0 = getActivity().getSharedPreferences(
				PREFS_LIST, 0);
		SharedPreferences.Editor editor0 = settings0.edit();
		editor0.clear();
		editor0.commit();
		
		//prefsmood
		SharedPreferences settings4 = getActivity().getSharedPreferences(
				PREFS_ANGRY, 0);
		SharedPreferences.Editor editor4 = settings4.edit();
		editor4.clear();
		editor4.commit();
		
		SharedPreferences settings5 = getActivity().getSharedPreferences(
				PREFS_SAD, 0);
		SharedPreferences.Editor editor5 = settings5.edit();
		editor5.clear();
		editor5.commit();
		
		SharedPreferences settings6 = getActivity().getSharedPreferences(
				PREFS_RELAXED, 0);
		SharedPreferences.Editor editor6 = settings6.edit();
		editor6.clear();
		editor6.commit();
		
		SharedPreferences settings7 = getActivity().getSharedPreferences(
				PREFS_HAPPY, 0);
		SharedPreferences.Editor editor7 = settings7.edit();
		editor7.clear();
		editor7.commit();
		
		SharedPreferences settings8 = getActivity().getSharedPreferences(
				PREFS_ENERGETIC, 0);
		SharedPreferences.Editor editor8 = settings8.edit();
		editor8.clear();
		editor8.commit();
		
		SharedPreferences settings9 = getActivity().getSharedPreferences(
				PREFS_DET, 0);
		SharedPreferences.Editor editor9 = settings9.edit();
		editor9.clear();
		editor9.commit();
		
		//detprefs
		SharedPreferences settings10 = getActivity().getSharedPreferences(
				PREFS_DETA, 0);
		SharedPreferences.Editor editor10 = settings10.edit();
		editor10.clear();
		editor10.commit();
		
		
		SharedPreferences settings11 = getActivity().getSharedPreferences(
				PREFS_DETS, 0);
		SharedPreferences.Editor editor11 = settings11.edit();
		editor11.clear();
		editor11.commit();
		
		SharedPreferences settings12 = getActivity().getSharedPreferences(
				PREFS_DETR, 0);
		SharedPreferences.Editor editor12 = settings12.edit();
		editor12.clear();
		editor12.commit();
		
		SharedPreferences settings13 = getActivity().getSharedPreferences(
				PREFS_DETH, 0);
		SharedPreferences.Editor editor13 = settings13.edit();
		editor13.clear();
		editor13.commit();
		
		SharedPreferences settings14 = getActivity().getSharedPreferences(
				PREFS_DETE, 0);
		SharedPreferences.Editor editor14 = settings14.edit();
		editor14.clear();
		editor14.commit();
		
		SharedPreferences settings15 = getActivity().getSharedPreferences(
				PREFS_ANGRYPIC, 0);
		SharedPreferences.Editor editor15 = settings15.edit();
		editor15.clear();
		editor15.commit();
		
		SharedPreferences settings16 = getActivity().getSharedPreferences(
				PREFS_SADPIC, 0);
		SharedPreferences.Editor editor16 = settings16.edit();
		editor16.clear();
		editor16.commit();
		
		SharedPreferences settings17 = getActivity().getSharedPreferences(
				PREFS_HAPPYPIC, 0);
		SharedPreferences.Editor editor17 = settings17.edit();
		editor17.clear();
		editor17.commit();
		
		SharedPreferences settings18 = getActivity().getSharedPreferences(
				PREFS_RELAXEDPIC, 0);
		SharedPreferences.Editor editor18 = settings18.edit();
		editor18.clear();
		editor18.commit();
		
		SharedPreferences settings19 = getActivity().getSharedPreferences(
				PREFS_ENERGETICPIC, 0);
		SharedPreferences.Editor editor19 = settings19.edit();
		editor19.clear();
		editor19.commit();
		
		SharedPreferences settings20 = getActivity().getSharedPreferences(
				PREFS_ELEVATEPIC, 0);
		SharedPreferences.Editor editor20 = settings20.edit();
		editor20.clear();
		editor20.commit();

		try {
			// getActivity().startService(
			// new Intent(StreamSongService.ACTION_LOGOUTSTOP));

			Intent intent = new Intent(getActivity(), LoginPage.class);

			intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);

			startActivity(intent);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return root;
	}

	@Override
	public void onResume() {

		super.onResume();

		mMenuDrawer = HomePageActivity.mMenuDrawer;

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

					} else if (HomeFragment.mLayout.collapsePanel()) {
						HomeFragment.mLayout.collapsePanel();
					} else if (HomeFragment.mLayout != null
							&& HomeFragment.mLayout.isPanelExpanded()
							|| HomeFragment.mLayout.isPanelAnchored()) {
						HomeFragment.mLayout.collapsePanel();
					}

					return true;

				}

				return false;
			}
		});

	}

}
