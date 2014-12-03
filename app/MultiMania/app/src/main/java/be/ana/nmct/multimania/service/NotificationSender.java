package be.ana.nmct.multimania.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.ui.TalkActivity;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.ScheduleTalkVm;

/**
 * Created by Axel on 18/11/2014.
 */

/**
 * This class handles sending the sending of notifications as well as planning when notifications
 * should be send to the user.
 */
public class NotificationSender {

    private static final String TAG = NotificationSender.class.getSimpleName();

    public final static int NOTIFICATION_ID = 12345;
    public final static String NOTIF_TALKID = "notification_talkid";
    public final static String INTENT_ALARMRECEIVER = "be.ana.nmct.multimania.ALARMRECEIVER";

    private static Context sContext;

    public NotificationSender(Context context) {
        this.sContext = context;
    }

    /**
     * Sets an alarm for a bunch of talks
     * @param talks The List<Talk> we need to set the alarms for
     */
    public void setAlarmForTalkList(List<ScheduleTalkVm> talks){
        for(ScheduleTalkVm talk : talks){
            setAlarmForTalk(talk);
        }
    }

    /**
     * Sets an alarm to trigger a notification for a Talk
     * @param talk The talk we need to set the alarm for
     */
    public void setAlarmForTalk(ScheduleTalkVm talk){
        Intent intent = new Intent(sContext, NotificationReceiver.class);
        intent.putExtra(NOTIF_TALKID, talk.id);
        intent.setAction(INTENT_ALARMRECEIVER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(sContext, (int)talk.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)sContext.getSystemService(Context.ALARM_SERVICE);
        long millis = Utility.getDateInMillis(talk.from) - 600000;      //600.000 = 10 minutes
        if(millis > 0){
            alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
        }

    }

    /**
     * Cancels a bunch of alarms for each Talk in talks
     * @param talks The List<Talk> we need to cancel the alarms for
     */
    public void cancelAlarmForTalkList(List<ScheduleTalkVm> talks){
        for(ScheduleTalkVm talk : talks){
            cancelAlarmForTalk(talk);
        }
    }

    /**
     * Cancels an alarm set to trigger a notification
     * @param talk The Talk we need to cancel the alarm for
     */
    public void cancelAlarmForTalk(ScheduleTalkVm talk){
        Intent intent = new Intent(sContext, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(sContext, (int)talk.id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager)sContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * Sends a notification containing a reminder for the user
     * @param context The context (e.g. an Activity)
     * @param talk The Talk we need to make a notification for
     */
    public static void sendNotification(Context context, ScheduleTalkVm talk){

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(talk.title)
                        .setContentText(context.getString(R.string.notification_text))
                        .setSound(getNotificationSoundUri())
                        .setLights(context.getResources().getColor(R.color.primaryColor), 1000, 5000);


        Intent targetIntent = new Intent(context, TalkActivity.class);
        targetIntent.setData(MultimaniaContract.TalkEntry.buildItemUri(talk.id));
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager nManager = (NotificationManager) context.getSystemService(sContext.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * Gets the default notification sound
     * @return A Uri indicating the default notification sound
     */
    private static Uri getNotificationSoundUri(){
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

}
