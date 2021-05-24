package com.mogal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mogal.adapters.ArticleAdapter;
import com.mogal.classes.Article;
import com.mogal.classes.User;
import com.mogal.receivers.AirplaneModeReceiver;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    User userDetails;
    AirplaneModeReceiver airplaneModeReceiver;

    @Override
    protected void onStart() {
        super.onStart();
        airplaneModeReceiver = new AirplaneModeReceiver();
        registerReceiver(airplaneModeReceiver, new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplaneModeReceiver);
    }

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
                    case R.id.menu_settings:
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_load_recent_articles:
                        getLocation();
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
            DatabaseReference ref = firebaseDatabase.getReference("users");
            ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userDetails = snapshot.getValue(User.class);
                    Picasso.get().load(userDetails.getProfile_picture()).into(profilePicture);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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

    public void getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String countryName = getCountryName(location.getLatitude(), location.getLongitude());
        loadNearbyArticles(countryName);
    }

    public String getCountryName(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty())
                return addresses.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadNearbyArticles(String country){
        articleArrayList.clear();
        DatabaseReference ref = firebaseDatabase.getReference("articles");
        ref
                .orderByChild("country")
                .equalTo(country)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
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
    }

    @Override
    public void onBackPressed() {
        return;
    }
}