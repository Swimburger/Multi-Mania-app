package be.ana.nmct.multimania.service;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.data.MultimaniaProvider;
import be.ana.nmct.multimania.model.NewsItem;

/**
 * Created by Astrid on 16/11/2014.
 */
public class SyncUtils {

    private final String TAG = this.getClass().getSimpleName();

    private final String mServer;
    private final Context mContext;



    public SyncUtils(Context context){
        mContext = context;
        try{
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            mServer = bundle.getString("be.ana.nmct.multimania.webservice.datasync");

        }catch(PackageManager.NameNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public void syncNews(ContentProviderClient provider, List<NewsItem> newsitems)throws Exception{

        for (int i = 0; i < newsitems.size(); i++){

            String id = String.valueOf(newsitems.get(i).id);
            Uri uri = MultimaniaContract.NewsItemEntry.CONTENT_URI.buildUpon().appendQueryParameter(MultimaniaContract.NewsItemEntry.NEWSITEM_ID, id).build();
            ContentResolver cr = mContext.getContentResolver();
            Cursor cursor = provider.query(uri,new String[]{ MultimaniaContract.NewsItemEntry.NEWSITEM_ID}, null, null, null);
            cursor.moveToFirst();
            String c = DatabaseUtils.dumpCursorToString(cursor);
            ContentValues values = new ContentValues();
            Log.d(TAG, c);
            Log.d(TAG, newsitems.get(i).toString());

            if(!newsitems.get(i).equals(cursor)){
                values.remove(MultimaniaContract.NewsItemEntry.NEWSITEM_ID);
                provider.update(uri,values, null, null);
            }

            /*
                if(!newsitems.get(i).equals(cursor))            //equals methode schrijven in model, cursor moet omgevormd worden naar NewsItem object
                    //update local db
                else
                   //skip
             */

        }

    }

}
