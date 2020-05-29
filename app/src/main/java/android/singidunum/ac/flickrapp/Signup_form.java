package android.singidunum.ac.flickrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signup_form extends AppCompatActivity {

    //Forma za registraciju

    //inicijalizacija potrebnih komponenata

    EditText inputForname, inputSurname, inputEmail, inputPassword, inputConfPassword;
    Button buttonRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);

        //pravljenje db helper objekta

        db = new DatabaseHelper(this);

        //referenciranje stvarnih input polja sa register forme

        inputForname = (EditText) findViewById(R.id.inputForename);
        inputSurname = (EditText) findViewById(R.id.inputSurname);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputConfPassword = (EditText) findViewById(R.id.inputConfirmPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kupimo podatke iz input polja referenciranih preko R.java klase

                String forename = inputForname.getText().toString();
                String surname = inputSurname.getText().toString();
                String email = inputEmail.getText().toString();
                String passwd  = inputPassword.getText().toString();
                String passwdConf = inputConfPassword.getText().toString();

                //validacija
                if(forename.equals("") || surname.equals("") || email.equals("") || passwd.equals("") || passwdConf.equals("")){
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    if(passwd.equals(passwdConf)){
                        Boolean chkemail = db.checkEmail(email);
                        if(chkemail == true){
                            Boolean insert = db.insertData(forename, surname, email, passwd);
                            if(insert == true){
                                Toast.makeText(getApplicationContext(), "Registered Successfuly", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(v.getContext(), HomeActivity.class).putExtra("email", email);;
                                startActivity(intent);
                            }
                        }

                        else {
                            Toast.makeText(getApplicationContext(), "Email already used", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords dont match", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });


    }
}
