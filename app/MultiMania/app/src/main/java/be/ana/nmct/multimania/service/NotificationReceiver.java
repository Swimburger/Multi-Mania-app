package be.ana.nmct.multimania.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.ui.SettingsFragment;
import be.ana.nmct.multimania.utils.SettingsUtil;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.ScheduleTalkVm;

/**
 * Created by Axel on 19/11/2014.
 */

/**
 * This class receives PendingIntents to send a notification to the user
 */
public class NotificationReceiver extends BroadcastReceiver {

    /**
     * Handles the sending of a notification if certain conditions are met
     * (user enabled notifications in settings and the Talk is a favorite of the user)
     * @param context The context (e.g.: an Activity)
     * @param intent The PendingIntent received
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        SettingsUtil util = new SettingsUtil(context, SettingsFragment.PREFERENCE_NAME);
        boolean notifyMe = util.getBooleanPreference(SettingsFragment.PREFERENCE_NOTIFY, true);

        if(notifyMe){
            long talkId = intent.getLongExtra(NotificationSender.NOTIF_TALKID, 1);
            Uri uri = MultimaniaContract.TalkEntry.buildItemUri(talkId);
            Talk talk = Utility.getTalkFromUri(context, uri);
            ScheduleTalkVm vm = Utility.convertTalkToScheduleTalkVm(talk);

            if(talk.isFavorite && System.currentTimeMillis() < Utility.getDateInMillis(talk.from)){
                NotificationSender.sendNotification(context, vm);
            }
        }
    }
}
