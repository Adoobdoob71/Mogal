package com.mogal;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mogal.classes.Article;
import com.mogal.utils.UsefulMethods;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateArticle extends AppCompatActivity {

    ImageButton eraseButton, uploadButton;
    EditText nameEditText, bodyEditText, pictureURLEditText;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FusedLocationProviderClient fusedLocationProviderClient;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);
        initializeVariables();
        handleButtons();
    }

    public void initializeVariables() {
        eraseButton = findViewById(R.id.activity_create_article_erase_button);
        uploadButton = findViewById(R.id.activity_create_article_upload_button);
        nameEditText = findViewById(R.id.activity_create_article_name_edit_text);
        bodyEditText = findViewById(R.id.activity_create_article_body_edit_text);
        pictureURLEditText = findViewById(R.id.activity_create_article_picture_url_edit_text);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        progressDialog = new ProgressDialog(this);
    }

    public void handleButtons() {
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Uploading article...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                getLocation();
            }
        });
    }

    public void uploadArticle(String country) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String UID = user.getUid();
            DatabaseReference ref = firebaseDatabase.getReference("articles");
            String key = ref.push().getKey();
            Article article = new Article(nameEditText.getText().toString(), bodyEditText.getText().toString(), pictureURLEditText.getText().toString(), UID, key, new Date(), country);

            if (article.getName().length() == 0 || article.getBody().length() == 0){
                progressDialog.dismiss();
                Toast.makeText(this, "Not all required fields are filled", Toast.LENGTH_SHORT).show();
                return;
            }

            ref.child(key).setValue(article).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Article uploaded!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean isGranted) {
            if (isGranted) {
                Toast.makeText(CreateArticle.this, "Great, permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateArticle.this, "You won't be able to use this feature if you don't give the permission", Toast.LENGTH_SHORT).show();
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
                if (task.isSuccessful() && task.getResult() != null){
                    String countryName = UsefulMethods.getCountryName(task.getResult().getLatitude(), task.getResult().getLongitude(), CreateArticle.this);
                    uploadArticle(countryName);
                }
                else
                    Toast.makeText(CreateArticle.this, "Couldn't get your location, try opening Google Maps", Toast.LENGTH_SHORT).show();
            }
        });
    }


}