package be.ana.nmct.multimania.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Niels on 28/10/2014.
 */
public class Utility {
    private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String ConvertDateToString(Date date){
        return sFormatter.format(date);
    }

    public static Date ConvertStringToDate(String date) throws ParseException {
        return sFormatter.parse(date);
    }
}
