package com.example.whatson;

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

public class ConfirmDeleteUser extends Activity {
	private Button yes;
	private Button no;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmdeleteuser);

		yes = (Button) findViewById(R.id.ConfirmarBOButton);
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DoPOSTBorrar d = new DoPOSTBorrar(ConfirmDeleteUser.this,
						getIntent().getExtras().getString("user"));
				yes.setEnabled(false);
				d.execute();
			}
		});

		no = (Button) findViewById(R.id.CancelarBOButton);
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private class DoPOSTBorrar extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String user = "";
		String ip = "10.0.2.2";
		String ip2 = "192.168.1.12";

		Exception exception = null;

		DoPOSTBorrar(Context context, String user) {
			mContext = context;
			this.user = user;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				nameValuePairs.add(new BasicNameValuePair("User", user));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/borrarUsuario.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

			} catch (Exception e) {
				Log.e("ClientServerDemo", "Error:", e);
				exception = e;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean valid) {
			// Update the UI

			if (exception != null) {
				Toast.makeText(mContext, exception.getMessage(),
						Toast.LENGTH_LONG).show();
			}

			super.onPostExecute(valid);
			finish();

		}

	}

}
