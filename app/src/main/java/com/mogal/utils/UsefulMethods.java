package com.mogal.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import androidx.core.app.TaskStackBuilder;

import com.mogal.MainActivity;
import com.mogal.R;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UsefulMethods {

    public static String calculateTimestamp(Date date_one, Date date_two){
        long difference = (date_two.getTime() - date_one.getTime()) / 60000;
        if (difference < 60)
            return difference + " mins ago";
        if (difference < 1440)
            return difference / 60 + " hours ago";
        return difference / 1440 + " days ago";
    }

    public static void reloadApp(Context context, Intent currentScreenIntent){
        TaskStackBuilder.create(context)
                .addNextIntent(new Intent(context, MainActivity.class))
                .addNextIntent(currentScreenIntent)
                .startActivities();
    }

    public static String getCountryName(double latitude, double longitude, Context context){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty())
                return addresses.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
