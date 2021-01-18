package com.jaysonjose.smartgreenhouse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class ImagePage extends AppCompatActivity {


    private ImageView imageViewProfile;
    private static final int pick = 2;
    private StorageReference str;
    private Uri resultUri;
    private DatabaseReference db;
    private EditText inputAddress,inputPhone,inputUsername;
    private Button btnUpdate,btnCancel;
    private ProgressBar progressBarReg;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_page);
        db = FirebaseDatabase.getInstance().getReference().child("Image");
        str = FirebaseStorage.getInstance().getReference();
        imageViewProfile = findViewById(R.id.imageViewProfile);
        inputAddress = findViewById(R.id.inputAddress);
        inputPhone = findViewById(R.id.inputPhone);
        inputUsername = findViewById(R.id.inputUsername);
        btnUpdate = findViewById(R.id.btnUpdate);
        progressBarReg = findViewById(R.id.progressBarReg);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        updateInformation();
        accountInformation();

        reference.child(userID).child("Account").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image1 = snapshot.child("myImage").getValue().toString();
                Picasso.with(ImagePage.this)
                        .load(image1)
                        .transform(new RoundedTransformation())
                        .resize(100, 100)
                        .centerCrop()
                        .into(imageViewProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery,pick);
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
                    String usernameData = userProfile.username;
                    String phoneData = userProfile.phone;
                    String addressData = userProfile.address;


                    inputUsername.setText(usernameData);
                    inputPhone.setText(phoneData);
                    inputAddress.setText(addressData);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImagePage.this, "Something Wrong Happened", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void updateInformation() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String newUsername = inputUsername.getText().toString().trim();
                String newPhone = inputPhone.getText().toString().trim();
                String newAddress = inputAddress.getText().toString().trim();

                if(newUsername.length()<6){
                    inputUsername.setError("Min Username length should be 6 characters");
                    inputUsername.requestFocus();
                    return;
                }
                if(newPhone.length()<11){
                    inputPhone.setError("Min Phone Number length not below 10 characters");
                    inputPhone.requestFocus();
                    return;
                }
                if(newAddress.length()<15){
                    inputAddress.setError("Please Enter Your Complete Address");
                    inputAddress.requestFocus();
                    return;
                }

                reference.child(userID).child("Account").child("username").setValue(newUsername);
                reference.child(userID).child("Account").child("phone").setValue(newPhone);
                reference.child(userID).child("Account").child("address").setValue(newAddress);
                Toast.makeText(ImagePage.this, "Update Successfully", Toast.LENGTH_SHORT).show();

              final Handler handler = new Handler();
              handler.postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      startActivity(new Intent(ImagePage.this,AccountFragment.class));
                  }
              },5000);



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBarReg.setVisibility(View.VISIBLE);
        if (requestCode==pick && resultCode==RESULT_OK && data!=null){
            Uri image = data.getData();
            CropImage.activity(image)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                 resultUri = result.getUri();

                str.child(userID).putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap<String,Object> data = new HashMap<>();
                                String newUri = uri.toString();
                                data.put("myImage",newUri);
                                reference.child(userID).child("Account").updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBarReg.setVisibility(View.GONE);
                                        Toast.makeText(ImagePage.this, "Image Store", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}