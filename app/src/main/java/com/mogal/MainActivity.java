package com.mogal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mogal.adapters.ArticleAdapter;
import com.mogal.classes.Article;
import com.mogal.utils.ImageHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton menuButton;
    LinearLayout searchBar;
    ImageView profilePicture;
    ListView articleListView;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ArticleAdapter articleAdapter;
    ArrayList<Article> articleArrayList;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();
        handleButtons();
        loadUserData();
        loadArticles();
    }

    public void initializeVariables(){
        menuButton = findViewById(R.id.activity_main_menu_button);
        searchBar = findViewById(R.id.activity_main_search_bar);
        profilePicture = findViewById(R.id.activity_main_profile_picture);
        articleListView = findViewById(R.id.activity_main_list_view);
        swipeRefreshLayout = findViewById(R.id.activity_main_refresh_list_view);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        articleArrayList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articleArrayList);
        articleListView.setAdapter(articleAdapter);
        user = firebaseAuth.getCurrentUser();
    }

    public void handleButtons(){
        final PopupMenu menu = new PopupMenu(this, menuButton);
        menu.getMenuInflater().inflate(R.menu.activity_main_menu_button_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent;
                switch(menuItem.getItemId()) {
                    case R.id.menu_create_article:
                        intent = new Intent(getApplicationContext(), CreateArticle.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_create_review:
//                        intent = new Intent(getApplicationContext(), ContactsActivity.class);
//                        startActivity(intent);
                        break;
                    case R.id.menu_reviews:
                        intent = new Intent(getApplicationContext(), ReviewsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_settings:
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.show();
            }
        });
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("user_id", user.getUid());
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                articleArrayList.clear();
                loadArticles();
                articleAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadUserData(){
        if (user != null) {
            SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
            String profilePictureURL = sharedPreferences.getString("profile_picture_url", "something");

            Picasso.get().load(profilePictureURL).into(profilePicture);
        }
    }

    public void loadArticles(){
        DatabaseReference ref = firebaseDatabase.getReference("articles");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Article article = dataSnapshot.getValue(Article.class);
                    article.setID(dataSnapshot.getKey());
                    articleArrayList.add(article);
                }
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        return;
    }
}