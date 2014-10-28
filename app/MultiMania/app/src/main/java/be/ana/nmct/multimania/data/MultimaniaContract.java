package be.ana.nmct.multimania.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Astrid on 28/10/2014.
 */
public class MultimaniaContract {


    public static final String PATH_MULTIMANIA = "multimania";
    public static final String PATH_NEWSITEM = "newsitem";
    public static final String PATH_TALK= "talk";
    public static final String PATH_ROOM = "room";
    public static final String PATH_USER = "user";
    public static final String PATH_TAG = "tag";

    //inner classes
    public static final class NewsItemEntry implements BaseColumns{
        //table name
        public static final String TABLE_NAME = "newsitem";

        public static final String NEWSITEM_ID = "newsitem_id";
        public static final String TITLE = "title";
        public static final String IMAGE = "image";
        public static final String SHORT_DESCRIPTION = "short_description";
        public static final String DESCRIPTION = "description";
        public static final String IMPORTANCE  = "importance";
        public static final String ORDER  = "order";
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
    }

    public static final class RoomEntry implements BaseColumns{
        public static final String TABLE_NAME = "room";

        public static final String ROOM_ID = "room_id";
        public static final String NAME = "name";
    }

    public static final class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "user";

        public static final String USER_ID = "user_id";
    }

    public static final class TagEntry implements  BaseColumns{
        public static final String TABLE_NAME = "tag";

        public static final String TAG_ID = "tag_id";
        public static final String NAME = "name";
    }
}
