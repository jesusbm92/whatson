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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
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

public class ModifySale extends Activity {

	private Button bcancel;
	private Button bcreate;
	private Spinner select;
	private String objectSelected;
	private EditText nombreOferta;
	private EditText descripcionOferta;
	private EditText direccionOferta;
	private TextView activaTexto;
	private Spinner activaSelect;
	private String activaSelected;
	private Button guardar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createsale);

		activaTexto = (TextView) findViewById(R.id.textView7);
		activaSelect = (Spinner) findViewById(R.id.spinner3);
		activaSelected = (String) activaSelect.getSelectedItem();
		activaSelect.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				activaSelected = (String) activaSelect.getSelectedItem();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		guardar = (Button) findViewById(R.id.GuardarCambios);
		guardar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DoPOSTGuardar d = new DoPOSTGuardar(ModifySale.this,
						nombreOferta.getText().toString(), descripcionOferta
								.getText().toString(), direccionOferta
								.getText().toString(), activaSelected,
						objectSelected);
				d.execute();

			}
		});

		nombreOferta = (EditText) findViewById(R.id.NewNombreOferta);
		descripcionOferta = (EditText) findViewById(R.id.NewDescOfer);
		direccionOferta = (EditText) findViewById(R.id.newDirOfer);
		select = (Spinner) findViewById(R.id.spinner2);
		DoPOST doPost = new DoPOST(this);
		doPost.execute();
		// Popular el select antes de esta linea con las categorias
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

		bcreate = (Button) findViewById(R.id.AceptarCrearOferta);
		bcreate.setVisibility(View.GONE);
		bcancel = (Button) findViewById(R.id.CancelarCrearOferta);
		bcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private class DoPOST extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		List<String> categoriasPost = new ArrayList<String>();
		String ip = "10.0.2.2";
		String ip2 = ModifySale.this.getString(R.string.ip);

		Exception exception = null;

		DoPOST(Context context) {
			mContext = context;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/listaCategorias.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				String result = EntityUtils.toString(entity);

				// Create a JSON object from the request response
				JSONArray jsonArray = new JSONArray(result);

				// Retrieve the data from the JSON object
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String nombre = jsonObject.getString("Nombre");

					categoriasPost.add(nombre);
				}

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
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					ModifySale.this, android.R.layout.simple_spinner_item,
					categoriasPost);
			select.setAdapter(adapter);
			DoPOSTRellenar d = new DoPOSTRellenar(ModifySale.this,
					ModifySale.this.getIntent().getExtras().getString("oferta"));
			d.execute();

		}
	}

	private class DoPOSTRellenar extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String nombreOferta = "";
		String direccion = "";
		String descripcion = "";
		String activa = "";
		List<String> categoriasPost = new ArrayList<String>();
		String ip = "10.0.2.2";
		String ip2 = ModifySale.this.getString(R.string.ip);

		Exception exception = null;

		DoPOSTRellenar(Context context, String nombreOferta) {
			mContext = context;
			this.nombreOferta = nombreOferta;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("nombreOfertaSelec",
						nombreOferta));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/saleDetailsNonRegistered.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);

				// Create a JSON object from the request response
				JSONObject jsonObject = new JSONObject(result);

				// Retrieve the data from the JSON object
				this.nombreOferta = (String) jsonObject.get("Nombre");
				this.descripcion = (String) jsonObject.get("Descripcion");
				this.direccion = (String) jsonObject.get("Direccion");
				this.activa = (String) jsonObject.get("Activa");

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
			ModifySale.this.nombreOferta.setText(nombreOferta);
			descripcionOferta.setText(descripcion);
			direccionOferta.setText(direccion);
			if (activa.equals("1")) {
				activaSelect.setSelection(0);
			} else {
				activaSelect.setSelection(1);
			}
		}

	}

	private class DoPOSTGuardar extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String nombreOferta = "";
		String direccionOferta = "";
		String descripcionOferta = "";
		String activaOferta = "";
		String categoriaOferta = "";
		String ip = "10.0.2.2";
		String ip2 = ModifySale.this.getString(R.string.ip);

		Exception exception = null;

		DoPOSTGuardar(Context context, String nombreOferta,
				String descripcionOferta, String direccionOferta,
				String activaOferta, String categoriaOferta) {
			mContext = context;
			this.nombreOferta = nombreOferta;
			this.descripcionOferta = descripcionOferta;
			this.direccionOferta = direccionOferta;
			if (activaOferta.equals("Si")) {
				activaSelect.setSelection(0);
				activaSelected = "1";
			} else {
				activaSelect.setSelection(1);
				activaSelected = "0";
			}
			this.activaOferta = activaSelected;
			this.categoriaOferta = categoriaOferta;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs.add(new BasicNameValuePair("Nombre",
						nombreOferta));
				nameValuePairs.add(new BasicNameValuePair("Descripcion",
						descripcionOferta));
				nameValuePairs.add(new BasicNameValuePair("Direccion",
						direccionOferta));
				nameValuePairs.add(new BasicNameValuePair("Activa",
						activaOferta));
				nameValuePairs.add(new BasicNameValuePair("Categoria",
						categoriaOferta));

				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/saleModify.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);

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
