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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView gotoRegister,forgotPassword,textView2;
    private Button btnLogin;
    private ProgressBar progressBar;
    private EditText inputEmail,inputPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gotoRegister = findViewById(R.id.gotoRegister);
        forgotPassword = findViewById(R.id.forgotPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        textView2 = findViewById(R.id.textView2);
        mAuth = FirebaseAuth.getInstance();


        registerForm();
        forgotPasswordForm();
        btnLogin();
    }


    private void btnLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String pass = inputPassword.getText().toString().trim();

                if(email.isEmpty()){
                    inputEmail.setError("Email is required!");
                    inputEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    inputEmail.setError("Invalid Email!");
                    inputEmail.requestFocus();
                    return;
                }
                if(pass.isEmpty()){
                    inputPassword.setError("Password is required!");
                    inputPassword.requestFocus();
                    return;
                }
                if(pass.length()<6){
                    inputPassword.setError("Min password length should be 6 characters");
                    inputPassword.requestFocus();
                    return;
                }

               progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()){
                                startActivity(new Intent(MainActivity.this,homeLayout.class));
                            }else{
                                user.sendEmailVerification();
                                Toast.makeText(MainActivity.this, "Check your Email to verify your Account", Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(MainActivity.this, "Failed To login", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });


            }
        });
    }

    private void forgotPasswordForm() {
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ResetPassword.class));
            }
        });
    }

    private void registerForm() {
        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegistrationForm.class));
            }
        });
    }
}