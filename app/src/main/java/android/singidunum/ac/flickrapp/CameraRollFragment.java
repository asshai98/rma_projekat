package android.singidunum.ac.flickrapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.singidunum.ac.flickrapp.R;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraRollFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraRollFragment extends Fragment {

    //Fragment na kome ce se prikazivati slike koje korisnici ubacuju na profil

    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    CameraRecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String author;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CameraRollFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraRollFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraRollFragment newInstance(String param1, String param2) {
        CameraRollFragment fragment = new CameraRollFragment();
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

        View v = inflater.inflate(R.layout.fragment_camera_roll, container, false);

        recyclerView = v.findViewById(R.id.recycler_view_upload);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh1);

        databaseHelper = new DatabaseHelper(getContext());

        if(savedInstanceState == null) {
            Bundle bundle = getActivity().getIntent().getExtras();
            if(bundle == null){
                author = null;
            } else {
                author = bundle.getString("email");
            }
        } else {
            author = (String) savedInstanceState.getSerializable("email");
        }

        adapter = new CameraRecyclerViewAdapter(databaseHelper.getAllImagesData(author), getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        FloatingActionButton btnAddPicture = (FloatingActionButton) v.findViewById(R.id.addPicture);
        btnAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddPhotoActivity.class);
                startActivity(i);

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArrayList<ImageModelClass> arrayList;
                arrayList = databaseHelper.getAllImagesData(author);
                CameraRecyclerViewAdapter cameraRecyclerViewAdapter = new CameraRecyclerViewAdapter(arrayList, getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(cameraRecyclerViewAdapter);
                cameraRecyclerViewAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return v;
    }


}
