package be.ana.nmct.multimania.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.TextView;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;

public class MyScheduleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MyScheduleAdapter mMyScheduleAdapter;

    public MyScheduleFragment() {  }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLoaderManager().initLoader(MainActivity.LOADER_MYSCHEDULE_TALK_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_schedule, container, false);

        mMyScheduleAdapter = new MyScheduleAdapter(this.getActivity(), null, 0);
        GridView grid = (GridView)v.findViewById(R.id.gridViewMySchedule);
        grid.setAdapter(mMyScheduleAdapter);

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), MultimaniaContract.TalkEntry.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mMyScheduleAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mMyScheduleAdapter.swapCursor(null);
    }

    private class MyScheduleAdapter extends CursorAdapter{

        private LayoutInflater mInflater;

        public MyScheduleAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = mInflater.inflate(R.layout.row_myschedule, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.txtInfo = (TextView)v.findViewById(R.id.txtInfo);
            holder.txtRoom = (TextView)v.findViewById(R.id.txtRoom);
            holder.txtTag = (TextView)v.findViewById(R.id.txtTag);
            holder.txtTime = (TextView)v.findViewById(R.id.txtTime);
            holder.txtTalkTitle = (TextView)v.findViewById(R.id.txtTalkTitle);
            v.setTag(holder);

            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder)view.getTag();

            int titleCol =  cursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.TITLE);
            int infoCol = cursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.DESCRIPTION);

            String title = cursor.getString(titleCol);
            String info = cursor.getString(infoCol);

            holder.txtTalkTitle.setText(title);
            holder.txtInfo.setText(info);
        }
    }

    static class ViewHolder{
        TextView txtTalkTitle;
        TextView txtRoom;
        TextView txtTime;
        TextView txtTag;
        TextView txtInfo;
    }

}
