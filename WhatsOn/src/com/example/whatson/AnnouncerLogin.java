package com.example.whatson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AnnouncerLogin extends Activity {

	private EditText user;
	private EditText password;
	private Button login;
	private String passwordBD = "";
	private String userBD = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userlogin);

		user = (EditText) findViewById(R.id.UserLoginText);
		password = (EditText) findViewById(R.id.UserPasswordText);
		login = (Button) findViewById(R.id.LoginButton);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DoPOST mDoPOST = new DoPOST(AnnouncerLogin.this, user.getText()
						.toString());

				mDoPOST.execute();
				login.setEnabled(false);

			}
		});

	}

	public static String sha1(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA1");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(String.format("%02X", messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void checkUser(String userToSearch) {

	}

	// Este metodo se eliminara cuando se haga la vista de usuario (sirve para
	// probar
	// si funciona la consulta de base de datos)
	public void launchIndexParaProbar(View view) {
		Intent i = new Intent(this, Index.class);
		startActivity(i);
	}

	private class DoPOST extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String userToSearch = "";
		String ip = "10.0.2.2";
		String ip2 = "192.168.1.12";
		// Result data
		String userres;
		String passres;

		Exception exception = null;

		DoPOST(Context context, String userToSearch) {
			mContext = context;
			this.userToSearch = userToSearch;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("UserToSearch",
						userToSearch));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/announcerLogin.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				String result = EntityUtils.toString(entity);

				// Create a JSON object from the request response
				JSONObject jsonObject = new JSONObject(result);

				// Retrieve the data from the JSON object
				userres = (String) jsonObject.get("user");
				passres = (String) jsonObject.get("password");

			} catch (Exception e) {
				Log.e("ClientServerDemo", "Error:", e);
				exception = e;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean valid) {
			// Update the UI
			userBD = this.userres;
			passwordBD = this.passres;

			if (exception != null) {
				Toast.makeText(mContext, exception.getMessage(),
						Toast.LENGTH_LONG).show();
			}

			super.onPostExecute(valid);
			login.setEnabled(true);
			if (passwordBD.equals(sha1(password.getText().toString()))) {
				finish();
			}

		}

	}
}
