package android.singidunum.ac.flickrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountInfoActivity extends AppCompatActivity {

    Button signOut, info;
    TextView appInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        signOut = findViewById(R.id.signOut);
        info = findViewById(R.id.info);
        appInfo = findViewById(R.id.appInfoLabel);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appInfo.setText("ImgExplore lets you create your own virtual portfolio, browse, like and explore other artists.");
            }
        });

    }
}
