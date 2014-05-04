package com.example.whatson;

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

public class UserDetails extends Activity {

	private Button bborrar;
	private Button bcancel;
	private TextView user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userdetails);

		user = (TextView) findViewById(R.id.NewNickname);
		user.setText(getIntent().getExtras().getString("user"));

		bborrar = (Button) findViewById(R.id.borrarUsuario);
		bborrar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				Intent i = new Intent(UserDetails.this, ConfirmDeleteUser.class);
				i.putExtra("user", getIntent().getExtras().getString("user"));
				startActivity(i);

			}
		});

		bcancel = (Button) findViewById(R.id.Cancelar2);
		bcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}
