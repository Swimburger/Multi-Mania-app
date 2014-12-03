package be.ana.nmct.multimania.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.ScheduleTalkVm;

/**
 * Created by Axel on 2/12/2014.
 */

/**
 * This class listens to the broadcast received if a device boots.
 * This is to re-enable all alarms set so the user will still receive notifications.
 */
public class BootListener extends BroadcastReceiver{

    /**
     * Sets all the alarms back after device boot
     * @param context The context (e.g.: an Activity)
     * @param intent The intent received from the Android system telling the device booted
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Cursor c = context.getContentResolver().query(
                MultimaniaContract.TalkEntry.CONTENT_URI,
                null,
                MultimaniaContract.TalkEntry.IS_FAVORITE + "=1 ",
                null,
                null
        );

        List<ScheduleTalkVm> talks = null;

        try {
            talks = buildItems(c);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(talks != null && talks.size() > 0){
            NotificationSender ns = new NotificationSender(context);
            ns.setAlarmForTalkList(talks);
        }
    }

    /**
     * Builds ScheduleTalkVm's from a Cursor
     * @param c The cursor to build items from
     * @return A List<ScheduleTalkVm> created from the Cursor
     * @throws ParseException Throws an exception if something went wrong parsing the java.util.Date from a String
     */
    private List<ScheduleTalkVm> buildItems(Cursor c) throws ParseException {
        List<ScheduleTalkVm> talkList = new ArrayList<ScheduleTalkVm>();

        final int dateFromIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.DATE_FROM);
        final int dateUntilIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.DATE_UNTIL);
        final int isFavoriteIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.IS_FAVORITE);
        final int titleIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.TITLE);
        final int roomIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.ROOM_NAME);
        final int idIndex = c.getColumnIndex(MultimaniaContract.TalkEntry._ID);
        final int calEventIdIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.CALEVENT_ID);
        final int descriptionIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.DESCRIPTION);

        if (c.moveToFirst()) {
            do {
                final ScheduleTalkVm vm = new ScheduleTalkVm();
                final long talkId = c.getLong(idIndex);

                vm.isFavorite = c.getInt(isFavoriteIndex) == 1;
                vm.title = c.getString(titleIndex);
                vm.room = c.getString(roomIndex);
                vm.id = talkId;
                vm.fromString = Utility.getTimeString(c.getString(dateFromIndex));
                vm.untilString = Utility.getTimeString(c.getString(dateUntilIndex));
                vm.calEventId = c.getLong(calEventIdIndex);
                vm.from = Utility.convertStringToDate(c.getString(dateFromIndex));
                vm.to = Utility.convertStringToDate(c.getString(dateFromIndex));
                vm.description = c.getString(descriptionIndex);

                talkList.add(vm);
            } while (c.moveToNext());
        }
        return talkList;
    }

}
