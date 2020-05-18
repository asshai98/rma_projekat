package android.singidunum.ac.flickrapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment implements GetJsonData.OnDataAvilable {

    //Fragment koji prikazuje unutar RecyclerView-a slike povucene preko apija, sa implementiranim osluskivacima za touch evente, kako bi kada korisnik dodirne neku sliku
    //mogla da se otvori nova aktivnost koja ce prikazati dodatne informacije poput opisa, naziva autora i tagova o toj odredjenoj slici
    //koristi se recyclerview koji se dinamicki popunjava slikama koje se skidaju sa apija kako korsnik skrola kroz njega, pri cemu se placeholderi napravljeni u xml fajlu zamenjuju stvarnom slikom

    private static final String TAG = "GalleryFragment";
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";
    private ImageRecyclerViewAdapter imageRecyclerViewAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
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


    //prilikom poziva svakog onResume() metoda, pravi se nova instanca GetJsonData klase koja se pokrece, ista prica kao kod Explore fragmenta,
    //neophodno je da se pre exetuce() metoda napravi nova instaca kako ne bi doslo do izbacivanja nullpointer exp zbog toga sto pokusvamo da pokrenemo nit koja se vec odvija u pozadini
    @Override
    public void onResume() {
        super.onResume();
        GetJsonData getJsonData = new GetJsonData("https://www.flickr.com/services/feeds/photos_public.gne","en-us",true, this);
        getJsonData.execute("landscape");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Kao kod explore fragmenta inflejtujemo layout

        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        /* za imageView korisitmo poseban adapter, kome cemo dodeliti kontekst u kome ga koristimo, tj za koju je aktivnost vezan i koja treba da ga izrenderuje,
           kao i listu fotografija, koja ce biti popunjena podacima koji su povuceni preko API=ja */
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        imageRecyclerViewAdapter = new ImageRecyclerViewAdapter(new ArrayList<Photo>(), getContext());
        recyclerView.setAdapter(imageRecyclerViewAdapter);

        return v;
    }

    //implementacije metoda onDataAvilable iz istog interfejsa
    @Override
    public void onDataAvilable(List<Photo> data, DownloadStatus status){
        if(status == DownloadStatus.OK){
            imageRecyclerViewAdapter.loadNewData(data);
        } else {
            //ukoliko download i procesiranje podataka fejluje
            Log.e(TAG, "onDownloadComplete failed with status: " + status );
        }
    }

}


