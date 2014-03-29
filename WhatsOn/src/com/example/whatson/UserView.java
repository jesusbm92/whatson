package com.example.whatson;

import java.security.Provider;
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

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class UserView extends FragmentActivity implements OnMapClickListener {

	private GoogleMap mapa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userview);
		mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		mapa.setMyLocationEnabled(true);
		mapa.getUiSettings().setZoomControlsEnabled(false);
		mapa.getUiSettings().setCompassEnabled(true);
		DoPOST dopost = new DoPOST(UserView.this, 2);
		dopost.execute();
	}

	@Override
	public void onMapClick(LatLng arg0) {
	}

	private class DoPOST extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		int idOferta;
		String ip = "10.0.2.2";
		String ip2 = "192.168.10.101";
		String nombre;
		String descripcion;
		String activa;
		String categoria;
		String latitud;
		String longitud;
		// Result data

		Exception exception = null;

		DoPOST(Context context, int idOferta) {
			mContext = context;
			this.idOferta = idOferta;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("idOferta", String
						.valueOf(idOferta)));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/mostrarOferta.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				String result = EntityUtils.toString(entity);

				// Create a JSON object from the request response
				JSONObject jsonObject = new JSONObject(result);

				// Retrieve the data from the JSON object
				nombre = (String) jsonObject.get("Nombre");
				descripcion = (String) jsonObject.get("Descripcion");
				String i = (String) jsonObject.get("Activa");
				String i1 = i;
				if (i1.equals("1")) {
					activa = "true";
				} else {
					activa = "false";
				}
				categoria = (String) jsonObject.get("Categoria");
				latitud = (String) jsonObject.get("Latitud");
				longitud = (String) jsonObject.get("Longitud");

			} catch (Exception e) {
				Log.e("ClientServerDemo", "Error:", e);
				exception = e;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean valid) {

			if (exception != null) {
				Toast.makeText(mContext, exception.getMessage(),
						Toast.LENGTH_LONG).show();
			}

			super.onPostExecute(valid);

			Double latitudfin = Double.parseDouble(latitud);
			Double longitudfin = Double.parseDouble(longitud);
			String nombrefin = nombre;
			Boolean visiblefin = new Boolean(activa);
			String descripcionfin = descripcion;

			mapa.addMarker(new MarkerOptions()
					.position(new LatLng(latitudfin, longitudfin))
					.title(nombrefin)
					.visible(visiblefin)
					.snippet(descripcionfin)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher))
					.anchor(0.5f, 0.5f));
			mapa.setOnMapClickListener(UserView.this);

		}
	}
}
