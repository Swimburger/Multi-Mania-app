package be.ana.nmct.multimania.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

import be.ana.nmct.multimania.data.MultimaniaContract.NewsItemEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.RoomEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.SpeakerEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TagEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TalkEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TalkSpeakerEntry;
import be.ana.nmct.multimania.data.MultimaniaContract.TalkTagEntry;

public class MultimaniaProvider extends ContentProvider {
    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mDbHelper;

    public static final int NEWS                    = 100;
    public static final int NEWS_ID                 = 101;

    public static final int TALK                    = 200;
    public static final int TALK_ID                 = 201;
    public static final int TALK_BY_ROOM_ID         = 202;
    public static final int TALK_BY_TAG_ID          = 203;

    public static final int TALK_DATE               = 300;

    public static final int ROOM                    = 400;
    public static final int ROOM_ID                 = 401;

    public static final int TAG                     = 500;
    public static final int TAG_ID                  = 501;
    public static final int TAG_BY_TALK_ID          = 502;

    public static final int SPEAKER                 = 600;
    public static final int SPEAKER_ID              = 601;
    public static final int SPEAKER_BY_TALK_ID      = 602;


    public static final int TALK_TAG                = 700;
    public static final int TALK_SPEAKER            = 800;

    private static final SQLiteQueryBuilder sTalksWithRoomAndTagsQueryBuilder;
    private static final SQLiteQueryBuilder sTalksByRoomIdQueryBuilder;
    private static final SQLiteQueryBuilder sTalksByTagIdQueryBuilder;
    private static final SQLiteQueryBuilder sTagsByTalkIdQueryBuilder;
    private static final SQLiteQueryBuilder sSpeakersByTalkIdQueryBuilder;

    static{
        sTalksWithRoomAndTagsQueryBuilder = new SQLiteQueryBuilder();
        sTalksByRoomIdQueryBuilder      = new SQLiteQueryBuilder();
        sTalksByTagIdQueryBuilder       = new SQLiteQueryBuilder();
        sTagsByTalkIdQueryBuilder       = new SQLiteQueryBuilder();
        sSpeakersByTalkIdQueryBuilder   = new SQLiteQueryBuilder();

        HashMap<String,String> columnMap = new HashMap<String, String>();
        final String talkTableName = TalkEntry.TABLE_NAME;
        columnMap.put(talkTableName + "." + TalkEntry._ID, talkTableName + "." + TalkEntry._ID+ " as "+ TalkEntry._ID);
        columnMap.put(talkTableName + "." + TalkEntry.TITLE, talkTableName + "." + TalkEntry.TITLE+ " as "+ TalkEntry.TITLE);
        columnMap.put(talkTableName + "." + TalkEntry.DESCRIPTION, talkTableName + "." + TalkEntry.DESCRIPTION+ " as "+ TalkEntry.DESCRIPTION);
        columnMap.put(talkTableName + "." + TalkEntry.DATE_FROM, talkTableName + "." + TalkEntry.DATE_FROM+ " as "+ TalkEntry.DATE_FROM);
        columnMap.put(talkTableName + "." + TalkEntry.DATE_UNTIL, talkTableName + "." + TalkEntry.DATE_UNTIL+ " as "+ TalkEntry.DATE_UNTIL);
        columnMap.put(talkTableName + "." + TalkEntry.ROOM_ID, talkTableName + "." + TalkEntry.ROOM_ID+ " as "+ TalkEntry.ROOM_ID);
        columnMap.put(talkTableName + "." + TalkEntry.IS_KEYNOTE, talkTableName + "." + TalkEntry.IS_KEYNOTE+ " as "+ TalkEntry.IS_KEYNOTE);
        columnMap.put(talkTableName + "." + TalkEntry.IS_FAVORITE, talkTableName + "." + TalkEntry.IS_FAVORITE+ " as "+ TalkEntry.IS_FAVORITE);
        columnMap.put(talkTableName + "." + TalkEntry.CALEVENT_ID, talkTableName + "." + TalkEntry.CALEVENT_ID + " as " + TalkEntry.CALEVENT_ID);
        columnMap.put(RoomEntry.TABLE_NAME + "." + RoomEntry.NAME,RoomEntry.TABLE_NAME + "." + RoomEntry.NAME+ " as "+RoomEntry.ROOM_NAME);

        sTalksWithRoomAndTagsQueryBuilder.setTables(
                TalkEntry.TABLE_NAME + " INNER JOIN " +
                        RoomEntry.TABLE_NAME + " ON (" +
                        TalkEntry.TABLE_NAME + "." + TalkEntry.ROOM_ID + " = " +
                        RoomEntry.TABLE_NAME + "." + RoomEntry._ID + ") "
        );

        sTalksWithRoomAndTagsQueryBuilder.setProjectionMap(columnMap);

        sTalksByRoomIdQueryBuilder.setTables(
                TalkEntry.TABLE_NAME + " INNER JOIN " +
                RoomEntry.TABLE_NAME + " ON " +
                TalkEntry.TABLE_NAME + "." + TalkEntry.ROOM_ID + " = " +
                RoomEntry.TABLE_NAME + "." + RoomEntry._ID
        );

        sTalksByRoomIdQueryBuilder.setProjectionMap(columnMap);

        sTalksByTagIdQueryBuilder.setTables(
                TalkEntry.TABLE_NAME + " INNER JOIN " +
                TalkTagEntry.TABLE_NAME + " ON " +
                TalkEntry.TABLE_NAME + "." + TalkEntry._ID + " = " +
                TalkTagEntry.TABLE_NAME + "." + TalkTagEntry.TALK_ID
        );

        sTalksByTagIdQueryBuilder.setProjectionMap(columnMap);

        sTagsByTalkIdQueryBuilder.setTables(
                TagEntry.TABLE_NAME + " INNER JOIN " +
                TalkTagEntry.TABLE_NAME + " ON " +
                TagEntry.TABLE_NAME + "." + TagEntry._ID + " = " +
                TalkTagEntry.TABLE_NAME + "." + TalkTagEntry.TAG_ID
        );

        sSpeakersByTalkIdQueryBuilder.setTables(
                SpeakerEntry.TABLE_NAME + " INNER JOIN " +
                TalkSpeakerEntry.TABLE_NAME + " ON " +
                SpeakerEntry.TABLE_NAME + "." + SpeakerEntry._ID + " = " +
                TalkSpeakerEntry.TABLE_NAME + "." + TalkSpeakerEntry.SPEAKER_ID
        );
    }


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MultimaniaContract.CONTENT_AUTHORITY;


