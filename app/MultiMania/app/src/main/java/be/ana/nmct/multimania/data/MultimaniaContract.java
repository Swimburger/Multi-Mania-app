package be.ana.nmct.multimania.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.util.Date;

import be.ana.nmct.multimania.utils.Utility;

/**
 * Created by Astrid on 28/10/2014.
 */
public class MultimaniaContract {

    public static final String CONTENT_AUTHORITY = "be.ana.nmct.multimania";
    public static final Uri    BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);
    private static final String CURSOR_DIR = "vnd.android.cursor.dir/";
    private static final String CURSOR_ITEM= "vnd.android.cursor.item/";

    public static final String PATH_NEWS        = "news";
    public static final String PATH_TALK        = "talks";
    public static final String PATH_ROOM        = "rooms";
    public static final String PATH_USER        = "users";
    public static final String PATH_TAG         = "tags";
    public static final String PATH_SPEAKER = "speakers";
    public static final String PATH_TALK_TAG    = "talk_tags";
    public static final String PATH_TALK_SPEAKER = "talk_speakers";

    public static String getDbDateString(Date date){
        return Utility.ConvertDateToString(date);
    }

    public static Date getDateFromDbString(String date) throws ParseException {
        return Utility.ConvertStringToDate(date);
    }

    //inner classes
    public static final class NewsItemEntry implements BaseColumns{
        //table name
        public static final String TABLE_NAME = "newsitem";

        public static final String NEWSITEM_ID = "newsitem_id";
        public static final String TITLE = "title";
        public static final String IMAGE = "image";
        public static final String SHORT_DESCRIPTION = "short_description";
        public static final String LONG_DESCRIPTION = "long_description";
        public static final String IMPORTANCE  = "importance";
        public static final String ORDER  = "news_order";


        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_NEWS;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_NEWS;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    public static final class TalkEntry implements BaseColumns{
        //table name
        public static final String TABLE_NAME = "talk";

        public static final String TALK_ID = "talk_id";
        public static final String TITLE = "title";
        public static final String DATE_FROM = "date_from";
        public static final String DATE_UNTIL = "date_until";
        public static final String DESCRIPTION = "description";
        public static final String ROOM_ID = "room_id";
        public static final String IS_KEYNOTE = "is_keynote";

        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_TALK;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_TALK;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TALK).build();

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    public static final class TalkTagEntry implements BaseColumns{
        public static final String TABLE_NAME = "talk_tag";
        public static final String TALK_TAG_ID = "talk_tag_id";
        public static final String TALK_ID = "talk_id";
        public static final String TAG_ID ="tag_id";

        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_TALK_TAG;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_TALK_TAG;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TALK_TAG).build();

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    public static final class RoomEntry implements BaseColumns{
        public static final String TABLE_NAME = "room";

        public static final String ROOM_ID = "room_id";
        public static final String NAME = "name";
        /**
         * for joins with talk
         */
        public static final String ROOM_NAME = "room_name";

        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_ROOM;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_ROOM;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROOM).build();

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    public static final class SpeakerEntry implements BaseColumns{
        public static final String TABLE_NAME = "speaker";

        public static final String SPEAKER_ID = "speaker_id";
        public static final String NAME = "name";

        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_SPEAKER;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_SPEAKER;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SPEAKER).build();

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    public static final class TalkSpeakerEntry implements BaseColumns{
        public static final String TABLE_NAME = "talk_speaker";
        public static final String TALK_TAG_ID = "talk_speaker_id";
        public static final String TALK_ID = "talk_id";
        public static final String SPEAKER_ID ="speaker_id";

        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_TALK_SPEAKER;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_TALK_SPEAKER;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TALK_SPEAKER).build();

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    public static final class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "user";

        public static final String USER_ID = "user_id";

        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_USER;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_USER;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    public static final class TagEntry implements  BaseColumns{
        public static final String TABLE_NAME = "tag";

        public static final String TAG_ID = "tag_id";
        public static final String NAME = "name";

        public static final String CONTENT_TYPE = CURSOR_DIR+CONTENT_AUTHORITY+"/"+PATH_TAG;
        public static final String CONTENT_ITEM_TYPE = CURSOR_ITEM+CONTENT_AUTHORITY+"/"+PATH_TAG;
        public static final Uri    CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TAG).build();

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }
}
