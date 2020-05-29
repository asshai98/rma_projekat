package android.singidunum.ac.flickrapp;

public class FavItem {
    //favorutie collection koje ce korisnici moci da prave
    private String imageTitle, userEmail;

    public FavItem(String imageTitle, String userEmail) {
        this.imageTitle = imageTitle;
        this.userEmail = userEmail;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
