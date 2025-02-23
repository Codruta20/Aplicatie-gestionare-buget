package com.example.aplicatiegestionarebuget;

import static android.widget.AdapterView.*;

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

import java.util.ArrayList;
import java.util.List;

public class Venituri extends AppCompatActivity {
    FloatingActionButton btnBack;
    Spinner spinnerListaCategVenit;
    Button btnAnuleaza, btnSalveaza;

    EditText sumaVenit, descriere;

    SQLiteDatabase db;
    String idVenit;
    List<String> listaIdCategorii;

    String dataSelectata;

    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venituri);

        btnBack=findViewById(R.id.btnBackV);
        btnAnuleaza=findViewById(R.id.btnAnuleazaVen);
        btnSalveaza=findViewById(R.id.btnSalveazaVen);

        spinnerListaCategVenit=findViewById(R.id.spinner);

        sumaVenit=findViewById(R.id.editSumaVen);
        descriere=findViewById(R.id.editDescriereVen);

        calendar=findViewById(R.id.calendarViewVen);

        Conectare conectare = new Conectare();
        db = conectare.returnDataBase();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Venituri.this, MainActivity.class);
                startActivity(i);
            }
        });

        calendar.setOnDateChangeListener(((view, year, month, dayOfMonth) ->
                dataSelectata=year + "-" + String.format("%02d", (month+1)) + "-" + String.format("%02d", dayOfMonth)));

        btnSalveaza.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String sumaVenitString = sumaVenit.getText().toString();
                String descriereString = descriere.getText().toString();

                if(sumaVenitString.isEmpty() && descriereString.isEmpty() && (dataSelectata == null || dataSelectata.isEmpty())){
                    Toast.makeText(Venituri.this, "Data si campurile valoare si descriere trebuie completate", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(sumaVenitString.isEmpty() && (dataSelectata == null || dataSelectata.isEmpty())) {
                    Toast.makeText(Venituri.this,"Data si campul valoare nu au fost completate!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(descriereString.isEmpty() && (dataSelectata == null || dataSelectata.isEmpty())) {
                    Toast.makeText(Venituri.this,"Data si campul descriere nu au fost completate!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(sumaVenitString.isEmpty() && descriereString.isEmpty()) {
                    Toast.makeText(Venituri.this,"Campurile valoare si descriere nu au fost completate!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(dataSelectata == null || dataSelectata.isEmpty()) {
                    Toast.makeText(Venituri.this,"Data nu a fost selectata!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(sumaVenitString.isEmpty()) {
                    Toast.makeText(Venituri.this,"Campul valoare trebuie completat!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (descriereString.isEmpty()) {
                    Toast.makeText(Venituri.this, "Campul descriere trebuie completat!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(spinnerListaCategVenit.getSelectedItemPosition()==0) {
                    Toast.makeText(Venituri.this,"Categoria nu a fost aleasa!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(Venituri.this, "Tranzactia a fost salvata!", Toast.LENGTH_SHORT).show();

                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("Suma", sumaVenit.getText().toString());
                contentValues.put("Descriere", descriere.getText().toString());
                contentValues.put("IdCategorieVenit", idVenit);
                contentValues.put("DataVenit", dataSelectata);
                db.insert("Venituri", null, contentValues);

                resetareCampuri();
            }
        });

        List<String> categorii = categoriiVenituri();

        ArrayAdapter<String> adaptor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorii);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListaCategVenit.setAdapter(adaptor);

        spinnerListaCategVenit.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idVenit = listaIdCategorii.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAnuleaza.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resetareCampuri();
            }
        });
    }

    private void resetareCampuri() {
        sumaVenit.setText("");
        descriere.setText("");

        dataSelectata = "";
        calendar.setDate(System.currentTimeMillis());

        spinnerListaCategVenit.setSelection(0);

        Toast.makeText(Venituri.this, "Campurile au fost resetate!", Toast.LENGTH_SHORT).show();
    }


    public List<String> categoriiVenituri() {
        List<String> listaCategorii = new ArrayList<>();
         listaIdCategorii = new ArrayList<>();

         listaCategorii.add("Alegeti categoria");
         listaIdCategorii.add("");

        String querySelectare = "SELECT IdCategorieVenit, NumeCategorie FROM CategoriiVenituri";

        Cursor cursor = db.rawQuery(querySelectare,null);

        if (cursor.moveToFirst()) {
            do {
                listaIdCategorii.add(cursor.getString(cursor.getColumnIndexOrThrow("IdCategorieVenit")));
                listaCategorii.add(cursor.getString(cursor.getColumnIndexOrThrow("NumeCategorie")));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return listaCategorii;
    };
}