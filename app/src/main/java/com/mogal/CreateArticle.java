package com.mogal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mogal.classes.Article;
import com.mogal.utils.ImageHandler;

import java.util.Date;

public class CreateArticle extends AppCompatActivity {

    ImageButton eraseButton, attachmentButton, uploadButton;
    EditText nameEditText, bodyEditText;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);
        initializeVariables();
        handleButtons();
    }

    public void initializeVariables(){
        eraseButton = findViewById(R.id.activity_create_article_erase_button);
        attachmentButton = findViewById(R.id.activity_create_article_attachment_button);
        uploadButton = findViewById(R.id.activity_create_article_upload_button);
        nameEditText = findViewById(R.id.activity_create_article_name_edit_text);
        bodyEditText = findViewById(R.id.activity_create_article_body_edit_text);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void handleButtons(){
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadArticle();
            }
        });
        attachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void uploadArticle(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String UID = user.getUid();
            SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
            String profilePictureURL = sharedPreferences.getString("profile_picture_url", "");
            DatabaseReference ref = firebaseDatabase.getReference("articles");
            String key = ref.push().getKey();
            Article article = new Article(nameEditText.getText().toString(), bodyEditText.getText().toString(), profilePictureURL, UID, key, new Date());
            ref.child(key).setValue(article).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Article uploaded!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}