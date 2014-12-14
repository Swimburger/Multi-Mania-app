package be.ana.nmct.multimania.ui;

import android.accounts.Account;
import android.app.Activity;
import android.content.BroadcastReceiver;
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
import be.ana.nmct.multimania.service.SyncAdapter;
import be.ana.nmct.multimania.utils.SettingsUtil;
import be.ana.nmct.multimania.utils.Utility;

/**
 * This activity is to see when the application for the first time startup
 */
public class LoadActivity extends Activity {
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


        mAccount = Utility.getSyncAccount(this);
        Utility.requestSync(mAccount,false);
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
            startActivity(mainIntent);
        }
    };


}