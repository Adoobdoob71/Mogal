package com.mogal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mogal.classes.User;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    ImageButton closeButton;
    TextInputEditText nicknameTextInput, profilePictureTextInput, emailTextInput, passwordTextInput;
    Button submitButton;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    User userProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeVariables();
        handleButtons();
    }

    public void initializeVariables(){
        closeButton = findViewById(R.id.activity_sign_up_close_button);
        nicknameTextInput = findViewById(R.id.activity_sign_up_nickname);
        profilePictureTextInput = findViewById(R.id.activity_sign_up_profile_picture);
        emailTextInput = findViewById(R.id.activity_sign_up_email);
        passwordTextInput = findViewById(R.id.activity_sign_up_password);
        submitButton = findViewById(R.id.activity_sign_up_submit_button);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void handleButtons(){
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    public void registerUser(){
        final DatabaseReference ref = firebaseDatabase.getReference("users");
        firebaseAuth
                .createUserWithEmailAndPassword(emailTextInput.getText().toString().trim(), passwordTextInput.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && nicknameTextInput.getText().toString().trim().length() != 0){
                            FirebaseUser user = task.getResult().getUser();
                            userProperties = new User(nicknameTextInput.getText().toString().trim(), profilePictureTextInput.getText().toString().trim(), true, user.getUid(), new Date());
                            ref
                                    .child(user.getUid())
                                    .setValue(userProperties)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("nickname", userProperties.getNickname());
                                                editor.putString("uid", userProperties.getUid());
                                                editor.putString("profile_picture_url", userProperties.getProfile_picture());
                                                editor.apply();
                                                Toast.makeText(getApplicationContext(), "Registered successfully!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Are you sure everything's filled?", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}