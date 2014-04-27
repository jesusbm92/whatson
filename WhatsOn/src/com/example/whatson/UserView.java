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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
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
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mapa.setMyLocationEnabled(true);
		mapa.getUiSettings().setZoomControlsEnabled(false);
		mapa.getUiSettings().setCompassEnabled(true);
		mapa.addMarker(new MarkerOptions()
				.position(new LatLng(50, 50))
				.title("Marker")
				.visible(false)
				.snippet("Marker para inicializar el markerclicklistener")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher))
				.anchor(0.5f, 0.5f));
		mapa.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				// Aqui poner lo que se quiera para cuando se pulse en la info
				// del marker
				Intent i = new Intent(UserView.this, SaleDetails.class);
				i.putExtra("user", getIntent().getStringExtra("user"));
				i.putExtra("oferta", marker.getTitle());
				startActivity(i);
			}
		});
		// Location myLoc = mapa.getMyLocation();
		// mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(
		// new LatLng(myLoc.getLatitude(), myLoc.getLongitude()), 18));
		DoPOST dopost = new DoPOST(UserView.this, 2);
		dopost.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menuuserview, menu);
		return true;
		/** true -> el menú ya está visible */
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.favourites:
			lanzarFavoritas(null);
			break;
		}
		return true;
		/** true -> consumimos el item, no se propaga */
	}

	private void lanzarFavoritas(View view) {
		Intent i = new Intent(this, Favourites.class);
		i.putExtra("user", getIntent().getStringExtra("user"));
		startActivity(i);
	}

	@Override
	public void onMapClick(LatLng arg0) {
	}

	private class DoPOST extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		int idOferta;
		String ip = "10.0.2.2";
		String ip2 = "192.168.137.88";
		Integer longitudArray;
		List<String> id = new ArrayList<String>();
		List<String> nombre = new ArrayList<String>();
		List<String> descripcion = new ArrayList<String>();
		List<String> activa = new ArrayList<String>();
		List<String> categoria = new ArrayList<String>();
		List<String> latitud = new ArrayList<String>();
		List<String> longitud = new ArrayList<String>();
		List<String> direccion = new ArrayList<String>();
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
				nameValuePairs.add(new BasicNameValuePair("user", "user"));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/mostrarTodasOfertas.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				String result = EntityUtils.toString(entity);

				// Create a JSON object from the request response
				JSONArray jsonArray = new JSONArray(result);

				longitudArray = jsonArray.length();

				// Retrieve the data from the JSON object
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String id1 = (String) jsonObject.get("id");
					String nombre1 = (String) jsonObject.get("Nombre");
					String descripcion1 = (String) jsonObject
							.get("Descripcion");
					String i1 = (String) jsonObject.get("Activa");
					String i2 = i1;
					if (i2.equals("1")) {
						activa.add("true");
					} else {
						activa.add("false");
					}
					String categoria1 = (String) jsonObject.get("Categoria");
					String latitud1 = (String) jsonObject.get("Latitud");
					String longitud1 = (String) jsonObject.get("Longitud");
					String direccion1 = (String) jsonObject.get("Direccion");

					id.add(id1);
					nombre.add(nombre1);
					descripcion.add(descripcion1);
					categoria.add(categoria1);
					latitud.add(latitud1);
					longitud.add(longitud1);
					direccion.add(direccion1);
				}

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

			for (int i = 0; i < latitud.size(); i++) {

				Double latitudfin = Double.parseDouble(latitud.get(i));
				Double longitudfin = Double.parseDouble(longitud.get(i));
				String nombrefin = nombre.get(i);
				String estaActiva = activa.get(i);
				Boolean visiblefin = new Boolean(estaActiva);
				String descripcionfin = descripcion.get(i);
				String direccionfin = direccion.get(i);

				mapa.addMarker(new MarkerOptions()
						.position(new LatLng(latitudfin, longitudfin))
						.title(nombrefin)
						.visible(visiblefin)
						.draggable(true)
						.snippet(descripcionfin + "\n" + direccionfin)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_launcher))
						.anchor(0.5f, 0.5f));
				mapa.setOnMapClickListener(UserView.this);
			}

		}
	}
}
