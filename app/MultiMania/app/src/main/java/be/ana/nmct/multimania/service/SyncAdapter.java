package be.ana.nmct.multimania.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import be.ana.nmct.multimania.data.GsonLoader;
import be.ana.nmct.multimania.model.NewsItem;

/**
 * Created by Astrid on 16/11/2014.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public SyncAdapter(Context context, boolean autoInitialize){
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try{
            SyncUtils utils = new SyncUtils(getContext());
            utils.syncNews(provider);
            utils.syncTalks(provider);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
