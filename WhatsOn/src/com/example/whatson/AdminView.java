package com.example.whatson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AdminView extends Activity {

	Button bverOfertas;
	Button bverAnunciantes;
	Button bverUsuarios;
	Button bverCategorias;
	Button batras;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adminview);

		bverOfertas = (Button) findViewById(R.id.ButtonVerOfertasA);
		bverOfertas.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(AdminView.this, ListSalesAdmin.class);
				startActivity(i);
			}
		});

		bverAnunciantes = (Button) findViewById(R.id.ButtonVerAnunciantesA);
		bverAnunciantes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(AdminView.this, ListAnnouncers.class);
				startActivity(i);
			}
		});
		
		bverUsuarios = (Button) findViewById(R.id.ButtonVerUsuariosA);
		bverUsuarios.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(AdminView.this, ListUsers.class);
				startActivity(i);
			}
		});
		
		bverCategorias = (Button) findViewById(R.id.ButtonVerCategoriasA);
		bverCategorias.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(AdminView.this, ListCategories.class);
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
