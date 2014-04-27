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

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class Favourites extends ListActivity {

	private List<String> ofertasFavoritas = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favourites);
		DoPOST doPost = new DoPOST(Favourites.this, getIntent().getExtras()
				.getString("user"));
		doPost.execute();

		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {

				String entry = (String) parent.getItemAtPosition(position);
				Intent i = new Intent(Favourites.this, SaleDetails.class);
				i.putExtra("user", getIntent().getStringExtra("user"));
				i.putExtra("oferta", entry);
				startActivity(i);

			};
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ofertasFavoritas.clear();
		setListAdapter(new ArrayAdapter<String>(Favourites.this,
				android.R.layout.simple_list_item_1, ofertasFavoritas));
		DoPOST doPost = new DoPOST(Favourites.this, getIntent().getExtras()
				.getString("user"));
		doPost.execute();

	}

	private class DoPOST extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String user = null;
		String ip = "10.0.2.2";
		String ip2 = "192.168.137.88";
		Integer longitudArray;
		// Cada oferta en formato Nombre,descripcion,direccion
		List<String> ofertasFavoritasconComas = new ArrayList<String>();
		// Result data

		Exception exception = null;

		DoPOST(Context context, String user) {
			mContext = context;
			this.user = user;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("user", user));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/mostrarFavoritas.php");
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
					String nombre1 = (String) jsonObject.get("Nombre");
					String descripcion1 = (String) jsonObject
							.get("Descripcion");
					String direccion1 = (String) jsonObject.get("Direccion");

					String ofertai = (nombre1 + "," + descripcion1 + "," + direccion1);
					ofertasFavoritasconComas.add(ofertai);
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

			for (String i : ofertasFavoritasconComas) {
				String nombre = i.split(",")[0];
				if (!ofertasFavoritas.contains(nombre)) {
					ofertasFavoritas.add(nombre);
				}

			}

			setListAdapter(new ArrayAdapter<String>(Favourites.this,
					android.R.layout.simple_list_item_1, ofertasFavoritas));

		}
	}
}
