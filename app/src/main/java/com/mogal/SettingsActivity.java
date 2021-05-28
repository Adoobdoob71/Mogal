package com.mogal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mogal.utils.UsefulMethods;

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
        if (firebaseAuth.getCurrentUser() != null){
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Signing in user...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            firebaseAuth.signOut();
            progressDialog.dismiss();
            UsefulMethods.reloadApp(this, new Intent(this, MainActivity.class));
        }
        else
            Toast.makeText(this, "Already signed out", Toast.LENGTH_SHORT).show();
    }

}