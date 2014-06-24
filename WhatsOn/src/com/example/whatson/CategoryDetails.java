package com.example.whatson;

import java.net.URLEncoder;
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
import org.json.JSONArray;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CategoryDetails extends Activity {

	private Button bcreate;
	private Button bcancel;
	private EditText nombreCategoria;
	private Button guardar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categorydetails);

		nombreCategoria = (EditText) findViewById(R.id.NewNombreCategoria);

		guardar = (Button) findViewById(R.id.GuardarCambios2);
		guardar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DoPOSTEditar d = new DoPOSTEditar(CategoryDetails.this,
						nombreCategoria.getText().toString(), getIntent()
								.getExtras().getString("categoria"));
				d.execute();
			}
		});
		guardar.setVisibility(View.GONE);

		bcreate = (Button) findViewById(R.id.AceptarCrearCat);
		bcreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (nombreCategoria.getText().toString() != "") {

					DoPOSTCrear d = new DoPOSTCrear(CategoryDetails.this,
							nombreCategoria.getText().toString());
					bcreate.setEnabled(false);
					d.execute();
				} else {
					AlertDialog msj = new AlertDialog.Builder(CategoryDetails.this).create();
					msj.setTitle("Error");
					msj.setMessage("Revise los datos introducidos");
					msj.show();
				}
			}
		});
		bcancel = (Button) findViewById(R.id.CancelarCrearCat2);
		bcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		if (getIntent().getExtras().getBoolean("create") == false) {
			guardar.setVisibility(View.VISIBLE);
			bcreate.setVisibility(View.GONE);
			nombreCategoria.setText(getIntent().getExtras().getString(
					"categoria"));
		}

	}

	private class DoPOSTCrear extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String nombre = "";
		String ip = "10.0.2.2";
		String ip2 = CategoryDetails.this.getString(R.string.ip);

		Exception exception = null;

		DoPOSTCrear(Context context, String nombre) {
			mContext = context;
			this.nombre = nombre;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				nameValuePairs.add(new BasicNameValuePair("Nombre", nombre));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/createCategory.php");
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
				AlertDialog msj = new AlertDialog.Builder(CategoryDetails.this).create();
				msj.setTitle("Error");
				msj.setMessage("No se pudo crear la categoria, revise los datos");
				msj.show();
			}

			super.onPostExecute(valid);
			finish();

		}

	}

	private class DoPOSTEditar extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String nombre = "";
		String nombreAntiguo = "";
		String ip = "10.0.2.2";
		String ip2 = CategoryDetails.this.getString(R.string.ip);

		Exception exception = null;

		DoPOSTEditar(Context context, String nombre, String nombreAntiguo) {
			mContext = context;
			this.nombre = nombre;
			this.nombreAntiguo = nombreAntiguo;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("Nombre", nombre));
				nameValuePairs.add(new BasicNameValuePair("NombreA",
						nombreAntiguo));

				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/editCategory.php");
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
				AlertDialog msj = new AlertDialog.Builder(CategoryDetails.this).create();
				msj.setTitle("Error");
				msj.setMessage("No pudo modificarse la categoria, revise los datos introducidos");
				msj.show();
			}

			super.onPostExecute(valid);
			finish();

		}

	}

}