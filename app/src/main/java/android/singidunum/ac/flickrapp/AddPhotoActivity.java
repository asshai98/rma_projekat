package android.singidunum.ac.flickrapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

public class AddPhotoActivity extends AppCompatActivity {

    private ImageView objectImageView;
    private EditText imageTitle, imageAuthor, imageDescription;
    private static final int IMAGE_REQUEST=100;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        objectImageView = findViewById(R.id.imageView);
        imageTitle = findViewById(R.id.imgName);
        imageAuthor = findViewById(R.id.imgAuthor);
        imageDescription = findViewById(R.id.imgDescription);

        dbHelper = new DatabaseHelper(this);

    }

    public void chooseImage(View objectView){
        Intent objectIntent = new Intent();
        objectIntent.setType("image/*");
        objectIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(objectIntent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            imageFilePath = data.getData();
            try {
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);

            } catch (IOException e) {
                e.printStackTrace();
            }

            objectImageView.setImageBitmap(imageToStore);

        }
    }

    public void storeImage(View view){
        try{
            dbHelper.storeImage(new ImageModelClass(imageTitle.getText().toString(), imageAuthor.getText().toString(), imageDescription.getText().toString(), imageToStore));
            Toast.makeText(this, "Successfuly added", Toast.LENGTH_SHORT).show();

        } catch (SQLiteException e){
            e.printStackTrace();
        }

    }
}
