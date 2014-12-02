package be.ana.nmct.multimania.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Axel on 2/12/2014.
 */
public class BootListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Multi-Mania knows you booted your device", Toast.LENGTH_LONG).show();
    }
}
