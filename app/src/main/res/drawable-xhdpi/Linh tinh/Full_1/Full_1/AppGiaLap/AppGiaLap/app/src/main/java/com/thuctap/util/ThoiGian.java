package com.thuctap.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kiệt Nhii on 3/23/2017.
 */

public class ThoiGian {
    public static String thoiGianOnOff(String dateStart, String dateStop){
        String kq = "";
            //String dateStart = "2012-03-14 09:33:58";
            //String dateStop = "2012-03-14 10:44:59";
            // Custom date format
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = format.parse(dateStart);
                d2 = format.parse(dateStop);
            } catch (ParseException e) {
            }
            // Get msec from each, and subtract.
            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000;
            long diffMinutes = diff / (60 * 1000);
            long diffHours = diff / (60 * 60 * 1000);
            /*System.out.println("Số giây : " + diffSeconds + " seconds.");
            System.out.println("Số phút: " + diffMinutes + " minutes.");
            System.out.println("Số giờ: " + diffHours + " hours.");*/
        if(diffMinutes>60){
            kq = diffHours + " giờ trước";
        }else {
             kq=diffMinutes + " phút trước.";
        }
        return kq;
        }
}
