package com.mogal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    ImageButton backButton;
    LinearLayout signInButton, signUpButton, signOutButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeVariables();
        handleButtons();
    }

    public void initializeVariables(){
        backButton = findViewById(R.id.activity_settings_back_button);
        signInButton = findViewById(R.id.activity_settings_sign_in);
        signUpButton = findViewById(R.id.activity_settings_sign_up);
        signOutButton = findViewById(R.id.activity_settings_sign_out);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void handleButtons(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Toast.makeText(getApplicationContext(), "Signed out successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signOut(){
        firebaseAuth.signOut();
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nickname", null);
        editor.putString("profile_picture_url", null);
        editor.putString("uid", null);
        editor.apply();
    }
}