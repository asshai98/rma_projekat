package android.singidunum.ac.flickrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    //prva aktivnost koja se pokrece prilikom startup-a aplikacije, rad sa animacijama i tranzicijama prilikom poziva login aktivnosti
    //u android manifestu podesen intent filter LAUNCH i MainActivity

    private static int SPLASH_SCREEN = 1400; //broj sekundi trajanja animacije

    //za animacije, inicijalizujemo odgovarajuce varijable kao i polja iz View-a

    Animation topAnimation, bottAnimation;
    ImageView image;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sklanjamo app bar i info bar kako bi aktivnost bila fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);


        //u kom konteksu koristimo koju animaciju def u xml-u
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //referenciramo polja iz R.java klase
        image = findViewById(R.id.imageView);
        message = findViewById(R.id.textView2);

        //setovanje animacija pronadjenim komponentama
        image.setAnimation(bottAnimation);
        message.setAnimation(topAnimation);

        //handler za pokretanje novog intenta preko animacije, poruka i slika se animira na isti nacin kao i na welcome screen-u kako bi se stvorio efekat lagane tranzicije izmedju aktivnosti
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(message, "logo_text");

                //preko tranzicije prenosimo logo i text u sledecu aktivnost
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                } //neophodan if jer je rad sa tranzicijama podrzan u API levelu 21
            }
        }, SPLASH_SCREEN);

    }


}
