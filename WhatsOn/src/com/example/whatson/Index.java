package com.example.whatson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Index extends Activity {

	private Button baccess;
	private Button babout;
	private Button badmin;
	private Button bexit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		babout = (Button) findViewById(R.id.ButtonAbout);
		babout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				launchAbout(null);
			}
		});

		bexit = (Button) findViewById(R.id.ButtonExit);
		bexit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		badmin = (Button) findViewById(R.id.ButtonAdmin);
		badmin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(Index.this, AdminLogin.class);
				startActivity(i);
			}
		});

		baccess = (Button) findViewById(R.id.ButtonAccess);
		baccess.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				launchAccess(null);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.index, menu);
		return true;
	}

	public void launchAbout(View view) {
		Intent i = new Intent(this, About.class);
		startActivity(i);
	}

	public void launchAccess(View view) {
		Intent i = new Intent(this, Access.class);
		startActivity(i);
	}

}
