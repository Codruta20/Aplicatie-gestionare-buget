package com.example.aplicatiegestionarebuget;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class Adaptor extends CursorAdapter {
   private Context context;
   private Cursor cursor;

   public Adaptor(Context context, Cursor cursor, boolean autoRequery) {
       super(context, cursor, autoRequery);
       this.context=context;
       this.cursor=cursor;
   }

   @Override
   public View newView(Context context, Cursor cursor, ViewGroup parent) {
       View view = LayoutInflater.from(context).inflate(R.layout.tranzactie, parent, false);
       return view;
   }

   @Override
    public void bindView(View view, Context context, Cursor cursor) {
       TextView categorie, descriere, data, suma;

       categorie=view.findViewById(R.id.trCategorie);
       descriere=view.findViewById(R.id.trDescriere);
       data=view.findViewById(R.id.trData);
       suma=view.findViewById(R.id.trSuma);

       categorie.setText(cursor.getString(cursor.getColumnIndexOrThrow("Categorie")));
       descriere.setText(cursor.getString(cursor.getColumnIndexOrThrow("Descriere")));
       data.setText(cursor.getString(cursor.getColumnIndexOrThrow("Data")));
       suma.setText(cursor.getString(cursor.getColumnIndexOrThrow("Suma")));
   }


}
