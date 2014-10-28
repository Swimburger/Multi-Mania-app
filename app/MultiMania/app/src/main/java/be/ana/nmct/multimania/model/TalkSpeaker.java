package be.ana.nmct.multimania.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Niels on 28/10/2014.
 */
public class TalkSpeaker {
    @SerializedName("talk_id")
    public int talkId;
    @SerializedName("speaker_id")
    public int speakerId;

    public TalkSpeaker(int talkId, int speakerId) {
        this.talkId = talkId;
        this.speakerId = speakerId;
    }
}
