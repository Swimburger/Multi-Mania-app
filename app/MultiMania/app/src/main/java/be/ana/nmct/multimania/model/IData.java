package be.ana.nmct.multimania.model;

import android.content.ContentValues;
import android.net.Uri;

/**
 * Created by Niels on 18/11/2014.
 */
public interface IData {
    public long getId();
    public String getPathSegment();
    public ContentValues getContentValues();
    public Uri getContentUri();
    public Uri getContentUriWithId();
    public String getTableName();
}
