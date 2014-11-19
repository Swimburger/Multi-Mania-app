package be.ana.nmct.multimania.ui;

import android.app.Fragment;
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
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.undobar.UndoBarController;

import java.text.ParseException;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.Utility;

public class MyScheduleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private final String TAG = this.getClass().getCanonicalName();

    public static final String DATE_KEY = "date_key";
    public static final String POSITION_KEY = "position_key";
    public static final String SELECTED_TALK = "selected_talk";


    private String mDate;
    private int mPosition;
    private MyScheduleAdapter mMyScheduleAdapter;
    private Cursor mData;

    public MyScheduleFragment() {

    }

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

        Bundle args = getArguments();
        mDate = args.getString(DATE_KEY);
        mPosition = args.getInt(POSITION_KEY);

        this.getLoaderManager().initLoader(MainActivity.LOADER_MYSCHEDULE_TALK_ID+mPosition, null, this);
        //setRetainInstance(true);//apperently the pageradapter takes care of the retaining//nested fragments cannot retaininstance

        mMyScheduleAdapter = new MyScheduleAdapter(this.getActivity(), mData, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_schedule, container, false);

        GridView grid = (GridView)v.findViewById(R.id.gridViewMySchedule);
        grid.setAdapter(mMyScheduleAdapter);
        grid.setOnItemClickListener(this);

        return v;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), MultimaniaContract.TalkEntry.CONTENT_URI,null,
                MultimaniaContract.TalkEntry.IS_FAVORITE+"=1 AND " +
                MultimaniaContract.TalkEntry.DATE_FROM + " LIKE ?"
                ,new String[]{mDate+"%"},MultimaniaContract.TalkEntry.DATE_FROM);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mData=cursor;
        if(mMyScheduleAdapter!=null)
            mMyScheduleAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mMyScheduleAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) mMyScheduleAdapter.getItem(position);
        final int idIndex = cursor.getColumnIndex(MultimaniaContract.TalkEntry._ID);
        final long talkId = cursor.getLong(idIndex);
        Uri uri = MultimaniaContract.TalkEntry.buildItemUri(talkId);
        Intent intent = new Intent(getActivity(),TalkActivity.class);
        intent.setData(uri);
        startActivity(intent);
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
            getIndexes(cursor);
        }

        @Override
        public Cursor swapCursor(Cursor newCursor) {
            getIndexes(newCursor);
            return super.swapCursor(newCursor);
        }

        private void getIndexes(Cursor newCursor) {
            if(newCursor != null){
                mTitleIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.TITLE);
                mFromIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.DATE_FROM);
                mToIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.DATE_UNTIL);
                mRoomIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.ROOM_NAME);
            }
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = mInflater.inflate(R.layout.row_myschedule, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.txtRoom = (TextView)v.findViewById(R.id.txtRoom);
            holder.txtTag = (TextView)v.findViewById(R.id.txtTag);
            holder.txtTime = (TextView)v.findViewById(R.id.txtTime);
            holder.txtTalkTitle = (TextView)v.findViewById(R.id.txtTalkTitle);
            holder.btnRemoveTalk = (ImageView)v.findViewById(R.id.btnRemoveTalk);
            v.setTag(holder);

            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder)view.getTag();

            String title = cursor.getString(mTitleIndex);
            String from = cursor.getString(mFromIndex);
            String to = cursor.getString(mToIndex);
            String room = cursor.getString(mRoomIndex);

            holder.txtTalkTitle.setText(title);
            try {
                holder.txtTime.setText(Utility.getTimeString(from) + " - " + Utility.getTimeString(to));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.txtRoom.setText(room);

            holder.btnRemoveTalk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }


    }



    static class ViewHolder{
        TextView txtTalkTitle;
        TextView txtRoom;
        TextView txtTime;
        TextView txtTag;
        ImageView btnRemoveTalk;

    }

}
