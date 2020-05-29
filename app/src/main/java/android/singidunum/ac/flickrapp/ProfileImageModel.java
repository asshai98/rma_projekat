package android.singidunum.ac.flickrapp;

import android.graphics.Bitmap;

//klasa za korisnicku profilnu sliku

public class ProfileImageModel {
    private Bitmap image;

    public ProfileImageModel(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
