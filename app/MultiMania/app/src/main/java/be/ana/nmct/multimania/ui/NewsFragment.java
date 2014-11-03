package be.ana.nmct.multimania.ui;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;

public class NewsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private NewsCursorAdapter mAdapter;
    private static final String ARGS_NEWS = "newsitem";
    private static Loader loader;

    //default ctor
    public NewsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter=new NewsCursorAdapter(getActivity(),null,0);
        setListAdapter(mAdapter);
        loader = getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news,container,false);
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


