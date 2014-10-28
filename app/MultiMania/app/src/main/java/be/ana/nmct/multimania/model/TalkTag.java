package be.ana.nmct.multimania.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Niels on 28/10/2014.
 */
public class TalkTag {
    @SerializedName("talk_id")
    public int talkId;
    @SerializedName("tag_id")
    public int tagId;

    public TalkTag(int talkId, int tagId) {
        this.talkId = talkId;
        this.tagId = tagId;
    }
}
