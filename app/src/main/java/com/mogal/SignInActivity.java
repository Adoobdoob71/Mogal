package com.mogal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mogal.classes.User;

public class SignInActivity extends AppCompatActivity {

    ImageButton closeButton;
    TextInputEditText emailTextInput, passwordTextInput;
    Button submitButton;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    User userProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initializeVariables();
        handleButtons();
    }

    public void initializeVariables(){
        closeButton = findViewById(R.id.activity_sign_in_close_button);
        emailTextInput = findViewById(R.id.activity_sign_in_email);
        passwordTextInput = findViewById(R.id.activity_sign_in_password);
        submitButton = findViewById(R.id.activity_sign_in_submit_button);
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
                signIn();
            }
        });
    }

    public void signIn(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Signing in user...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        firebaseAuth
                .signInWithEmailAndPassword(emailTextInput.getText().toString().trim(), passwordTextInput.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Sign in was successful", Toast.LENGTH_SHORT).show();
//                    saveSignInProperties(task.getResult().getUser());
                    progressDialog.dismiss();
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Sign in wasn't successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public void saveSignInProperties(FirebaseUser user){
//        DatabaseReference reference = firebaseDatabase.getReference("users");
//        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userProperties = snapshot.getValue(User.class);
//                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("nickname", userProperties.getNickname());
//                editor.putString("uid", userProperties.getUid());
//                editor.putString("profile_picture_url", userProperties.getProfile_picture());
//                editor.commit();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}