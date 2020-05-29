package android.singidunum.ac.flickrapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private static final int PERMISSIONS_REQUEST_CAMERA = 200;
    private static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 201;
    private static final int IMAGE_REQUEST = 1;

    private  String path = null;

    String user;



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

        //dugme za kameru

        FloatingActionButton buttonLaunchCamera = (FloatingActionButton) v.findViewById(R.id.fab);
        buttonLaunchCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);

                }  else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                } else {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File imageFile = null;
                    try {
                        imageFile = getImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(imageFile != null){
                        Uri imageUri = FileProvider.getUriForFile(getContext(), "android.singidunum.ac.imgexplore", imageFile);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(i, IMAGE_REQUEST);
                    }
                }
            }
        });

        return v;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case PERMISSIONS_REQUEST_CAMERA: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent startCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(startCamera, PERMISSIONS_REQUEST_CAMERA);
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: denied");
                }
                return;
            }
            case PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    String file = path + ".jpg";
                    File newFile = new File(file);

                    try {
                        newFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Uri fileUri = Uri.fromFile(newFile);

                    Intent startCameraFile = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                    startCameraFile.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    startActivityForResult(startCameraFile, PERMISSIONS_WRITE_EXTERNAL_STORAGE);


                }
            }
        }
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

    private File getImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageName, ".jpg", storageDir);
        path = imageFile.getAbsolutePath();
        return imageFile;
    }

}


