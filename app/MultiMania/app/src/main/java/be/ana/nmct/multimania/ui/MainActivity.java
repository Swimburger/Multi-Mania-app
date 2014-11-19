package be.ana.nmct.multimania.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.utils.SettingsUtil;


public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String PREFERENCE_NAME = "launch_values";
    public static final String PREFERENCE_FIRSTTIMELAUNCH = "first_time_launch";

    public static final int LOADER_SCHEDULE_DATES_ID =0;
    public static final int LOADER_SCHEDULE_TALK_ID=10;
    public static final int LOADER_MYSCHEDULE_TALK_ID=20;
    public static final int LOADER_NEWS_ID=3;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Handle first time launching
        SettingsUtil launchUtil = new SettingsUtil(this, PREFERENCE_NAME);
        boolean firstTimeLaunch = launchUtil.getBooleanPreference(PREFERENCE_FIRSTTIMELAUNCH, true);

        if(firstTimeLaunch){
            SettingsUtil settingsUtil = new SettingsUtil(this, SettingsFragment.PREFERENCE_NAME);
            settingsUtil.setPreference(SettingsFragment.PREFERENCE_NOTIFY, true);
            settingsUtil.setPreference(SettingsFragment.PREFERENCE_SYNC, false);
            launchUtil.setPreference(PREFERENCE_FIRSTTIMELAUNCH, false);
        }
    }
    
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        //boolean isNull = fragment==null;
        boolean isNotRetained = true;
        switch (position) {
            case 0:
                isNotRetained=!(fragment instanceof SchedulesFragment);
                if(isNotRetained) {
                    fragment = new SchedulesFragment();
                }
                break;
            case 1:
                isNotRetained=!(fragment instanceof MySchedulesFragment);
                if(isNotRetained) {
                    fragment = new MySchedulesFragment();
                }
                break;
            case 2:
                fragment = new MapFragment();
                break;
            case 3:
                isNotRetained=!(fragment instanceof NewsFragment);
                if(isNotRetained){
                    fragment = new NewsFragment();
                }
                break;
            case 4:
                fragment = new AboutFragment();
                break;
            case 5:
                fragment = new SettingsFragment();
                break;
        }
        if(isNotRetained){
            fragmentManager.beginTransaction().replace(R.id.container,fragment).commit();
        }
        onSectionAttached(position);
    }
    public void onSectionAttached(int number) {

        switch (number) {
            case 0:
                mTitle = getString(R.string.title_schedule);
                break;
            case 1:
                mTitle = getString(R.string.title_myschedule);
                break;
            case 2:
                mTitle = getString(R.string.title_map);
                break;
            case 3:
                mTitle = getString(R.string.title_news);
                break;
            case 4:
                mTitle = getString(R.string.title_about);
                break;
            case 5:
                mTitle = getString(R.string.title_settings);
                break;
            //add more cases here
        }
        getActionBar().setTitle(mTitle);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        if(actionBar!=null){
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        //actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


}
