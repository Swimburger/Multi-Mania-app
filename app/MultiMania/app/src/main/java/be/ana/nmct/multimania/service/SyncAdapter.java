package be.ana.nmct.multimania.service;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import be.ana.nmct.multimania.ui.MainActivity;
import be.ana.nmct.multimania.utils.SettingsUtil;
import be.ana.nmct.multimania.utils.SyncUtils;

/**
 * Created by Astrid on 16/11/2014.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String SYNC_READY_BROADCAST = "be.ana.nmct.multimania.sync_ready";
    private static final String TAG = SyncAdapter.class.getSimpleName();
    public static final java.lang.String FORCE_DOWNLOAD_FAVORITES = "force_download_favorites";
    // Global variables
    // Define a variable to contain a content resolver instance
    private ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }
    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        SettingsUtil accountSettings = new SettingsUtil(getContext().getApplicationContext(), MainActivity.PREFERENCE_NAME);
        getContext().getApplicationInfo().loadDescription(getContext().getPackageManager());
        boolean forceDownloadFavorites = extras.getBoolean(FORCE_DOWNLOAD_FAVORITES,false);
        SyncUtils utils = new SyncUtils(getContext());
        String startSync = "Sync starts for ";
        String endSync = "Sync end for ";
        try{
            Log.d(TAG,startSync + "rooms");
            utils.syncRooms(provider);
            Log.d(TAG,endSync + "rooms");
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            Log.d(TAG,startSync + "tags");
            utils.syncTags(provider);
            Log.d(TAG, endSync + "tags");
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            Log.d(TAG,startSync + "speakers");
            utils.syncSpeakers(provider);
            Log.d(TAG, endSync + "speakers");
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            Log.d(TAG,startSync + "talks");
            String id = accountSettings.getStringPreference(MainActivity.PREFERENCE_ACCOUNT);
            Log.d(TAG,"User id:"+id);
            utils.syncTalks(provider,id, forceDownloadFavorites);
            Log.d(TAG, endSync + "talks");
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            Log.d(TAG,startSync + "talkTags");
            utils.syncTalkTags(provider);
            Log.d(TAG, endSync + "talkTags");
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            Log.d(TAG,startSync + "talkSpeakers");
            utils.syncTalkSpeakers(provider);
            Log.d(TAG, endSync + "talkSpeakers");
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            Log.d(TAG,startSync + "news");
            utils.syncNews(provider);
            Log.d(TAG, endSync + "news");
        }catch(Exception e){
            e.printStackTrace();
        }

        /*Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setAction(MainActivity.LOAD_SYNC);
        getContext().startActivity(intent);*/

        Intent intent = new Intent();
        intent.setAction(SYNC_READY_BROADCAST);
        //LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        getContext().sendBroadcast(intent);
        Log.d(TAG,"Sync ready in adapter");
    }
}
