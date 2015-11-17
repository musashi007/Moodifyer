package com.example.moodifyer;

import net.simonvt.menudrawer.MenuDrawer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WindowSample extends ActionBarActivity implements
		View.OnClickListener {

	private static final String STATE_MENUDRAWER = "net.simonvt.menudrawer.samples.WindowSample.menuDrawer";
	private static final String STATE_ACTIVE_VIEW_ID = "net.simonvt.menudrawer.samples.WindowSample.activeViewId";

	private MenuDrawer mMenuDrawer;
	private TextView mContentTextView;

	private int mActiveViewId;

	@Override
	public void onCreate(Bundle inState) {
		super.onCreate(inState);
		if (inState != null) {
			mActiveViewId = inState.getInt(STATE_ACTIVE_VIEW_ID);
		}

		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW);
		mMenuDrawer.setContentView(R.layout.activity_home_page);
		mMenuDrawer.setMenuView(R.layout.menu_scrollview);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		mContentTextView = (TextView) findViewById(R.id.contentText);

		findViewById(R.id.item1).setOnClickListener(this);
		findViewById(R.id.item2).setOnClickListener(this);
		findViewById(R.id.item3).setOnClickListener(this);
		findViewById(R.id.item4).setOnClickListener(this);
		findViewById(R.id.item5).setOnClickListener(this);
		findViewById(R.id.item6).setOnClickListener(this);

		TextView activeView = (TextView) findViewById(mActiveViewId);
		if (activeView != null) {
			mMenuDrawer.setActiveView(activeView);
			mContentTextView.setText("Active item: " + activeView.getText());
		}

		// This will animate the drawer open and closed until the user manually
		// drags it. Usually this would only be
		// called on first launch.
		mMenuDrawer.peekDrawer();
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
	public void onBackPressed() {
		final int drawerState = mMenuDrawer.getDrawerState();
		if (drawerState == MenuDrawer.STATE_OPEN
				|| drawerState == MenuDrawer.STATE_OPENING) {
			mMenuDrawer.closeMenu();
			return;
		}

		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		mMenuDrawer.setActiveView(v);
		mContentTextView.setText("Active item: " + ((TextView) v).getText());
		mMenuDrawer.closeMenu();
		mActiveViewId = v.getId();

		switch (v.getId()) {

		case R.id.item1: {
			Toast.makeText(getApplicationContext(), "Hoooray",
					Toast.LENGTH_SHORT).show();
			// Intent intent = new Intent(getApplicationContext(),
			// Sample.class);
			// startActivity(intent);
			//mMenuDrawer.removeView(View.inflate(getApplicationContext(), R.layout.activity_windowsample, root));
			
			
			mMenuDrawer.setContentView(R.layout.activity_home_page);
			break;
		}
		case R.id.item2: {
			Toast.makeText(getApplicationContext(), "Hoooray",
					Toast.LENGTH_SHORT).show();
			// Intent intent = new Intent(getApplicationContext(),
			// Sample.class);
			// startActivity(intent);
			//View namebar = findViewById(R.layout.activity_sample);
			//mMenuDrawer.removeView(namebar);
			mMenuDrawer.setContentView(R.layout.activity_windowsample);
			break;
		}
			

		}
	}
}
