package com.mogal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mogal.utils.ImageHandler;

public class ArticleActivity extends AppCompatActivity {

    ImageButton backButton;
    ImageView articlePicture, profilePicture;
    TextView articleName, articlePosterNickname, articleBody;
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
        String profilePictureUrl = data.getStringExtra("article_profile_picture_url");
        articleName.setText(data.getStringExtra("article_name"));
        articlePosterNickname.setText(data.getStringExtra("article_poster_nickname"));
        articleBody.setText(data.getStringExtra("article_body"));
        ImageHandler articlePictureImageHandler = new ImageHandler(articlePicture, articlePictureUrl);
        ImageHandler articleProfilePictureImageHandler = new ImageHandler(profilePicture, profilePictureUrl);
        articlePictureImageHandler.start();
        articleProfilePictureImageHandler.start();
    }
}