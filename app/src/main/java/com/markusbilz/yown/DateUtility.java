package com.markusbilz.yown;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class DateUtility {
    // formatting according to  https://www.sqlite.org/lang_datefunc.html
    private final static SimpleDateFormat datetimeSql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    @SuppressWarnings("unused")
    private final static SimpleDateFormat dateUi = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private final static SimpleDateFormat timeUi = new SimpleDateFormat("HH:mm", Locale.US);
    @SuppressWarnings("unused")
    private final static SimpleDateFormat datetimeUi = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);

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
        long timeDelta;
        try {
            prevDate = datetimeSql.parse(dateTime);
            time = timeUi.format(prevDate);
            timeDelta = (today.getTime() - prevDate.getTime()) / (86400000);
        } catch (ParseException e) {
            return dateTime;
        }
        if (timeDelta == 0) {
            return "today at " + time;
        } else if (timeDelta == 1) {
            return "yesterday at" + time;
        } else {
            return timeDelta + " days ago";
        }
    }
}
