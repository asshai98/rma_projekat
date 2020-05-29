package android.singidunum.ac.flickrapp;

import android.graphics.Bitmap;
import android.util.Log;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ImageModelClass {
    //klasa za slike koje ce korisnik moci da dodaje na svoj profil u cameraroll sekciji

    private String imageName, imageAuthor, imageDescription, favStatus;
    private Bitmap image;


    public ImageModelClass(String imageName, String imageAuthor, String imageDescription, Bitmap image) {
        this.imageName = imageName;
        this.imageAuthor = imageAuthor;
        this.imageDescription = imageDescription;
        this.image = image;
        this.favStatus = "0";
    }

    public String getFavStatus() {
        return favStatus;

    }



    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageAuthor() {
        return imageAuthor;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImageAuthor(String imageAuthor) {
        this.imageAuthor = imageAuthor;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


}
