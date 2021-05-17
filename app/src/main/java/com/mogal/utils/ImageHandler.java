package com.mogal.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.net.MalformedURLException;
import java.net.URL;

public class ImageHandler extends Thread {

    public ImageView imageView;
    public String image_url;

    public ImageHandler(ImageView imageView, String image_url){
        this.imageView = imageView;
        this.image_url = image_url;
    }

    public ImageHandler(){}

    public void setImageView(ImageView imageView){
        this.imageView = imageView;
    }

    public void setImage_url(String image_url) { this.image_url = image_url; }

    @Override
    public void run(){
        try {
            URL url = new URL(this.image_url);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            this.imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
