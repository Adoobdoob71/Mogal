package com.mogal.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mogal.ArticleActivity;
import com.mogal.R;
import com.mogal.classes.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ResultAdapter extends ArrayAdapter<Article> {

    public ResultAdapter(@NonNull Context context, @NonNull List<Article> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, parent, false);

        final Article article = getItem(position);
        ImageView resultImage = convertView.findViewById(R.id.search_result_item_picture);
        TextView resultName = convertView.findViewById(R.id.search_result_item_name);
        TextView resultBody = convertView.findViewById(R.id.search_result_item_body);

        resultName.setText(article.getName());
        resultBody.setText(article.getBody());

        if (article.getPicture().length() != 0)
            Picasso.get().load(article.getPicture()).into(resultImage);

        convertView.setOnClickListener(new View.OnClickListener() {
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
        return convertView;
    }
}
