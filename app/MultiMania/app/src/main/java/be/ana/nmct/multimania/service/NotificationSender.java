package be.ana.nmct.multimania.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.ui.TalkActivity;

/**
 * Created by Axel on 18/11/2014.
 */
public class NotificationSender {

    private static final String TAG = NotificationSender.class.getSimpleName();

    public final static int NOTIFICATION_ID = 12345;
    public final static String NOTIF_TALKID = "notification_talkid";

    private static Context sContext;

    private static Talk testTalk;

    static{
        Calendar start = Calendar.getInstance();
        start.add(Calendar.SECOND, 10);
        Date from = start.getTime();
        start.add(Calendar.HOUR, 1);
        Date until = start.getTime();
        testTalk = new Talk(1, "Batman talks about saving Gotham City", from, until, "The Dark Knight returns to tell us about his favorite moments...", 1, false);
    }

    public NotificationSender(Context context) {
        this.sContext = context;
        setAlarmForTalk(testTalk);
    }

    public void setAlarmForTalkList(List<Talk> talks){
        for(Talk talk : talks){
            setAlarmForTalk(talk);
        }
    }

    public void setAlarmForTalk(Talk talk){

        DateTime from = new DateTime(talk.from);
        Calendar alarmDate = Calendar.getInstance();
        alarmDate.setTimeInMillis(System.currentTimeMillis());
        alarmDate.set(from.getYear(), from.getMonthOfYear(), from.getHourOfDay(), from.getMinuteOfDay(), 0);

        Intent intent = new Intent(sContext, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(sContext, (int)talk.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)sContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmDate.getTimeInMillis(), pendingIntent);
    }

    public void cancelAlarmForTalkList(List<Talk> talks){
        for(Talk talk : talks){
            cancelAlarmForTalk(talk);
        }
    }

    public void cancelAlarmForTalk(Talk talk){
        PendingIntent pendingIntent = PendingIntent.getBroadcast(sContext, (int)talk.id, null, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)sContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void sendNotification(Context context, Talk talk){

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(talk.title)
                        .setContentText("A keynote is about to begin!")
                        .setSound(getNotificationSoundUri())
                        .setLights(Color.parseColor("#FFFFFF"), 1000, 5000);


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
