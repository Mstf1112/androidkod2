package com.example.logsignsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlayerDB extends SQLiteOpenHelper {
    public static final String databaseName = "Player.db";
    public PlayerDB(@Nullable Context context) {
        super(context, "Player.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("CREATE TABLE players (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists players");
    }

    public void clear() {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        MyDatabase.execSQL("DELETE from players");
    }

    public Boolean insertData(String name){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        long result = MyDatabase.insert("players", null, contentValues);
        return result != -1;
    }

    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }

    public List<Player> getPlayerList() {
        List<Player> playerList = new ArrayList<>();

        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM players", null);

        int idColumnIndex = cursor.getColumnIndex("id");
        int nameColumnIndex = cursor.getColumnIndex("name");

        if (cursor.moveToFirst()) {
            do {
                int id = (idColumnIndex != -1) ? cursor.getInt(idColumnIndex) : 0;
                String name = (nameColumnIndex != -1) ? cursor.getString(nameColumnIndex) : "";
                Player player = new Player(id, name);
                playerList.add(player);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return playerList;
    }

    @Nullable
    public Player getPlayer(int id) {
        SQLiteDatabase myDatabase = this.getReadableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM players where id = ?", new String[]{Integer.toString(id)});

        int idColumnIndex = cursor.getColumnIndex("id");
        int nameColumnIndex = cursor.getColumnIndex("name");

        if (cursor.moveToFirst()) {
            do {
                int _id = (idColumnIndex != -1) ? cursor.getInt(idColumnIndex) : 0;
                String name = (nameColumnIndex != -1) ? cursor.getString(nameColumnIndex) : "";
                Player player = new Player(_id, name);
                return player;
            } while (cursor.moveToNext());
        }

        cursor.close();
        return null;
    }
}