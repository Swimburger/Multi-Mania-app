package be.ana.nmct.multimania.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Axel on 10/11/2014.
 */
public class SettingsUtil {

    private Context mContext;
    private String mPreferenceName;

    /**
     * Constructor method
     * @param context The context (e.g.: an Activity)
     * @param preferenceName The name of the SharedPreferences "file"
     */
    public SettingsUtil(Context context, String preferenceName) {
        this.mContext = context;
        this.mPreferenceName = preferenceName;
    }

    /**
     * Gets a boolean from SharedPreferences
     * @param settingName The name of the boolean to retrieve
     * @param defaultValue The default value to return if the boolean wasn't found in SharedPreferences
     * @return The requested boolean stored in SharedPreferences
     */
    public Boolean getBooleanPreference(String settingName, boolean defaultValue){
        SharedPreferences prefs = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);
        return prefs.getBoolean(settingName, defaultValue);
    }

    /**
     * Sets a boolean in SharedPreferences
     * @param settingName The name of the setting to save
     * @param value The boolean value to save in SharedPreferences
     * @return A boolean indicating if the value was stored successfully
     */
    public boolean setPreference(String settingName, boolean value){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE).edit();
        editor.putBoolean(settingName, value);
        return  editor.commit();
    }

    /**
     * Gets a long from SharedPreferences
     * @param settingName The name of the long to retrieve
     * @return The requested long stored in SharedPreferences
     */
    public long getLongPreference(String settingName){
        SharedPreferences prefs = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);
        return prefs.getLong(settingName, 0);
    }

    /**
     * Sets a long in SharedPreferences
     * @param settingName The name of the setting to save
     * @param value The long value to save in SharedPreferences
     * @return A boolean indicating if the value was stored successfully
     */
    public boolean setPreference(String settingName, long value){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE).edit();
        editor.putLong(settingName, value);
        return  editor.commit();
    }

    /**
     * Gets a String from SharedPreferences
     * @param settingName The name of the String to retrieve
     * @return The requested String stored in SharedPreferences
     */
    public String getStringPreference(String settingName){
        return getStringPreference(settingName, null);
    }

    /**
     * Gets a String from SharedPreferences
     * @param settingName The name of the String to retrieve
     * @param defaultValue The default value to return if the requested String wasn't found in SharedPreferences
     * @return The requested String stored in SharedPreferences
     */
    public String getStringPreference(String settingName,String defaultValue){
        SharedPreferences prefs = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);
        return prefs.getString(settingName, null);
    }

    /**
     * Sets a String in SharedPreferences
     * @param settingName The name of The name of the String to save in SharedPreferences
     * @param value The String to save in SharedPreferences
     * @return A boolean indicating if the value was stored successfully
     */
    public boolean setPreference(String settingName, String value){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE).edit();
        editor.putString(settingName, value);
        return  editor.commit();
    }

}
