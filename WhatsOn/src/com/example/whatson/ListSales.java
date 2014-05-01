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
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListSales extends ListActivity {

	private List<String> ofertasAnunciante = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listsales);
		DoPOST doPost = new DoPOST(ListSales.this, getIntent().getExtras()
				.getString("announcer"));
		doPost.execute();

		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {

				String entry = (String) parent.getItemAtPosition(position);
				Intent i = new Intent(ListSales.this, SaleDetailsAnnouncer.class);
				i.putExtra("announcer", getIntent().getStringExtra("announcer"));
				i.putExtra("oferta", entry);
				startActivity(i);

			};
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ofertasAnunciante.clear();
		setListAdapter(new ArrayAdapter<String>(ListSales.this,
				android.R.layout.simple_list_item_1, ofertasAnunciante));
		DoPOST doPost = new DoPOST(ListSales.this, getIntent().getExtras()
				.getString("announcer"));
		doPost.execute();

	}

	private class DoPOST extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String announcer = null;
		String ip = "10.0.2.2";
		String ip2 = "192.168.1.12";
		Integer longitudArray;
		// Cada oferta en formato Nombre,descripcion,direccion
		List<String> ofertasAnnouncer = new ArrayList<String>();
		// Result data

		Exception exception = null;

		DoPOST(Context context, String announcer) {
			mContext = context;
			this.announcer = announcer;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("Announcer",
						announcer));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/ofertasAnunciante.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				String result = EntityUtils.toString(entity);

				// Create a JSON object from the request response
				JSONArray jsonArray = new JSONArray(result);

				// Retrieve the data from the JSON object
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String nombre1 = (String) jsonObject.get("Nombre");
					ofertasAnnouncer.add(nombre1);
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

			for (String i : ofertasAnnouncer) {
				if (!ofertasAnunciante.contains(i)) {
					ofertasAnunciante.add(i);
				}

			}

			setListAdapter(new ArrayAdapter<String>(ListSales.this,
					android.R.layout.simple_list_item_1, ofertasAnunciante));

		}
	}
}
