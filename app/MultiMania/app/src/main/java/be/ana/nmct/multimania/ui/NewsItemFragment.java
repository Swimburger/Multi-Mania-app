package be.ana.nmct.multimania.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.model.NewsItem;

public class NewsItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private int mChosenNewsItem;

    public NewsItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mChosenNewsItem = getArguments().getInt(MultimaniaContract.NewsItemEntry.NEWSITEM_ID);
        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MultimaniaContract.NewsItemEntry.CONTENT_URI, null, " (" + MultimaniaContract.NewsItemEntry.NEWSITEM_ID + " = ?)", new String[]{""+ mChosenNewsItem},null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
