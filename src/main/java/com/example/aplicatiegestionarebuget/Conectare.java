package com.example.aplicatiegestionarebuget;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class Conectare {
    public SQLiteDatabase returnDataBase(){
        try {
            String databasePath = "/data/data/com.example.aplicatiegestionarebuget/database/DB.db";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
            return db;
        } catch (SQLiteException e) {
            throw new RuntimeException(e);
        }
    }
}
