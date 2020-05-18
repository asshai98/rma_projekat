package android.singidunum.ac.flickrapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    //fragment koji sluzi kao placeholder za ostale fragmene, About, Collections i CameraRoll gde ce se prikazivati dodatne informacije povucene iz baze, druge aktivnosti ili samog telefona

    //inicijalizacija komponenata fragmenta

    private ViewPager viewPager;
    private TabLayout tabLayout;

    //inicijalizacija fragmenata

    private CameraRollFragment cameraRollFragment;
    private CollectionFragment collectionFragment;
    private AboutFragment aboutFragment;


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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

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

        // Inflate the layout for this fragment
        return view;
    }
}
