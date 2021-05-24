package com.mogal.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mogal.ArticleActivity;
import com.mogal.R;
import com.mogal.classes.Article;
import com.mogal.classes.User;
import com.mogal.utils.UsefulMethods;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    public ArticleAdapter(@NonNull Context context, @NonNull List<Article> objects) {
        super(context, 0, objects);
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("users");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_item, parent, false);

        Article article = getItem(position);
        TextView movie_title = convertView.findViewById(R.id.article_item_title);
        TextView movie_body = convertView.findViewById(R.id.article_item_body);
        TextView timestamp = convertView.findViewById(R.id.article_item_timestamp);
        TextView poster_nickname = convertView.findViewById(R.id.article_item_poster_nickname);
        TextView article_country = convertView.findViewById(R.id.article_item_country);

        ImageView article_picture = convertView.findViewById(R.id.article_item_image_view);
        ImageView poster_profile_picture = convertView.findViewById(R.id.article_item_poster_profile_picture);
        ImageButton read_more_button = convertView.findViewById(R.id.article_item_read_more);

        movie_title.setText(article.getName());
        movie_body.setText(article.getBody());
        timestamp.setText(UsefulMethods.calculateTimestamp(article.getTime(), new Date()));
        article_country.setText(article.getCountry());

        if (article.getPicture().length() != 0)
            Picasso.get().load(article.getPicture()).into(article_picture);

        loadUserData(article, poster_nickname, poster_profile_picture);
        handleEvents(read_more_button, article);

        return convertView;
    }

    public void handleEvents(ImageButton read_more_button, final Article article){
        read_more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ArticleActivity.class);
                intent.putExtra("article_picture_url", article.getPicture());
                intent.putExtra("article_name", article.getName());
                intent.putExtra("article_body", article.getBody());
                intent.putExtra("article_poster_uid", article.getPosterUID());
                getContext().startActivity(intent);
            }
        });
    }

    public void loadUserData(Article article, final TextView poster_nickname, final ImageView profile_picture){
        ref.child(article.getPosterUID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        poster_nickname.setText(user.getNickname());
                        Picasso.get().load(user.getProfile_picture()).into(profile_picture);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
