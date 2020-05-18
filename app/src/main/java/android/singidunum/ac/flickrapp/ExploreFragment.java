package android.singidunum.ac.flickrapp;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;

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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment implements GetJsonData.OnDataAvilable {

    //fragment koji preko search view-a omogucuje korisnicima da unosom nekih kljucnih reci pretrazuju slike
    //mehanizam pretrage u susutini vrsi filtriranje na osnovu tagova koje svaka slika u sklopu svojih informacija, a ti tagovi su zapravo reci koje korisnici unose u
    //searchview polje razdvojene zarezom

    //definisanje konstanti i incijalizacija polja

    private static final String TAG = "ExploreFragment";
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";
    private SearchView searchView;
    private ImageRecyclerViewAdapter imageRecyclerViewAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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

    //inflajutujemo layout parsiranjem def. xml fajla i dodajemo onQueryTextListener kako bi mogli da omogucimo korisniku da kada odradi tap/touch na odredjenu sliku iz recycler view-a,
    //ode na novu aktivnost PhotoDetailActivity gde ce u okviru cardview-a da se prikazu detalji slike

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Pravimo view inflejtovanjem layouta
        View v = inflater.inflate(R.layout.fragment_explore, container, false);


        //za prikaz slika ispod search view-a, recycler view kao kod GalleryFragment-a

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        imageRecyclerViewAdapter = new ImageRecyclerViewAdapter(new ArrayList<Photo>(), getContext());
        recyclerView.setAdapter(imageRecyclerViewAdapter);

        //prikaz search view-a, korisitmo mendadzer za pretragu
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) v.findViewById(R.id.searchField);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getActivity().getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconified(false);

        //setovanje onQuerytextListener-a, koji ce osluskivati promene i tekst koji korisnik unese unutar search polja i u onQuerySubmit vrsimo obradu querija na osnovu kljucnih reci/tagova koje je korisnik uneo
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 0){
                    //posto prvo obradjujemo query a zatim skidamo podatke koje prikazujemo unutar recyclerView-a,
                    //potrebno je da se uvek nad novom instacnom getJsonData izvrsi execute quer-ija, jer je GetJSONData asinhrona klasa,
                    //tako da ukoliko imamo istu instancu prakitcno bismo trazili da se pokrene vec pokrenuta nit prilikom skidanja podataka i doslo bi do izbacivanja nullpointer exep.
                    new GetJsonData("https://www.flickr.com/services/feeds/photos_public.gne", "en-us", true, ExploreFragment.this).execute(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return v;
    }

    //metod iz interejsa onDataAvilable koji obavestava klasu aktivnosti koja ga implementriana da su podaci skinuti kao i status GET metoda
    @Override
    public void onDataAvilable(List<Photo> data, DownloadStatus status){
        if(status == DownloadStatus.OK){
            imageRecyclerViewAdapter.loadNewData(data); //u recycler view dinamicki ucitavamo podatke kako se skidaju preko api-ja
        } else {
            //ukoliko download i procesiranje podataka fejluje
            Log.e(TAG, "onDownloadComplete failed with status: " + status );
        }
    }
}
