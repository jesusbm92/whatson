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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
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

public class ListSalesAdmin extends ListActivity {

	private List<String> listaOfertas = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listsales);
		DoPOST doPost = new DoPOST(ListSalesAdmin.this);
		doPost.execute();

		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {

				 String entry = (String) parent.getItemAtPosition(position);
				 Intent i = new Intent(ListSalesAdmin.this,
				 SaleDetailsAdmin.class);
				 i.putExtra("oferta", entry);
				 startActivity(i);

			};
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		listaOfertas.clear();
		setListAdapter(new ArrayAdapter<String>(ListSalesAdmin.this,
				android.R.layout.simple_list_item_1, listaOfertas));
		DoPOST doPost = new DoPOST(ListSalesAdmin.this);
		doPost.execute();

	}

	private class DoPOST extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String ip = "10.0.2.2";
		String ip2 = ListSalesAdmin.this.getString(R.string.ip);
		Integer longitudArray;
		// Cada oferta en formato Nombre,descripcion,direccion
		List<String> ofertas = new ArrayList<String>();
		// Result data

		Exception exception = null;

		DoPOST(Context context) {
			mContext = context;
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			try {

				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost("http://" + ip2
						+ "/clientservertest/ofertasAdmin.php");
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
					ofertas.add(nombre1);
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
				AlertDialog msj = new AlertDialog.Builder(ListSalesAdmin.this).create();
				msj.setTitle("Error");
				msj.setMessage("No se pudieron cargar las ofertas, intente de nuevo");
				msj.show();
			}

			super.onPostExecute(valid);

			for (String i : ofertas) {
				if (!listaOfertas.contains(i)) {
					listaOfertas.add(i);
				}

			}

			setListAdapter(new ArrayAdapter<String>(ListSalesAdmin.this,
					android.R.layout.simple_list_item_1, listaOfertas));

		}
	}
}
