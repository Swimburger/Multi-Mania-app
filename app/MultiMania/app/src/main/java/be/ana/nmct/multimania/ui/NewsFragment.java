package be.ana.nmct.multimania.ui;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.data.MultimaniaProvider;

public class NewsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CustomCursorAdapter mAdapter;
    private static final String ARGS_NEWS = "newsitem";
    private static Loader loader;

    //default ctor
    public NewsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loader = getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), null, new String[]{MultimaniaContract.NewsItemEntry.TITLE},null, null, null){

            @Override
            public Cursor loadInBackground() {

                MultimaniaProvider prov = new MultimaniaProvider();
                return prov.query(MultimaniaContract.NewsItemEntry.CONTENT_URI, getProjection(), getSelection(), getSelectionArgs(),getSortOrder());
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(mAdapter == null){
            mAdapter = new CustomCursorAdapter(getActivity(), data, 0);
            setListAdapter(mAdapter);
        }
        mAdapter.swapCursor(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        mAdapter.notifyDataSetChanged();
    }
}

class CustomCursorAdapter extends CursorAdapter{
    private LayoutInflater mInflater;

    public CustomCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return mInflater.inflate(R.layout.row_news, parent, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView mTitle = (TextView)view.findViewById(R.id.txtNewsTitle);

        System.out.println(cursor.getColumnIndex(MultimaniaContract.NewsItemEntry.TITLE));
        mTitle.setText(cursor.getColumnIndex(MultimaniaContract.NewsItemEntry.TITLE));
    }
}
