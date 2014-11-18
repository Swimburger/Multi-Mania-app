package be.ana.nmct.multimania.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.service.NotificationSender;

public class TalkActivity extends Activity implements TalkFragment.TitleLoadListener{

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mUri = intent.getData();
        if(mUri != null){
            if(savedInstanceState == null){
                TalkFragment fragment = new TalkFragment();
                Bundle extras = new Bundle();
                extras.putParcelable(TalkFragment.URI_KEY, mUri);
                fragment.setArguments(extras);

                getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
                fragment.setTitleLoadListener(this);
            }else{
                TalkFragment fragment = (TalkFragment)getFragmentManager().findFragmentById(android.R.id.content);
                if(fragment != null){
                    fragment.setTitleLoadListener(this);
                }
            }
        } else{
            //get id of the talk
            Bundle bundle = intent.getExtras();
            long talkId = bundle.getLong(NotificationSender.NOTIF_TALKID);

            //load TalkFragment with correct talk loaded
            TalkFragment fragment = new TalkFragment();
            Bundle extras = new Bundle();
            Uri uri = MultimaniaContract.TalkEntry.buildItemUri(talkId);
            extras.putParcelable(TalkFragment.URI_KEY, uri);
            fragment.setArguments(extras);

            getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
            fragment.setTitleLoadListener(this);
        }
    }

    @Override
    public void onTitleloaded(String title) {
        getActionBar().setTitle(title);
    }

}
