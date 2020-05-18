package android.singidunum.ac.flickrapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //rad sa SQLite engin-om, DatabaseHelper obavlja osnovne CRUD operacije, insertuje podatke prilikom registracije u tabelu user i proverava validnost unetih podataka


    //konstruktor - navodimo za koju aktivnost se koristi, naziv baze, factory metod i verziju baze

    public DatabaseHelper(@Nullable Context context) {
        super(context, "Login.db", null, 1);
    }

    //CRUD metodi, pravimo tabelu user sa nazivima odg polja preko execSQL naredbe koja parsira sql upit, onUpdate brise vec postojecu tabelu user ukoliko ona postoji
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(user_id integer primary key not null, forename text, surname text, email text, password text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
    }

    //rad sa register formom, kupimo podatke iz input polja koja setujemo unutar contentValues, a zatim preko naredbe insert ubacujemo u tabelu user

    public boolean insertData(String forename, String surname, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("forename", forename);
        contentValues.put("surname", surname);
        contentValues.put("email", email);
        contentValues.put("password", password);
        long insert = db.insert("user", null, contentValues);

        if(insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    //provera da li email vec postoji u bazi

    public Boolean checkEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase(); //ucitavamo tabelu u readable modu kako bi mogli da isparsiramo SELECT naredbe, tj procitali podatke iz tabele

        Cursor curosor = db.rawQuery("Select * from user where email=?", new String[]{email});  //pokazivac na trenutno iscitan podatak

        if (curosor.getCount() > 0) return false;
        else return true;
    }

    //provera da li su email i pasword validni

    public Boolean emailAndPasswdCheck(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase(); //ucitavamo tabelu u readable modu kako bi mogli da isparsiramo SELECT naredbe, tj procitali podatke iz tabele

        Cursor cursor = db.rawQuery("Select * from user where email=? and password=?", new String[]{email, password});

        if(cursor.getCount() > 0) return true;
        else return false;
    }
}
