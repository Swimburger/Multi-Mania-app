package be.ana.nmct.multimania.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.Utility;

public class MyScheduleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = this.getClass().getCanonicalName();

    public static final String DATE_KEY = "date_key";
    public static final String POSITION_KEY = "position_key";

    public static final String SELECTED_TALK = "selected_talk";


    private String mDate;
    private int mPosition;
    private MyScheduleAdapter mMyScheduleAdapter;

    public MyScheduleFragment() {  }

    public static MyScheduleFragment newInstance(String date,int position) {
        MyScheduleFragment fragmentFirst = new MyScheduleFragment();
        Bundle args = new Bundle();
        args.putString(DATE_KEY, date);
        args.putInt(POSITION_KEY,position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLoaderManager().initLoader(MainActivity.LOADER_MYSCHEDULE_TALK_ID, null, this);

        Bundle args = getArguments();
        mDate = args.getString(DATE_KEY);
        mPosition = args.getInt(POSITION_KEY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_schedule, container, false);

        mMyScheduleAdapter = new MyScheduleAdapter(this.getActivity(), null, 0);
        GridView grid = (GridView)v.findViewById(R.id.gridViewMySchedule);
        grid.setAdapter(mMyScheduleAdapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), TalkActivity.class);
                intent.putExtra(SELECTED_TALK, position);
                startActivity(intent);
            }
        });

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
        private int mTitleIndex;
        private int mFromIndex;
        private int mToIndex;
        private int mRoomIndex;
        private int mTagIndex;

        public MyScheduleAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public Cursor swapCursor(Cursor newCursor) {
            if(newCursor != null){
                mTitleIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.TITLE);
                mFromIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.DATE_FROM);
                mToIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.DATE_UNTIL);
                mRoomIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.ROOM_NAME);
            }
            return super.swapCursor(newCursor);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = mInflater.inflate(R.layout.row_myschedule, parent, false);

            ViewHolder holder = new ViewHolder();
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

            String title = cursor.getString(mTitleIndex);
            Date from = new Date();
            Date to = new Date();
            String room = cursor.getString(mRoomIndex);

            //parse dates
            try {
                from = Utility.ConvertStringToDate(cursor.getString(mFromIndex));
                to = Utility.ConvertStringToDate(cursor.getString(mToIndex));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            holder.txtTalkTitle.setText(title);
            holder.txtTime.setText(Utility.sTimeFormat.format(from) + " - " + Utility.sTimeFormat.format(to));
            holder.txtRoom.setText(room);
        }
    }

    static class ViewHolder{
        TextView txtTalkTitle;
        TextView txtRoom;
        TextView txtTime;
        TextView txtTag;
    }

}
