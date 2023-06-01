package com.example.logsignsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EgzersizDB extends SQLiteOpenHelper {
    public static final String databaseName = "Egzersiz.db";
    public EgzersizDB(@Nullable Context context) {
        super(context, "Egzersiz.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("CREATE TABLE Egzersizler (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, playerId INTEGER, FOREIGN KEY(playerId) REFERENCES players(id))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists Egzersizler");
    }

    public void clear() {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        MyDatabase.execSQL("DELETE from Egzersizler");
    }

    public Boolean insertData(int playerId, String name){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("playerId", playerId);
        contentValues.put("name", name);
        long result = MyDatabase.insert("Egzersizler", null, contentValues);
        return result != -1;
    }

    public List<Egzersiz> getEgzersizList(int playerId) {
        List<Egzersiz> EgzersizList = new ArrayList<>();

        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM Egzersizler where playerId = ?", new String[]{Integer.toString(playerId)});

        int idColumnIndex = cursor.getColumnIndex("id");
        int playerIdColumnIndex = cursor.getColumnIndex("playerId");
        int nameColumnIndex = cursor.getColumnIndex("name");

        if (cursor.moveToFirst()) {
            do {
                int _id = (idColumnIndex != -1) ? cursor.getInt(idColumnIndex) : 0;
                int _playerId = (playerIdColumnIndex != -1) ? cursor.getInt(playerIdColumnIndex) : 0;
                String _name = (nameColumnIndex != -1) ? cursor.getString(nameColumnIndex) : "";
                Egzersiz egzersiz = new Egzersiz(_id, _playerId, _name);
                EgzersizList.add(egzersiz);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return EgzersizList;
    }
}
