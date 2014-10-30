package be.ana.nmct.multimania.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

    private static String ACCOUNT_NAME;
    private static String CALENDAR_NAME;
    private Context context;

    public GoogleCalUtil(Context context) {
        this.context = context;
        this.ACCOUNT_NAME = context.getResources().getString(R.string.calendar_account_name);
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

    private static ContentValues buildNewCalContentValues() {
        final ContentValues cv = new ContentValues();

        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME);
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, CALENDAR_NAME);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_NAME);
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xEA8561);

        //user can only read the calendar
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_READ);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, ACCOUNT_NAME);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        return cv;
    }

    private static Uri buildCalUri() {
        return CAL_URI
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
    }

    public void createCalendar() {
        ContentResolver cr = context.getContentResolver();
        final ContentValues cv = buildNewCalContentValues();
        Uri calUri = buildCalUri();
        cr.insert(calUri, cv);
    }

}
