package be.ana.nmct.multimania.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;

import be.ana.nmct.multimania.R;

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
    //checken of internet aanligt
    //checken of data gesyncd is
}