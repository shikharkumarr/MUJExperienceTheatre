package com.aliferous.mujtheatrebooking;

import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateComparator {

    public static int compareDates(String dateString1, String dateString2) {
        SimpleDateFormat format = new SimpleDateFormat("dd / MM / yyyy");
        int a = 0;

        try {
            Date date1 = format.parse(dateString1);
            Date date2 = format.parse(dateString2);

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);

            long diff = cal2.getTimeInMillis() - cal1.getTimeInMillis();
            long days = diff / (24 * 60 * 60 * 1000);

            if (date1.before(date2)) {
                a = 1;
            }
            else if (days > 40){
                a = 2;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return a;
    }
}
