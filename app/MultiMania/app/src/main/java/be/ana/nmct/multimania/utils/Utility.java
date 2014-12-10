package be.ana.nmct.multimania.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.TouchDelegate;
import android.view.View;
import android.view.animation.AlphaAnimation;

import org.joda.time.LocalTime;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.vm.ScheduleTalkVm;

/**
 * A utility class containing static helper methods
 * Created by Niels on 28/10/2014.
 */
public final class Utility {
    private static final String sDateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String sTimeFormat = "HH:mm";
    private static final SimpleDateFormat sFormatter = new SimpleDateFormat(sDateFormat);
    private static final SimpleDateFormat sTimeFormatter = new SimpleDateFormat(sTimeFormat);

    private Utility(){}

    /**
     * Converts a java.util.Date to a String
     * @param date The Date to convert
     * @return The converted date into a String
     */
    public static String convertDateToString(Date date){
        return sFormatter.format(date);
    }

    /**
     * Converts a String to java.util.Date
     * @param date The String to convert
     * @return A java.util.Date converted from the passed String
     * @throws ParseException Exception when the String date wasn't correctly formatted
     */
    public static Date convertStringToDate(String date) throws ParseException {
        return sFormatter.parse(date);
    }

    /**
     * Converts a String formatted like 00:00:00 to a Joda LocalTime object
     * @param time The String containing the Time to convert
     * @return A valid LocalTime object
     * @throws InvalidParameterException if an invalid String was passed
     */
    public static LocalTime convertStringToLocalTime(String time) throws InvalidParameterException{
        String[] split = time.split(":");

        if(split.length != 2){
            try{
                return new LocalTime(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            } catch(Exception ex){
                throw new InvalidParameterException("The parameter passed is not in a valid format, format should be like 00:00:00");
            }
        }
        throw new InvalidParameterException("The parameter passed is not in a valid format, format should be like 00:00:00");
    }

    /**
     * Gets the default TimeZone id (e.g. UTC)
     * @return The default TimeZone id
     */
    public static String getTimeZoneId(){
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }

    /**
     * Converts a java.util.Date into millis
     * @param date The Date to convert
     * @return The date in millis (stored in a long)
     */
    public static long getDateInMillis(Date date){
        if(date != null){
            return date.getTime();
        } else {
            return -1;
        }
    }

    /**
     * Gets the DateFormat string
     * @return Returns the DateFormat string
     */
    public static String getDateFormat() {
        return sDateFormat;
    }

    /**
     * Gets the TimeFormat string
     * @return Returns the TimeFormat string
     */
    public static String getTimeFormat() {
        return sTimeFormat;
    }

    /**
     * Converts a DateString to a TimeString
     * @return Returns the TimeString
     */
    public static String getTimeString(String date) throws ParseException {
        return  sTimeFormatter.format(convertStringToDate(date));
    }

    /**
     * Converts a Date to a TimeString
     * @return Returns the TimeString
     */
    public static String getTimeString(Date date) throws ParseException {
        return  sTimeFormatter.format(date);
    }

    /**
     * Creates an AlphaAnimation
     * @param from Start alpha value
     * @param to Stop alpha value
     * @param duration Time the animation should last in milliseconds
     * @param offset The amount of time the animation should wait after being started in milliseconds
     * @return Returns an AlphAnimation
     */
    public static AlphaAnimation getAlphaAnimation(float from, float to, long duration, long offset) {
        AlphaAnimation anim = new AlphaAnimation(from,to);
        anim.setDuration(duration);
        anim.setStartOffset(offset);
        anim.setFillAfter(true);
        return anim;
    }

    /**
     * Inserts content into a HTML5 snippet
     * @param info Content to insert into the HTML5 snippet
     * @return Returns a HTML5 snippet with the content inside the body
     */
    public static String getHtml(String info) {
        return  "<html><head><link rel=\"stylesheet\" type\"text/css\" href=\"style.css\" /></head><body>" +
                info +
                "</body></html>";
    }


    public static Talk getTalkFromUri(Context context, Uri uri){
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        if(c.moveToFirst()){
            int idIndex = c.getColumnIndex(MultimaniaContract.TalkEntry._ID);
            int titleIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.TITLE);
            int isFavoriteIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.IS_FAVORITE);
            int fromIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.DATE_FROM);


            Talk talk = null;
            try {
                talk = new Talk(c.getInt(idIndex), c.getString(titleIndex), convertStringToDate(c.getString(fromIndex)), null, "", 0, false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            talk.isFavorite = c.getInt(isFavoriteIndex) == 1;

            return talk;
        } else {
            return null;
        }
    }

    /**
     * Converts dp values to px values
     * @param context The application or activity Android context
     * @param dp The dp value
     * @return Returns the pixel value
     */
    public static int dpToPx(Context context,int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Converts px values to dp values
     * @param context The application or activity Android context
     * @param px The pixel value
     * @return Returns the dp value
     */
    public static int pxToDp(Context context,int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    /**
     * Enlarges the Touchable area of a view
     * @param root The root view of the param viewToExpand
     * @param viewToExpand The view that needs a larger touch area
     * @param padding The amount of padding that will be applied to the view
     */
    public static void enlargeTouchArea(View root, final View viewToExpand, final int padding) {
        root.post(new Runnable() {
            @Override
            public void run() {
                Rect delegateArea = new Rect();
                View delegate = viewToExpand;

                delegate.getHitRect(delegateArea);
                delegateArea.top -= padding;
                delegateArea.bottom += padding;
                delegateArea.left -= padding;
                delegateArea.right += padding;

                TouchDelegate expandedArea = new TouchDelegate(delegateArea, delegate);

                if(View.class.isInstance(delegate.getParent())){
                    ((View)delegate.getParent()).setTouchDelegate(expandedArea);
                }

            }
        });
    }

    /**
     * Converts a List of Talks to a List of ScheduleTalkVm's
     * @param talkList The List of Talks to convert
     * @return A List ScheduleTalkVm's converted from List of Talk
     */
    public static List<ScheduleTalkVm> convertTalkListToScheduleTalkVmList(List<Talk> talkList){
        List<ScheduleTalkVm> result = new ArrayList<ScheduleTalkVm>();

        for(Talk talk : talkList){
            result.add(convertTalkToScheduleTalkVm(talk));
        }

        return result;
    }

    /**
     * Converts a Talk to a ScheduleTalkVm
     * @param talk The Talk to convert
     * @return A ScheduleTalkVm converted from a Talk
     */
    public static ScheduleTalkVm convertTalkToScheduleTalkVm(Talk talk){
        return new ScheduleTalkVm((int) talk.id, talk.title, talk.from, talk.to, talk.description, talk.roomId, talk.isKeynote);
    }


}
