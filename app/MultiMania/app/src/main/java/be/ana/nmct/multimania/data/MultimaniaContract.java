package be.ana.nmct.multimania.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The MultimaniaContract class is a class that contains all strings used for querying the database and content provider,
 * subclasses that represent the data models with strings and helper methods for database and content provider querying.
 * Created by Astrid on 28/10/2014.
 */
public final class MultimaniaContract {

    /**
     * The content authority of the content provider
     */
    public static final String CONTENT_AUTHORITY    = "be.ana.nmct.multimania";
    public static final Uri    BASE_CONTENT_URI     = Uri.parse("content://"+ CONTENT_AUTHORITY);
    /**
     * A string representing a directory of data items
     */
    private static final String CURSOR_DIR          = "vnd.android.cursor.dir/";
    /**
     * A string representing one data item
     */
    private static final String CURSOR_ITEM         = "vnd.android.cursor.item/";

    public static final String PATH_NEWS            = "news";
    public static final String PATH_TALK            = "talks";
    public static final String PATH_ROOM            = "rooms";
    public static final String PATH_USER            = "users";
    public static final String PATH_TAG             = "tags";
    public static final String PATH_SPEAKER         = "speakers";
    public static final String PATH_TALK_TAG        = "talk_tags";
    public static final String PATH_TALK_SPEAKER    = "talk_speakers";
    public static final String PATH_DATE            = "dates";

    /**
     * The NewsItemEntry class contains static data members that are used for querying the database and ContentProvider for NewsItems
     * @see be.ana.nmct.multimania.model.NewsItem
     */
    public static final class NewsItemEntry implements BaseColumns{
        /**
         * The table name for the SQLite database
         */
        public static final String TABLE_NAME           = "newsitem";


        public static final String TITLE                = "title";
        public static final String IMAGE                = "image";
        public static final String SHORT_DESCRIPTION    = "short_description";
        public static final String LONG_DESCRIPTION     = "long_description";
        public static final String IMPORTANCE           = "importance";
        /**
         * The order has a value of "news_order" instead of "order" because "order" is a reserved keyword in SQL
         */
        public static final String ORDER                = "news_order";


        public static final String CONTENT_TYPE         = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_NEWS;
        public static final String CONTENT_ITEM_TYPE    = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_NEWS;
        public static final Uri    CONTENT_URI          = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();

        /**
         * Builds a NewsItem uri for an id
         * @param id Id of the NewsItem
         * @return The uri corresponding with the NewsItem to query it through the ContentProvider
         */
        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * The TalkEntry class contains static data members that are used for querying the database and ContentProvider for Talks
     * @see be.ana.nmct.multimania.model.Talk
     */
    public static final class TalkEntry implements BaseColumns{
        /**
         * The table name for the SQLite database
         */
        public static final String TABLE_NAME = "talk";

        public static final String TALK_ID      = "talk_id";
        public static final String TITLE        = "title";
        public static final String DATE_FROM    = "date_from";
        public static final String DATE_UNTIL   = "date_until";
        public static final String DESCRIPTION  = "description";
        public static final String ROOM_ID      = "room_id";
        public static final String IS_KEYNOTE   = "is_keynote";
        public static final String IS_FAVORITE  = "is_favorite";
        public static final String ROOM_NAME    = "room_name";
        public static final String DAY          = "days";
        public static final String CALEVENT_ID  = "calevent_id";
        public static final String TAGS         = "tags";


        public static final String CONTENT_TYPE      = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_TALK;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_TALK;
        public static final String DATE_CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_TALK+"/"+PATH_DATE;
        public static final Uri    CONTENT_URI       = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TALK).build();
        public static final Uri DATE_CONTENT_URI     = CONTENT_URI.buildUpon().appendPath(PATH_DATE).build();

        /**
         * Builds a Talk uri for an id
         * @param id Id of the Talk
         * @return The uri corresponding with the Talk to query it through the ContentProvider
         */
        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    /**
     * The TalkTagEntry class contains static data members that are used for querying the database and ContentProvider for TalkTags
     * @see be.ana.nmct.multimania.model.TalkTag
     */
    public static final class TalkTagEntry implements BaseColumns{
        /**
         * The table name for the SQLite database
         */
        public static final String TABLE_NAME   = "talk_tag";

