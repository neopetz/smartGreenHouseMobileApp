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
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private TextView textViewBack;
    private EditText inputEmail;
    private Button btnResetPassword;
    private ProgressBar progressBar2;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        textViewBack = findViewById(R.id.textViewBack);
        inputEmail = findViewById(R.id.inputEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        progressBar2 = findViewById(R.id.progressBar2);
        auth = FirebaseAuth.getInstance();

        backMain();
        resetPassword();
    }

    private void resetPassword() {
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAdd = inputEmail.getText().toString().trim();

                if(emailAdd.isEmpty()){
                    inputEmail.setError("Email is required!");
                    inputEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches()){
                    inputEmail.setError("Invalid Email!");
                    inputEmail.requestFocus();
                    return;
                }
                progressBar2.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(emailAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ResetPassword.this, "Check Your Email To Reset Your Password ", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ResetPassword.this, " Try Again", Toast.LENGTH_LONG).show();
                        }
                        progressBar2.setVisibility(View.GONE);
                    }
                });
            }

        });
    }

    private void backMain() {
        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPassword.this,MainActivity.class));
            }
        });
    }
}