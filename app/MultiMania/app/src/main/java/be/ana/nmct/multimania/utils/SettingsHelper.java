package be.ana.nmct.multimania.utils;

import android.content.Context;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.service.NotificationSender;
import be.ana.nmct.multimania.ui.SettingsFragment;
import be.ana.nmct.multimania.vm.ScheduleTalkVm;

/**
 * Created by Axel on 26/11/2014.
 */
public class SettingsHelper {

    private Context mContext;
    private GoogleCalUtil mCalUtil;
    private String mCalendarName;

    public SettingsHelper(Context context) {
        this.mContext = context;
        this.mCalendarName = context.getString(R.string.calendar_name);
        this.mCalUtil = new GoogleCalUtil(context, mCalendarName);
    }

    /**
     * Helper method for handling talk behaviour (e.g. enable/disable notification for the talk)
     * @param item The talk for which the settings need to be changed
     */
    public void settingsHandler(ScheduleTalkVm item) {

        SettingsUtil settings = new SettingsUtil(mContext, SettingsFragment.PREFERENCE_NAME);

        //handle alarms
        if(settings.getBooleanPreference(SettingsFragment.PREFERENCE_NOTIFY, true)){
            NotificationSender notSender = new NotificationSender(mContext);

            if(item.isFavorite){
                notSender.setAlarmForTalk(item);
            } else{
                notSender.cancelAlarmForTalk(item);
            }
        }

        //handle calendar
        if(settings.getBooleanPreference(SettingsFragment.PREFERENCE_SYNC, false)){

            if(item.isFavorite){
                mCalUtil.addTalk(item);
            } else {
                mCalUtil.deleteTalk(item);
            }

        }

    }

}
