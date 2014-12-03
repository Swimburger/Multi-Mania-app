package be.ana.nmct.multimania.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.List;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.vm.ScheduleTalkVm;

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

    public GoogleCalUtil(Context context, String calendarName) {
        this.mContext = context;
        this.mCalendarName = calendarName;
        this.mUtil = new SettingsUtil(mContext, PREFERENCE_NAME);
    }

    /**
     * Creates instance of ContentValues with all calendar properties set
     * @return ContentValues from CalendarContract
     */
    private ContentValues buildNewCalContentValues() {
        final ContentValues cv = new ContentValues();
        String accountName = mUtil.getStringPreference(PREFERENCE_ACCOUNTNAME);
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, mCalendarName);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, mCalendarName);
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, mContext.getResources().getColor(R.color.primaryColor));
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_READ);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, accountName);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        return cv;
    }

    /**
     * Creates a Calendar entry in the users calendar (Google Cal or other cal app)
     */
    public void createCalendar() {
        ContentResolver cr = mContext.getContentResolver();
        final ContentValues cv = buildNewCalContentValues();
        Uri newUri = cr.insert(buildCalUri(), cv);
        mUtil.setPreference(PREFERENCE_CALENDER_ID, Long.parseLong(newUri.getLastPathSegment()));
    }


    /**
     * Deletes a Calendar entry in the users calendar (Google Cal or other cal app)
     */
    public void deleteCalendar() {
        ContentResolver cr = mContext.getContentResolver();
        long calId = mUtil.getLongPreference(PREFERENCE_CALENDER_ID);
        Uri calUri = ContentUris.withAppendedId(buildCalUri(), calId);
        cr.delete(calUri, null, null);
    }

    /**
     * Adds a list of talks to the calendar
     * @param talks The list of talks to add
     */
    public void addTalkList(List<ScheduleTalkVm> talks){
        for(int i = 0; i < talks.size(); i++){
            addTalk(talks.get(i));
        }
    }

    /** Adds a talk to the calendar
     * @param talk The context (e.g. activity)
     * @return returns the calEventId
     */
    public long addTalk(ScheduleTalkVm talk) {
        ContentResolver cr = mContext.getContentResolver();
        ContentValues cv = new ContentValues();

        long calId = mUtil.getLongPreference(PREFERENCE_CALENDER_ID);
        cv.put(CalendarContract.Events.CALENDAR_ID, calId);
        cv.put(CalendarContract.Events.TITLE, talk.title);
        cv.put(CalendarContract.Events.DTSTART, Utility.getDateInMillis(talk.from));
        cv.put(CalendarContract.Events.DTEND, Utility.getDateInMillis(talk.to));
        cv.put(CalendarContract.Events.EVENT_LOCATION, talk.room);
        cv.put(CalendarContract.Events.DESCRIPTION, talk.description);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, Utility.getTimeZoneId());

        long calEventId = Long.parseLong(cr.insert(buildEventUri(), cv).getLastPathSegment());
        saveCalEventId(talk, calEventId);

        return calEventId;
    }

    /**
     *  Saves the calendar event Id to the SQLite db for future reference
     * @param talk The talk to save the caleventId to
     * @param eventId The eventId to save
     * @return an integer indicating how many rows were updated (should be 1 row)
     */
    private int saveCalEventId(ScheduleTalkVm talk, long eventId) {
        ContentValues cv = new ContentValues();
        cv.put(MultimaniaContract.TalkEntry.CALEVENT_ID, eventId);
        return mContext.getContentResolver().update(
                MultimaniaContract.TalkEntry.CONTENT_URI,
                cv,
                MultimaniaContract.TalkEntry._ID + "=?",
                new String[] {String.valueOf(talk.id)}
        );
    }

    /**
     * Deletes a talk from the users calendar (in Google Cal or other calendar app)
     * @param talk The talk to delete
     * @return The amount of rows that were affected
     */
    public int deleteTalk(Talk talk){
        return mContext.getContentResolver().delete(
                buildEventUri(),
                "("+ CalendarContract.Events._ID+" = ?)",
                new String[] {String.valueOf(talk.calEventId)}
        );
    }

    /**
     * Gets the Uri needed to CRUD the calendar
     * @return Uri that enables CRUD actions on the calendar
     */
    public Uri buildCalUri() {
        return CAL_URI
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, mUtil.getStringPreference(PREFERENCE_ACCOUNTNAME))
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
    }

    /**
     * Gets the Uri needed to CRUD an event
     * @return Uri that enables CRUD actions on an event
     */
    private Uri buildEventUri() {
        return EVENT_URI
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, mUtil.getStringPreference(PREFERENCE_ACCOUNTNAME))
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
    }
}
