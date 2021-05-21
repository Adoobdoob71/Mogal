package com.mogal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mogal.adapters.ArticleAdapter;
import com.mogal.classes.Article;
import com.mogal.classes.User;
import com.mogal.utils.ImageHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    ListView articleListView;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ArticleAdapter articleAdapter;
    ArrayList<Article> articleArrayList;
    ImageView profilePicture;
    LinearLayout backgroundImage;
    TextView nickname, joinedOn, articlesNumber, reviewsNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeVariables();
        Intent intent = getIntent();
        loadUserData(intent);
    }

    public void initializeVariables(){
        backgroundImage = findViewById(R.id.activity_profile_user_background);
        profilePicture = findViewById(R.id.activity_profile_user_picture);
        articleListView = findViewById(R.id.activity_profile_article_list);
        nickname = findViewById(R.id.activity_profile_nickname);
        joinedOn = findViewById(R.id.activity_profile_joined_on);
        articlesNumber = findViewById(R.id.activity_profile_articles_number);
        reviewsNumber = findViewById(R.id.activity_profile_reviews_number);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        articleArrayList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articleArrayList);
        articleListView.setAdapter(articleAdapter);
    }

    public void loadUserData(Intent intent){
        final String userID = intent.getStringExtra("user_id");
        final DatabaseReference userRef = firebaseDatabase.getReference("users").child(userID);
        final DatabaseReference reviewRef = firebaseDatabase.getReference("reviews");
        final DatabaseReference articleRef = firebaseDatabase.getReference("articles");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                nickname.setText(user.getNickname());
                joinedOn.setText("Joined on " + user.getJoinedOn().toLocaleString());

                if (user.getProfile_picture().length() != 0)
                    Picasso.get().load(user.getProfile_picture()).into(profilePicture);

                articleRef.orderByChild("posterUID").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        articlesNumber.setText(snapshot.getChildrenCount() + "");
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Article article = dataSnapshot.getValue(Article.class);
                            articleArrayList.add(article);
                        }
                        articleAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                reviewRef.orderByChild("posterUID").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reviewsNumber.setText(snapshot.getChildrenCount() + "");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}