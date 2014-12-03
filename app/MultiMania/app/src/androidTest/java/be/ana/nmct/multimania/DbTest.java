package be.ana.nmct.multimania;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.text.ParseException;
import java.util.Map;
import java.util.Set;

import be.ana.nmct.multimania.data.DbHelper;
import be.ana.nmct.multimania.data.MultimaniaContract.NewsItemEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.RoomEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.SpeakerEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TagEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TalkEntry;
import be.ana.nmct.multimania.model.NewsItem;
import be.ana.nmct.multimania.model.Room;
import be.ana.nmct.multimania.model.Speaker;
import be.ana.nmct.multimania.model.Tag;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.model.TalkSpeaker;
import be.ana.nmct.multimania.model.TalkTag;
import be.ana.nmct.multimania.utils.Utility;

/**
 * DbTest contains all test regarding the SQLite database
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class DbTest extends ApplicationTestCase<Application> {
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
    public DbTest() {
        super(Application.class);
    }

    public void testCreateDb(){
        mContext.deleteDatabase(DbHelper.DATABASE_NAME);
        SQLiteDatabase db = new DbHelper(mContext).getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
    }

    public void testInsertNewsItem(){
        SQLiteDatabase db = new DbHelper(mContext).getWritableDatabase();
        long returnedId = DbHelper.insertItem(db, sNewsItem);
        assertTrue(-1l != returnedId);
        Log.d(TAG,"Returned id = "+ returnedId + " and id that should be inserted = "+sNewsItem.id);
        Cursor cursor = DbHelper.getNewsItemById(db, sNewsItem.id);
        cursor.moveToFirst();
        int idIndex =cursor.getColumnIndex(NewsItemEntry._ID);
        assertEquals(cursor.getInt(idIndex),sNewsItem.id);
        db.close();
    }

    public void testInsertRoom(){
        SQLiteDatabase db = new DbHelper(mContext).getWritableDatabase();
        long returnedId = DbHelper.insertItem(db, sRoom);
        assertTrue(-1l!=returnedId);
        Log.d(TAG,"Returned id = "+ returnedId + " and id that should be inserted = "+sRoom.id);
        Cursor cursor = DbHelper.getRoomById(db, sRoom.id);
        cursor.moveToFirst();
        int idIndex =cursor.getColumnIndex(RoomEntry._ID);
        assertEquals(cursor.getInt(idIndex),sRoom.id);
        db.close();
    }

    public void testInsertTag(){
        SQLiteDatabase db = new DbHelper(mContext).getWritableDatabase();
        long returnedId = DbHelper.insertItem(db, sTag);
        assertTrue(-1l!=returnedId);
        Log.d(TAG,"Returned id = "+ returnedId + " and id that should be inserted = "+sTag.id);
        Cursor cursor = DbHelper.getTagById(db, sTag.id);
        cursor.moveToFirst();
        int idIndex =cursor.getColumnIndex(TagEntry._ID);
        assertEquals(cursor.getInt(idIndex),sTag.id);
        db.close();
    }

    public void testInsertSpeaker(){
        SQLiteDatabase db = new DbHelper(mContext).getWritableDatabase();
        long returnedId = DbHelper.insertItem(db, sSpeaker);
        assertTrue(-1l!=returnedId);
        Log.d(TAG,"Returned id = "+ returnedId + " and id that should be inserted = "+sTag.id);
        Cursor cursor = DbHelper.getSpeakerById(db, sTag.id);
        cursor.moveToFirst();
        int idIndex =cursor.getColumnIndex(SpeakerEntry._ID);
        assertEquals(cursor.getInt(idIndex),sTag.id);
        db.close();
    }

    public void testInsertTalk(){
        SQLiteDatabase db = new DbHelper(mContext).getWritableDatabase();
        long returnedId = DbHelper.insertItem(db, sTalk);
        assertTrue(-1l!=returnedId);
        Log.d(TAG,"Returned id = "+ returnedId + " and id that should be inserted = "+sTalk.id);
        Cursor cursor = DbHelper.getTalkById(db, sTalk.id);
        cursor.moveToFirst();
        int idIndex =cursor.getColumnIndex(TalkEntry._ID);
        assertEquals(cursor.getInt(idIndex),sTalk.id);
        db.close();
    }

    public void testInsertTalkTag(){
        SQLiteDatabase db = new DbHelper(mContext).getWritableDatabase();
        long returnedId = DbHelper.insertItem(db, sTalkTag);
        assertTrue(-1l!=returnedId);
        Log.d(TAG, "Returned id = " + returnedId);
        db.close();
    }

    public void testInsertTalkSpeaker(){
        SQLiteDatabase db = new DbHelper(mContext).getWritableDatabase();
        long returnedId = DbHelper.insertItem(db, sTalkSpeaker);
        assertTrue(-1l!=returnedId);
        Log.d(TAG, "Returned id = " + returnedId);
        db.close();
    }

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }
}