        public static final String TALK_ID      = "talk_id";
        public static final String TAG_ID       = "tag_id";

        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_TALK_TAG;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_TALK_TAG;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TALK_TAG).build();

        /**
         * Builds a TalkTag uri for an id
         * @param id Id of the TalkTag
         * @return The uri corresponding with the TalkTag to query it through the ContentProvider
         */
        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    /**
     * The RoomEntry class contains static data members that are used for querying the database and ContentProvider for Rooms
     * @see be.ana.nmct.multimania.model.Room
     */
    public static final class RoomEntry implements BaseColumns{
        /**
         * The table name for the SQLite database
         */
        public static final String TABLE_NAME   = "room";

        public static final String ROOM_ID      = "room_id";
        public static final String NAME         = "name";
        /**
         * Used for inner joins with Talks
         */
        public static final String ROOM_NAME    = TalkEntry.ROOM_NAME;

        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_ROOM;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_ROOM;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROOM).build();

        /**
         * Builds a Room uri for an id
         * @param id Id of the Room
         * @return The uri corresponding with the Room to query it through the ContentProvider
         */
        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    /**
     * The SpeakerEntry class contains static data members that are used for querying the database and ContentProvider for Speakers
     * @see be.ana.nmct.multimania.model.Speaker
     */
    public static final class SpeakerEntry implements BaseColumns{
        /**
         * The table name for the SQLite database
         */
        public static final String TABLE_NAME   = "speaker";

        public static final String NAME         = "name";

        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_SPEAKER;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_SPEAKER;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SPEAKER).build();

        /**
         * Builds a Speaker uri for an id
         * @param id Id of the Speaker
         * @return The uri corresponding with the Speaker to query it through the ContentProvider
         */
        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
        /**
         * Builds a special uri to query all the Speakers for a certain Talk
         * @param talkId Id of the Talk
         * @return The uri corresponding with the query to select the speakers of a Talk
         */
        public static Uri builtGetSpeakersByTalkIdUri(long talkId){
            return ContentUris.appendId(CONTENT_URI.buildUpon().appendPath(PATH_TALK),talkId).build();
        }
    }

    /**
     * The TalkSpeakerEntry class contains static data members that are used for querying the database and ContentProvider for TalkSpeakers
     * @see be.ana.nmct.multimania.model.TalkSpeaker
     */
    public static final class TalkSpeakerEntry implements BaseColumns{
        /**
         * The table name for the SQLite database
         */
        public static final String TABLE_NAME   = "talk_speaker";

        public static final String TALK_ID      = "talk_id";
        public static final String SPEAKER_ID   ="speaker_id";

        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_TALK_SPEAKER;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_TALK_SPEAKER;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TALK_SPEAKER).build();

        /**
         * Builds a TalkSpeaker uri for an id
         * @param id Id of the TalkSpeaker
         * @return The uri corresponding with the TalkSpeaker to query it through the ContentProvider
         */
        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    /**
     * The TagEntry class contains static data members that are used for querying the database and ContentProvider for Tags
     * @see be.ana.nmct.multimania.model.Tag
     */
    public static final class TagEntry implements  BaseColumns{
        /**
         * The table name for the SQLite database
         */
        public static final String TABLE_NAME   = "tag";

        public static final String NAME         = "name";

        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_TAG;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_TAG;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TAG).build();

        /**
         * Builds a Tag uri for an id
         * @param id Id of the Tag
         * @return The uri corresponding with the Tag to query it through the ContentProvider
         */
        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        /**
         * Builds a special uri to query all the Tags for a certain Talk
         * @param talkId Id of the Talk
         * @return The uri corresponding with the query to select the Tags of a Talk
         */
        public static Uri builtGetTagsByTalkIdUri(long talkId) {
            return ContentUris.appendId(CONTENT_URI.buildUpon().appendPath(PATH_TALK),talkId).build();
        }
    }
}
