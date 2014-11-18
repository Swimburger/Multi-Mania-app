package be.ana.nmct.multimania.model;

import android.content.ContentValues;
import android.net.Uri;

import be.ana.nmct.multimania.data.MultimaniaContract;

/**
 * Created by Axel on 22/10/2014.
 */
public class Room implements IData {
    public static final String SEGMENT = "rooms";
    public long id;
    public String name;

    public Room(){}

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
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
        values.put(MultimaniaContract.RoomEntry._ID,id);
        values.put(MultimaniaContract.RoomEntry.NAME,name);
        return values;
    }

    @Override
    public Uri getContentUri() {
        return MultimaniaContract.RoomEntry.CONTENT_URI;
    }

    @Override
    public Uri getContentUriWithId() {
        return MultimaniaContract.RoomEntry.buildItemUri(id);
    }

    @Override
    public String getTableName() {
        return MultimaniaContract.RoomEntry.TABLE_NAME;
    }
}
