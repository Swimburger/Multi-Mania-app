package be.ana.nmct.multimania.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.ana.nmct.multimania.data.MultimaniaContract.NewsItemEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.RoomEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.SpeakerEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TagEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TalkEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TalkSpeakerEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TalkTagEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.UserEntry;
import be.ana.nmct.multimania.model.NewsItem;
import be.ana.nmct.multimania.model.Room;
import be.ana.nmct.multimania.model.Speaker;
import be.ana.nmct.multimania.model.Tag;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.model.TalkSpeaker;
import be.ana.nmct.multimania.model.TalkTag;
import be.ana.nmct.multimania.utils.Utility;

/**
 * Created by Astrid on 28/10/2014.
 */
public class DbHelper  extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "multimania.db";

    public static DbHelper INSTANCE;
    private static Object lock = new Object();

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbHelper getInstance(Context context){
        if(INSTANCE == null){
            synchronized (lock){
                if(INSTANCE == null){
                    INSTANCE = new DbHelper(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db,0,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NewsItemEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TalkTagEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TalkSpeakerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SpeakerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RoomEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TagEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TalkEntry.TABLE_NAME);

        createNewsItemTable(db);
        createRoomTable(db);
        createTagTable(db);
        createSpeakerTable(db);
        createTalkTable(db);
        createTalkTagTable(db);
        createTalkSpeakerTable(db);


        //createUserTable(db);

    }

    private void createNewsItemTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + NewsItemEntry.TABLE_NAME + " (" +
                NewsItemEntry._ID + " INTEGER PRIMARY KEY, " +
                NewsItemEntry.TITLE + " TEXT NOT NULL, " +
                NewsItemEntry.IMAGE + " TEXT, " +
                NewsItemEntry.SHORT_DESCRIPTION + " TEXT, " +
                NewsItemEntry.LONG_DESCRIPTION + " TEXT, " +
                NewsItemEntry.IMPORTANCE + " INTEGER, " +
                "'"+ NewsItemEntry.ORDER + "' INTEGER" + ");";

        db.execSQL(sql);
    }

    private void createRoomTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + RoomEntry.TABLE_NAME + " (" +
                RoomEntry._ID + " INTEGER PRIMARY KEY, " +
                RoomEntry.NAME + " TEXT NOT NULL UNIQUE" + ");";

        db.execSQL(sql);
    }

    private void createTagTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TagEntry.TABLE_NAME + " (" +
                TagEntry._ID + " INTEGER PRIMARY KEY, " +
                TagEntry.NAME + " TEXT NOT NULL UNIQUE" + ");";

        db.execSQL(sql);
    }

    private void createSpeakerTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + SpeakerEntry.TABLE_NAME + " (" +
                SpeakerEntry._ID + " INTEGER PRIMARY KEY, " +
                SpeakerEntry.NAME + " TEXT NOT NULL UNIQUE" + ");";

        db.execSQL(sql);
    }

    private void createTalkTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TalkEntry.TABLE_NAME + " (" +
                TalkEntry._ID + " INTEGER PRIMARY KEY, " +
                TalkEntry.TITLE + " TEXT NOT NULL, " +
                TalkEntry.DATE_FROM + " TEXT NOT NULL, " +
                TalkEntry.DATE_UNTIL + " TEXT NOT NULL, " +
                TalkEntry.DESCRIPTION + " TEXT NOT NULL, " +
                TalkEntry.ROOM_ID + " INTEGER, " +
                TalkEntry.IS_KEYNOTE + " INTEGER, " +
                " FOREIGN KEY (" + TalkEntry.ROOM_ID + ") REFERENCES " +
                RoomEntry.TABLE_NAME + " ("+RoomEntry._ID+") "+
                ");";

        db.execSQL(sql);
    }

    private void createTalkTagTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TalkTagEntry.TABLE_NAME + " (" +
                TalkTagEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TalkTagEntry.TALK_ID + " INTEGER NOT NULL, " +
                TalkTagEntry.TAG_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + TalkTagEntry.TALK_ID+") REFERENCES " +
                TalkEntry.TABLE_NAME + " ("+TalkEntry._ID+"), " +
                " FOREIGN KEY (" + TalkTagEntry.TAG_ID+") REFERENCES " +
                TagEntry.TABLE_NAME + " ("+TagEntry._ID+"), " +
                " UNIQUE (" + TalkTagEntry.TALK_ID + ", " + TalkTagEntry.TAG_ID+") ON CONFLICT REPLACE);";

        db.execSQL(sql);
    }

    private void createTalkSpeakerTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TalkSpeakerEntry.TABLE_NAME + " (" +
                TalkSpeakerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TalkSpeakerEntry.TALK_ID + " INTEGER NOT NULL, " +
                TalkSpeakerEntry.SPEAKER_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + TalkSpeakerEntry.TALK_ID+") REFERENCES " +
                TalkEntry.TABLE_NAME + " ("+TalkEntry._ID+"), " +
                " FOREIGN KEY (" + TalkSpeakerEntry.SPEAKER_ID+") REFERENCES " +
                TagEntry.TABLE_NAME + " ("+SpeakerEntry._ID+"), " +
                " UNIQUE (" + TalkSpeakerEntry.TALK_ID + ", " + TalkSpeakerEntry.SPEAKER_ID+") ON CONFLICT REPLACE);";

        db.execSQL(sql);
    }

    private void createUserTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " STRING" + ");";

        db.execSQL(sql);
    }

    public static long InsertNewsItem(SQLiteDatabase db, NewsItem newsItem) {
        ContentValues values = getContentValues(newsItem);
        return db.insert(NewsItemEntry.TABLE_NAME,null,values);
    }

    public static ContentValues getContentValues(NewsItem newsItem) {
        ContentValues values = new ContentValues();
        values.put(NewsItemEntry._ID,newsItem.id);
        values.put(NewsItemEntry.TITLE,newsItem.title);
        values.put(NewsItemEntry.IMAGE,newsItem.image);
        values.put(NewsItemEntry.SHORT_DESCRIPTION,newsItem.shortDescription);
        values.put(NewsItemEntry.LONG_DESCRIPTION,newsItem.longDescription);
        values.put(NewsItemEntry.IMPORTANCE,newsItem.importance);
        values.put(NewsItemEntry.ORDER,newsItem.order);
        return values;
    }

    public static Cursor GetNewsItemById(SQLiteDatabase db, int id) {
        return db.query(NewsItemEntry.TABLE_NAME,null,NewsItemEntry._ID+"=?",new String[]{""+id},null,null,null);
    }

    public static long InsertTag(SQLiteDatabase db, Tag tag) {
        ContentValues values = getContentValues(tag);
        return db.insert(TagEntry.TABLE_NAME,null,values);
    }

    public static ContentValues getContentValues(Tag tag) {
        ContentValues values = new ContentValues();
        values.put(TagEntry._ID,tag.id);
        values.put(TagEntry.NAME,tag.name);
        return values;
    }

    public static Cursor GetTagById(SQLiteDatabase db, int id) {
        return db.query(TagEntry.TABLE_NAME,null,TagEntry._ID+"=?",new String[]{""+id},null,null,null);
    }

    public static long InsertRoom(SQLiteDatabase db, Room room) {
        ContentValues values = getContentValues(room);
        return db.insert(RoomEntry.TABLE_NAME, null, values);
    }

    public static ContentValues getContentValues(Room room) {
        ContentValues values = new ContentValues();
        values.put(RoomEntry._ID,room.id);
        values.put(RoomEntry.NAME,room.name);
        return values;
    }

    public static Cursor GetRoomById(SQLiteDatabase db, int id) {
        return db.query(RoomEntry.TABLE_NAME,null,RoomEntry._ID+"=?",new String[]{""+id},null,null,null);
    }

    public static long InsertTalk(SQLiteDatabase db, Talk talk) {
        ContentValues values = getContentValues(talk);
        return db.insert(TalkEntry.TABLE_NAME, null, values);
    }

    public static ContentValues getContentValues(Talk talk) {
        ContentValues values = new ContentValues();
        values.put(TalkEntry._ID,talk.id);
        values.put(TalkEntry.TITLE,talk.title);
        values.put(TalkEntry.DESCRIPTION,talk.description);
        values.put(TalkEntry.DATE_FROM, Utility.ConvertDateToString(talk.from));
        values.put(TalkEntry.DATE_UNTIL,Utility.ConvertDateToString(talk.to));
        values.put(TalkEntry.ROOM_ID,talk.roomId);
        values.put(TalkEntry.IS_KEYNOTE,talk.isKeynote);
        return values;
    }

    public static Cursor GetTalkById(SQLiteDatabase db, long id) {
        return db.query(TalkEntry.TABLE_NAME,null,TalkEntry._ID+"=?",new String[]{""+id},null,null,null);
    }

    public static long InsertSpeaker(SQLiteDatabase db, Speaker speaker) {
        ContentValues values = getContentValues(speaker);
        return db.insert(SpeakerEntry.TABLE_NAME, null, values);
    }

    public static ContentValues getContentValues(Speaker speaker) {
        ContentValues values = new ContentValues();
        values.put(SpeakerEntry._ID,speaker.id);
        values.put(SpeakerEntry.NAME,speaker.name);
        return values;
    }

    public static Cursor GetSpeakerById(SQLiteDatabase db, int id) {
        return db.query(SpeakerEntry.TABLE_NAME,null,SpeakerEntry._ID+"=?",new String[]{""+id},null,null,null);
    }

    public static long InsertTalkTag(SQLiteDatabase db, TalkTag talkTag) {
        ContentValues values = getContentValues(talkTag);
        return db.insert(TalkTagEntry.TABLE_NAME, null, values);
    }

    public static ContentValues getContentValues(TalkTag talkTag) {
        ContentValues values = new ContentValues();
        values.put(TalkTagEntry.TALK_ID,talkTag.talkId);
        values.put(TalkTagEntry.TAG_ID,talkTag.tagId);
        return values;
    }

    public static long InsertTalkSpeaker(SQLiteDatabase db, TalkSpeaker talkSpeaker) {
        ContentValues values = getContentValues(talkSpeaker);
        return db.insert(TalkSpeakerEntry.TABLE_NAME, null, values);
    }

    public static ContentValues getContentValues(TalkSpeaker talkSpeaker) {
        ContentValues values = new ContentValues();
        values.put(TalkSpeakerEntry.TALK_ID,talkSpeaker.talkId);
        values.put(TalkSpeakerEntry.SPEAKER_ID,talkSpeaker.speakerId);
        return values;
    }
}
