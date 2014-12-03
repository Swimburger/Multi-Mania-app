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
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.ui.TalkActivity;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.ScheduleTalkVm;

/**
 * Created by Axel on 18/11/2014.
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

    public void setAlarmForTalkList(List<ScheduleTalkVm> talks){
        for(ScheduleTalkVm talk : talks){
            setAlarmForTalk(talk);
        }
    }

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

    public void cancelAlarmForTalkList(List<ScheduleTalkVm> talks){
        for(ScheduleTalkVm talk : talks){
            cancelAlarmForTalk(talk);
        }
    }

    public void cancelAlarmForTalk(ScheduleTalkVm talk){
        Intent intent = new Intent(sContext, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(sContext, (int)talk.id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager)sContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

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

    private static Uri getNotificationSoundUri(){
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

}
