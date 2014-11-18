package be.ana.nmct.multimania.model;

import android.content.ContentValues;
import android.net.Uri;

import be.ana.nmct.multimania.data.MultimaniaContract;

/**
 * Created by Axel on 22/10/2014.
 */
public class Tag implements IData {
    public static final String SEGMENT = "tags";
    public long id;
    public String name;

    public Tag(){}
    public Tag(int id, String name) {
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
        values.put(MultimaniaContract.TagEntry._ID,id);
        values.put(MultimaniaContract.TagEntry.NAME,name);
        return values;
    }

    @Override
    public Uri getContentUri() {
        return MultimaniaContract.TagEntry.CONTENT_URI;
    }

    @Override
    public Uri getContentUriWithId() {
        return MultimaniaContract.TagEntry.buildItemUri(id);
    }

    @Override
    public String getTableName() {
        return MultimaniaContract.TagEntry.TABLE_NAME;
    }
}
