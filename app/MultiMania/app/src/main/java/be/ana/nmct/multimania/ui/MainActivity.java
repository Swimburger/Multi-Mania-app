package be.ana.nmct.multimania.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.SettingsUtil;


public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String PREFERENCE_NAME = "launch_values";
    public static final String PREFERENCE_FIRSTTIMELAUNCH = "first_time_launch";

    public static final int LOADER_SCHEDULE_DATES_ID    = 0;
    public static final int LOADER_SCHEDULE_TALK_ID     = 10;
    public static final int LOADER_MYSCHEDULE_TALK_ID   = 20;
    public static final int LOADER_NEWS_ID              = 3;
    public static final int LOADER_TAGS_ID              = 4;
    public static final int LOADER_SETTINGS             = 5;

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = MultimaniaContract.CONTENT_AUTHORITY;
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "multi-mania.be";
    // The account name
    public static final String ACCOUNT = "Mult-Mania";
    // Instance fields
    Account mAccount;

    // Sync interval constants
    public static final long SYNC_INTERVAL = 60L*60L*24;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        Configuration configuration = getResources().getConfiguration();
        if ((configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL||
            (configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL||
            (configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE){
            // Set up the drawer.
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        }


        //Handle first time launching
        SettingsUtil launchUtil = new SettingsUtil(this, PREFERENCE_NAME);
        boolean firstTimeLaunch = launchUtil.getBooleanPreference(PREFERENCE_FIRSTTIMELAUNCH, true);

        mAccount = CreateSyncAccount(this);

        if(firstTimeLaunch){
            SettingsUtil settingsUtil = new SettingsUtil(this, SettingsFragment.PREFERENCE_NAME);
            settingsUtil.setPreference(SettingsFragment.PREFERENCE_NOTIFY, true);
            settingsUtil.setPreference(SettingsFragment.PREFERENCE_SYNC, false);
            launchUtil.setPreference(PREFERENCE_FIRSTTIMELAUNCH, false);

            requestSync();
        }

        //Beautify for Lollipop users
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColorDark));
        }

        ContentResolver.addPeriodicSync(
                mAccount,
                AUTHORITY,
                Bundle.EMPTY,
                SYNC_INTERVAL);
    }

    private void requestSync() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
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
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.fade_in_object,R.anim.fade_out_object);
            ft.replace(R.id.container,fragment).commit();
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
        actionBar.setIcon(R.drawable.ic_actionbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void openTwitter(View v){
        launchUrlIntent(getString(R.string.twitter_url));
    }
    public void openFacebook(View v){
        launchUrlIntent(getString(R.string.facebook_url));
    }
    public void openSite(View v){
        launchUrlIntent(getString(R.string.site_url));
    }

    public void launchUrlIntent(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }
}
