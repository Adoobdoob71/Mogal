package com.mogal.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.mogal.ArticleActivity;
import com.mogal.R;
import com.mogal.classes.Article;
import com.mogal.utils.ImageHandler;
import com.mogal.utils.UsefulMethods;

import java.util.Date;
import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {
    public ArticleAdapter(@NonNull Context context, @NonNull List<Article> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_item, parent, false);

        Article article = getItem(position);
        TextView movie_title = convertView.findViewById(R.id.article_item_title);
        TextView movie_description = convertView.findViewById(R.id.article_item_description);
        TextView timestamp = convertView.findViewById(R.id.article_item_timestamp);
        TextView poster_nickname = convertView.findViewById(R.id.article_item_poster_nickname);
        ImageView movie_picture = convertView.findViewById(R.id.article_item_image_view);
        ImageView poster_profile_picture = convertView.findViewById(R.id.article_item_poster_profile_picture);
        ImageButton read_more_button = convertView.findViewById(R.id.article_item_read_more);
        MaterialCheckBox like_button = convertView.findViewById(R.id.article_item_like_button);

        movie_title.setText(article.getName());
        movie_description.setText(article.getDescription());
        timestamp.setText(UsefulMethods.calculateTimestamp(article.getTime(), new Date()));
        poster_nickname.setText(article.getPoster().getNickname());
        ImageHandler movie_picture_handler = new ImageHandler(movie_picture, article.getPicture());
        ImageHandler poster_profile_handler = new ImageHandler(poster_profile_picture, article.getPoster().getProfile_picture());
        movie_picture_handler.start();
        poster_profile_handler.start();
        handleEvents(read_more_button, like_button, article);
        return convertView;
    }

    public void handleEvents(ImageButton read_more_button, MaterialCheckBox like_button, final Article article){
        read_more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ArticleActivity.class);
                intent.putExtra("ArticleID", article.getID());
                getContext().startActivity(intent);
            }
        });

        like_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //like-unlike here, needs work
            }
        });
    }

}
