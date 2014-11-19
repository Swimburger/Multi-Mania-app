package be.ana.nmct.multimania.service;

import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.os.RemoteException;
import android.provider.BaseColumns;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import be.ana.nmct.multimania.data.GsonLoader;
import be.ana.nmct.multimania.model.IData;
import be.ana.nmct.multimania.model.NewsItem;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.model.TalkSpeaker;

/**
 * Created by Astrid on 16/11/2014.
 */
public class SyncUtils {
    private static final String TAG = SyncUtils.class.getSimpleName();
    private final Context mContext;

    public SyncUtils(Context context){
        mContext = context;
    }

    public void syncNews(ContentProviderClient provider)throws Exception{
        List<IData> news = new ArrayList<IData>(
                new GsonLoader<NewsItem>(mContext,NewsItem.SEGMENT,new TypeToken<List<NewsItem>>(){}).loadInBackground());
        syncData(provider,news);
    }

    //TODO:other data models, only talks should be different

    //TODO:sync to server for talks, first check the last update (not implemented yet)
    // and compare it to the last update online GET /users/:id/lastupdated.
    // If droid has a more recent update, sync to server, else sync to device
    //TODO:sync to Android for talks, check if user has chosen a email address, if so use GET /users/:id/talks, else use GET /talks

    public void syncTalks(ContentProviderClient provider)throws Exception{
        List<IData> talks = new ArrayList<IData>(
                new GsonLoader<Talk>(mContext, TalkSpeaker.SEGMENT, new TypeToken<List<Talk>>(){}).loadInBackground());
        syncData(provider , talks);
    }

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
