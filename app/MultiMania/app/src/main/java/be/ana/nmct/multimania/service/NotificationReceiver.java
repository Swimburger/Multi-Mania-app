package be.ana.nmct.multimania.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.utils.Utility;

/**
 * Created by Axel on 19/11/2014.
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long talkId = intent.getLongExtra(NotificationSender.NOTIF_TALKID, 1);
        Uri uri = MultimaniaContract.TalkEntry.buildItemUri(talkId);
        Talk talk = Utility.getTalkFromUri(context, uri);
        NotificationSender.sendNotification(context, talk);
    }

}
