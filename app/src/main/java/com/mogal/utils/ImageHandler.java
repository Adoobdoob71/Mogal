package com.mogal.utils;

import android.widget.ImageView;

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
        //load image bitmap

        this.destroy();
    }
}
