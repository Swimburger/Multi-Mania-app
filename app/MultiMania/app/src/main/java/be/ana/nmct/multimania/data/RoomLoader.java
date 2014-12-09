package be.ana.nmct.multimania.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Astrid on 28/10/2014.
 */
public class RoomLoader extends AsyncTaskLoader<Cursor>{

    private Cursor mData;

    public RoomLoader(Context context){
        super(context);
    }

    /**
     * Loads the rooms in a background thread
     */
    @Override
    public Cursor loadInBackground() {
        DbHelper helper = DbHelper.getInstance(getContext());
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.query(
                MultimaniaContract.RoomEntry.TABLE_NAME,
                new String[]{MultimaniaContract.RoomEntry.ROOM_ID,
                        MultimaniaContract.RoomEntry.NAME},
                null,
                null,
                null,
                null,
                MultimaniaContract.RoomEntry.NAME + " ASC");

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
     * Starts loading the rooms
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
     * Reset loading off the rooms
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