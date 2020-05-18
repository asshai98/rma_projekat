package android.singidunum.ac.flickrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import static android.singidunum.ac.flickrapp.GalleryFragment.PHOTO_TRANSFER;

public class PhotoDetailActivity extends AppCompatActivity {

    /* Aktivnost koja se okida kada korisnik 'klikne' na sliku i gde se prikazuju detalji slike pokupljene sa API-ja */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        //setujemo dohvacene informacije u odg. polja

        if(photo != null){
            TextView photoTitle = (TextView) findViewById(R.id.photoTitle);
            photoTitle.setText("Title: " + photo.getTitle());

            TextView photoAuthor = (TextView) findViewById(R.id.photoAuthor);
            photoAuthor.setText("Author: " + photo.getAuthor());

            TextView photoDesc = (TextView) findViewById(R.id.photoDesc);
            photoDesc.setText("Description: " + photo.getDescription());

            TextView photoTags = (TextView) findViewById(R.id.photoTags);
            photoTags.setText("Tags: " + photo.getTags());

            //za ucitavanje slike
            ImageView photoView = (ImageView) findViewById(R.id.photoView);

            Picasso.with(this).load(photo.getLink())
                    .error(R.drawable.baseline_photo_black_48dp)
                    .placeholder(R.drawable.baseline_photo_black_48dp)
                    .into(photoView);
        }
    }
}
