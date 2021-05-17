package com.mogal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton menuButton;
    LinearLayout searchBar;
    ImageView profilePicture;
    ListView articleListView;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ArticleAdapter articleAdapter;
    ArrayList<Article> articleArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();
        handleButtons();
        loadData();
    }

    public void initializeVariables(){
        menuButton = findViewById(R.id.activity_main_menu_button);
        searchBar = findViewById(R.id.activity_main_search_bar);
        profilePicture = findViewById(R.id.activity_main_profile_picture);
        articleListView = findViewById(R.id.activity_main_list_view);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        articleArrayList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articleArrayList);
        articleListView.setAdapter(articleAdapter);
    }

    public void handleButtons(){
        PopupMenu menu = new PopupMenu(this, menuButton);
        menu.getMenuInflater().inflate(R.menu.activity_main_menu_button_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent;
                switch(menuItem.getItemId()) {
                    case R.id.menu_profile:
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_contacts:
                        intent = new Intent(getApplicationContext(), ContactsActivity.class);
                        startActivity(intent);
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
    }

    public void loadData(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String UID = user.getUid();
            SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
            String profilePictureURL = sharedPreferences.getString("profile_picture_url", "");
            ImageHandler imageHandler = new ImageHandler(profilePicture, profilePictureURL);
            imageHandler.start();
        }
        DatabaseReference ref = firebaseDatabase.getReference("articles");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Article article = (Article) dataSnapshot.getValue();
                    article.setID(dataSnapshot.getKey());
                    articleAdapter.add(article);
                }
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