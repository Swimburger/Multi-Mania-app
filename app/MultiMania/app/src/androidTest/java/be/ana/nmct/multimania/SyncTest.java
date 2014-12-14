package be.ana.nmct.multimania;

import android.app.Application;
import android.content.ContentProviderClient;
import android.test.ApplicationTestCase;

import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.SyncUtils;

/**
 * ProviderTest is a class that contains all tests regarding the communication with the ContentProvider
 * Created by Niels on 29/10/2014.
 */
public class SyncTest extends ApplicationTestCase<Application> {
    public static final String TAG = SyncTest.class.getSimpleName();


    public SyncTest()  {
        super(Application.class);
    }

    public void testForceDownload() throws Exception {
        SyncUtils utils = new SyncUtils(getContext());
        ContentProviderClient client = getContext().getContentResolver().acquireContentProviderClient(MultimaniaContract.CONTENT_AUTHORITY);
        utils.syncTalks(client,"nielsswimberghe@gmail.com",true);
    }
}
