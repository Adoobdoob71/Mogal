package com.mogal.utils;

import com.mogal.R;

import java.util.Date;

public class UsefulMethods {

    public static String calculateTimestamp(Date date_one, Date date_two){
        long difference = (date_two.getTime() - date_one.getTime()) / 60000;
        if (difference < 60)
            return difference + "mins ago";
        if (difference < 1440)
            return difference / 60 + "hours ago";
        return difference / 1440 + "days ago";
    }


}
