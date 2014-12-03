package be.ana.nmct.multimania.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.TouchDelegate;
import android.view.View;
import android.view.animation.AlphaAnimation;

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

    public static String getDateFormat() {
        return sDateFormat;
    }

    public static String getTimeFormat() {
        return sTimeFormat;
    }

    public static String getTimeString(String date) throws ParseException {
        return  sTimeFormatter.format(convertStringToDate(date));
    }

    public static String getTimeString(Date date) throws ParseException {
        return  sTimeFormatter.format(date);
    }

    public static AlphaAnimation getAlphaAnimation(float from, float to, long duration, long offset) {
        AlphaAnimation anim = new AlphaAnimation(from,to);
        anim.setDuration(duration);
        anim.setStartOffset(offset);
        anim.setFillAfter(true);
        return anim;
    }

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

    public static int dpToPx(Context context,int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

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
     * Converts a List<Talk> to a List<ScheduleTalkVm>
     * @param talkList The List<Talk> to convert
     * @return A List<ScheduleTalkVm> converted from List<Talk>
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
