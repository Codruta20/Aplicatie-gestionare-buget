package com.example.aplicatiegestionarebuget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.sql.Connection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
PieChart pieChart;
Button btnVenituri, btnCheltuieli, btnStatistici, btnTranzactii;
SQLiteDatabase db;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Conectare conectare = new Conectare();
        db = conectare.returnDataBase();

        btnVenituri=findViewById(R.id.btnVenituri);
        btnCheltuieli=findViewById(R.id.btnCheltuieli);
        btnStatistici=findViewById(R.id.btnStatistici);
        btnTranzactii=findViewById(R.id.btnTranzactii);

        pieChart=findViewById(R.id.pieChart);

        String queryTotalVenit = "SELECT SUM(Suma) AS Total FROM Venituri";
        String queryTotalChelt = "SELECT SUM(Suma) AS Total FROM Cheltuieli";

        Cursor cursorVenit = db.rawQuery(queryTotalVenit, null);
        Cursor cursorChelt = db.rawQuery(queryTotalChelt, null);

        int totalVenit = 0;
        int totalChelt = 0;

        if(cursorVenit.moveToFirst()) {
            totalVenit=cursorVenit.getInt(cursorVenit.getColumnIndex("Total"));
        }

        if(cursorChelt.moveToFirst()) {
            totalChelt=cursorChelt.getInt(cursorChelt.getColumnIndex("Total"));
        }

        cursorVenit.close();
        cursorChelt.close();
        Log.d("PieChartDebug", "Total Venit: " + totalVenit);
        Log.d("PieChartDebug", "Total Cheltuieli: " + totalChelt);



        ArrayList<PieEntry> valPieChart = new ArrayList<>();
        valPieChart.add(new PieEntry(totalVenit, "Venituri"));
        valPieChart.add(new PieEntry(totalChelt, "Cheltuieli"));

        PieDataSet dataSet = new PieDataSet(valPieChart, "Balanta");
        dataSet.setColors(new int[]{Color.GREEN, Color.RED});
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);

        int balanta = totalVenit - totalChelt;

        pieChart.setData(data);
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(String.valueOf(balanta)+" RON");
        pieChart.setCenterTextSize(18f);
        pieChart.animateY(1000);

        btnVenituri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Venituri.class);
                startActivity(i);
            }
        });
        btnCheltuieli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Cheltuieli.class);
                startActivity(i);
            }
        });
        btnStatistici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Statistici.class);
                startActivity(i);
            }
        });
        btnTranzactii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Tranzactii.class);
                startActivity(i);
            }
        });
    }
}