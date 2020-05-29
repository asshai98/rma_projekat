package android.singidunum.ac.flickrapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.singidunum.ac.flickrapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private CollectionAdapter collectionAdapter;
    private String user;
    private ArrayList<FavItem> favItemArrayList = new ArrayList<>();

    //fragment koji prikazuje kolekcije omiljenih slika koje korisnici prave

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CollectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectionFragment newInstance(String param1, String param2) {
        CollectionFragment fragment = new CollectionFragment();
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
        View v = inflater.inflate(R.layout.fragment_collection, container, false);
        databaseHelper = new DatabaseHelper(getContext());
        recyclerView = v.findViewById(R.id.recycler_view_fav);

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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(collectionAdapter);
        loadData();

        return v;
    }

    private  void loadData(){
        if(favItemArrayList != null){
            favItemArrayList.clear();
        }
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = databaseHelper.selectAllFromFavouriteList();
        try{
            while(cursor.moveToNext()){
                String title = cursor.getString(1);
                String user = cursor.getString(2);
                FavItem favItem = new FavItem(title, user);
                favItemArrayList.add(favItem);
            }
        } finally {
            if(cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

        collectionAdapter = new CollectionAdapter(getContext(), favItemArrayList);
        recyclerView.setAdapter(collectionAdapter);
    }
}
