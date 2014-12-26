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
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.service.SyncAdapter;
import be.ana.nmct.multimania.utils.SettingsUtil;
import be.ana.nmct.multimania.utils.Utility;

/**
 * This activity is shown when the application starts for the first time.
 * It then loads all the data in the background and redirects the player to the MainActivity when ready.
 */
public class LoadActivity extends Activity {
    private static final String TAG = LoadActivity.class.getSimpleName();

    public static final String PREFERENCE_NAME = "launch_values";
    public static final String PREFERENCE_FIRSTTIMELAUNCH = "first_time_launch";

   private Account mAccount;
   private AnimationDrawable mAnimation;

   private TextView txtLoading;

   private Timer mLoadingTimer;
   private TimerTask mTimerTask;
   private Handler mHandler;


    public static final String LOAD_SYNC = "be.ana.nmct.multimania.SYNCCOMPLETE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        ImageView image = (ImageView) findViewById(R.id.imageGif);
        image.setBackgroundResource(R.drawable.gifloader);

        txtLoading = (TextView)findViewById(R.id.txtLoading);
        mHandler = new Handler();

        mAnimation = (AnimationDrawable)image.getBackground();
        mAnimation.start();

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
        mLoadingTimer = new Timer();
        initializeTimerTask();
        mLoadingTimer.schedule(mTimerTask, 0, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimerTask();
    }

    private void initializeTimerTask() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!txtLoading.getText().toString().contains("...")){
                            txtLoading.setText(txtLoading.getText() + ".");
                        } else {
                            txtLoading.setText(getString(R.string.loading));
                        }
                    }
                });
            }
        };
    }

    private void stopTimerTask(){
        if(mLoadingTimer != null){
            mLoadingTimer.cancel();
        }
    }

    private BroadcastReceiver syncCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Sync ready received");
            //context.startActivity(MainActivity.class);
            mAnimation.stop();

            SettingsUtil launchUtil = new SettingsUtil(LoadActivity.this, PREFERENCE_NAME);
            launchUtil.setPreference(PREFERENCE_FIRSTTIMELAUNCH, false);

            Intent mainIntent = new Intent(LoadActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(mainIntent);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(syncCompleteReceiver);
    }
}