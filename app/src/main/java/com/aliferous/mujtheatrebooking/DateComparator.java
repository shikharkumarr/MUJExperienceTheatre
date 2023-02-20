package com.aliferous.mujtheatrebooking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateComparator {

    public static boolean compareDates(String dateString1, String dateString2) {
        SimpleDateFormat format = new SimpleDateFormat("dd / MM / yyyy", Locale.ENGLISH);
        try {
            Date date1 = format.parse(dateString1);
            Date date2 = format.parse(dateString2);

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);

            long diff = cal2.getTimeInMillis() - cal1.getTimeInMillis();
            long days = diff / (24 * 60 * 60 * 1000);

            if (days > 40 || date1.before(date2)) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return true;
    }
}
