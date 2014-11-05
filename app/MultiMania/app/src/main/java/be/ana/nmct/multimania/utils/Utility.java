package be.ana.nmct.multimania.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Niels on 28/10/2014.
 */
public class Utility {
    private static final String sFormat = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat sFormatter = new SimpleDateFormat(sFormat);
    public static String ConvertDateToString(Date date){
        return sFormatter.format(date);
    }

    public static Date ConvertStringToDate(String date) throws ParseException {
        return sFormatter.parse(date);
    }

    public static String getTimeZoneId(){
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }

    public static long getDateInMillis(Date date){
        long l = (long)date.getTime() % 1000;
        return l < 0 ? l + 1000 : l;
    }

    public static String getDateFormat() {
        return sFormat;
    }

}
