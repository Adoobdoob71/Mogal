package com.mogal.utils;

import android.widget.ImageView;

public class ImageHandler extends Thread {

    public ImageView imageView;

    public ImageHandler(ImageView imageView){
        this.imageView = imageView;
    }

    public ImageHandler(){}

    public void setImageView(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    public void run(){
        //load image bitmap

        this.destroy();
    }
}
