package com.example.moodifyer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class SignUpPage extends Activity implements OnCheckedChangeListener,
		OnClickListener {

	private Calendar calendar;

	public static String message_type = "ADDUSER";
	public static String sign_user_account = "http://de.fio.re/MP2/register.php";

	private int year, month, day;
	public static Button setDateAge, signUpAccount;
	public static Switch genderSignUp;
	public static String gender = "Male", date, emailTxt, usernameTxt,
			passwordTxt, confirm_passwordTxt, haha, jsonResult, error_message;
	public static boolean genderBoolean;

	public EditText email, username, password, confirm_password;
	private static final int MAXIMUM_BIT_LENGTH = 100;
	private static final int RADIX = 5;

	public static String user_id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_page);

		ActionBar mActionBar = getActionBar();
		mActionBar.hide();

		genderSignUp = (Switch) findViewById(R.id.genderSignUp);

		setDateAge = (Button) findViewById(R.id.ageSignUp);
		signUpAccount = (Button) findViewById(R.id.signup);

		email = (EditText) findViewById(R.id.emailSignUp);
		username = (EditText) findViewById(R.id.usernameSignUp);
		password = (EditText) findViewById(R.id.passwordSignUp);
		confirm_password = (EditText) findViewById(R.id.confirm_passwordSignUp);

		email.setHintTextColor(getResources().getColor(R.color.black));
		username.setHintTextColor(getResources().getColor(R.color.black));
		password.setHintTextColor(getResources().getColor(R.color.black));
		confirm_password.setHintTextColor(getResources()
				.getColor(R.color.black));

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		// showDate(year, month + 1, day);
		setDateAge.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(999);
				Toast.makeText(getApplicationContext(), date,
						Toast.LENGTH_SHORT).show();
			}
		});
		genderSignUp.setOnCheckedChangeListener(this);
		genderSignUp.setChecked(genderBoolean);

		signUpAccount.setOnClickListener(this);

	}

	@SuppressLint("TrulyRandom")
	public static String getRandomText() {
		// cryptographically strong random number generator
		SecureRandom random = new SecureRandom();

		// randomly generated BigInteger
		BigInteger bigInteger = new BigInteger(MAXIMUM_BIT_LENGTH, random);

		// String representation of this BigInteger in the given radix.
		String randomText = bigInteger.toString(RADIX);

		return randomText;
	}

	@SuppressWarnings("deprecation")
	public void setDate(View view) {
		showDialog(999);
		Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			return new DatePickerDialog(this, myDateListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			// arg1 = year
			// arg2 = month
			// arg3 = day
			showDate(arg1, arg2 + 1, arg3);
		}
	};

	private void showDate(int year, int month, int day) {
		setDateAge.setText(new StringBuilder().append(day).append("/")
				.append(month).append("/").append(year));

		date = String.valueOf(new StringBuilder().append(day).append("/")
				.append(month).append("/").append(year));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up_page, menu);
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (buttonView.equals(genderSignUp)) {
			if (isChecked) {
				gender = "Female";
				genderBoolean = true;
			} else {
				gender = "Male";
				genderBoolean = false;
			}
		}
	}

	private class SignUpUserAccountTask extends
			AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String s = postData(params);

			return s;
		}

		private String postData(String[] params) {

			user_id = "";
			user_id = getRandomText();
			emailTxt = email.getText().toString();
			usernameTxt = username.getText().toString();
			passwordTxt = password.getText().toString();

			HttpClient httpclient = new DefaultHttpClient();

			HttpPost signuppost = new HttpPost(sign_user_account);

			try {
				List<NameValuePair> accountparams = new ArrayList<NameValuePair>();
				accountparams.add(new BasicNameValuePair("message_type",
						message_type));
				accountparams.add(new BasicNameValuePair("user_id", user_id));
				accountparams.add(new BasicNameValuePair("email", emailTxt));
				accountparams.add(new BasicNameValuePair("username",
						usernameTxt));
				accountparams.add(new BasicNameValuePair("password",
						passwordTxt));
				accountparams
						.add(new BasicNameValuePair("date_of_birth", date));
				accountparams.add(new BasicNameValuePair("gender", gender));
				// accountparams.add(new BasicNameValuePair("avatar", gender));

				signuppost.setEntity(new UrlEncodedFormEntity(accountparams));
				haha = accountparams.toString();
				Log.d("Chikka JSENT", haha);

				HttpParams httpParameters = signuppost.getParams();
				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);
				// Set the default socket timeout (SO_TIMEOUT)
				// in milliseconds which is the timeout for waiting for data.
				int timeoutSocket = 10000;
				HttpConnectionParams
						.setSoTimeout(httpParameters, timeoutSocket);
				// new
				HttpResponse httpResponse = httpclient.execute(signuppost);

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

			Toast.makeText(getApplicationContext(), "Account Registered.",
					Toast.LENGTH_SHORT).show();
			email.setText("");
			username.setText("");
			password.setText("");
			setDateAge.setText("Date of Birth");
			date = "";
			genderSignUp.setChecked(false);
			gender = "Male";
			confirm_password.setText("");
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

		confirm_passwordTxt = confirm_password.getText().toString();

		if (email.getText().toString().trim().equals("")) {
			error_message = "ERROR! All data is required. ";

			return false;

		} else if (username.getText().toString().trim().equals("")) {
			error_message = "ERROR! All data is required. ";
			return false;
		} else if (password.getText().toString().trim().equals("")) {
			error_message = "ERROR! All data is required. ";
			return false;
		} else if (date.trim().equals("")) {
			error_message = "ERROR! All data is required.";
			return false;
		} else if (gender.trim().equals("")) {
			error_message = "ERROR! All data is required. ";
			return false;
		} else if (!password.getText().toString().equals(confirm_passwordTxt)) {
			// error_message = error_message.concat(error_message
			// + " Error! Password mismatched.");
			error_message = "Error! Password mismatched.";
			return false;
		} else
			return true;

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.signup:
			if (!validate()) {
				Toast.makeText(getBaseContext(), error_message,
						Toast.LENGTH_LONG).show();
				error_message = "";
			} else {
				new SignUpUserAccountTask().execute();
			}
			// call AsynTask to perform network operation on separate thread

			break;
		}
	}
	/*
	 * @Override public void onClick(View view) { // TODO Auto-generated method
	 * stub switch (view.getId()) { case R.id.signup: { if (!validate())
	 * Toast.makeText(getBaseContext(), "Incomplete data!",
	 * Toast.LENGTH_LONG).show();
	 * 
	 * new SignUpUserAccountTask().execute();
	 * 
	 * } break; } }
	 */
}
