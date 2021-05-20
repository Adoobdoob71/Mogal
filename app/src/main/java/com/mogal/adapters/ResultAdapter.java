package com.mogal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mogal.R;
import com.mogal.classes.Article;
import com.mogal.utils.ImageHandler;

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

        Article article = getItem(position);
        ImageView resultImage = convertView.findViewById(R.id.search_result_item_picture);
        TextView resultName = convertView.findViewById(R.id.search_result_item_name);
        TextView resultBody = convertView.findViewById(R.id.search_result_item_body);

        resultName.setText(article.getName());
        resultBody.setText(article.getBody());
        ImageHandler imageHandler = new ImageHandler(resultImage, article.getPicture());
        imageHandler.start();
        return convertView;
    }
}
