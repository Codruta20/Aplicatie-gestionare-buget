package com.example.aplicatiegestionarebuget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Statistici extends AppCompatActivity {
FloatingActionButton btnBackS;
Button btnStatVenit, btnStatChelt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistici);

        btnBackS=findViewById(R.id.btnBackS);
        btnStatVenit=findViewById(R.id.btnStatVen);
        btnStatChelt=findViewById(R.id.btnStatCh);

        btnBackS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Statistici.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnStatVenit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Statistici.this, StatisticiVenituri.class);
                startActivity(i);
            }
        });

        btnStatChelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Statistici.this, StatisticiCheltuieli.class);
                startActivity(i);
            }
        });

    }
}