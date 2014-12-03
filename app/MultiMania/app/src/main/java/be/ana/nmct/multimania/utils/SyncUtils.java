package be.ana.nmct.multimania.utils;

import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.ana.nmct.multimania.data.ApiActions;
import be.ana.nmct.multimania.data.GsonLoader;
import be.ana.nmct.multimania.model.IData;
import be.ana.nmct.multimania.model.NewsItem;
import be.ana.nmct.multimania.model.Room;
import be.ana.nmct.multimania.model.Speaker;
import be.ana.nmct.multimania.model.Tag;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.model.TalkSpeaker;
import be.ana.nmct.multimania.model.TalkTag;

import static be.ana.nmct.multimania.data.MultimaniaContract.TalkEntry;

/**
 * A helper class that contains all syncing functionality
 * Created by Astrid on 16/11/2014.
 */
public class SyncUtils {
    private static final String TAG = SyncUtils.class.getSimpleName();
    private final Context mContext;

    public SyncUtils(Context context){
        mContext = context;
    }

    /**
     * Downloads and updates the local Rooms
     * @param provider A class that has the same query functionality as a ContentProvider
     * @throws Exception
     * @see be.ana.nmct.multimania.model.Room
     */
    public void syncRooms(ContentProviderClient provider)throws Exception{
        List<IData> news = new ArrayList<IData>(
                new GsonLoader<Room>(mContext, Room.SEGMENT,new TypeToken<List<Room>>(){}).loadInBackground());
        syncData(provider,news);
    }

    /**
     * Downloads and updates the local Tags
     * @param provider A class that has the same query functionality as a ContentProvider
     * @throws Exception
     * @see be.ana.nmct.multimania.model.Tag
     */
    public void syncTags(ContentProviderClient provider)throws Exception{
        List<IData> news = new ArrayList<IData>(
                new GsonLoader<Tag>(mContext, Tag.SEGMENT,new TypeToken<List<Tag>>(){}).loadInBackground());
        syncData(provider,news);
    }

    /**
     * Downloads and updates the local Speakers
     * @param provider A class that has the same query functionality as a ContentProvider
     * @throws Exception
     * @see be.ana.nmct.multimania.model.Speaker
     */
    public void syncSpeakers(ContentProviderClient provider)throws Exception{
        List<IData> news = new ArrayList<IData>(
                new GsonLoader<Speaker>(mContext, Speaker.SEGMENT,new TypeToken<List<Speaker>>(){}).loadInBackground());
        syncData(provider,news);
    }



    /**
     * sync to server for talks, first check the last update (not implemented yet)
     * and compare it to the last update online GET /users/:id/lastupdated.
     * If droid has a more recent update, sync to server, else sync to device
     * sync to Android for talks, check if user has chosen a email address, if so use GET /users/:id/talks, else use GET /talks
     * @param provider A class that has the same query functionality as a ContentProvider
     * @param userId The id/email of the user
     * @throws Exception
     */
    public void syncTalks(ContentProviderClient provider, String userId)throws Exception{
        List<IData> talks;
        if(userId==null){
            talks = new ArrayList<IData>(
                    new GsonLoader<Talk>(mContext, Talk.SEGMENT, new TypeToken<List<Talk>>(){}).loadInBackground());

            syncData(provider , talks);
        }else{
            Date date = Utility.convertStringToDate(ApiActions.getLastUpdated(mContext, userId).get());
            SettingsUtil util = new SettingsUtil(mContext,"sync");
            Date localDate = Utility.convertStringToDate(util.getStringPreference("lastUpdated",null));

            boolean onlineDateIsMoreRecent = localDate==null|| date.compareTo(localDate)>0;
            if(onlineDateIsMoreRecent){
                talks = new ArrayList<IData>(
                        new GsonLoader<Talk>(mContext, "users/" + userId + "/" + TalkSpeaker.SEGMENT, new TypeToken<List<Talk>>(){}).loadInBackground());
                syncData(provider , talks);
                util.setPreference("lastUpdated",Utility.convertDateToString(new Date()));
            }else{
                UploadFavorites(provider,userId);
            }
        }
    }

