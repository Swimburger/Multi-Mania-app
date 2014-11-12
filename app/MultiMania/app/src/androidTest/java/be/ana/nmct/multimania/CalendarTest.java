package be.ana.nmct.multimania;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.test.ApplicationTestCase;

import java.text.ParseException;

import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.utils.GoogleCalUtil;
import be.ana.nmct.multimania.utils.SettingsUtil;
import be.ana.nmct.multimania.utils.Utility;

/**
 * Created by Axel on 12/11/2014.
 */
public class CalendarTest extends ApplicationTestCase<Application> {

    private final String TAG = this.getClass().getSimpleName();

    private static final String CALENDAR_NAME = "Multi-Mania 2015";
    private static final String ACCOUNT_NAME = "ana@gmail.com";

    private static Talk sTestTalk;
    private static GoogleCalUtil sCalUtil;

    private ContentResolver mContentResolver;

    static{
        try {
            sTestTalk = new Talk(1,"Test talk", Utility.ConvertStringToDate("2014-05-19 10:45:00"),Utility.ConvertStringToDate("2014-05-19 11:30:00"),"TestDescription",1,false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public CalendarTest() {
        super(Application.class);
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        //Set shared preferences
        SettingsUtil util = new SettingsUtil(this.getContext(), GoogleCalUtil.PREFERENCE_NAME);
        util.setPreference(GoogleCalUtil.PREFERENCE_ACCOUNTNAME, ACCOUNT_NAME);
    }

    private Cursor getCalendarCursor(){
        ContentResolver cr = mContentResolver;
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[] {ACCOUNT_NAME, CalendarContract.ACCOUNT_TYPE_LOCAL, ACCOUNT_NAME};
        return cr.query(uri, null, selection, selectionArgs, null);
    }

    public void testCalendarCreated(){

        //Create the calendar
        sCalUtil = new GoogleCalUtil(this.getContext(), CALENDAR_NAME);
        sCalUtil.createCalendar();

        Cursor c = getCalendarCursor();

        assertNotNull(c);

        c.moveToFirst();
        int nameIndex = c.getColumnIndexOrThrow(CalendarContract.Calendars.ACCOUNT_NAME);
        String accountName = c.getString(nameIndex);

        assertTrue(accountName.equals(ACCOUNT_NAME));
    }
}
