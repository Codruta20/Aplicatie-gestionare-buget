package com.example.aplicatiegestionarebuget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatisticiVenituri extends AppCompatActivity {
FloatingActionButton btnBackSV;
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
        setContentView(R.layout.activity_statistici_venituri);

        btnBackSV=findViewById(R.id.btnBackSV);
        btnGenerare=findViewById(R.id.btnGenerareGraficVen);

        inputDataStart=findViewById(R.id.editDataStartV);
        inputDataSfarsit=findViewById(R.id.editDataSfarsitV);

        lineChart=findViewById(R.id.lineChartVen);

        Conectare conectare = new Conectare();
        db = conectare.returnDataBase();


        btnBackSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StatisticiVenituri.this, Statistici.class);
                startActivity(i);
            }
        });

        inputDataStart.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int zi = calendar.get(Calendar.DAY_OF_MONTH);
            int luna = calendar.get(Calendar.MONTH);
            int an = calendar.get(Calendar.YEAR);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticiVenituri.this,
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

            DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticiVenituri.this,
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
                    Toast.makeText(StatisticiVenituri.this, "Alegeti data de inceput si data de sfarsit!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(dataStart, dataSfarsit);
                Cursor cursor = db.rawQuery("SELECT DataVenit, Suma FROM Venituri " +
                                "WHERE DataVenit BETWEEN ? AND ? " +
                                "ORDER BY DataVenit ASC",
                                 new String[]{dataStart, dataSfarsit});

                date = new ArrayList<>();
                suma = new ArrayList<>();

                while (cursor.moveToNext()) {
                    date.add(cursor.getString(cursor.getColumnIndexOrThrow("DataVenit")));
                    suma.add(cursor.getFloat(cursor.getColumnIndexOrThrow("Suma")));
                }
                cursor.close();

                List<Entry> entries = new ArrayList<>();

                for(int i=0; i<date.size(); i++) {
                    entries.add(new Entry(i, suma.get(i)));
                };

                LineDataSet dataSet = new LineDataSet(entries, "Venituri");
                dataSet.setColors(Color.GREEN);
                dataSet.setCircleColor(Color.GREEN);

                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);
                lineChart.invalidate();
            }
        });
    }
}