        matcher.addURI(authority, MultimaniaContract.PATH_NEWS, NEWS);
        matcher.addURI(authority, MultimaniaContract.PATH_NEWS + "/#", NEWS_ID);

        matcher.addURI(authority, MultimaniaContract.PATH_TALK, TALK);
        matcher.addURI(authority, MultimaniaContract.PATH_TALK+"/#", TALK_ID);
        matcher.addURI(authority, MultimaniaContract.PATH_TALK+"/"+MultimaniaContract.PATH_ROOM+"/#", TALK_BY_ROOM_ID);
        matcher.addURI(authority, MultimaniaContract.PATH_TALK+"/"+MultimaniaContract.PATH_TAG+"/#", TALK_BY_TAG_ID);

        matcher.addURI(authority, MultimaniaContract.PATH_TALK+"/"+MultimaniaContract.PATH_DATE,TALK_DATE);

        matcher.addURI(authority, MultimaniaContract.PATH_ROOM, ROOM);
        matcher.addURI(authority, MultimaniaContract.PATH_ROOM+"/#", ROOM_ID);

        matcher.addURI(authority, MultimaniaContract.PATH_TAG, TAG);
        matcher.addURI(authority, MultimaniaContract.PATH_TAG+"/#", TAG_ID);
        matcher.addURI(authority, MultimaniaContract.PATH_TAG+"/"+MultimaniaContract.PATH_TALK+"/#", TAG_BY_TALK_ID);

        matcher.addURI(authority, MultimaniaContract.PATH_SPEAKER, SPEAKER);
        matcher.addURI(authority, MultimaniaContract.PATH_SPEAKER+"/#", SPEAKER_ID);
        matcher.addURI(authority, MultimaniaContract.PATH_SPEAKER+"/"+MultimaniaContract.PATH_TALK+"/#", SPEAKER_BY_TALK_ID);

        matcher.addURI(authority, MultimaniaContract.PATH_TALK_TAG, TALK_TAG);

