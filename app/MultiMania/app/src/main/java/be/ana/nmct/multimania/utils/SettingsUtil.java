package be.ana.nmct.multimania.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Axel on 10/11/2014.
 */
public class SettingsUtil {

    private Context mContext;
    private String mPreferenceName;

    public SettingsUtil(Context context, String preferenceName) {
        this.mContext = context;
        this.mPreferenceName = preferenceName;
    }

    //boolean preferences
    public Boolean getBooleanPreference(String settingName){
        SharedPreferences prefs = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);
        return prefs.getBoolean(settingName, false);
    }

    public boolean setPreference(String settingName, boolean value){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE).edit();
        editor.putBoolean(settingName, value);
        return  editor.commit();
    }

    //long preferences
    public long getLongPreference(String settingName){
        SharedPreferences prefs = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);
        return prefs.getLong(settingName, 0);
    }

    public boolean setPreference(String settingName, long value){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE).edit();
        editor.putLong(settingName, value);
        return  editor.commit();
    }

    //string preferences
    public String getStringPreference(String settingName){
        SharedPreferences prefs = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);
        return prefs.getString(settingName, null);
    }

    public boolean setPreference(String settingName, String value){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE).edit();
        editor.putString(settingName, value);
        return  editor.commit();
    }

}
