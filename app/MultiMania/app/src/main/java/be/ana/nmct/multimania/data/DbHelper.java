package be.ana.nmct.multimania.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.execSQL("DROP TABLE IF EXISTS " + MultimaniaContract.NewsItemEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MultimaniaContract.TalkEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MultimaniaContract.RoomEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MultimaniaContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MultimaniaContract.TagEntry.TABLE_NAME);

        createNewsItemTable(db);
        createTalkTable(db);
        createRoomTable(db);
        createUserTable(db);
        createTagTable(db);
    }

    private void createNewsItemTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + MultimaniaContract.NewsItemEntry.TABLE_NAME + " (" +
                MultimaniaContract.NewsItemEntry.NEWSITEM_ID + " INTEGER PRIMARY KEY," +
                MultimaniaContract.NewsItemEntry.TITLE + " TEXT NOT NULL," +
                MultimaniaContract.NewsItemEntry.IMAGE + " TEXT," +
                MultimaniaContract.NewsItemEntry.SHORT_DESCRIPTION + " TEXT NOT NULL," +
                MultimaniaContract.NewsItemEntry.DESCRIPTION + " TEXT NOT NULL," +
                MultimaniaContract.NewsItemEntry.IMPORTANCE + " INTEGER," +
                MultimaniaContract.NewsItemEntry.ORDER + " INTEGER" + ");";

        db.execSQL(sql);
    }

    private void createTalkTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + MultimaniaContract.TalkEntry.TABLE_NAME + " (" +
                MultimaniaContract.TalkEntry.TALK_ID + " INTEGER PRIMARY KEY," +
                MultimaniaContract.TalkEntry.TITLE + " TEXT NOT NULL," +
                MultimaniaContract.TalkEntry.DATE_FROM + " TEXT NOT NULL," +
                MultimaniaContract.TalkEntry.DATE_UNTIL + " TEXT NOT NULL," +
                MultimaniaContract.TalkEntry.DESCRIPTION + " TEXT NOT NULL," +
                MultimaniaContract.TalkEntry.ROOM_ID + " INTEGER," +
                MultimaniaContract.TalkEntry.IS_KEYNOTE + " INTEGER" + ");";

        db.execSQL(sql);
    }

    private void createRoomTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + MultimaniaContract.RoomEntry.TABLE_NAME + " (" +
                MultimaniaContract.RoomEntry.ROOM_ID + " INTEGER PRIMARY KEY," +
                MultimaniaContract.RoomEntry.NAME + " TEXT NOT NULL" + ");";

        db.execSQL(sql);
    }

    private void createUserTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + MultimaniaContract.UserEntry.TABLE_NAME + " (" +
                MultimaniaContract.UserEntry.USER_ID + " STRING" + ");";

        db.execSQL(sql);
    }

    private void createTagTable(SQLiteDatabase db){
        String sql = "CREATE TABLE " + MultimaniaContract.TagEntry.TABLE_NAME + " (" +
                MultimaniaContract.TagEntry.TAG_ID + " INTEGER PRIMARY KEY," +
                MultimaniaContract.TagEntry.NAME + " TEXT NOT NULL" + ");";

        db.execSQL(sql);
    }
}
