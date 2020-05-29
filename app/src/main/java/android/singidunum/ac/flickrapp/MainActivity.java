package android.singidunum.ac.flickrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Login forma


    //inicijalizacija komponenata
    EditText inputSignInEmail, inputSignInPassword;
    Button buttonLogin;
    DatabaseHelper db;

    ImageView image;
    TextView message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        inputSignInEmail = (EditText) findViewById(R.id.inputSignInEmail);
        inputSignInPassword = (EditText) findViewById(R.id.inputSignInPasswd);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        //za animaciju
        image = findViewById(R.id.logoImage);
        message = findViewById(R.id.labelWelcome);

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String email = inputSignInEmail.getText().toString();
                String password = inputSignInPassword.getText().toString();

                Boolean Checkemailpass = db.emailAndPasswdCheck(email,password);
                if(Checkemailpass == true){
                    Toast.makeText(getApplicationContext(), "Successfuly logged in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), HomeActivity.class).putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid credientals", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

  public void openSignInActivity(View v){
        Intent intent = new Intent(this, Signup_form.class);
        //za animaciju, animira se logo i text Welcome back...
        Pair[] pairs = new Pair[2];

        pairs[0] = new Pair<View, String>(image, "logo_image");
        pairs[1] = new Pair<View, String>(message, "logo_text");

    //obzirom da je animacija podrzana u api nivou 21 obavezan if kako aplikacija ne bi crashovala
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
          ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
          startActivity(intent, options.toBundle());
      }




  }



}
