package be.ana.nmct.multimania.service;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import be.ana.nmct.multimania.data.ApiService;
import be.ana.nmct.multimania.data.DbHelper;
import be.ana.nmct.multimania.data.GsonLoader;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.data.MultimaniaProvider;
import be.ana.nmct.multimania.model.NewsItem;

/**
 * Created by Astrid on 16/11/2014.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mContentResolver;
    Context mContext;

    public SyncAdapter(Context context, boolean autoInitialize){
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        List<NewsItem> news = new GsonLoader<NewsItem>(mContext,"news",new TypeToken<List<NewsItem>>(){}).loadInBackground();
        try{
            SyncUtils utils = new SyncUtils(getContext());
            utils.syncNews(provider, news);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
