package com.jaysonjose.smartgreenhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationForm extends AppCompatActivity {

    private EditText inputEmail,inputPassword,inputFullName,inputPasswordConfirm;
    private Button btnRegister;
    ProgressBar progressBarReg;
    private FirebaseAuth mAuth;
    String valFullName,valConfirmPassword,valEmail,valPassword,valPhone="none",valUsername="none",valAddress="none",valImage="https://firebasestorage.googleapis.com/v0/b/smartgreenhouse-9da94.appspot.com/o/718hZ5nw2TL._SL1500_.jpg?alt=media&token=bba92b87-3fa9-4ca9-8817-bdabd16e88da";
    DatabaseReference reference;
    String userId;
    FirebaseUser userLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        inputEmail = findViewById(R.id.inputEmail);
        inputFullName = findViewById(R.id.inputFullName);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordConfirm = findViewById(R.id.inputPasswordConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        progressBarReg = findViewById(R.id.progressBarReg);
        mAuth = FirebaseAuth.getInstance();
        reference  = FirebaseDatabase.getInstance().getReference("/Users");


        registerUser();
    }

    private void registerUser() {

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valEmail = inputEmail.getText().toString().trim();
                valPassword = inputPassword.getText().toString().trim();
                valConfirmPassword = inputPasswordConfirm.getText().toString().trim();
                valFullName = inputFullName.getText().toString().trim();

                if(valFullName.isEmpty()){
                    inputFullName.setError("Full name is required!");
                    inputFullName.requestFocus();
                    return;
                }

                if(valEmail.isEmpty()){
                    inputEmail.setError("Email is required!");
                    inputEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(valEmail).matches()){
                    inputEmail.setError("Invalid Email!");
                    inputEmail.requestFocus();
                    return;
                }
                if(valPassword.isEmpty()){
                    inputPassword.setError("Password is required!");
                    inputPassword.requestFocus();
                    return;
                }

                if(valPassword.length()<6){
                    inputPassword.setError("Min password length should be 6 characters");
                    inputPassword.requestFocus();
                    return;
                }

                if(!valConfirmPassword.equals(valPassword)){
                    inputPasswordConfirm.setError("Password did not Matched");
                    inputPasswordConfirm.requestFocus();
                    return;
                }

                progressBarReg.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(valEmail,valPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){

                                    User user = new User(valFullName,valEmail,valUsername,valPhone,valAddress,valImage);
                                    userLog = FirebaseAuth.getInstance().getCurrentUser();
                                    userId = userLog.getUid();

                                    reference.child(userId).child("Account")
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Map<String, Object> childUpdates = new HashMap<>();

                                                childUpdates.put("control/fan/automatic",21);
                                                childUpdates.put("control/fan/status","OFF");
                                                childUpdates.put("control/fan/time",1);

                                                childUpdates.put("control/lights/automatic","ON");
                                                childUpdates.put("control/lights/status","OFF");

                                                childUpdates.put("control/sprinkler/automatic",40);
                                                childUpdates.put("control/sprinkler/status","OFF");
                                                childUpdates.put("control/sprinkler/time",1);

                                                childUpdates.put("control/stream/offline_ip","none");
                                                childUpdates.put("control/stream/online_ip","none");

                                                childUpdates.put("mode","manual");

                                                childUpdates.put("monitoring/heat",100);
                                                childUpdates.put("monitoring/humidity",100);
                                                childUpdates.put("monitoring/soil",100);
                                                childUpdates.put("monitoring/temperature",100);

                                                reference.child(userId).updateChildren(childUpdates);
                                                Toast.makeText(RegistrationForm.this, "User has been Successfully registered", Toast.LENGTH_LONG).show();

                                                startActivity(new Intent(RegistrationForm.this,MainActivity.class));
                                            }else{
                                                Toast.makeText(RegistrationForm.this, "Failed to register Try Again!!", Toast.LENGTH_LONG).show();
                                            }
                                            progressBarReg.setVisibility(View.GONE);

                                        }
                                    });


                                }else{
                                    Toast.makeText(RegistrationForm.this, "Failed to register", Toast.LENGTH_LONG).show();
                                    progressBarReg.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

    }
}