package be.ana.nmct.multimania.model;

import android.content.ContentValues;
import android.net.Uri;

import be.ana.nmct.multimania.data.MultimaniaContract;

/**
 * Created by Niels on 28/10/2014.
 */
public class Speaker implements IData {
    public static final String SEGMENT = "speakers";
    public int id;
    public String name;

    public Speaker(){}

    public Speaker(int id, String name) {
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
        values.put(MultimaniaContract.SpeakerEntry._ID,id);
        values.put(MultimaniaContract.SpeakerEntry.NAME,name);
        return values;
    }

    @Override
    public Uri getContentUri() {
        return MultimaniaContract.SpeakerEntry.CONTENT_URI;
    }

    @Override
    public Uri getContentUriWithId() {
        return MultimaniaContract.SpeakerEntry.buildItemUri(id);
    }

    @Override
    public String getTableName() {
        return MultimaniaContract.SpeakerEntry.TABLE_NAME;
    }
}
