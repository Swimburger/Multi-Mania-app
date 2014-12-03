package be.ana.nmct.multimania;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.text.ParseException;

import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.data.MultimaniaContract.NewsItemEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.RoomEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.SpeakerEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TagEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TalkEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TalkSpeakerEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TalkTagEntry;
import be.ana.nmct.multimania.model.NewsItem;
import be.ana.nmct.multimania.model.Room;
import be.ana.nmct.multimania.model.Speaker;
import be.ana.nmct.multimania.model.Tag;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.model.TalkSpeaker;
import be.ana.nmct.multimania.model.TalkTag;
import be.ana.nmct.multimania.utils.Utility;

/**
 * ProviderTest is a class that contains all tests regarding the communication with the ContentProvider
 * Created by Niels on 29/10/2014.
 */
public class ProviderTest extends ApplicationTestCase<Application> {
    public static final String TAG = DbTest.class.getSimpleName();

    private static NewsItem sNewsItem = new NewsItem(1,"Test","test.png","short description","long description",0,1);
    private static Room sRoom = new Room(1,"Test room");
    private static Tag sTag = new Tag(1,"TEST");
    private static Speaker sSpeaker = new Speaker(1,"Test speaker");
    private static Talk sTalk = null;
    private static TalkTag sTalkTag = new TalkTag(1,1);
    private static TalkSpeaker sTalkSpeaker = new TalkSpeaker(1,1);
    static{
        try {
            sTalk = new Talk(1,"Test talk", Utility.convertStringToDate("2014-05-19 10:45:00"),Utility.convertStringToDate("2014-05-19 11:30:00"),"TestDescription",1,false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private ContentResolver mContentResolver;

    public ProviderTest()  {
        super(Application.class);
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        mContentResolver = context.getContentResolver();
    }

    public void testGetType(){
        String type = null;

        type = mContext.getContentResolver().getType(NewsItemEntry.CONTENT_URI);
        assertEquals(NewsItemEntry.CONTENT_TYPE,type);

        type = mContext.getContentResolver().getType(ContentUris.withAppendedId(NewsItemEntry.CONTENT_URI,1));
        assertEquals(NewsItemEntry.CONTENT_ITEM_TYPE,type);

        type = mContext.getContentResolver().getType(TalkEntry.CONTENT_URI);
        assertEquals(TalkEntry.CONTENT_TYPE,type);

        type = mContext.getContentResolver().getType(ContentUris.withAppendedId(TalkEntry.CONTENT_URI,1));
        assertEquals(TalkEntry.CONTENT_ITEM_TYPE,type);

        type = mContext.getContentResolver().getType(
                ContentUris.appendId(
                TalkEntry.CONTENT_URI.buildUpon().appendPath(MultimaniaContract.PATH_ROOM),
                        1).build()
        );
        assertEquals(TalkEntry.CONTENT_TYPE,type);

        type = mContext.getContentResolver().getType(
                ContentUris.appendId(
                        TalkEntry.CONTENT_URI.buildUpon().appendPath(MultimaniaContract.PATH_TAG),
                        1).build()
        );
        assertEquals(TalkEntry.CONTENT_TYPE,type);

        type = mContext.getContentResolver().getType(RoomEntry.CONTENT_URI);
        assertEquals(RoomEntry.CONTENT_TYPE,type);

        type = mContext.getContentResolver().getType(TagEntry.CONTENT_URI);
        assertEquals(TagEntry.CONTENT_TYPE,type);

        type = mContext.getContentResolver().getType(SpeakerEntry.CONTENT_URI);
        assertEquals(SpeakerEntry.CONTENT_TYPE,type);

        type = mContext.getContentResolver().getType(TalkTagEntry.CONTENT_URI);
        assertEquals(TalkTagEntry.CONTENT_TYPE,type);

        type = mContext.getContentResolver().getType(TalkSpeakerEntry.CONTENT_URI);
        assertEquals(TalkSpeakerEntry.CONTENT_TYPE,type);
    }

    // brings our database to an empty state
    public void testDeleteAllRecords() {
        mContext.getContentResolver().delete(
                NewsItemEntry.CONTENT_URI,
                null,
                null
        );
        Cursor cursor = mContext.getContentResolver().query(
                NewsItemEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        mContext.getContentResolver().delete(
                TalkEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                TalkEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        mContext.getContentResolver().delete(
                RoomEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                RoomEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        mContext.getContentResolver().delete(
                TagEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                TagEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        mContext.getContentResolver().delete(
                SpeakerEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                SpeakerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        mContext.getContentResolver().delete(
                TalkTagEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                TalkTagEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        mContext.getContentResolver().delete(
                TalkSpeakerEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                TalkSpeakerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    public void testInsertNewsItem(){
        ContentValues values = sNewsItem.getContentValues();
        Uri uri = mContentResolver.insert(NewsItemEntry.CONTENT_URI, values);
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        DbTest.validateCursor(cursor,values);
        cursor.close();
    }

    public void testInsertRoom(){
        ContentValues values = sRoom.getContentValues();
        Uri uri = mContentResolver.insert(RoomEntry.CONTENT_URI, values);
    }

    public void testInsertTag(){
        ContentValues values = sTag.getContentValues();
        Uri uri = mContentResolver.insert(TagEntry.CONTENT_URI, values);
    }

    public void testInsertSpeaker(){
        ContentValues values = sSpeaker.getContentValues();
        Uri uri = mContentResolver.insert(SpeakerEntry.CONTENT_URI, values);
    }

    public void testInsertTalk(){
        ContentValues values = sTalk.getContentValues();
        Uri uri = mContentResolver.insert(TalkEntry.CONTENT_URI, values);
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        Log.d(TAG,cursor.getColumnNames().toString());
        assertEquals(cursor.getCount(),1);
        cursor.close();
    }

    public void testInsertTalkTag(){
        ContentValues values = sTalkTag.getContentValues();
        Uri uri = mContentResolver.insert(TalkTagEntry.CONTENT_URI, values);
    }

    public void testInsertTalkSpeaker(){
        ContentValues values = sTalkSpeaker.getContentValues();
        Uri uri = mContentResolver.insert(TalkSpeakerEntry.CONTENT_URI, values);
    }


    //TODO: Write update contentprovider

    public void testGetDates(){
        Cursor cursor = mContext.getContentResolver().query(
                MultimaniaContract.TalkEntry.DATE_CONTENT_URI,null,null,null,null
        );
        int dayIndex = cursor.getColumnIndex(TalkEntry.DAY);
        while(cursor.moveToNext()){
            Log.d(TAG,"Day: "+cursor.getString(dayIndex));
        }
    }
}
