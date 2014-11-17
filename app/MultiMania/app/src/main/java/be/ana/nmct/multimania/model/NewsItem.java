package be.ana.nmct.multimania.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Axel on 22/10/2014.
 */
public class NewsItem {

    public int id;
    public String title;
    @SerializedName("img")
    public String image;
    @SerializedName("short_description")
    public String shortDescription;
    @SerializedName("long_description")
    public String longDescription;
    public int importance;
    public int order;


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
}
