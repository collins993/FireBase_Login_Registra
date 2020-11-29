package com.example.loginappwithfirebase;

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
    public TextView register, txtForgotPassWord;
    public EditText edtEmail, edtPassword;
    public Button btnLogin;
    public FirebaseAuth mAuth;
    public ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.txtRegister);
        txtForgotPassWord = findViewById(R.id.txtForgotPassWord);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        txtForgotPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_email = edtEmail.getText().toString().trim();
                String user_password = edtPassword.getText().toString().trim();

                if (user_email.isEmpty()) {
                    edtEmail.setError("Enter email address");
                    edtEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
                    edtEmail.setError("Please provide valid email address");
                    edtEmail.requestFocus();
                    return;
                }

                if (user_password.isEmpty()) {
                    edtPassword.setError("Password Required");
                    edtPassword.requestFocus();
                    return;
                }

                if (user_password.length() < 6) {
                    edtPassword.setError("Min password length should be 6 characters");
                    edtPassword.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            if (firebaseUser.isEmailVerified()) {
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(MainActivity.this, "Check Email for Verification Code", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed, Check credentials again..", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }

}