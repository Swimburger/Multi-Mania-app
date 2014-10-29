package be.ana.nmct.multimania.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.List;

import be.ana.nmct.multimania.model.Talk;

/**
 * Created by Axel on 29/10/2014.
 */
public class GoogleCalUtil {

    private final static String TAG = GoogleCalUtil.class.getCanonicalName();


    //single insert via intent
    public static void saveTalkToGoogleCalendar(Context context, Talk talk){

        try {
            //set intent data to send to cal
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE, talk.title);
            intent.putExtra(CalendarContract.Events.DESCRIPTION, talk.description);
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, talk.roomId);

            context.startActivity(intent);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    public static void syncTalksToGoogleCalendar(List<Talk> talks){

    }

}
