package be.ana.nmct.multimania;

import android.app.Application;
import android.content.ContentResolver;
import android.os.RemoteException;
import android.test.ApplicationTestCase;

import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import be.ana.nmct.multimania.data.GsonLoader;
import be.ana.nmct.multimania.model.IData;
import be.ana.nmct.multimania.model.NewsItem;
import be.ana.nmct.multimania.model.Room;
import be.ana.nmct.multimania.model.Speaker;
import be.ana.nmct.multimania.model.Tag;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.model.TalkSpeaker;
import be.ana.nmct.multimania.model.TalkTag;
import be.ana.nmct.multimania.utils.SyncUtils;

/**
 * Created by Axel on 20/11/2014.
 */

/**
 * This class imports all Talks and sets their Talk.from to 10 minutes from the current time.
 * This way we can play around with the notifications at the current time.
 */
public class NotificationImport extends ApplicationTestCase<Application> {

    private final static int NOTIFY_INTERVAL = 1;

    public NotificationImport() {
        super(Application.class);
    }

    /**
     * This method gets all models from the online API but sets the talk.from & talk.to to the current time + 10 minutes
     * @throws RemoteException An exception raised when there was a problem connecting to the server
     */
    public void testImportNotificationTalks() throws RemoteException {
        List<IData> models = new ArrayList<IData>();

        List<NewsItem> news = new GsonLoader<NewsItem>(mContext,"news",new TypeToken<List<NewsItem>>(){}).loadInBackground();
        List<Tag> tags = new GsonLoader<Tag>(mContext,"tags",new TypeToken<List<Tag>>(){}).loadInBackground();
        List<Room> rooms = new GsonLoader<Room>(mContext,"rooms",new TypeToken<List<Room>>(){}).loadInBackground();
        List<Speaker> speakers = new GsonLoader<Speaker>(mContext,"speakers",new TypeToken<List<Speaker>>(){}).loadInBackground();
        List<Talk> talks = new GsonLoader<Talk>(mContext,"talks",new TypeToken<List<Talk>>(){}).loadInBackground();
        List<TalkTag> talktags = new GsonLoader<TalkTag>(mContext,"talk_tags",new TypeToken<List<TalkTag>>(){}).loadInBackground();
        List<TalkSpeaker> talkspeakers = new GsonLoader<TalkSpeaker>(mContext,"talk_speakers",new  TypeToken<List<TalkSpeaker>>(){}).loadInBackground();

        int i = 0;
        for (Talk talk : talks){
            DateTime from = new DateTime();
            talk.from = from.plusMinutes((NOTIFY_INTERVAL + i) + 10).toDate();
            talk.to = from.plusMinutes((NOTIFY_INTERVAL + (i + NOTIFY_INTERVAL)) + 10).toDate();    //10 refers to 10 minutes from now
            i++;
        }

        models.addAll(news);
        models.addAll(tags);
        models.addAll(rooms);
        models.addAll(speakers);
        models.addAll(talks);
        models.addAll(talktags);
        models.addAll(talkspeakers);

        ContentResolver resolver = mContext.getContentResolver();
        SyncUtils.syncData(resolver,models);
    }

}
