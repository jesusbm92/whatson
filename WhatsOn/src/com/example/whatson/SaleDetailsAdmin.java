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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SaleDetailsAdmin extends Activity {

	Button ofertaFavorita;
	Button ofertaNoFavorita;
	TextView nombreOferta;
	TextView direccionOferta;
	TextView descripcionOferta;
	TextView activaOferta;
	Button bcancel;
	Button modificar;
	Button borrar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saledetails);
		ofertaFavorita = (Button) findViewById(R.id.FavoritaOferta);
		ofertaFavorita.setVisibility(View.GONE);
		ofertaNoFavorita = (Button) findViewById(R.id.NoFavoritaOferta);
		ofertaNoFavorita.setVisibility(View.GONE);

		modificar = (Button) findViewById(R.id.modificaOferta);
		modificar.setVisibility(View.GONE);
		borrar = (Button) findViewById(R.id.borrarOferta);
		borrar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Quitar este finish si da problemas
				finish();
				Intent i = new Intent(SaleDetailsAdmin.this,
						ConfirmDeleteSale.class);
				i.putExtra("oferta", getIntent().getExtras()
						.getString("oferta"));
				startActivity(i);

			}
		});
		nombreOferta = (TextView) findViewById(R.id.NombreOferta);
		direccionOferta = (TextView) findViewById(R.id.DireccionOferta);
		descripcionOferta = (TextView) findViewById(R.id.DescripcionOferta);
		activaOferta = (TextView) findViewById(R.id.ActivaOferta);
		DoPOST mDoPOST = new DoPOST(SaleDetailsAdmin.this, getIntent()
				.getStringExtra("oferta"));

		mDoPOST.execute();

		bcancel = (Button) findViewById(R.id.CancelarOferta);
		bcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public void onResume() { // After a pause OR at startup
		super.onResume();
		DoPOST mDoPOST = new DoPOST(SaleDetailsAdmin.this, getIntent()
				.getStringExtra("oferta"));

		mDoPOST.execute();
	}

	private class DoPOST extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String user = "";
		String nombreOfertaSelec = "";
		String ip = "10.0.2.2";
		String ip2 = "192.168.1.12";

		String nombre = "";
		String direccion = "";
		String descripcion = "";
		String activa = "";

		Exception exception = null;

		DoPOST(Context context, String nombreOfertaSelec) {
			mContext = context;
			this.nombreOfertaSelec = nombreOfertaSelec;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("nombreOfertaSelec",
						nombreOfertaSelec));
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
				this.nombre = (String) jsonObject.get("Nombre");
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
			nombreOferta.setText(nombre);
			descripcionOferta.setText(descripcion);
			direccionOferta.setText(direccion);
			if (activa.equals("1")) {
				activaOferta.setText("Si");
			} else {
				activaOferta.setText("No");
			}

		}

	}

}
