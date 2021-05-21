package com.mogal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mogal.classes.Article;
import com.mogal.classes.User;
import com.mogal.utils.ImageHandler;
import com.squareup.picasso.Picasso;

public class ArticleActivity extends AppCompatActivity {

    ImageButton backButton;
    ImageView articlePicture, profilePicture;
    TextView articleName, articlePosterNickname, articleBody;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        initializeVariables();
        handleButtons();
        loadData();

    }

    public void initializeVariables(){
        backButton = findViewById(R.id.activity_article_back_button);
        articlePicture = findViewById(R.id.activity_article_picture);
        profilePicture = findViewById(R.id.activity_article_profile_picture);
        articleName = findViewById(R.id.activity_article_name);
        articlePosterNickname = findViewById(R.id.activity_article_nickname);
        articleBody = findViewById(R.id.activity_article_body);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void handleButtons(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void loadData(){
        Intent data = getIntent();
        String articlePictureUrl = data.getStringExtra("article_picture_url");

        articleName.setText(data.getStringExtra("article_name"));
        articleBody.setText(data.getStringExtra("article_body"));

        if (articlePictureUrl.length() != 0)
            Picasso.get().load(articlePictureUrl).into(articlePicture);

        DatabaseReference ref = firebaseDatabase.getReference("users");
        ref.child(data.getStringExtra("article_poster_uid")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                articlePosterNickname.setText(user.getNickname());

                Picasso.get().load(user.getProfile_picture()).into(profilePicture);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}