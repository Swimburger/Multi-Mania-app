package be.ana.nmct.multimania.model;

import android.content.ContentValues;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import be.ana.nmct.multimania.data.MultimaniaContract;

/**
 * Created by Niels on 28/10/2014.
 */
public class TalkSpeaker implements IData {
    public static final String SEGMENT = "talk_speakers";
    @SerializedName("talk_id")
    public int talkId;
    @SerializedName("speaker_id")
    public int speakerId;

    public TalkSpeaker(){}

    public TalkSpeaker(int talkId, int speakerId) {
        this.talkId = talkId;
        this.speakerId = speakerId;
    }

    @Override
    public long getId() {
        return -1;
    }

    @Override
    public String getPathSegment() {
        return SEGMENT;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MultimaniaContract.TalkSpeakerEntry.TALK_ID,talkId);
        values.put(MultimaniaContract.TalkSpeakerEntry.SPEAKER_ID,speakerId);
        return values;
    }

    @Override
    public Uri getContentUri() {
        return MultimaniaContract.TalkSpeakerEntry.CONTENT_URI;
    }

    @Override
    public Uri getContentUriWithId() {
        return null;
    }

    @Override
    public String getTableName() {
        return MultimaniaContract.TalkSpeakerEntry.TABLE_NAME;
    }
}
