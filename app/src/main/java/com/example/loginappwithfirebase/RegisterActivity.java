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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public EditText fullName, age, email, password;
    public ProgressBar pgBar;
    public TextView banner;
    public Button registerUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        banner = findViewById(R.id.txtBanner);
        registerUser = findViewById(R.id.btnRegister);
        fullName = findViewById(R.id.edtFullName);
        age = findViewById(R.id.edtAge);
        email = findViewById(R.id.edtUserMail);
        password = findViewById(R.id.edtUserPassword);
        pgBar = findViewById(R.id.progressBar2);

        backToMain();
        registerUser();
    }

    public void backToMain() {
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void registerUser() {
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FullName = fullName.getText().toString().trim();
                String Age = age.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String PassWord = password.getText().toString().trim();

                if (FullName.isEmpty()) {
                    fullName.setError("FullName is Required");
                    fullName.requestFocus();
                    return;
                }
                if (Age.isEmpty()) {
                    age.setError("Age is Required");
                    age.requestFocus();
                    return;
                }
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
                if (PassWord.isEmpty()) {
                    password.setError("Password is Required");
                    password.requestFocus();
                    return;

                }
                if (password.length() < 6) {
                    password.setError("Min password length should be 6 characters");
                    password.requestFocus();
                    return;
                }

                pgBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(Email, PassWord)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    User user = new User(FullName, Age, Email);
                                    FirebaseDatabase.getInstance().getReference("User")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                                pgBar.setVisibility(View.GONE);
                                            }
                                            else
                                                Toast.makeText(RegisterActivity.this, "Failed to register, try again", Toast.LENGTH_LONG).show();
                                                pgBar.setVisibility(View.GONE);
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                                    pgBar.setVisibility(View.GONE);

                                }
                            }
                        });

            }
        });
    }
}