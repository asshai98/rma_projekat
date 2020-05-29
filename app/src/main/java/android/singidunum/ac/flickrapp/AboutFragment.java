package android.singidunum.ac.flickrapp;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.singidunum.ac.flickrapp.R;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

    DatabaseHelper databaseHelper;
    String user;

    EditText forename, surname, email, password;

    //Klasican about fragment koji prikazuje informacije korisniku, koje on moze da menja


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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

        View v = inflater.inflate(R.layout.fragment_about, container, false);

        databaseHelper = new DatabaseHelper(getContext());

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

        //referenciranje input polja

        forename = v.findViewById(R.id.editForename);
        surname = v.findViewById(R.id.editSurname);
        email = v.findViewById(R.id.editEmail);
        password = v.findViewById(R.id.editUserPassword);


        //menjanje podataka
        Button btnEdit = v.findViewById(R.id.editUserData);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = forename.getText().toString();
                String userSurname = surname.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                try {
                    databaseHelper.editUserData(name, userSurname, userEmail, userPassword, user);
                    Toast.makeText(getContext(), "Updated successfuly", Toast.LENGTH_SHORT).show();
                } catch (SQLiteException exeption) {
                    exeption.printStackTrace();
                }


            }
        });

        UserModel userModel = databaseHelper.getUserData(user);

        forename.setText(userModel.getForename());
        surname.setText(userModel.getSurname());
        email.setText(userModel.getEmail());
        password.setText(userModel.getPassword());


        return v;
    }
}
