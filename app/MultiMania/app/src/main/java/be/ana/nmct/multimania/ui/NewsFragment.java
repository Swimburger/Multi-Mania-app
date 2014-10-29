package be.ana.nmct.multimania.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;

public class NewsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter mAdapter;
    private static final String ARGS_NEWS = "newsitem";
    private int mNewsItem;

    //default ctor
    public NewsFragment(){}

    public static NewsFragment newInstance(int newsitem){
        Bundle arguments = new Bundle();
        arguments.putInt(ARGS_NEWS, newsitem);

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            Bundle arguments = getArguments();
            if(arguments != null){
                int newsitem = arguments.getInt(ARGS_NEWS, 0);
                if(newsitem != 0){
                    mNewsItem = newsitem;
                }
            }
        }

        String[] columns = new String[]{MultimaniaContract.NewsItemEntry.TITLE};
        int[] viewIds = new int[] {android.R.id.text1};

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, null,
                columns, viewIds, 0);

        setListAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MultimaniaContract.NewsItemEntry.CONTENT_URI, new String[]{
                MultimaniaContract.NewsItemEntry.NEWSITEM_ID,
                MultimaniaContract.NewsItemEntry.TITLE,
                MultimaniaContract.NewsItemEntry.IMAGE,
                MultimaniaContract.NewsItemEntry.SHORT_DESCRIPTION,
                MultimaniaContract.NewsItemEntry.LONG_DESCRIPTION,
                MultimaniaContract.NewsItemEntry.IMPORTANCE,
                MultimaniaContract.NewsItemEntry.ORDER},
                "(" + MultimaniaContract.NewsItemEntry.NEWSITEM_ID + " = ?)",
                new String[]{"" + mNewsItem}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
