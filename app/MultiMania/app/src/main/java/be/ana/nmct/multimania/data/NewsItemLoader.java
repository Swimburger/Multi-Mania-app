package be.ana.nmct.multimania.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Astrid on 28/10/2014.
 */
public class NewsItemLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mData;

    public NewsItemLoader(Context context){
        super(context);
    }

    /**
     * Loads the newsitems in a background thread
     */
    @Override
    public Cursor loadInBackground() {
        DbHelper helper = DbHelper.getInstance(getContext());
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.query(
                MultimaniaContract.NewsItemEntry.TABLE_NAME,
                new String[]{MultimaniaContract.NewsItemEntry._ID,
                            MultimaniaContract.NewsItemEntry.TITLE,
                            MultimaniaContract.NewsItemEntry.IMAGE,
                            MultimaniaContract.NewsItemEntry.SHORT_DESCRIPTION,
                            MultimaniaContract.NewsItemEntry.LONG_DESCRIPTION,
                            MultimaniaContract.NewsItemEntry.IMPORTANCE,
                            MultimaniaContract.NewsItemEntry.ORDER},
                null,
                null,
                null,
                null,
                MultimaniaContract.NewsItemEntry.TITLE + " ASC");

        cursor.getCount();
        return cursor;
    }

    @Override
    public void deliverResult(Cursor data) {
        if(isReset()){
            if(data!=null){
                data.close();
            }
            return;
        }
        Cursor oldData = mData;
        mData = data;

        if(isStarted()){
            super.deliverResult(data);
        }

        if(oldData != null && oldData != data && !oldData.isClosed()){
            oldData.close();
        }
    }

    /**
     * Starts loading the newsitems
    */
    @Override
    protected void onStartLoading() {
        if(mData != null){
            deliverResult(mData);
        }

        if(takeContentChanged() || mData == null){
            forceLoad();
        }
    }

    /**
     * Stop loading
     */
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor data) {
        if(data != null && !data.isClosed()){
            data.close();
        }
    }

    /**
     * Reset loading off the newsitems
     */
    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if(mData != null && !mData.isClosed()){
            mData.close();
        }

        mData = null;
    }
}
