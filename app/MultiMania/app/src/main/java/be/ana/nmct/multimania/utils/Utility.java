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
import java.util.Date;
import java.util.TimeZone;

import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.model.Talk;

/**
 * Created by Niels on 28/10/2014.
 */
public final class Utility {
    private static final String sDateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String sTimeFormat = "HH:mm";
    private static final SimpleDateFormat sFormatter = new SimpleDateFormat(sDateFormat);
    private static final SimpleDateFormat sTimeFormatter = new SimpleDateFormat(sTimeFormat);

    private Utility(){}

    public static String convertDateToString(Date date){
        return sFormatter.format(date);
    }

    public static Date convertStringToDate(String date) throws ParseException {
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

            return new Talk(c.getInt(idIndex), c.getString(titleIndex), null, null, "", 0, false);
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

}
