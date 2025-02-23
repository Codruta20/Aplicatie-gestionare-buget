package com.example.aplicatiegestionarebuget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Color;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatisticiCheltuieli extends AppCompatActivity {
FloatingActionButton btnBackSC;
EditText inputDataStart, inputDataSfarsit;
String dataStart, dataSfarsit;

SQLiteDatabase db;

List<String> date;
List<Float> suma;

LineChart lineChart;

Button btnGenerare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistici_cheltuieli);

        btnBackSC=findViewById(R.id.btnBackSC);
        btnGenerare=findViewById(R.id.btnGenerareGraficCh);

        inputDataStart=findViewById(R.id.editDataStartC);
        inputDataSfarsit=findViewById(R.id.editDataSfarsitC);

        lineChart=findViewById(R.id.lineChartCh);

        Conectare conectare = new Conectare();
        db = conectare.returnDataBase();

        btnBackSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StatisticiCheltuieli.this, Statistici.class);
                startActivity(i);
            }
        });

        inputDataStart.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int zi = calendar.get(Calendar.DAY_OF_MONTH);
            int luna = calendar.get(Calendar.MONTH);
            int an = calendar.get(Calendar.YEAR);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticiCheltuieli.this,
                    (DatePicker view, int anSelectat, int lunaSelectata, int ziSelectata) -> {
                        Calendar dataSelectata = Calendar.getInstance();
                        dataSelectata.set(anSelectat,lunaSelectata,ziSelectata);
                        inputDataStart.setText(dateFormat.format(dataSelectata.getTime()));
                        dataStart = dateFormat.format(dataSelectata.getTime());
                    },
                    an, luna, zi
            );
            datePickerDialog.show();
        });

        inputDataSfarsit.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int zi = calendar.get(Calendar.DAY_OF_MONTH);
            int luna = calendar.get(Calendar.MONTH);
            int an = calendar.get(Calendar.YEAR);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticiCheltuieli.this,
                    (DatePicker view, int anSelectat, int lunaSelectata, int ziSelectata) -> {
                        Calendar dataSelectata = Calendar.getInstance();
                        dataSelectata.set(anSelectat,lunaSelectata,ziSelectata);
                        inputDataSfarsit.setText(dateFormat.format(dataSelectata.getTime()));
                        dataSfarsit = dateFormat.format(dataSelectata.getTime());
                    },
                    an, luna, zi
            );
            datePickerDialog.show();
        });


        btnGenerare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataStart == null || dataSfarsit == null || dataStart.isEmpty() || dataSfarsit.isEmpty()) {
                    Toast.makeText(StatisticiCheltuieli.this, "Alegeti data de inceput si data de sfarsit!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = db.rawQuery("SELECT DataCheltuiala, Suma FROM Cheltuieli " +
                                "WHERE DataCheltuiala BETWEEN ? AND ? " +
                                "ORDER BY DataCheltuiala ASC",
                                 new String[]{dataStart, dataSfarsit});

                date = new ArrayList<>();
                suma = new ArrayList<>();

                while (cursor.moveToNext()) {
                    date.add(cursor.getString(cursor.getColumnIndexOrThrow("DataCheltuiala")));
                    suma.add(cursor.getFloat(cursor.getColumnIndexOrThrow("Suma")));
                }
                cursor.close();

                List<Entry> entries = new ArrayList<>();

                for(int i=0; i<date.size(); i++) {
                    entries.add(new Entry(i, suma.get(i)));
                };

                LineDataSet dataSet = new LineDataSet(entries, "Cheltuieli");
                dataSet.setColors(Color.RED);
                dataSet.setCircleColor(Color.RED);

                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);
                lineChart.invalidate();
            }
        });
    }
}