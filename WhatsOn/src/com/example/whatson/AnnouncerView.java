package com.example.whatson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AnnouncerView extends Activity {

	Button bverOfertas;
	Button bcrearOferta;
	Button batras;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcerview);

		bverOfertas = (Button) findViewById(R.id.ButtonVerOfertas);
		bverOfertas.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		bcrearOferta = (Button) findViewById(R.id.ButtonCrearOferta);
		bcrearOferta.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(AnnouncerView.this, CreateSale.class);
				i.putExtra("announcer",
						getIntent().getExtras().getString("announcer"));
				startActivity(i);
			}
		});

		batras = (Button) findViewById(R.id.ButtonAtras);
		batras.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
}
