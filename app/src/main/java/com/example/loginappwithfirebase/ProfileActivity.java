package com.example.loginappwithfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    public Button btnLogOut;
    public FirebaseUser firebaseUser;
    public DatabaseReference databaseReference;
    public String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        userID = firebaseUser.getUid();

        final TextView users_email = findViewById(R.id.users_email);
        final TextView textView = findViewById(R.id.textView);
        final TextView users_full_name = findViewById(R.id.users_full_name);
        final TextView users_age = findViewById(R.id.users_age);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String age = userProfile.age;

                    textView.setText("Welcome " + fullName);
                    users_email.setText(email);
                    users_full_name.setText(fullName);
                    users_age.setText(age);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something wrong occurred", Toast.LENGTH_LONG).show();
            }
        });
    }
}