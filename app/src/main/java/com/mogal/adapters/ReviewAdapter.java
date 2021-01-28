package com.mogal.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mogal.R;
import com.mogal.classes.Review;
import com.mogal.utils.ImageHandler;
import com.mogal.utils.UsefulMethods;

import java.util.Date;
import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(@NonNull Context context, @NonNull List<Review> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
        }
        Review review = getItem(position);
        TextView nickname = convertView.findViewById(R.id.review_item_nickname);
        TextView timestamp = convertView.findViewById(R.id.review_item_timestamp);
        TextView body = convertView.findViewById(R.id.review_item_body);
        ImageView imageView = convertView.findViewById(R.id.review_item_profile_picture);
        nickname.setText(review.getNickname());
        timestamp.setText(UsefulMethods.calculateTimestamp(review.getTime(), new Date()));
        body.setText(review.getBody());
        ImageHandler imageHandler = new ImageHandler(imageView, review.getProfile_picture());
        imageHandler.start();
        return convertView;
    }
}
