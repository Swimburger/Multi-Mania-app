package be.ana.nmct.multimania.ui;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;

public class NewsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private NewsCursorAdapter mAdapter;
    private static final String ARGS_NEWS = "newsitem";
    private static Loader loader;
    private Cursor mData;

    //default ctor
    public NewsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mAdapter=new NewsCursorAdapter(getActivity(),null,0);
        setListAdapter(mAdapter);
        loader = getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news,container,false);
        mAdapter.swapCursor(mData);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),MultimaniaContract.NewsItemEntry.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mData=data;
        mAdapter.swapCursor(mData);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        Uri uri = MultimaniaContract.NewsItemEntry.buildItemUri(
                cursor.getLong(
                    cursor.getColumnIndex(
                            MultimaniaContract.NewsItemEntry._ID
                    )
                )
        );

        Intent intent = new Intent(getActivity(),NewsItemActivity.class);
        intent.setData(uri);
        startActivity(intent);

        /*Fragment fragment = new NewsItemFragment();

        Bundle args = new Bundle();
        args.putInt(NewsFragment.ARGS_NEWS, position);
        System.out.println(ARGS_NEWS + " pos: " + position);
        fragment.setArguments(args);

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.container, fragment).commit();*/

    }

    private class NewsCursorAdapter extends CursorAdapter{
        private LayoutInflater mInflater;
        Animation animFadein;



        public NewsCursorAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
            // mInflater = LayoutInflater.from(context);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            animFadein = AnimationUtils.loadAnimation(context.getApplicationContext(),
                    R.anim.fade_in);
        }



        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            //mInflater = LayoutInflater.from(context);

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
            //TODO: Move the getColumnIndex to the constructor so it doesn't get called for every row item
            int titleCol = cursor.getColumnIndexOrThrow(MultimaniaContract.NewsItemEntry.TITLE);
            int imgCol = cursor.getColumnIndexOrThrow(MultimaniaContract.NewsItemEntry.IMAGE);
            int shortDescriptionCol = cursor.getColumnIndexOrThrow(MultimaniaContract.NewsItemEntry.SHORT_DESCRIPTION);

            String title = cursor.getString(titleCol);
            String shortDescription = cursor.getString(shortDescriptionCol);
            String img = cursor.getString(imgCol);

            holder.txtTitle.setText(title);
            holder.txtShortDescription.setText(shortDescription);
           // holder.imgNews.setImageURI(Uri.parse(img));
            Ion.with(holder.imgNews)
                    //.animateLoad()

                    //.resize(view.getMeasuredWidth(),
                    //        holder.imgNews.getHeight())
                    //.centerCrop()
                    .smartSize(true)
                    .animateIn(animFadein)
                    .load(img);
        }


    }

    static class ViewHolder{
        TextView txtTitle;
        TextView txtShortDescription;
        ImageView imgNews;
    }
}


