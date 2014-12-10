package be.ana.nmct.multimania.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.service.SyncAdapter;
import be.ana.nmct.multimania.utils.SettingsUtil;

/**
 * This activity is to see when the application for the first time startup
 */
public class LoadActivity extends Activity {
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "multi-mania.be";
    // The account name
    public static final String ACCOUNT = "Mult-Mania";

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = MultimaniaContract.CONTENT_AUTHORITY;
    private static final String TAG = LoadActivity.class.getSimpleName();

    public static final String PREFERENCE_NAME = "launch_values";
    public static final String PREFERENCE_FIRSTTIMELAUNCH = "first_time_launch";

    // Instance fields
    Account mAccount;

    AnimationDrawable animation;
    TextView txtLoading;

    public static final String LOAD_SYNC = "be.ana.nmct.multimania.SYNCCOMPLETE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        ImageView image = (ImageView) findViewById(R.id.imageGif);
        image.setBackgroundResource(R.drawable.gifloader);

        txtLoading = (TextView)findViewById(R.id.txtLoading);

        txtLoading.setText("Loading...");

        animation = (AnimationDrawable)image.getBackground();
        animation.start();

        //Beautify for Lollipop users
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColorDark));
        }


        mAccount = CreateSyncAccount(this);
        requestSync();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SyncAdapter.SYNC_READY_BROADCAST);
        registerReceiver(syncCompleteReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private BroadcastReceiver syncCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Sync ready received");
            //context.startActivity(MainActivity.class);
            animation.stop();

            SettingsUtil launchUtil = new SettingsUtil(LoadActivity.this, PREFERENCE_NAME);
            launchUtil.setPreference(PREFERENCE_FIRSTTIMELAUNCH, false);

            Intent mainIntent = new Intent(LoadActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(mainIntent);
        }
    };

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

    private void requestSync() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
    }
}