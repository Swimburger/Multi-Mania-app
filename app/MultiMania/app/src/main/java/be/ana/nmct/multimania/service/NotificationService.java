package be.ana.nmct.multimania.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.ui.MainActivity;
import be.ana.nmct.multimania.utils.Utility;

/**
 * Created by Axel on 16/11/2014.
 */
public class NotificationService extends Service {

    private final String TAG = this.getClass().getSimpleName();

    public final static int NOTIFICATION_ID = 12345;

    private static List<Date> sDates;

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        sDates = getDates();
        Talk talk = getTalkToNotify();

        if(talk != null){
            sendNotification(talk);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private List<Date> getDates() {
        //TODO: get dates of the event
        Date today = new Date();
        ArrayList list = new ArrayList<Date>();
        list.add(today);
        return list;
    }

    private Talk getTalkToNotify() {
        for (Date date : sDates){
            if(Utility.isSameDay(date, Calendar.getInstance().getTime())){
                //TODO: Date is the same, check for the hour
            }
        }
        return null;
    }

    private void sendNotification(Talk talk){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(talk.title)
                        .setContentText("Your favorited event is about to begin!");


        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }

}
