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

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.ui.MainActivity;
import be.ana.nmct.multimania.ui.TalkActivity;

/**
 * Created by Axel on 18/11/2014.
 */
public class NotificationSender {

    private final String TAG = this.getClass().getSimpleName();

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
        //setAlarmForTalk(testTalk);
        sendNotification(testTalk);
    }

    public void setAlarmForTalk(Talk talk){
        //Actual code
        /*DateTime from = new DateTime(talk.from);

        Calendar alarmDate = Calendar.getInstance();
        alarmDate.setTimeInMillis(System.currentTimeMillis());
        alarmDate.set(from.getYear(), Calendar.JANUARY, from.getHourOfDay(), from.getMinuteOfDay(), 0);     //replace January with correct month
        */

        //testcode
        DateTime from = new DateTime();
        from.plusMillis(10);
        Calendar alarmDate = Calendar.getInstance();
        alarmDate.setTimeInMillis(System.currentTimeMillis());



        AlarmManager alarmManager = (AlarmManager) sContext.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(sContext, MainActivity.class);
        intent.putExtra("MyMessage","HERE I AM PASSING THEPERTICULAR MESSAGE WHICH SHOULD BE SHOW ON RECEIVER OF ALARM");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(sContext, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmDate.getTimeInMillis(),pendingIntent);

    }

    public static void sendNotification(Talk talk){

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(sContext)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(talk.title)
                        .setContentText("One of your favorited events is about to begin!")
                        .setSound(getNotificationSoundUri())
                        .setLights(Color.parseColor("#FFFFFF"), 1000, 5000);


        Intent targetIntent = new Intent(sContext, MainActivity.class);
        targetIntent.putExtra(NOTIF_TALKID, talk.id);
        PendingIntent contentIntent = PendingIntent.getActivity(sContext, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager nManager = (NotificationManager) sContext.getSystemService(sContext.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }

    private static Uri getNotificationSoundUri(){
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

}
