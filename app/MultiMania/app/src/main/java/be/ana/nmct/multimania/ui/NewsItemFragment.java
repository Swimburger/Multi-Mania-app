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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;

public class NewsItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private CustomCursorAdapterNews mAdapter;
    private int mChosenNewsItem;

    public NewsItemFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bundle bundle = getArguments();
        // mChosenNewsItem = bundle.getInt(NewsFragment.ARGS_NEWS);
        if(getArguments() != null){
            mChosenNewsItem = getArguments().getInt(MultimaniaContract.NewsItemEntry.NEWSITEM_ID);
            System.out.println(mChosenNewsItem);
        }
        mAdapter=new CustomCursorAdapterNews(getActivity(),null,0);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MultimaniaContract.NewsItemEntry.CONTENT_URI, null, " (" + MultimaniaContract.NewsItemEntry.NEWSITEM_ID + " = ?)", new String[]{""+ mChosenNewsItem},null);
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


class CustomCursorAdapterNews extends CursorAdapter {
    private LayoutInflater mInflater;
    //private final Context context;

    public CustomCursorAdapterNews(Context context, Cursor c, int flags) {
        super(context, c, flags);
        //this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        mInflater = LayoutInflater.from(context);

        View v  = mInflater.inflate(R.layout.fragment_news_item, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.txtTitle = (TextView) v.findViewById(R.id.txtNewsItemTitle);
        v.setTag(holder);
        return v;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        int titleCol = cursor.getColumnIndexOrThrow(MultimaniaContract.NewsItemEntry.TITLE);
        String title = cursor.getString(titleCol);
        holder.txtTitle.setText(title);

    }

    static class ViewHolder{
        TextView txtTitle;
    }
}