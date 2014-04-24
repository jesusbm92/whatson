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
import android.widget.TextView;
import android.widget.Toast;

public class SaleDetails extends Activity {

	Button ofertaFavorita;
	TextView nombreOferta;
	TextView direccionOferta;
	TextView descripcionOferta;
	TextView activaOferta;
	Button bcancel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saledetails);
		ofertaFavorita = (Button) findViewById(R.id.FavoritaOferta);
		nombreOferta = (TextView) findViewById(R.id.NombreOferta);
		direccionOferta = (TextView) findViewById(R.id.DireccionOferta);
		descripcionOferta = (TextView) findViewById(R.id.DescripcionOferta);
		activaOferta = (TextView) findViewById(R.id.ActivaOferta);
		DoPOST mDoPOST = new DoPOST(SaleDetails.this, getIntent()
				.getStringExtra("user"), getIntent().getStringExtra("oferta"));

		mDoPOST.execute();

		bcancel = (Button) findViewById(R.id.CancelarOferta);
		bcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private class DoPOST extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String user = "";
		String nombreOfertaSelec = "";
		String ip = "10.0.2.2";
		String ip2 = "192.168.137.88";

		String nombre = "";
		String direccion = "";
		String descripcion = "";
		Boolean esFavorita = false;
		String activa = "";

		Exception exception = null;

		DoPOST(Context context, String user, String nombreOfertaSelec) {
			mContext = context;
			this.user = user;
			this.nombreOfertaSelec = nombreOfertaSelec;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("user", user));
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
						+ "/clientservertest/saleDetails.php");
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
				this.esFavorita = (Boolean) jsonObject.get("esFavorita");

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
			if (esFavorita.equals(true)) {
				ofertaFavorita.setVisibility(View.INVISIBLE);
			}

		}

		// private class DoPOSTAnnouncer extends AsyncTask<String, Void,
		// Boolean> {
		//
		// Context mContext = null;
		// String userToRegister = "";
		// String passwordToRegister = "";
		// String ip = "10.0.2.2";
		// String ip2 = "192.168.10.136";
		//
		// Exception exception = null;
		//
		// DoPOSTAnnouncer(Context context, String userToRegister,
		// String passwordToRegister) {
		// mContext = context;
		// this.userToRegister = userToRegister;
		// this.passwordToRegister = passwordToRegister;
		// }
		//
		// @Override
		// protected Boolean doInBackground(String... arg0) {
		//
		// try {
		//
		// // Setup the parameters
		// ArrayList<NameValuePair> nameValuePairs = new
		// ArrayList<NameValuePair>(
		// 2);
		// nameValuePairs.add(new BasicNameValuePair("UserToRegister",
		// userToRegister));
		// nameValuePairs.add(new BasicNameValuePair(
		// "PasswordToRegister", passwordToRegister));
		// // Add more parameters as necessary
		//
		// // Create the HTTP request
		// HttpParams httpParameters = new BasicHttpParams();
		//
		// // Setup timeouts
		// HttpConnectionParams.setConnectionTimeout(httpParameters,
		// 15000);
		// HttpConnectionParams.setSoTimeout(httpParameters, 15000);
		//
		// HttpClient httpclient = new DefaultHttpClient(
		// httpParameters);
		// HttpPost httppost = new HttpPost("http://" + ip2
		// + "/clientservertest/registerAnnouncer.php");
		// httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		// HttpResponse response = httpclient.execute(httppost);
		// HttpEntity entity = response.getEntity();
		//
		// } catch (Exception e) {
		// Log.e("ClientServerDemo", "Error:", e);
		// exception = e;
		// }
		//
		// return true;
		// }
		//
		// @Override
		// protected void onPostExecute(Boolean valid) {
		// // Update the UI
		//
		// if (exception != null) {
		// Toast.makeText(mContext, exception.getMessage(),
		// Toast.LENGTH_LONG).show();
		// }
		//
		// super.onPostExecute(valid);
		// baccept.setEnabled(true);
		// AlertDialog msj = new AlertDialog.Builder(Register.this)
		// .create();
		// msj.setTitle("Exito");
		// msj.setMessage("Anunciante "
		// + user.getText().toString()
		// +
		// " creado correctamente, por favor acceda a la aplicación para continuar");
		// msj.show();
		//
		// }
		//
		// }

	}

}
