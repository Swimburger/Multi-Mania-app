package be.ana.nmct.multimania.model;

import android.content.ContentValues;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import be.ana.nmct.multimania.data.MultimaniaContract;

/**
 * Created by Niels on 28/10/2014.
 */
public class TalkTag implements IData {
    public static final String SEGMENT = "talk_tags";
    @SerializedName("talk_id")
    public int talkId;
    @SerializedName("tag_id")
    public int tagId;

    public TalkTag(int talkId, int tagId) {
        this.talkId = talkId;
        this.tagId = tagId;
    }

    @Override
    public long getId() {
        return -1;
    }

    @Override
    public String getPathSegment() {
        return SEGMENT;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MultimaniaContract.TalkTagEntry.TALK_ID,talkId);
        values.put(MultimaniaContract.TalkTagEntry.TAG_ID,tagId);
        return values;
    }

    @Override
    public Uri getContentUri() {
        return MultimaniaContract.TalkTagEntry.CONTENT_URI;
    }

    @Override
    public Uri getContentUriWithId() {
        return null;
    }

    @Override
    public String getTableName() {
        return MultimaniaContract.TalkTagEntry.TABLE_NAME;
    }
}
