package be.ana.nmct.multimania.model;

import android.content.ContentValues;
import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.Utility;

/**
 * Created by Axel on 22/10/2014.
 */
public class Talk implements IData {
    public static final String SEGMENT = "talks";
    public long id;
    public String title;
    public Date from;
    public Date to;
    @SerializedName("content")
    public String description;
    @SerializedName("room_id")
    public int roomId;
    public boolean isKeynote;
    public boolean isFavorite;
    public long calEventId;
    @Expose
    public List<Tag> tags;

    public Talk(){}

    public Talk(int id, String title, Date from, Date to, String description, int roomId, boolean isKeynote) {
        this.id = id;
        this.title = title;
        this.from = from;
        this.to = to;
        this.description = description;
        this.roomId = roomId;
        this.isKeynote = isKeynote;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getPathSegment() {
        return SEGMENT;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MultimaniaContract.TalkEntry._ID,id);
        values.put(MultimaniaContract.TalkEntry.TITLE,title);
        values.put(MultimaniaContract.TalkEntry.DESCRIPTION,description);
        values.put(MultimaniaContract.TalkEntry.DATE_FROM, Utility.convertDateToString(from));
        values.put(MultimaniaContract.TalkEntry.DATE_UNTIL,Utility.convertDateToString(to));
        values.put(MultimaniaContract.TalkEntry.ROOM_ID,roomId);
        values.put(MultimaniaContract.TalkEntry.IS_KEYNOTE,isKeynote);
        values.put(MultimaniaContract.TalkEntry.IS_FAVORITE,isFavorite);
        return values;
    }

    @Override
    public String toString() {
        return "Talk{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", description='" + description + '\'' +
                ", roomId=" + roomId +
                ", isKeynote=" + isKeynote +
                ", isFavorite=" + isFavorite +
                ", calEventId=" + calEventId +
                ", tags=" + tags +
                '}';
    }

    @Override
    public Uri getContentUri() {
        return MultimaniaContract.TalkEntry.CONTENT_URI;
    }

    @Override
    public Uri getContentUriWithId() {
        return MultimaniaContract.TalkEntry.buildItemUri(id);
    }

    @Override
    public String getTableName() {
        return MultimaniaContract.TalkEntry.TABLE_NAME;
    }
}
