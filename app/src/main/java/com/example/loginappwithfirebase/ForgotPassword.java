package com.example.loginappwithfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ForgotPassword extends AppCompatActivity {

    public TextView banner;
    public EditText email;
    public Button btnReset;
    public ProgressBar progressBar3;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        banner = findViewById(R.id.banner);
        email = findViewById(R.id.email);
        btnReset = findViewById(R.id.btnReset);
        progressBar3 = findViewById(R.id.progressBar3);
        auth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();

                if (Email.isEmpty()) {
                    email.setError("Email is Required");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    email.setError("Please provide valid email address");
                    email.requestFocus();
                    return;
                }
                progressBar3.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassword.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ForgotPassword.this, "Try again, something went wrong", Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
        });
    }
}