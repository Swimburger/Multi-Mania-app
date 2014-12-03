package be.ana.nmct.multimania.model;

import android.content.ContentValues;
import android.net.Uri;

/**
 * Interface for more generic coding and testing
 * Created by Niels on 18/11/2014.
 */
public interface IData {
    /**
     * Gets the id of the data model if there is one
     * @return Returns the id of the data model if there is one, if there is none, -1 is returned
     */
    public long getId();

    /**
     * Gets the path segment used in the web api
     * @return Returns the path segment used in the web api
     */
    public String getPathSegment();

    /**
     * Gets the ContentValues from the data model
     * @return Returns the ContentValues from the data model
     */
    public ContentValues getContentValues();

    /**
     * Gets the content uri for the data model that is used for querying the ContentProvider
     * @return Returns the content uri for the data model that is used for querying the ContentProvider
     */
    public Uri getContentUri();
    
    /**
     * Gets the content uri with id for the data model that is used for querying the ContentProvider
     * @return Returns the content uri with id for the data model that is used for querying the ContentProvider, if there is no id, null is returned
     */
    public Uri getContentUriWithId();

    /**
     * Gets the table name for the corresponding data model
     * @return Returns the table name for the corresponding data model
     */
    public String getTableName();
}
