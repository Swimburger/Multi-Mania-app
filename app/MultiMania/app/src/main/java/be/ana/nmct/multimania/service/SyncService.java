package be.ana.nmct.multimania.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Astrid on 16/11/2014.
 */
public class SyncService extends Service {

    private static SyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    public SyncService(){}

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (sSyncAdapterLock){
            if(sSyncAdapter == null){
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
