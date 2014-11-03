package be.ana.nmct.multimania;

import android.app.Application;
import android.content.ContentValues;
import android.test.ApplicationTestCase;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import be.ana.nmct.multimania.data.DbHelper;
import be.ana.nmct.multimania.data.GsonLoader;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.model.NewsItem;
import be.ana.nmct.multimania.model.Room;
import be.ana.nmct.multimania.model.Speaker;
import be.ana.nmct.multimania.model.Tag;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.model.TalkSpeaker;
import be.ana.nmct.multimania.model.TalkTag;

/**
 * Created by Niels on 3/11/2014.
 */
public class Import extends ApplicationTestCase<Application> {
    public Import() {
        super(Application.class);
    }

    public void testDeleteDb(){
        mContext.deleteDatabase(DbHelper.DATABASE_NAME);
    }

    public void testImport(){

        List<NewsItem> news = new GsonLoader<NewsItem>(mContext,"news",new TypeToken<List<NewsItem>>(){}).loadInBackground();
        List<ContentValues> newsValues = new ArrayList<ContentValues>();

        List<Tag> tags = new GsonLoader<Tag>(mContext,"tags",new TypeToken<List<Tag>>(){}).loadInBackground();
        List<ContentValues> tagValues = new ArrayList<ContentValues>();

        List<Room> rooms = new GsonLoader<Room>(mContext,"rooms",new TypeToken<List<Room>>(){}).loadInBackground();
        List<ContentValues> roomValues = new ArrayList<ContentValues>();

        List<Speaker> speakers = new GsonLoader<Speaker>(mContext,"speakers",new TypeToken<List<Speaker>>(){}).loadInBackground();
        List<ContentValues> speakerValues = new ArrayList<ContentValues>();

        List<Talk> talks = new GsonLoader<Talk>(mContext,"talks",new TypeToken<List<Talk>>(){}).loadInBackground();
        List<ContentValues> talkValues = new ArrayList<ContentValues>();

        List<TalkTag> talktags = new GsonLoader<TalkTag>(mContext,"talk_tags",new TypeToken<List<TalkTag>>(){}).loadInBackground();
        List<ContentValues> talkTagValues = new ArrayList<ContentValues>();

        List<TalkSpeaker> talkspeakers = new GsonLoader<TalkSpeaker>(mContext,"talk_speakers",new  TypeToken<List<TalkSpeaker>>(){}).loadInBackground();
        List<ContentValues> talkSpeakerValues = new ArrayList<ContentValues>();

        for(NewsItem newsItem:news){
            newsValues.add(DbHelper.getContentValues(newsItem));
        }

        for(Tag tag:tags){
            tagValues.add(DbHelper.getContentValues(tag));
        }

        for(Room room:rooms){
            roomValues.add(DbHelper.getContentValues(room));
        }

        for(Speaker speaker:speakers){
            speakerValues.add(DbHelper.getContentValues(speaker));
        }

        for(Talk talk:talks){
            talkValues.add(DbHelper.getContentValues(talk));
        }

        for(TalkTag talkTag:talktags){
            talkTagValues.add(DbHelper.getContentValues(talkTag));
        }

        for(TalkSpeaker talkSpeaker:talkspeakers){
            talkSpeakerValues.add(DbHelper.getContentValues(talkSpeaker));
        }

        mContext.getContentResolver().bulkInsert(MultimaniaContract.NewsItemEntry.CONTENT_URI,
                newsValues.toArray(new ContentValues[newsValues.size()])
        );

        mContext.getContentResolver().bulkInsert(MultimaniaContract.TagEntry.CONTENT_URI,
                tagValues.toArray(new ContentValues[tagValues.size()])
                );

        mContext.getContentResolver().bulkInsert(MultimaniaContract.RoomEntry.CONTENT_URI,
                roomValues.toArray(new ContentValues[roomValues.size()])
        );

        mContext.getContentResolver().bulkInsert(MultimaniaContract.SpeakerEntry.CONTENT_URI,
                speakerValues.toArray(new ContentValues[speakerValues.size()])
        );

        mContext.getContentResolver().bulkInsert(MultimaniaContract.TalkEntry.CONTENT_URI,
                talkValues.toArray(new ContentValues[talkValues.size()])
        );

        mContext.getContentResolver().bulkInsert(MultimaniaContract.TalkTagEntry.CONTENT_URI,
                talkTagValues.toArray(new ContentValues[talkTagValues.size()])
        );

        mContext.getContentResolver().bulkInsert(MultimaniaContract.TalkSpeakerEntry.CONTENT_URI,
                talkSpeakerValues.toArray(new ContentValues[talkSpeakerValues.size()])
        );

    }
}
