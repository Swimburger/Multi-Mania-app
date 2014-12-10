package be.ana.nmct.multimania.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.SettingsUtil;


public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String PREFERENCE_NAME = "launch_values";
    public static final String PREFERENCE_FIRSTTIMELAUNCH = "first_time_launch";
    public static final String PREFERENCE_ACCOUNT = "account_name";

    public static final int LOADER_SCHEDULE_DATES_ID    = 0;
    public static final int LOADER_SCHEDULE_TALK_ID     = 10;
    public static final int LOADER_MYSCHEDULE_TALK_ID   = 20;
    public static final int LOADER_NEWS_ID              = 3;
    public static final int LOADER_SETTINGS             = 5;

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = MultimaniaContract.CONTENT_AUTHORITY;
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "multi-mania.be";
    // The account name
    public static final String ACCOUNT = "Mult-Mania";
    // Instance fields
    Account mAccount;

    private MenuItem mAddAccountMenuItem;

    // Sync interval constants
    public static final long SYNC_INTERVAL = 60L*60L*24;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private SettingsUtil mAccountSettings;
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

        mAccount = CreateSyncAccount(this);
        mAccountSettings = new SettingsUtil(this, PREFERENCE_NAME);

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

    @Override
    protected void onResume() {
        super.onResume();

        //Handle first time launching
        SettingsUtil launchUtil = new SettingsUtil(this, PREFERENCE_NAME);
        boolean firstTimeLaunch = launchUtil.getBooleanPreference(PREFERENCE_FIRSTTIMELAUNCH, true);

        if(firstTimeLaunch){
            SettingsUtil settingsUtil = new SettingsUtil(this, SettingsFragment.PREFERENCE_NAME);
            settingsUtil.setPreference(SettingsFragment.PREFERENCE_NOTIFY, true);
            settingsUtil.setPreference(SettingsFragment.PREFERENCE_SYNC, false);
            //launchUtil.setPreference(PREFERENCE_FIRSTTIMELAUNCH, false);

            requestSync();

            //Intent loadintent = new Intent(this, LoadActivity.class);
            //startActivity(loadintent);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mAddAccountMenuItem = menu.findItem(R.id.action_account);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_account:

                TextView txtDialogMessage = new TextView(this);
                txtDialogMessage.setText(R.string.login_description);
                txtDialogMessage.setGravity(Gravity.CENTER_HORIZONTAL);
                txtDialogMessage.setPadding(20, 10, 20, 15);
                txtDialogMessage.setTextAppearance(this,android.R.style.TextAppearance_Medium);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(txtDialogMessage)
                        .setTitle(R.string.action_account)
                        .setCancelable(true)
                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                askUserEmail();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
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

    private void askUserEmail() {
        try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
            startActivityForResult(intent, SettingsFragment.REQUEST_CODE_EMAIL);
        } catch (ActivityNotFoundException e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SettingsFragment.REQUEST_CODE_EMAIL && data != null && resultCode == Activity.RESULT_OK) {

            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            if (accountName != "") {
                mAccountSettings.setPreference(PREFERENCE_ACCOUNT, accountName);
                Toast.makeText(this, getString(R.string.account_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.account_picker_invalid), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.account_picker_none), Toast.LENGTH_LONG).show();
        }
    }

}
