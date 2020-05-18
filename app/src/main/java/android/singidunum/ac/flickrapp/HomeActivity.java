package android.singidunum.ac.flickrapp;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    //klasa koja sluzi prakticno kao placeholder za fragmente koji se nalaze u okviru tablayota i koja ih prakticno drzi sve na okupu


    //inicijalizacija komponenata toolbar-a

    private ViewPager viewPager;
    private TabLayout tabLayout;

    //inicijalizacija fragmenata

    private GalleryFragment galleryFragment;
    private ExploreFragment exploreFragment;
    private ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_home);


        //referenciranje komponenti toolbara i tab layouta
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        //referenciranje fragmenata

        galleryFragment = new GalleryFragment();
        exploreFragment = new ExploreFragment();
        profileFragment = new ProfileFragment();

        //setovanje viewPager-a i adaptera

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(galleryFragment, "Gallery");
        viewPagerAdapter.addFragment(exploreFragment, "Explore");
        viewPagerAdapter.addFragment(profileFragment, "Profile");
        viewPager.setAdapter(viewPagerAdapter);



    }

}
