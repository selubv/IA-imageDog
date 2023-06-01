package com.example.mlseriesdemonstrator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class integrantes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrantes);

    }
    public void onClick(View view) {
        Intent i = new Intent(integrantes.this, MenuActivity.class);
        startActivity(i);
    }
}