    /**
     * Uploads the favorite talks to the server
     * @param provider A class that has the same query functionality as a ContentProvider
     * @param userId The id/email of the user
     */
    private void UploadFavorites(ContentProviderClient provider, String userId) {
        try {
            Cursor cursor = provider.query(TalkEntry.CONTENT_URI, null, null, null, null);
            final int idIndex = cursor.getColumnIndex(TalkEntry._ID);
            final int isFavoriteIndex = cursor.getColumnIndex(TalkEntry.IS_FAVORITE);
            while(cursor.moveToNext()){
                long talkId = cursor.getLong(idIndex);
                boolean isFavorite = cursor.getInt(isFavoriteIndex) == 1;
                boolean success;
                if(isFavorite){
                      success = ApiActions.postFavoriteTalk(mContext, userId, talkId).get().equals("success");
                }else{
                    success = ApiActions.deleteFavoriteTalk(mContext, userId, talkId).get().equals("success");
                }
                Log.d(TAG,"TalkId:"+talkId+"| isFavorite:"+isFavorite+"| isSynced:"+success);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void syncTalkTags(ContentProviderClient provider)throws Exception{
        List<IData> news = new ArrayList<IData>(
                new GsonLoader<TalkTag>(mContext, TalkTag.SEGMENT,new TypeToken<List<TalkTag>>(){}).loadInBackground());
        syncData(provider,news);
    }

    public void syncTalkSpeakers(ContentProviderClient provider)throws Exception{
        List<IData> news = new ArrayList<IData>(
                new GsonLoader<TalkSpeaker>(mContext, TalkSpeaker.SEGMENT,new TypeToken<List<TalkSpeaker>>(){}).loadInBackground());
        syncData(provider,news);
    }

    public void syncNews(ContentProviderClient provider)throws Exception{
        List<IData> news = new ArrayList<IData>(
                new GsonLoader<NewsItem>(mContext,NewsItem.SEGMENT,new TypeToken<List<NewsItem>>(){}).loadInBackground());
        syncData(provider,news);
    }

    /**
     * Syncs a list of IData items with the ContentProvider
     * @param provider A class that has the same query functionality as a ContentProvider
     * @param models The data items that need syncing
     * @throws RemoteException
     * @see be.ana.nmct.multimania.model.IData
     */
    public static void syncData(ContentProviderClient provider,List<IData> models) throws RemoteException {
        for(IData model: models){
            boolean exists = false;
            if(model.getId()!=-1){
                exists = provider.query(model.getContentUriWithId(),null,null,null,null).getCount()>0;
            }
            if(exists){
                provider.update(model.getContentUri(),
                        model.getContentValues(),
                        BaseColumns._ID+"="+model.getId(),
                        null);
            }else{
                provider.insert(model.getContentUri(),
                        model.getContentValues());
            }
        }
    }

    /**
     * Syncs a list of IData items with the ContentProvider
     * @param provider A ContentProvider for querying the database in an API like fashion
     * @param models The data items that need syncing
     * @throws RemoteException
     * @see be.ana.nmct.multimania.model.IData
     */
    public static void syncData(ContentProvider provider,List<IData> models) throws RemoteException {
        for(IData model: models){
            boolean exists = false;
            if(model.getId()!=-1){
                exists = provider.query(model.getContentUriWithId(),null,null,null,null).getCount()>0;
            }
            if(exists){
                provider.update(model.getContentUri(),
                        model.getContentValues(),
                        BaseColumns._ID+"="+model.getId(),
                        null);
            }else{
                provider.insert(model.getContentUri(),
                        model.getContentValues());
            }
        }
    }

    /**
     * Syncs a list of IData items with the ContentProvider
     * @param resolver A class that has the same query functionality as a ContentProvider
     * @param models The data items that need syncing
     * @throws RemoteException
     * @see be.ana.nmct.multimania.model.IData
     */
    public static void syncData(ContentResolver resolver,List<IData> models) throws RemoteException {

        for(IData model: models){
            boolean exists = false;
            if(model.getId()!=-1){
                exists = resolver.query(model.getContentUriWithId(),null,null,null,null).getCount()>0;
            }
            if(exists){
                resolver.update(model.getContentUri(),
                        model.getContentValues(),
                        BaseColumns._ID + "=" + model.getId(),
                        null);
            }else{
                resolver.insert(model.getContentUri(),
                        model.getContentValues());
            }
        }

    }
}
