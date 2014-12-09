package be.ana.nmct.multimania.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Astrid on 28/10/2014.
 */
public class TalkLoader extends AsyncTaskLoader<Cursor>{

    private Cursor mData;
    private final int mRoomId;

    private TalkLoader(Context context, int roomId){
        super(context);
        this.mRoomId = roomId;
    }

    /**
     * Loads the talks in a background thread
     */
    @Override
    public Cursor loadInBackground() {

        DbHelper helper = DbHelper.getInstance(getContext());
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.query(
                MultimaniaContract.TalkEntry.TABLE_NAME,
                new String[]{MultimaniaContract.TalkEntry.TALK_ID,
                            MultimaniaContract.TalkEntry.TITLE,
                            MultimaniaContract.TalkEntry.DATE_FROM,
                            MultimaniaContract.TalkEntry.DATE_UNTIL,
                            MultimaniaContract.TalkEntry.DESCRIPTION,
                            MultimaniaContract.TalkEntry.ROOM_ID,
                            MultimaniaContract.TalkEntry.IS_KEYNOTE},
                "(" + MultimaniaContract.TalkEntry.ROOM_ID + " = ?)",
                new String[]{"" + mRoomId},
                null,
                null,
                MultimaniaContract.TalkEntry.TITLE + " ASC");

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
     * Starts loading the talks
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
     * Stop loading the talks
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
     * Reset loading off the talks
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
