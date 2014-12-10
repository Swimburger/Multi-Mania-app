package be.ana.nmct.multimania.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import be.ana.nmct.multimania.R;

/**
 * This activity is to see when the application for the first time startup
 */
public class LoadActivity extends Activity {

    AnimationDrawable animation;
    TextView txtLoading;
    private Context mContext;

    public static final String LOAD_SYNC = "be.ana.nmct.multimania.SYNCCOMPLETE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = this;
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

    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LOAD_SYNC);
        bManager.registerReceiver(syncCompleteReceiver, intentFilter);
    }

    private BroadcastReceiver syncCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("broadcast Received: "+ intent.getAction());
            //context.startActivity(MainActivity.class);
            animation.stop();
            Intent mainIntent = new Intent(mContext, MainActivity.class);
            startActivity(mainIntent);
        }
    };
}