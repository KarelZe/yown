package com.markusbilz.yown;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class DateUtility {
    // formatting according to https://www.sqlite.org/lang_datefunc.html
    private final static SimpleDateFormat datetimeSql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private final static SimpleDateFormat timeUi = new SimpleDateFormat("HH:mm", Locale.US);

    /**
     * Function returns current date time as a string in date time format that is used with sqlite
     * data bases.
     *
     * @return String with current date time
     */
    static String nowSql() {
        return datetimeSql.format(new Date());
    }

    /**
     * Function converts a date string in 'datetimeSql'-format to a better human readable format.
     * For today, yesterday and the the days before the granularity of information decreases. If
     * the parameter can not be converted, the original string is being returned.
     *
     * @param dateTime time string for conversion
     * @return String with time delta.
     */
    static String dateTimeUi(String dateTime) {
        Date today = new Date();
        Date prevDate;
        String time;

        // calculate milliseconds of current day
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long msOfCurrentDay = today.getTime() - c.getTimeInMillis();
        // calculate time delta between prev date and today
        long timeDelta;
        try {
            prevDate = datetimeSql.parse(dateTime);
            time = timeUi.format(prevDate);
            timeDelta = (today.getTime() - prevDate.getTime());
        } catch (ParseException e) {
            return dateTime;
        }
        if (timeDelta <= msOfCurrentDay) {
            return "today at " + time;
        } else if (timeDelta <= msOfCurrentDay + 86_400_000) {
            return "yesterday at " + time;
        } else {
            return (timeDelta / 86_400_000) + " days ago";
        }
    }
}
