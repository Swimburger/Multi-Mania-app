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
import be.ana.nmct.multimania.model.IData;

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
                TalkEntry.CALEVENT_ID + " INTEGER, " +
                TalkEntry.IS_KEYNOTE + " INTEGER, " +
                TalkEntry.IS_FAVORITE + " INTEGER DEFAULT 0, " +
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

    public static Cursor getNewsItemById(SQLiteDatabase db, long id) {
        return db.query(NewsItemEntry.TABLE_NAME, null, NewsItemEntry._ID + "=?", new String[]{"" + id}, null, null, null);
    }

    public static Cursor getTagById(SQLiteDatabase db, long id) {
        return db.query(TagEntry.TABLE_NAME, null, TagEntry._ID + "=?", new String[]{"" + id}, null, null, null);
    }

    public static Cursor getRoomById(SQLiteDatabase db, long id) {
        return db.query(RoomEntry.TABLE_NAME, null, RoomEntry._ID + "=?", new String[]{"" + id}, null, null, null);
    }

    public static Cursor getTalkById(SQLiteDatabase db, long id) {
        return db.query(TalkEntry.TABLE_NAME, null, TalkEntry._ID + "=?", new String[]{"" + id}, null, null, null);
    }

    public static Cursor getSpeakerById(SQLiteDatabase db, long id) {
        return db.query(SpeakerEntry.TABLE_NAME,null,SpeakerEntry._ID+"=?",new String[]{""+id},null,null,null);
    }

    public static long insertItem(SQLiteDatabase db, IData iData) {
        ContentValues values = iData.getContentValues();
        return db.insert(iData.getTableName(), null, values);
    }
}
