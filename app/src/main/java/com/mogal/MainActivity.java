package com.mogal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeVariables();
        HandleBottomNavigationView();
    }

    public void InitializeVariables(){
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
    }

    public void HandleBottomNavigationView(){
        final NavOptions.Builder navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.fragment_fade_enter);
        navBuilder.setExitAnim(R.anim.fragment_fade_exit);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        assert navHostFragment != null: "Could not find the navController from the navHostFragment, could be because navHostFragment is null";
        final NavController navController = navHostFragment.getNavController();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.bottom_navigation_view_menu_home_item:
                        navController.navigate(R.id.articlesFragment, null, navBuilder.build());
                        break;
                    case R.id.bottom_navigation_view_menu_reviews_item:
                        navController.navigate(R.id.reviewsFragment, null, navBuilder.build());
                        break;
                    case R.id.bottom_navigation_view_menu_contacts_item:
                        navController.navigate(R.id.contactsFragment, null, navBuilder.build());
                        break;
                    case R.id.bottom_navigation_view_menu_profile_item:
                        navController.navigate(R.id.profileFragment, null, navBuilder.build());
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}