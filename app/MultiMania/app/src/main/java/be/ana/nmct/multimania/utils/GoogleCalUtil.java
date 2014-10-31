package be.ana.nmct.multimania.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.model.Talk;

/**
 * Created by Axel on 29/10/2014.
 */
public class GoogleCalUtil {

    private final static String TAG = GoogleCalUtil.class.getCanonicalName();
    private final static Uri CAL_URI = CalendarContract.Calendars.CONTENT_URI;
    private final static Uri EVENT_URI = CalendarContract.Events.CONTENT_URI;

    private final static String PREF_CALENDER_ID = "calendar_id";
    private final static String PREF_ACCOUNTNAME = "calendar_accountname";

    private static String CALENDAR_NAME;
    private Context context;

    public GoogleCalUtil(Context context) {
        this.context = context;
        this.CALENDAR_NAME = context.getResources().getString(R.string.calendar_name);
    }

    //single insert via intent
    public void saveTalkToGoogleCalendarViaIntent(Talk talk){
        try {
            //set intent data to send to cal
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE, talk.title);
            intent.putExtra(CalendarContract.Events.DESCRIPTION, talk.description);
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, talk.roomId);

            this.context.startActivity(intent);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private ContentValues buildNewCalContentValues() {
        final ContentValues cv = new ContentValues();

        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, getAccount());
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, CALENDAR_NAME);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_NAME);
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xEA8561);
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_READ);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, getAccount());
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        return cv;
    }

    public void createCalendar() {
        ContentResolver cr = context.getContentResolver();
        final ContentValues cv = buildNewCalContentValues();
        Uri calUri = buildCalUri();
        Uri newUri = cr.insert(buildCalUri(), cv);
        setCalId(Long.parseLong(newUri.getLastPathSegment()));
    }

    public void deleteCalendar() {
        ContentResolver cr = context.getContentResolver();
        Uri calUri = ContentUris.withAppendedId(buildCalUri(), getCalId());
        cr.delete(calUri, null, null);
    }


    public void addTalk(Talk talk) {
        ContentResolver cr = context.getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.CALENDAR_ID, getCalId());
        cv.put(CalendarContract.Events.TITLE, talk.title);
        cv.put(CalendarContract.Events.DTSTART, Utility.getDateInMillis(talk.from));
        cv.put(CalendarContract.Events.DTEND, Utility.getDateInMillis(talk.to));
        cv.put(CalendarContract.Events.EVENT_LOCATION, talk.roomId);
        cv.put(CalendarContract.Events.DESCRIPTION, talk.description);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, Utility.getTimeZoneId());
        cr.insert(buildEventUri(), cv);
    }


    private Uri buildCalUri() {
        return CAL_URI
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, getAccount())
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
    }

    private Uri buildEventUri() {
        return EVENT_URI
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, getAccount())
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
    }


    private boolean setCalId(long calId){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_CALENDER_ID, Context.MODE_PRIVATE).edit();
        editor.putLong(PREF_CALENDER_ID, calId);
        return  editor.commit();
    }

    private long getCalId(){
        SharedPreferences prefs = context.getSharedPreferences(PREF_CALENDER_ID, Context.MODE_PRIVATE);
        return prefs.getLong(PREF_CALENDER_ID, 0);
    }

    public boolean setAccount(String accountName){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_ACCOUNTNAME, Context.MODE_PRIVATE).edit();
        editor.putString(PREF_ACCOUNTNAME, accountName);
        return  editor.commit();
    }

    public String getAccount(){
        SharedPreferences prefs = context.getSharedPreferences(PREF_ACCOUNTNAME, Context.MODE_PRIVATE);
        return prefs.getString(PREF_ACCOUNTNAME, null);
    }



}
