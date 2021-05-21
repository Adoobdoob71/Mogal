package com.mogal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mogal.adapters.ResultAdapter;
import com.mogal.classes.Article;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ImageButton backButton;
    EditText searchQuery;
    ListView resultsListView;
    ArrayList<Article> articleArrayList;
    ResultAdapter resultAdapter;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initializeVariables();
        handleButtons();
        handleEvents();
    }

    public void initializeVariables(){
        backButton = findViewById(R.id.activity_search_back_button);
        searchQuery = findViewById(R.id.activity_search_search_edit_text);
        resultsListView = findViewById(R.id.activity_search_list_view);
        articleArrayList = new ArrayList<>();
        resultAdapter = new ResultAdapter(this, articleArrayList);
        resultsListView.setAdapter(resultAdapter);
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

    public void handleEvents(){
        final DatabaseReference ref = firebaseDatabase.getReference("articles");

        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchQuery.getText().toString().trim().length() != 0){
                    articleArrayList.clear();
                    String query = searchQuery.getText().toString().trim();
                    ref.orderByChild("name").startAt(query).endAt(query + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Article article = dataSnapshot.getValue(Article.class);
                                article.setID(dataSnapshot.getKey());
                                articleArrayList.add(article);
                            }
                            resultAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}