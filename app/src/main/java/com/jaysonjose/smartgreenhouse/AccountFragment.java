package com.jaysonjose.smartgreenhouse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView textViewFullName,textViewEmail;
    private TextView editTextUsername,editTextPhone,editTextTextAddress;
    private Button btnUpdate;
    private ImageView imageView;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
   //private TextView email,fullName,phone,address,username;
   private DatabaseReference db;
    private static final int pick = 2;
    private StorageReference str;
    private Uri resultUri;





    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
       View view =  inflater.inflate(R.layout.fragment_account, null);

        textViewFullName = view.findViewById(R.id.textViewFullName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextTextAddress = view.findViewById(R.id.editTextTextAddress);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        imageView = view.findViewById(R.id.imageView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        db = FirebaseDatabase.getInstance().getReference().child("Image");
        str = FirebaseStorage.getInstance().getReference().child("Images");
        imageView = view.findViewById(R.id.imageView);

       accountInformation();
    //   updateInformation();
       imageProfile();

       btnUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getActivity(),ImagePage.class));
           }
       });


       return  view;
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }

    private void imageProfile() {

        reference.child(userID).child("Account").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image1 = snapshot.child("myImage").getValue().toString();
                Picasso.with(getActivity())
                        .load(image1)
                        .transform(new RoundedTransformation())
                        .resize(100, 100)
                        .centerCrop()
                        .into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void accountInformation() {


        reference.child(userID).child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    String fullNameData = userProfile.fullName;
                    String emailData = userProfile.email;
                    String usernameData = userProfile.username;
                    String phoneData = userProfile.phone;
                    String addressData = userProfile.address;

                    textViewFullName.setText(fullNameData);
                    textViewEmail.setText(emailData);
                    editTextUsername.setText(usernameData);
                    editTextPhone.setText(phoneData);
                    editTextTextAddress.setText(addressData);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something Wrong Happened", Toast.LENGTH_LONG).show();
            }
        });

    }
}