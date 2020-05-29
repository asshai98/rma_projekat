package android.singidunum.ac.flickrapp;

import android.graphics.Bitmap;

public class UserModel {
    private String forename, surname, email, password;
    private Bitmap profileImage;

    public UserModel(String forename, String surname, String email, String password) {
        this.forename = forename;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }


    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