        matcher.addURI(authority, MultimaniaContract.PATH_TALK_SPEAKER, TALK_SPEAKER);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch(match){
            case NEWS:
                return NewsItemEntry.CONTENT_TYPE;
            case NEWS_ID:
                return NewsItemEntry.CONTENT_ITEM_TYPE;
            case TALK:
                return TalkEntry.CONTENT_TYPE;
            case TALK_ID:
                return TalkEntry.CONTENT_ITEM_TYPE;
            case TALK_BY_ROOM_ID:
                return TalkEntry.CONTENT_TYPE;
            case TALK_BY_TAG_ID:
                return TalkEntry.CONTENT_TYPE;
            case TALK_DATE:
                return TalkEntry.DATE_CONTENT_TYPE;
            case ROOM:
                return RoomEntry.CONTENT_TYPE;
            case TAG:
                return TagEntry.CONTENT_TYPE;
            case TAG_ID:
                return TagEntry.CONTENT_ITEM_TYPE;
            case TAG_BY_TALK_ID:
                return TagEntry.CONTENT_TYPE;
            case SPEAKER:
                return SpeakerEntry.CONTENT_TYPE;
            case SPEAKER_ID:
                return SpeakerEntry.CONTENT_ITEM_TYPE;
            case SPEAKER_BY_TALK_ID:
                return SpeakerEntry.CONTENT_TYPE;
            case TALK_TAG:
                return TalkTagEntry.CONTENT_TYPE;
            case TALK_SPEAKER:
                return TalkSpeakerEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case NEWS:
                retCursor = mDbHelper.getReadableDatabase().query(
                        NewsItemEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case NEWS_ID:
                retCursor = mDbHelper.getReadableDatabase().query(
                        NewsItemEntry.TABLE_NAME,
                        projection,
                        NewsItemEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TALK:
                retCursor = sTalksWithRoomAndTagsQueryBuilder.query(
                        mDbHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TALK_ID:
                retCursor = sTalksWithRoomAndTagsQueryBuilder.query(
                        mDbHelper.getReadableDatabase(),
                        projection,
                        TalkEntry.TABLE_NAME+"."+TalkEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TALK_BY_ROOM_ID:
                retCursor = getTalksByRoomId(uri,projection,sortOrder);
                break;
            case TALK_BY_TAG_ID:
                retCursor = getTalksByTagId(uri,projection,sortOrder);
                break;
            case TALK_DATE:
                retCursor = mDbHelper.getReadableDatabase().query(
                        TalkEntry.TABLE_NAME,
                        new String[]{"SUBSTR("+TalkEntry.DATE_FROM+",0,11) AS "+TalkEntry.DAY},
                        null,
                        null,
                        TalkEntry.DAY,
                        null,
                        sortOrder
                );
                break;
            case ROOM:
                retCursor = mDbHelper.getReadableDatabase().query(
                        RoomEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ROOM_ID:
                retCursor = DbHelper.getRoomById(mDbHelper.getReadableDatabase(), ContentUris.parseId(uri));
                break;
            case TAG:
                retCursor = mDbHelper.getReadableDatabase().query(
                        TagEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TAG_ID:
                retCursor = DbHelper.getTagById(mDbHelper.getReadableDatabase(), ContentUris.parseId(uri));
                break;
            case TAG_BY_TALK_ID:
                retCursor = getTagsByTalkId(uri,projection,sortOrder);
                break;
            case SPEAKER:
                retCursor = mDbHelper.getReadableDatabase().query(
                        SpeakerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case SPEAKER_ID:
                retCursor = DbHelper.getSpeakerById(mDbHelper.getReadableDatabase(), ContentUris.parseId(uri));
                break;
            case SPEAKER_BY_TALK_ID:
                retCursor = getSpeakersByTalkId(uri,projection,sortOrder);
                break;
            case TALK_TAG:
                retCursor = mDbHelper.getReadableDatabase().query(
                        TalkTagEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TALK_SPEAKER:
                retCursor = mDbHelper.getReadableDatabase().query(
                        TalkSpeakerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:"+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getTalksByRoomId(Uri uri, String[] projection, String sortOrder) {
        long id = ContentUris.parseId(uri);
        return sTalksByRoomIdQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                TalkEntry.ROOM_ID + " = '" + id + "'",
                null,
                null,
                null,
                sortOrder
                );
    }

    private Cursor getTalksByTagId(Uri uri, String[] projection, String sortOrder) {
        long id = ContentUris.parseId(uri);
        return sTalksByRoomIdQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                TalkTagEntry.TABLE_NAME + "." + TalkTagEntry.TAG_ID + " = '" + id + "'",
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getSpeakersByTalkId(Uri uri, String[] projection, String sortOrder) {
        long id = ContentUris.parseId(uri);
        return sSpeakersByTalkIdQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                TalkSpeakerEntry.TABLE_NAME + "." + TalkSpeakerEntry.TALK_ID + " = '" + id + "'",
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTagsByTalkId(Uri uri, String[] projection, String sortOrder) {
        long id = ContentUris.parseId(uri);
        return sTagsByTalkIdQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                TalkTagEntry.TABLE_NAME + "." + TalkTagEntry.TALK_ID + " = '" + id + "'",
                null,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case NEWS:{
                long id = db.insert(NewsItemEntry.TABLE_NAME,null,values);
                if(id >0){
                    returnUri = NewsItemEntry.buildItemUri(id);
                }else{
                    throw  new SQLException("Failed to insert row into "+ uri);
                }
                break;
            }
            case TALK:{
                long id = db.insert(TalkEntry.TABLE_NAME,null,values);
                if(id >0){
                    returnUri = TalkEntry.buildItemUri(id);
                }else{
                    throw  new SQLException("Failed to insert row into "+ uri);
                }
                break;
            }
            case ROOM:{
                long id = db.insert(RoomEntry.TABLE_NAME,null,values);
                if(id >0){
                    returnUri = RoomEntry.buildItemUri(id);
                }else{
                    throw  new SQLException("Failed to insert row into "+ uri);
                }
                break;
            }
            case TAG:{
                long id = db.insert(TagEntry.TABLE_NAME,null,values);
                if(id >0){
                    returnUri = TagEntry.buildItemUri(id);
                }else{
                    throw  new SQLException("Failed to insert row into "+ uri);
                }
                break;
            }
            case SPEAKER:{
                long id = db.insert(SpeakerEntry.TABLE_NAME,null,values);
                if(id >0){
                    returnUri = SpeakerEntry.buildItemUri(id);
                }else{
                    throw  new SQLException("Failed to insert row into "+ uri);
                }
                break;
            }
            case TALK_TAG:{
                long id = db.insert(TalkTagEntry.TABLE_NAME,null,values);
                if(id >0){
                    returnUri = TalkTagEntry.buildItemUri(id);
                }else{
                    throw  new SQLException("Failed to insert row into "+ uri);
                }
                break;
            }
            case TALK_SPEAKER:{
                long id = db.insert(TalkSpeakerEntry.TABLE_NAME,null,values);
                if(id >0){
                    returnUri = TalkSpeakerEntry.buildItemUri(id);
                }else{
                    throw  new SQLException("Failed to insert row into "+ uri);
                }
                break;
            }
            default:
                throw  new UnsupportedOperationException("Unknown uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(returnUri,null);
        return returnUri;
    }

    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case NEWS:{
                return bulkInsert(uri, values, db,NewsItemEntry.TABLE_NAME);
            }
            case TALK:{
                return bulkInsert(uri, values, db,TalkEntry.TABLE_NAME);
            }
            case ROOM:{
                return bulkInsert(uri, values, db,RoomEntry.TABLE_NAME);
            }
            case TAG:{
                return bulkInsert(uri, values, db,TagEntry.TABLE_NAME);
            }
            case SPEAKER:{
                return bulkInsert(uri, values, db,SpeakerEntry.TABLE_NAME);
            }
            case TALK_TAG:{
                return bulkInsert(uri, values, db,TalkTagEntry.TABLE_NAME);
            }
            case TALK_SPEAKER:{
                return bulkInsert(uri, values, db,TalkSpeakerEntry.TABLE_NAME);
            }
        }
        return super.bulkInsert(uri, values);
    }

    private int bulkInsert(Uri uri, ContentValues[] values, SQLiteDatabase db,String tableName) {
        db.beginTransaction();
        int returnCount = 0;
        try{
            for(ContentValues value : values){
                long _id = db.insert(tableName,null,value);
                if(_id!=-1){
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match){
            case NEWS:{
                rowsUpdated = db.update(NewsItemEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case TALK:{
                rowsUpdated = db.update(TalkEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case ROOM:{
                rowsUpdated = db.update(RoomEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case TAG:{
                rowsUpdated = db.update(TagEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case SPEAKER:{
                rowsUpdated = db.update(SpeakerEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case TALK_TAG:{
                rowsUpdated = db.update(TalkTagEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case TALK_SPEAKER:{
                rowsUpdated = db.update(TalkSpeakerEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            default:
                throw  new UnsupportedOperationException("Unknown uri: "+uri);
        }
        if(0!=rowsUpdated){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match){
            case NEWS:{
                rowsDeleted = db.delete(NewsItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TALK:{
                rowsDeleted = db.delete(TalkEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case ROOM:{
                rowsDeleted = db.delete(RoomEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TAG:{
                rowsDeleted = db.delete(TagEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case SPEAKER:{
                rowsDeleted = db.delete(SpeakerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TALK_TAG:{
                rowsDeleted = db.delete(TalkTagEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TALK_SPEAKER:{
                rowsDeleted = db.delete(TalkSpeakerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw  new UnsupportedOperationException("Unknown uri: "+uri);
        }

        if(null == selection || rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

}
