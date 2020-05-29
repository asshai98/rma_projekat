package android.singidunum.ac.flickrapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    DatabaseHelper databaseHelper;
    String user;
    private static final int IMAGE_REQUEST=100;
    private Uri imageFilePath;
    private Bitmap imageToStore;

    //fragment koji sluzi kao placeholder za ostale fragmene, About, Collections i CameraRoll gde ce se prikazivati dodatne informacije povucene iz baze, druge aktivnosti ili samog telefona

    //inicijalizacija komponenata fragmenta

    private ViewPager viewPager;
    private TabLayout tabLayout;

    //inicijalizacija fragmenata

    private CameraRollFragment cameraRollFragment;
    private CollectionFragment collectionFragment;
    private AboutFragment aboutFragment;
    private ImageView setProfileImage;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tablayout);

        //pravljenje fragmenata

        cameraRollFragment = new CameraRollFragment();
        collectionFragment = new CollectionFragment();
        aboutFragment = new AboutFragment();

        //setovanje viewPager-a i adaptera

        tabLayout.setupWithViewPager(viewPager);
        //dodajemo u listu fragment i naziv fragmenta koji se prikazati u tabovima
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(),0);
        viewPagerAdapter.addFragment(cameraRollFragment, "CameraRoll");
        viewPagerAdapter.addFragment(collectionFragment, "Collections");
        viewPagerAdapter.addFragment(aboutFragment, "About");
        viewPager.setAdapter(viewPagerAdapter);

        if(savedInstanceState == null) {
            Bundle bundle = getActivity().getIntent().getExtras();
            if(bundle == null){
                user = null;
            } else {
                user = bundle.getString("email");
            }
        } else {
            user = (String) savedInstanceState.getSerializable("email");
        }

        //kada korisnik klikne na ikonicu kamere moze da odabere folder iz kojeg zeli da uplouduje sliku
        ImageView profileImage = view.findViewById(R.id.addProfileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objectIntent = new Intent();
                objectIntent.setType("image/*");
                objectIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(objectIntent, IMAGE_REQUEST);
            }
        });

        setProfileImage = view.findViewById(R.id.profile_image);

        //setovanje slike
        ImageView profilePic = view.findViewById(R.id.profile_image);

        ProfileImageModel profileImageModel = databaseHelper.getUserProfileImage(user);

        if(profileImageModel == null){
            profilePic.setImageResource(R.drawable.avatar);
        } else {
            profilePic.setImageBitmap(profileImageModel.getImage());
        }

        //meni za pokretanje nove aktivnosti

        ImageView profileMenu = view.findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AccountInfoActivity.class);
                startActivity(i);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            imageFilePath = data.getData();
            try {
                imageToStore = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            setProfileImage.setImageBitmap(imageToStore);
            databaseHelper.storeProfileImage(imageToStore, user);

        }
    }
}
