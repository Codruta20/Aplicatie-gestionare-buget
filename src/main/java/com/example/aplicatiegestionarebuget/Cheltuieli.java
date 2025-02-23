package com.example.aplicatiegestionarebuget;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cheltuieli extends AppCompatActivity {
FloatingActionButton btnBackC;
Button btnSalveaza, btnAnuleaza;

Spinner spinnerListaCategoriiChelt;

EditText sumaChelt, descriere;

SQLiteDatabase db;
String idCheltuiala;

List<String> listaIdCategorii;

String dataSelectata;

CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheltuieli);

        btnBackC=findViewById(R.id.btnBackC);
        btnAnuleaza=findViewById(R.id.btnAnuleazaCh);
        btnSalveaza=findViewById(R.id.btnSalveazaCh);

        spinnerListaCategoriiChelt=findViewById(R.id.spinner2);

        sumaChelt=findViewById(R.id.editSumaCh);
        descriere=findViewById(R.id.editDescriereCh);

        calendar=findViewById(R.id.calendarViewCh);

        Conectare conectare = new Conectare();
        db = conectare.returnDataBase();

        btnBackC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Cheltuieli.this, MainActivity.class);
                startActivity(i);
            }
        });

        calendar.setOnDateChangeListener(((view, year, month, dayOfMonth) ->
                dataSelectata=year + "-" + String.format("%02d", (month+1)) + "-" + String.format("%02d", dayOfMonth)));


        btnSalveaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sumaCheltString = sumaChelt.getText().toString();
                String descriereString = descriere.getText().toString();

                if(sumaCheltString.isEmpty() && descriereString.isEmpty() && (dataSelectata == null || dataSelectata.isEmpty())){
                    Toast.makeText(Cheltuieli.this, "Data si campurile valoare si descriere trebuie completate", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(sumaCheltString.isEmpty() && (dataSelectata == null || dataSelectata.isEmpty())) {
                    Toast.makeText(Cheltuieli.this,"Data si campul valoare nu au fost completate!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(descriereString.isEmpty() && (dataSelectata == null || dataSelectata.isEmpty())) {
                    Toast.makeText(Cheltuieli.this,"Data si campul descriere nu au fost completate!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(sumaCheltString.isEmpty() && descriereString.isEmpty()) {
                    Toast.makeText(Cheltuieli.this,"Campurile valoare si descriere nu au fost completate!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(dataSelectata == null || dataSelectata.isEmpty()) {
                    Toast.makeText(Cheltuieli.this,"Data nu a fost selectata!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(sumaCheltString.isEmpty()) {
                    Toast.makeText(Cheltuieli.this,"Campul valoare trebuie completat!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (descriereString.isEmpty()) {
                    Toast.makeText(Cheltuieli.this, "Campul descriere trebuie completat!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(spinnerListaCategoriiChelt.getSelectedItemPosition()==0) {
                    Toast.makeText(Cheltuieli.this,"Categoria nu a fost aleasa!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(Cheltuieli.this, "Tranzactia a fost salvata!", Toast.LENGTH_SHORT).show();
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("Suma", sumaChelt.getText().toString());
                contentValues.put("Descriere", descriere.getText().toString());
                contentValues.put("IdCategorieCheltuiala", idCheltuiala);
                contentValues.put("DataCheltuiala", dataSelectata);
                db.insert("Cheltuieli", null, contentValues);

                resetareCampuri();
            }
        });

        List<String> categorii = categoriiCheltuieli();

        ArrayAdapter<String> adaptor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorii);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListaCategoriiChelt.setAdapter(adaptor);

        spinnerListaCategoriiChelt.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idCheltuiala = listaIdCategorii.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAnuleaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetareCampuri();
            }
        });

    }

    public void resetareCampuri() {
        sumaChelt.setText("");
        descriere.setText("");

        dataSelectata = "";
        calendar.setDate(System.currentTimeMillis());

        spinnerListaCategoriiChelt.setSelection(0);

        Toast.makeText(Cheltuieli.this, "Campurile au fost resetate!", Toast.LENGTH_SHORT).show();
    }

    public List<String> categoriiCheltuieli() {
        List<String> listaCategorii = new ArrayList<>();
        listaIdCategorii = new ArrayList<>();

        listaCategorii.add("Alegeti categoria");
        listaIdCategorii.add("");

        String querySelectare = "SELECT IdCategorieCheltuiala, NumeCategorie FROM CategoriiCheltuieli";


        Cursor cursor = db.rawQuery(querySelectare,null);

        if (cursor.moveToFirst()) {
            do {
                listaIdCategorii.add(cursor.getString(cursor.getColumnIndexOrThrow("IdCategorieCheltuiala")));
                listaCategorii.add(cursor.getString(cursor.getColumnIndexOrThrow("NumeCategorie")));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return listaCategorii;
    };
}