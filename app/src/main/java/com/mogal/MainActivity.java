package com.mogal;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.mogal.utils.UsefulMethods;
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
    FusedLocationProviderClient fusedLocationProviderClient;
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

    public void initializeVariables() {
        menuButton = findViewById(R.id.activity_main_menu_button);
        searchBar = findViewById(R.id.activity_main_search_bar);
        profilePicture = findViewById(R.id.activity_main_profile_picture);
        articleListView = findViewById(R.id.activity_main_list_view);
        swipeRefreshLayout = findViewById(R.id.activity_main_refresh_list_view);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        articleArrayList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articleArrayList);
        articleListView.setAdapter(articleAdapter);
        user = firebaseAuth.getCurrentUser();
    }

    public void handleButtons() {
        final PopupMenu menu = new PopupMenu(this, menuButton);
        menu.getMenuInflater().inflate(R.menu.activity_main_menu_button_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.menu_create_article:
                        if (firebaseAuth.getCurrentUser() != null){
                            intent = new Intent(getApplicationContext(), CreateArticle.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(MainActivity.this, "You're not signed in", Toast.LENGTH_SHORT).show();
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
                if (user != null){
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("user_id", user.getUid());
                    startActivity(intent);
                }
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

    public void loadUserData() {
        if (user != null) {
            DatabaseReference ref = firebaseDatabase.getReference("users");
            ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userDetails = snapshot.getValue(User.class);
                    if (userDetails.getProfile_picture() != null && userDetails.getProfile_picture().length() != 0)
                        Picasso.get().load(userDetails.getProfile_picture()).into(profilePicture);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void loadArticles() {
        DatabaseReference ref = firebaseDatabase.getReference("articles");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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


    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean isGranted) {
            if (isGranted) {
                Toast.makeText(MainActivity.this, "Great, permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "You won't be able to use this feature if you don't give the permission", Toast.LENGTH_SHORT).show();
            }
        }
    });

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
            return;
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is disabled", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()){
                    String countryName = UsefulMethods.getCountryName(task.getResult().getLatitude(), task.getResult().getLongitude(), MainActivity.this);
                    loadNearbyArticles(countryName);
                }
                else
                    Toast.makeText(MainActivity.this, "Couldn't get your location", Toast.LENGTH_SHORT).show();
            }
        });
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