package be.ana.nmct.multimania;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.List;

import be.ana.nmct.multimania.data.GsonLoader;
import be.ana.nmct.multimania.data.PostUserTask;
import be.ana.nmct.multimania.model.Room;
import be.ana.nmct.multimania.model.Speaker;
import be.ana.nmct.multimania.model.Tag;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.model.TalkSpeaker;
import be.ana.nmct.multimania.model.TalkTag;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApiTest extends ApplicationTestCase<Application> {
    public static final String TESTUSERID="testuserid";

    public ApiTest() {
        super(Application.class);
    }

    public void testTags(){
       List<Tag> tags = new GsonLoader<Tag>(mContext,"tags").loadInBackground();
       assertNotNull(tags);
    }

    public void testRooms(){
        List<Room> tags = new GsonLoader<Room>(mContext,"rooms").loadInBackground();
        assertNotNull(tags);
    }

    public void testSpeakers(){
        List<Speaker> tags = new GsonLoader<Speaker>(mContext,"speakers").loadInBackground();
        assertNotNull(tags);
    }

    public void testTalks(){
        List<Talk> tags = new GsonLoader<Talk>(mContext,"talks").loadInBackground();
        assertNotNull(tags);
    }

    public void testTalkTags(){
        List<TalkTag> tags = new GsonLoader<TalkTag>(mContext,"talk_tags").loadInBackground();
        assertNotNull(tags);
    }

    public void testTalkSpeakers(){
        List<TalkSpeaker> tags = new GsonLoader<TalkSpeaker>(mContext,"talk_speakers").loadInBackground();
        assertNotNull(tags);
    }

    public void testPostUser(){
        PostUserTask task = new PostUserTask(TESTUSERID);
        String userId =task.executeSynchronously();
        assertEquals(TESTUSERID,userId);
    }
}
