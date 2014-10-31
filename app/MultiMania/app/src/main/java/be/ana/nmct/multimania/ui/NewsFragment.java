package be.ana.nmct.multimania.ui;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;

public class NewsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CustomCursorAdapter mAdapter;
    private static final String ARGS_NEWS = "newsitem";
    private static Loader loader;

    //default ctor
    public NewsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter=new CustomCursorAdapter(getActivity(),null,0);
        setListAdapter(mAdapter);
        loader = getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),MultimaniaContract.NewsItemEntry.CONTENT_URI,null,null,null,null);
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

class CustomCursorAdapter extends CursorAdapter{
    private LayoutInflater mInflater;
    private final Context context;

    public CustomCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
       // mInflater = LayoutInflater.from(context);
        //mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        mInflater = LayoutInflater.from(context);

        View v  = mInflater.inflate(R.layout.row_news, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.txtTitle = (TextView) v.findViewById(R.id.txtNewsTitle);
        holder.txtShortDescription = (TextView) v.findViewById(R.id.txtNewsText);
        holder.imgNews = (ImageView)v.findViewById(R.id.imgNews);
        v.setTag(holder);
        return v;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        int titleCol = cursor.getColumnIndexOrThrow(MultimaniaContract.NewsItemEntry.TITLE);
        int imgCol = cursor.getColumnIndexOrThrow(MultimaniaContract.NewsItemEntry.IMAGE);
        int shortDescriptionCol = cursor.getColumnIndexOrThrow(MultimaniaContract.NewsItemEntry.SHORT_DESCRIPTION);

        String title = cursor.getString(titleCol);
        String shortDescription = cursor.getString(shortDescriptionCol);
        String img = cursor.getString(imgCol);

        holder.txtTitle.setText(title);
        holder.txtShortDescription.setText(shortDescription);
        holder.imgNews.setImageURI(Uri.parse(img));
    }

    static class ViewHolder{
        TextView txtTitle;
        TextView txtShortDescription;
        ImageView imgNews;
    }
}
