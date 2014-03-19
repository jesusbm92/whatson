package com.example.whatson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Access extends Activity {

	private Button bback;
	private Button buser;
	private Button bguest;
	private Button bannouncer;
	private Button bregister;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.access);
		bback = (Button) findViewById(R.id.ButtonBack);
		bback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backToIndex(null);
			}
		});
		buser = (Button) findViewById(R.id.ButtonUser);
		buser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				launchUserLogin(null);

			}
		});
		bannouncer = (Button) findViewById(R.id.ButtonAnnouncer);
		bannouncer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchAnnouncerLogin(null);
			}
		});
		
		bregister = (Button) findViewById(R.id.ButtonRegister);
		bregister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchRegister(null);
			}
		});
	}

	public void backToIndex(View view) {
		finish();
	}

	public void launchUserLogin(View view) {
		Intent i = new Intent(this, UserLogin.class);
		startActivity(i);
	}
	
	public void launchAnnouncerLogin(View view){
		Intent i = new Intent(this, AnnouncerLogin.class);
		startActivity(i);
	}
	
	public void launchRegister(View view){
		Intent i = new Intent (this, Register.class);
		startActivity(i);
	}
}
