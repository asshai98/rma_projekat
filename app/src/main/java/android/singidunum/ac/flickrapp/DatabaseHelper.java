package android.singidunum.ac.flickrapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInByte;

    //rad sa SQLite engin-om, DatabaseHelper obavlja osnovne CRUD operacije

    //konstruktor - navodimo za koju aktivnost se koristi, naziv baze, factory metod i verziju baze

    public DatabaseHelper(@Nullable Context context) {
        super(context, "Login.db", null, 13);
    }

    //CRUD metodi, pravimo tabelu user sa nazivima odg polja preko execSQL naredbe koja parsira sql upit
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(user_id integer primary key not null, forename text, surname text, email text, password text)");
        db.execSQL("Create table image(image_id integer primary key not null, title text, author text, description text, image_object blob)");
        db.execSQL("Create table favourites(favourite_id integer primary key  not null, image_title text, user_email text, fav_status text)");
        db.execSQL("Create table profile_images(profile_image_id integer primary key not null, profile_image blob, user_email text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists image");
        db.execSQL("drop table if exists favourites");
        db.execSQL("drop table if exists profile_images");
        onCreate(db);
    }


    //cuvanje slike koje korisnici ubacuju na svoj profil

    public boolean storeImage(ImageModelClass objectModelClass){
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap imageToStoreBitmap = objectModelClass.getImage();

        objectByteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
        imageInByte = objectByteArrayOutputStream.toByteArray();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", objectModelClass.getImageName());
        contentValues.put("author", objectModelClass.getImageAuthor());
        contentValues.put("description", objectModelClass.getImageDescription());
        contentValues.put("image_object", imageInByte);

        long insert = db.insert("image", null, contentValues);

        if(insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    //cuvanje u favourites
    public void addFavourite(String image_title, String user_email, String fav_status){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("image_title", image_title);
        values.put("user_email", user_email);
        values.put("fav_status", fav_status);
        db.insert("favourites", null, values);
    }

    //citanje podataka
    public Cursor readAllFavourites(String user_email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from favourites where user_email=?", new String[]{user_email});
        return cursor;
    }

    //brisanje
    public void removeFromFavourites(String user_email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newContentValues = new ContentValues();
        newContentValues.put("fav_status", "0");
        String[] whereArgs = new String[] {String.valueOf(user_email)};
        db.update("favourites", newContentValues, "user_email=?", whereArgs);
    }

    //selekcije svega iz liste ciji je fav_status = 1
    //public Cursor selectAllFromFavouriteList(){
      //  SQLiteDatabase db = this.getReadableDatabase();
      //  String sql = "select * from favourites where fav_status='1'";
      //  return db.rawQuery(sql, null, null);
 //   }

    //prikupljanje podataka korisnickih slika iz cameraroll sekcije

    public ArrayList<ImageModelClass> getAllImagesData(String author){
        SQLiteDatabase object = this.getReadableDatabase();
        ArrayList<ImageModelClass> list = new ArrayList<>();

        Cursor cursor = object.rawQuery("Select * from image where author=?", new String[]{author});

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                String imageTitle = cursor.getString(1);
                String authorName = cursor.getString(2);
                String description = cursor.getString(3);

                byte[] imageBytes = cursor.getBlob(4);
                Bitmap objectBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                list.add(new ImageModelClass(imageTitle, authorName, description, objectBitmap));
            }
        }
        return list;
    }

    //prikupljanje korisnickih podataka

    public UserModel getUserData(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        UserModel userModel = null;
        Cursor cursor = db.rawQuery("Select * from user where email=?", new String[]{username});
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                String forename = cursor.getString(1);
                String surname = cursor.getString(2);
                String email = cursor.getString(3);
                String password = cursor.getString(4);
                userModel = new UserModel(forename, surname, email, password);

            }
        }
        return userModel;
    }

    public void editUserData(String forename, String surname, String email, String password, String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newContentValues = new ContentValues();
        newContentValues.put("forename", forename);
        newContentValues.put("surname", surname);
        newContentValues.put("email", email);
        newContentValues.put("password", password);
        String[] whereArgs = new String[] {String.valueOf(username)};

        db.update("user", newContentValues, "email=?", whereArgs);
    }


    //cuvanje profilne slike

    public boolean storeProfileImage(Bitmap profileImageToStore, String email){

        SQLiteDatabase db = this.getWritableDatabase();

        objectByteArrayOutputStream = new ByteArrayOutputStream();
        profileImageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
        imageInByte = objectByteArrayOutputStream.toByteArray();

        ContentValues contentValues = new ContentValues();
        contentValues.put("profile_image", imageInByte);
        contentValues.put("user_email", email);

        long insert = db.insert("profile_images", null, contentValues);
        if(insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    //prikupljanje profilne slike za odgovarajuce usera
    public ProfileImageModel getUserProfileImage(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        ProfileImageModel profileImageModel = null;
        Cursor cursor = db.rawQuery("Select * from profile_images where user_email=?", new String[]{username});
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                byte[] profileImageBytes = cursor.getBlob(1);
                Bitmap profileImage = BitmapFactory.decodeByteArray(profileImageBytes, 0, profileImageBytes.length);
                profileImageModel = new ProfileImageModel(profileImage);
            }
        }

        return  profileImageModel;
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
