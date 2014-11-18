package be.ana.nmct.multimania.model;

import android.content.ContentValues;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import be.ana.nmct.multimania.data.MultimaniaContract;

/**
 * Created by Axel on 22/10/2014.
 */
public class NewsItem implements IData {
    public static final String SEGMENT = "news";
    public long id;
    public String title;
    @SerializedName("img")
    public String image;
    @SerializedName("short_description")
    public String shortDescription;
    @SerializedName("long_description")
    public String longDescription;
    public int importance;
    public int order;

    public NewsItem(){};
    public NewsItem(int id, String title, String img, String shortDescription, String longDescription, int importance, int order) {
        this.id=id;
        this.title=title;
        this.image=img;
        this.shortDescription=shortDescription;
        this.longDescription=longDescription;
        this.importance=importance;
        this.order=order;
    }

    @Override
    public boolean equals(Object o) {
        NewsItem n = (NewsItem)o;
        if(this.id == n.id && this.title == n.title && this.image == n.image && this.importance == this.importance && this.shortDescription == n.shortDescription && this.longDescription == n.longDescription && this.order == n.order){
            return true;
        } else{
            return  false;
        }
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
        values.put(MultimaniaContract.NewsItemEntry._ID,id);
        values.put(MultimaniaContract.NewsItemEntry.TITLE,title);
        values.put(MultimaniaContract.NewsItemEntry.IMAGE,image);
        values.put(MultimaniaContract.NewsItemEntry.SHORT_DESCRIPTION,shortDescription);
        values.put(MultimaniaContract.NewsItemEntry.LONG_DESCRIPTION,longDescription);
        values.put(MultimaniaContract.NewsItemEntry.IMPORTANCE,importance);
        values.put(MultimaniaContract.NewsItemEntry.ORDER,order);
        return values;
    }

    @Override
    public Uri getContentUri() {
        return MultimaniaContract.NewsItemEntry.CONTENT_URI;
    }

    @Override
    public Uri getContentUriWithId() {
        return MultimaniaContract.NewsItemEntry.buildItemUri(id);
    }

    @Override
    public String getTableName() {
        return MultimaniaContract.NewsItemEntry.TABLE_NAME;
    }
}
