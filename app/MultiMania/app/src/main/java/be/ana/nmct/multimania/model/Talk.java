package be.ana.nmct.multimania.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Axel on 22/10/2014.
 */
public class Talk {

    public int id;
    public String title;
    public Date from;
    public Date to;
    @SerializedName("content")
    public String description;
    @SerializedName("room_id")
    public int roomId;
    public boolean isKeynote;
    @Expose
    public List<Tag> tags;

}
