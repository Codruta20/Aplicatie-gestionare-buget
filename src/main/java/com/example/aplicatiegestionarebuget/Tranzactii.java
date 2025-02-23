package com.example.aplicatiegestionarebuget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tranzactii extends AppCompatActivity {
    FloatingActionButton btnBack;
    SQLiteDatabase db;
    ListView lista;

    String querySelectare;

    Adaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tranzactii);

        btnBack=findViewById(R.id.btnBackT);

        lista=findViewById(R.id.listTranzactii);

        Conectare conectare = new Conectare();
        db = conectare.returnDataBase();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Tranzactii.this, MainActivity.class);
                startActivity(i);
            }
        });

        querySelectare = "SELECT Venituri.IdVenit AS _id, Venituri.DataVenit AS Data, Venituri.Suma AS Suma, Venituri.Descriere AS Descriere, " +
                         "DataInserare, CategoriiVenituri.NumeCategorie AS Categorie, 'venit' as tip   FROM Venituri " +
                         "INNER JOIN CategoriiVenituri ON Venituri.IdCategorieVenit=CategoriiVenituri.IdCategorieVenit " +
                         "UNION " +
                         "SELECT Cheltuieli.IdCheltuiala AS _id, Cheltuieli.DataCheltuiala AS Data, Cheltuieli.Suma AS Suma, Cheltuieli.Descriere AS Descriere, " +
                         "DataInserare,  CategoriiCheltuieli.NumeCategorie AS Categorie, 'cheltuiala' as tip FROM Cheltuieli " +
                         "INNER JOIN CategoriiCheltuieli ON Cheltuieli.IdCategorieCheltuiala=CategoriiCheltuieli.IdCategorieCheltuiala " +
                         "ORDER BY DataInserare DESC";



        Cursor cursor = db.rawQuery(querySelectare,null);


        Adaptor adaptor = new Adaptor(this, cursor, false);

        lista.setAdapter(adaptor);



        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursorNou = (Cursor) parent.getItemAtPosition(position);
                String categorie = cursorNou.getString(cursorNou.getColumnIndexOrThrow("Categorie"));
                String descriere = cursorNou.getString(cursorNou.getColumnIndexOrThrow("Descriere"));
                String data = cursorNou.getString(cursorNou.getColumnIndexOrThrow("Data"));
                String suma = cursorNou.getString(cursorNou.getColumnIndexOrThrow("Suma"));
                String tip = cursorNou.getString(cursorNou.getColumnIndexOrThrow("tip"));
                String Id = cursorNou.getString(cursorNou.getColumnIndexOrThrow("_id"));

                afisareModal(categorie, descriere, data, suma, tip, Id);

            }
        });
    }

    private void afisareModal(String categorie, String descriere, String data, String suma, String tip, String Id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detalii tranzactie");

        String detalii = "Categorie: " + categorie +
                         "\n" +
                         "Descriere: " + descriere +
                         "\n" +
                         "Data: " + data +
                         "\n" +
                         "Suma: " + suma + " RON";


        builder.setMessage(detalii);


        builder.setPositiveButton("Închide", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setNegativeButton("STERGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tabel = Objects.equals(tip, "venit") ? "Venituri" : "Cheltuieli";

                String where = Objects.equals(tip, "venit") ? "IdVenit=?" : "IdCheltuiala=?";

                stergeTranzactie(Integer.parseInt(Id),tabel,where);

                refreshTranzactii();
            }
        });
        builder.show();
    }

    private void stergeTranzactie(Integer id, String numeTabel, String where) {
        db.delete(numeTabel, where, new String[]{String.valueOf(id)});
    }


    private void refreshTranzactii() {
        Cursor cursorNou = db.rawQuery(querySelectare, null);
        adaptor.changeCursor(cursorNou);
        adaptor.notifyDataSetChanged();
    }
}