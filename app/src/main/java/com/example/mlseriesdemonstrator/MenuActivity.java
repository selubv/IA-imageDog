package com.example.mlseriesdemonstrator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity {
    ImageView siguiente, reconocimiento, estadoanimo,audioMascota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setContentView(R.layout.activity_menu);
        siguiente=(ImageView)findViewById(R.id.idreconocimifac);
        reconocimiento = (ImageView)findViewById(R.id.idreconocimientomascota);
        estadoanimo = (ImageView)findViewById(R.id.idestadodeanimo);
        audioMascota = (ImageView)findViewById(R.id.idaudiosd);


        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MainActivity1.class);
                startActivity(i);
            }
        });

        reconocimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MainActivity2.class);
                startActivity(i);
            }
        });
        estadoanimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MainActivity3.class);
                startActivity(i);
            }
        });
        audioMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}