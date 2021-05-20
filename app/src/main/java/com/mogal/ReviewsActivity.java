package com.mogal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mogal.adapters.ReviewAdapter;
import com.mogal.classes.Review;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {

    ImageButton backButton;
    ListView reviewListView;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> reviewArrayList;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        initializeVariables();
        handleButtons();
        loadData();
    }

    public void initializeVariables(){
        backButton = findViewById(R.id.activity_reviews_back_button);
        reviewListView = findViewById(R.id.activity_reviews_list_view);
        reviewArrayList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewArrayList);
        reviewListView.setAdapter(reviewAdapter);
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
        DatabaseReference ref = firebaseDatabase.getReference("reviews");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Review review = (Review) dataSnapshot.getValue();
                    reviewArrayList.add(review);
                }
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}