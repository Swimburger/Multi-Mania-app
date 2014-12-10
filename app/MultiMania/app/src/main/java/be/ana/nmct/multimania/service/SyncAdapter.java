package be.ana.nmct.multimania.service;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import be.ana.nmct.multimania.utils.SyncUtils;

/**
 * Created by Astrid on 16/11/2014.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String SYNC_READY_BROADCAST = "be.ana.nmct.multimania.sync_ready";
    private static final String TAG = SyncAdapter.class.getSimpleName();
    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

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
        SyncUtils utils = new SyncUtils(getContext());
        try{
            utils.syncRooms(provider);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            utils.syncTags(provider);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            utils.syncSpeakers(provider);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            utils.syncTalks(provider,null);//TODO:get the user id
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            utils.syncTalkTags(provider);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            utils.syncTalkSpeakers(provider);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            utils.syncNews(provider);
        }catch(Exception e){
            e.printStackTrace();
        }

        /*Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setAction(MainActivity.LOAD_SYNC);
        getContext().startActivity(intent);*/

        Intent intent = new Intent();
        intent.setAction(SYNC_READY_BROADCAST);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        getContext().sendBroadcast(intent);
        Log.d(TAG,"Sync ready in adapter");
    }
}
