package com.example.whatson;

import java.util.ArrayList;
import java.util.List;

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
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Register extends Activity {

	private Button baccept;
	private Button bcancel;
	private Spinner select;
	private String objectSelected;
	private EditText user;
	private EditText password;
	private EditText passwordagain;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		user = (EditText) findViewById(R.id.NewUser);
		password = (EditText) findViewById(R.id.NewPasswordText1);
		passwordagain = (EditText) findViewById(R.id.NewPasswordAgain);
		select = (Spinner) findViewById(R.id.spinner1);
		objectSelected = (String) select.getSelectedItem();
		select.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				objectSelected = select.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		baccept = (Button) findViewById(R.id.AcceptRegister);
		baccept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!password.getText().toString()
						.equals(passwordagain.getText().toString())) {
					AlertDialog msj = new AlertDialog.Builder(Register.this)
							.create();
					msj.setTitle("Error");
					msj.setMessage("Las contraseñas no coinciden");
					msj.show();
				} else {

					if (objectSelected.toString().equals("Usuario")) {
						DoPOST mDoPOST = new DoPOST(Register.this, user
								.getText().toString(), UserLogin.sha1(password
								.getText().toString()));

						mDoPOST.execute();
						baccept.setEnabled(false);
					}

					if (objectSelected.toString().equals("Anunciante")) {
						DoPOSTAnnouncer mDoPOSTan = new DoPOSTAnnouncer(
								Register.this, user.getText().toString(),
								UserLogin.sha1(password.getText().toString()));

						mDoPOSTan.execute();
						baccept.setEnabled(false);
					}

				}
			}
		});
		bcancel = (Button) findViewById(R.id.Cancel);
		bcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private class DoPOST extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String userToRegister = "";
		String passwordToRegister = "";
		String ip = "10.0.2.2";
		String ip2= "192.168.10.172";


		Exception exception = null;

		DoPOST(Context context, String userToRegister, String passwordToRegister) {
			mContext = context;
			this.userToRegister = userToRegister;
			this.passwordToRegister = passwordToRegister;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("UserToRegister",
						userToRegister));
				nameValuePairs.add(new BasicNameValuePair("PasswordToRegister",
						passwordToRegister));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost(
						"http://"+ip2+"/clientservertest/registerUser.php");
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
			baccept.setEnabled(true);
			AlertDialog msj = new AlertDialog.Builder(Register.this).create();
			msj.setTitle("Exito");
			msj.setMessage("Usuario "
					+ user.getText().toString()
					+ " creado correctamente, por favor acceda a la aplicación para continuar");
			msj.show();

		}

	}

	private class DoPOSTAnnouncer extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String userToRegister = "";
		String passwordToRegister = "";
		String ip = "10.0.2.2";
		String ip2= "192.168.10.136";


		Exception exception = null;

		DoPOSTAnnouncer(Context context, String userToRegister,
				String passwordToRegister) {
			mContext = context;
			this.userToRegister = userToRegister;
			this.passwordToRegister = passwordToRegister;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("UserToRegister",
						userToRegister));
				nameValuePairs.add(new BasicNameValuePair("PasswordToRegister",
						passwordToRegister));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost(
						"http://"+ip2+"/clientservertest/registerAnnouncer.php");
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
			baccept.setEnabled(true);
			AlertDialog msj = new AlertDialog.Builder(Register.this).create();
			msj.setTitle("Exito");
			msj.setMessage("Anunciante "
					+ user.getText().toString()
					+ " creado correctamente, por favor acceda a la aplicación para continuar");
			msj.show();

		}

	}

}
