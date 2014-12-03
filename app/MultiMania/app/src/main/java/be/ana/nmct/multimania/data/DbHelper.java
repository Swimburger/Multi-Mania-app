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
import be.ana.nmct.multimania.model.IData;

/**
 * The DbHelper class contains the functions for creating the database, and helper functions to get data and insert data
 * Created by Astrid on 28/10/2014.
 */
public class DbHelper  extends SQLiteOpenHelper{
    /**
     * Version of the SQLite database
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * Name of the SQLite database
     */
    public static final String DATABASE_NAME = "multimania.db";
    /**
     * The DbHelper instance following the singleton pattern
     */
    private static DbHelper sInstance;
    /**
     * A lock object used to make sure two different threads aren't doing the same job
     */
    private static Object lock = new Object();

    /**
     * This constructor should be private following the singleton pattern, but we aren't completely consistent in our application.
     * @param context The Android context (Activity or Application context)
     */
    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * The getInstance function returns the one instance that is defined static
     * @param context The Android context (Activity or Application context)
     * @return Returns the DbHelper instance
     */
    public static DbHelper getInstance(Context context){
        if(sInstance == null){
            synchronized (lock){
                if(sInstance == null){
                    sInstance = new DbHelper(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * In the onCreate function we create the tables
     * @param db Android will pass in a writable SQLiteDatabase that we use to create the tables
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db,0,DATABASE_VERSION);
    }

    /**
     * In the onUpgrade function we drop, create and upgrade the database
     * @param db Writable SQLiteDatabase
     * @param oldVersion Previous version of the database
     * @param newVersion New version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NewsItemEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TalkTagEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TalkSpeakerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SpeakerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RoomEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TagEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TalkEntry.TABLE_NAME);

        createNewsItemTable(db);
        createRoomTable(db);
        createTagTable(db);
        createSpeakerTable(db);
        createTalkTable(db);
        createTalkTagTable(db);
        createTalkSpeakerTable(db);
    }

    /**
     * Creates the NewsItemTable
     * @param db Writable SQLiteDatabase
     */
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

    /**
     * Creates the RoomTable
     * @param db Writable SQLiteDatabase
     */
    private void createRoomTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + RoomEntry.TABLE_NAME + " (" +
                RoomEntry._ID + " INTEGER PRIMARY KEY, " +
                RoomEntry.NAME + " TEXT NOT NULL UNIQUE" + ");";

        db.execSQL(sql);
    }

    /**
     * Creates the TagTable
     * @param db Writable SQLiteDatabase
     */
    private void createTagTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TagEntry.TABLE_NAME + " (" +
                TagEntry._ID + " INTEGER PRIMARY KEY, " +
                TagEntry.NAME + " TEXT NOT NULL UNIQUE" + ");";

        db.execSQL(sql);
    }

    /**
     * Creates the SpeakerTable
     * @param db Writable SQLiteDatabase
     */
    private void createSpeakerTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + SpeakerEntry.TABLE_NAME + " (" +
                SpeakerEntry._ID + " INTEGER PRIMARY KEY, " +
                SpeakerEntry.NAME + " TEXT NOT NULL UNIQUE" + ");";

        db.execSQL(sql);
    }

    /**
     * Creates the TalkTable, mind the order of table creation
     * @param db Writable SQLiteDatabase
     */
    private void createTalkTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TalkEntry.TABLE_NAME + " (" +
                TalkEntry._ID + " INTEGER PRIMARY KEY, " +
                TalkEntry.TITLE + " TEXT NOT NULL, " +
                TalkEntry.DATE_FROM + " TEXT NOT NULL, " +
                TalkEntry.DATE_UNTIL + " TEXT NOT NULL, " +
                TalkEntry.DESCRIPTION + " TEXT NOT NULL, " +
                TalkEntry.ROOM_ID + " INTEGER, " +
                TalkEntry.CALEVENT_ID + " INTEGER, " +
                TalkEntry.IS_KEYNOTE + " INTEGER, " +
                TalkEntry.IS_FAVORITE + " INTEGER DEFAULT 0, " +
                " FOREIGN KEY (" + TalkEntry.ROOM_ID + ") REFERENCES " +
                RoomEntry.TABLE_NAME + " ("+RoomEntry._ID+") "+
                ");";

        db.execSQL(sql);
    }

    /**
     * Create the TalkTagTable, mind the order of table creation
     * @param db Writable SQLiteDatabase
     */
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

    /**
     * Creates the TalkSpeakerTable, mind the order of table creation
     * @param db Writable SQLiteDatabase
     */
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

    /**
     * Gets a NewsItem by id
     * @param db Readable or writable SQLiteDatabase
     * @param id The id of the NewsItem
     * @return Returns a cursor with zero or one row with the NewsItem Data
     */
    public static Cursor getNewsItemById(SQLiteDatabase db, long id) {
        return db.query(NewsItemEntry.TABLE_NAME, null, NewsItemEntry._ID + "=?", new String[]{"" + id}, null, null, null);
    }

    /**
     * Gets a Tag by id
     * @param db Readable or writable SQLiteDatabase
     * @param id The id of the Tag
     * @return Returns a cursor with zero or one row with the Tag Data
     */
    public static Cursor getTagById(SQLiteDatabase db, long id) {
        return db.query(TagEntry.TABLE_NAME, null, TagEntry._ID + "=?", new String[]{"" + id}, null, null, null);
    }

    /**
     * Gets a Room by id
     * @param db Readable or writable SQLiteDatabase
     * @param id The id of the Room
     * @return Returns a cursor with zero or one row with the Room Data
     */
    public static Cursor getRoomById(SQLiteDatabase db, long id) {
        return db.query(RoomEntry.TABLE_NAME, null, RoomEntry._ID + "=?", new String[]{"" + id}, null, null, null);
    }

    /**
     * Gets a Talk by id
     * @param db Readable or writable SQLiteDatabase
     * @param id The id of the Talk
     * @return Returns a cursor with zero or one row with the Talk Data
     */
    public static Cursor getTalkById(SQLiteDatabase db, long id) {
        return db.query(TalkEntry.TABLE_NAME, null, TalkEntry._ID + "=?", new String[]{"" + id}, null, null, null);
    }

    /**
     * Gets a Speaker by id
     * @param db Readable or writable SQLiteDatabase
     * @param id The id of the Speaker
     * @return Returns a cursor with zero or one row with the Speaker Data
     */
    public static Cursor getSpeakerById(SQLiteDatabase db, long id) {
        return db.query(SpeakerEntry.TABLE_NAME,null,SpeakerEntry._ID+"=?",new String[]{""+id},null,null,null);
    }

    /**
     * Inserts a IData item, the IData interface is applied to all data models for abstract database operations
     * @param db Writable SQLiteDatabase
     * @param iData A class that implements the IData interface
     * @return Returns the id of the inserted item
     */
    public static long insertItem(SQLiteDatabase db, IData iData) {
        ContentValues values = iData.getContentValues();
        return db.insert(iData.getTableName(), null, values);
    }
}
