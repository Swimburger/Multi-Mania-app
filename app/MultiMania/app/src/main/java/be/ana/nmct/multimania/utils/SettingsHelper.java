package be.ana.nmct.multimania.utils;

import android.content.Context;
import android.net.Uri;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.model.Talk;
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

    public void settingsHandler(ScheduleTalkVm item) {

        SettingsUtil settings = new SettingsUtil(mContext, SettingsFragment.PREFERENCE_NAME);

        //handle alarms
        if(settings.getBooleanPreference(SettingsFragment.PREFERENCE_NOTIFY, true)){

            Uri uri = MultimaniaContract.TalkEntry.buildItemUri(item.id);
            Talk talk = Utility.getTalkFromUri(mContext, uri);
            NotificationSender notSender = new NotificationSender(mContext);

            if(item.isFavorite){
                notSender.setAlarmForTalk(talk);
            } else{
                notSender.cancelAlarmForTalk(talk);
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
