package be.ana.nmct.multimania.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class NewsItemActivity extends Activity implements NewsItemFragment.TitleLoadListener {

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mUri=intent.getData();

        if (savedInstanceState == null) {
            NewsItemFragment fragment = new NewsItemFragment();
            Bundle extras = new Bundle();
            extras.putParcelable(NewsItemFragment.URI_KEY,mUri);
            fragment.setArguments(extras);

            getFragmentManager().beginTransaction()
                    .add(android.R.id.content,fragment)
                    .commit();
            fragment.setTitleLoadListener(this);
        }else{
            NewsItemFragment fragment = (NewsItemFragment) getFragmentManager().findFragmentById(android.R.id.content);
            if(fragment!=null){
                fragment.setTitleLoadListener(this);
            }
        }
    }

    @Override
    public void onTitleloaded(String title) {
        getActionBar().setTitle(title);
    }
}
