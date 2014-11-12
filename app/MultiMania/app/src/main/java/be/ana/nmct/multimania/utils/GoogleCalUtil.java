package be.ana.nmct.multimania.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.List;

import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.model.Talk;

/**
 * Created by Axel on 29/10/2014.
 */
public class GoogleCalUtil {

    private final static String TAG = GoogleCalUtil.class.getCanonicalName();

    private final static Uri CAL_URI = CalendarContract.Calendars.CONTENT_URI;
    private final static Uri EVENT_URI = CalendarContract.Events.CONTENT_URI;

    public final static String PREFERENCE_NAME = "calendar_preferences";
    public final static String PREFERENCE_CALENDER_ID = "calendar_id";
    public final static String PREFERENCE_ACCOUNTNAME = "calendar_accountname";

    private Context mContext;
    private static String mCalendarName;
    private SettingsUtil mUtil;
    private Cursor mCursor;

    public GoogleCalUtil(Context context, String calendarName) {
        this.mContext = context;
        this.mCalendarName = calendarName;
        this.mUtil = new SettingsUtil(mContext, PREFERENCE_NAME);
    }

   /* //single insert via intent
    public void saveTalkToGoogleCalendarViaIntent(Talk talk){
        try {
            //set intent data to send to cal
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE, talk.title);
            intent.putExtra(CalendarContract.Events.DESCRIPTION, talk.description);
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, talk.roomId);

            this.mContext.startActivity(intent);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }*/

    private ContentValues buildNewCalContentValues() {
        final ContentValues cv = new ContentValues();

        String accountName = mUtil.getStringPreference(PREFERENCE_ACCOUNTNAME);
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, mCalendarName);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, mCalendarName);
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xEA8561);
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_READ);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, accountName);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        return cv;
    }

    public void createCalendar() {
        ContentResolver cr = mContext.getContentResolver();
        final ContentValues cv = buildNewCalContentValues();
        Uri newUri = cr.insert(buildCalUri(), cv);
        mUtil.setPreference(PREFERENCE_CALENDER_ID, Long.parseLong(newUri.getLastPathSegment()));
    }

    public void deleteCalendar() {
        ContentResolver cr = mContext.getContentResolver();
        long calId = mUtil.getLongPreference(PREFERENCE_CALENDER_ID);
        Uri calUri = ContentUris.withAppendedId(buildCalUri(), calId);
        cr.delete(calUri, null, null);
    }

    public void addTalkList(List<Talk> talks){
        for(int i = 0; i < talks.size(); i++){
            addTalk(talks.get(i));
        }
    }

    public void addTalk(Talk talk) {
        ContentResolver cr = mContext.getContentResolver();
        ContentValues cv = new ContentValues();

        long calId = mUtil.getLongPreference(PREFERENCE_CALENDER_ID);
        cv.put(CalendarContract.Events.CALENDAR_ID, calId);
        cv.put(CalendarContract.Events.TITLE, talk.title);
        cv.put(CalendarContract.Events.DTSTART, Utility.getDateInMillis(talk.from));
        cv.put(CalendarContract.Events.DTEND, Utility.getDateInMillis(talk.to));
        cv.put(CalendarContract.Events.EVENT_LOCATION, talk.roomId);
        cv.put(CalendarContract.Events.DESCRIPTION, talk.description);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, Utility.getTimeZoneId());

        Uri uri = buildEventUri();
        cr.insert(uri, cv);

        updateEvent(talk,Long.parseLong(uri.getLastPathSegment()));
    }

    private int updateEvent(Talk talk, long eventId) {
        ContentValues cv = new ContentValues();
        cv.put(MultimaniaContract.TalkEntry.CALEVENT_ID, eventId);
        return mContext.getContentResolver().update(
                MultimaniaContract.TalkEntry.CONTENT_URI,
                cv,
                MultimaniaContract.TalkEntry._ID + "WHERE ?",
                new String[] {String.valueOf(talk.id)}
        );
    }

    public void deleteTalk(Talk talk){
        ContentResolver cr = mContext.getContentResolver();
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, talk.calEventId);
        cr.delete(deleteUri, null, null);
    }

    private Uri buildCalUri() {
        return CAL_URI
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, mUtil.getStringPreference(PREFERENCE_ACCOUNTNAME))
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
    }

    private Uri buildEventUri() {
        return EVENT_URI
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, mUtil.getStringPreference(PREFERENCE_ACCOUNTNAME))
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
    }

    private Uri talkEntryUri(Cursor cursor){
        return MultimaniaContract.NewsItemEntry.buildItemUri(
                cursor.getLong(
                        cursor.getColumnIndex(
                                MultimaniaContract.TalkEntry._ID
                        )
                )
        );
    }
}
