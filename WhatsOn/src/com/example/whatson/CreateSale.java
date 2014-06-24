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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateSale extends Activity {

	private Button bcreate;
	private Button bcancel;
	private Spinner select;
	private String objectSelected;
	private EditText nombreOferta;
	private EditText descripcionOferta;
	private EditText direccionOferta;
	private TextView activaTexto;
	private Spinner activaSelect;
	private Button guardar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createsale);

		// Ocultamos de la vista de creación los elementos para la
		// vista de modificacion
		activaTexto = (TextView) findViewById(R.id.textView7);
		activaTexto.setVisibility(View.GONE);
		activaSelect = (Spinner) findViewById(R.id.spinner3);
		activaSelect.setVisibility(View.GONE);
		guardar = (Button) findViewById(R.id.GuardarCambios);
		guardar.setVisibility(View.GONE);

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
		bcreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (nombreOferta.getText().toString() != ""
						&& descripcionOferta.getText().toString() != ""
						&& direccionOferta.getText().toString() != "") {

					DoPOSTDireccion d = new DoPOSTDireccion(CreateSale.this,
							nombreOferta.getText().toString(),
							descripcionOferta.getText().toString(),
							direccionOferta.getText().toString(),
							objectSelected, getIntent().getExtras().getString(
									"announcer"));
					bcreate.setEnabled(false);
					d.execute();
				} else {
					// Poner aqui el mnsajito para decir que hay que rellenar
					// los
					// campos
				}
			}
		});
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
		String ip2 = CreateSale.this.getString(R.string.ip);

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
				AlertDialog msj = new AlertDialog.Builder(CreateSale.this).create();
				msj.setTitle("Error");
				msj.setMessage("No se pudo cargar la lista de categorias, recargue la pagina");
				msj.show();
			}

			super.onPostExecute(valid);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					CreateSale.this, android.R.layout.simple_spinner_item,
					categoriasPost);
			select.setAdapter(adapter);

		}
	}

	private class DoPOSTDireccion extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String nombre = "";
		String descripcion = "";
		String direccion = "";
		String announcer = "";
		String categoria = "";
		String ip = "10.0.2.2";
		String ip2 = CreateSale.this.getString(R.string.ip);

		Double longitud1 = 0.0;
		Double latitud1 = 0.0;
		Exception exception = null;

		DoPOSTDireccion(Context context, String nombre, String descripcion,
				String direccion, String categoria, String announcer) {
			mContext = context;
			this.nombre = nombre;
			this.descripcion = descripcion;
			this.direccion = direccion;
			this.categoria = categoria;
			this.announcer = announcer;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("Direccion",
						direccion));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost(
						"http://maps.googleapis.com/maps/api/geocode/json?address="
								+ URLEncoder.encode(direccion, "UTF-8")
								+ "&sensor=true");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				String result = EntityUtils.toString(entity);

				// Create a JSON object from the request response
				JSONObject jsonObject = new JSONObject(result);

				JSONArray res = jsonObject.getJSONArray("results");

				// Retrieve the data from the JSON object
				JSONObject full = res.getJSONObject(0);
				JSONObject geometry = full.getJSONObject("geometry");
				JSONObject location = geometry.getJSONObject("location");

				latitud1 = location.getDouble("lat");
				longitud1 = location.getDouble("lng");

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
				Toast.makeText(
						mContext,
						"Su oferta no pudo ser creada, por favor vuelva a introducir la direccion y, si vuelve a fallar, introducirla en otro formato",
						Toast.LENGTH_LONG).show();
				bcreate.setEnabled(true);
			}

			else {
				super.onPostExecute(valid);

				DoPOSTCrear d = new DoPOSTCrear(CreateSale.this, nombre,
						descripcion, direccion, categoria, latitud1, longitud1,
						announcer);
				d.execute();
			}
		}
	}

	private class DoPOSTCrear extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String nombre = "";
		String descripcion = "";
		String direccion = "";
		String announcer = "";
		String categoria = "";
		Double latitud = 0.0;
		Double longitud = 0.0;
		String ip = "10.0.2.2";
		String ip2 = CreateSale.this.getString(R.string.ip);

		Exception exception = null;

		DoPOSTCrear(Context context, String nombre, String descripcion,
				String direccion, String categoria, Double latitud,
				Double longitud, String announcer) {
			mContext = context;
			this.nombre = nombre;
			this.descripcion = descripcion;
			this.direccion = direccion;
			this.categoria = categoria;
			this.announcer = announcer;
			this.latitud = latitud;
			this.longitud = longitud;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						6);
				nameValuePairs.add(new BasicNameValuePair("Nombre", nombre));
				nameValuePairs.add(new BasicNameValuePair("Descripcion",
						descripcion));
				nameValuePairs.add(new BasicNameValuePair("Categoria",
						categoria));
				nameValuePairs.add(new BasicNameValuePair("Announcer",
						announcer));
				nameValuePairs.add(new BasicNameValuePair("Latitud", latitud
						.toString()));
				nameValuePairs.add(new BasicNameValuePair("Longitud", longitud
						.toString()));
				nameValuePairs.add(new BasicNameValuePair("Direccion",
						direccion));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/createSale.php");
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
				AlertDialog msj = new AlertDialog.Builder(CreateSale.this).create();
				msj.setTitle("Error");
				msj.setMessage("No se pudo crear la oferta, revise los datos");
				msj.show();
				
				Toast.makeText(mContext, exception.getMessage(),
						Toast.LENGTH_LONG).show();
			}

			super.onPostExecute(valid);
			AlertDialog msj = new AlertDialog.Builder(CreateSale.this).create();
			msj.setTitle("Exito");
			msj.setMessage("Oferta creada correctamente");
			msj.show();
		}

	}

}