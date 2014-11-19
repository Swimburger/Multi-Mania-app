package be.ana.nmct.multimania.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.model.Talk;

/**
 * Created by Axel on 19/11/2014.
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long talkId = intent.getLongExtra(NotificationSender.NOTIF_TALKID, 1);
        Uri uri = MultimaniaContract.TalkEntry.buildItemUri(talkId);
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);

        if(c.moveToFirst()){
            int idIndex = c.getColumnIndex(MultimaniaContract.TalkEntry._ID);
            int titleIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.TITLE);

            Talk talk = new Talk(c.getInt(idIndex), c.getString(titleIndex), null, null, "", 0, false);
            NotificationSender.sendNotification(context, talk);
        }
    }

}
