package be.ana.nmct.multimania.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;

import com.cocosw.undobar.UndoBarController;

import java.text.ParseException;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.MyScheduleRowHolder;

public class MyScheduleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private final String TAG = this.getClass().getCanonicalName();

    public static final String DATE_KEY = "date_key";
    public static final String POSITION_KEY = "position_key";
    public static final String SELECTED_TALK = "selected_talk";

    private String mDate;
    private int mPosition;
    private MyScheduleAdapter mMyScheduleAdapter;
    private Cursor mData;
    private UndoBarController mUndoBar;

    public MyScheduleFragment() {

    }

    public static MyScheduleFragment newInstance(String date, int position) {
        MyScheduleFragment fragmentFirst = new MyScheduleFragment();
        Bundle args = new Bundle();
        args.putString(DATE_KEY, date);
        args.putInt(POSITION_KEY, position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mDate = args.getString(DATE_KEY);
        mPosition = args.getInt(POSITION_KEY);

        this.getLoaderManager().initLoader(MainActivity.LOADER_MYSCHEDULE_TALK_ID + mPosition, null, this);
        mMyScheduleAdapter = new MyScheduleAdapter(this.getActivity(), mData, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_schedule, container, false);
        GridView grid = (GridView) v.findViewById(R.id.gridViewMySchedule);
        grid.setAdapter(mMyScheduleAdapter);
        grid.setOnItemClickListener(this);

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), MultimaniaContract.TalkEntry.CONTENT_URI, null,
                MultimaniaContract.TalkEntry.IS_FAVORITE + "=1 AND " +
                        MultimaniaContract.TalkEntry.DATE_FROM + " LIKE ?"
                , new String[]{mDate + "%"}, MultimaniaContract.TalkEntry.DATE_FROM);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mData = cursor;
        if (mMyScheduleAdapter != null) {
            mMyScheduleAdapter.swapCursor(cursor);
            mMyScheduleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mMyScheduleAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Cursor cursor = (Cursor) mMyScheduleAdapter.getItem(position);
        final int idIndex = cursor.getColumnIndex(MultimaniaContract.TalkEntry._ID);
        final long talkId = cursor.getLong(idIndex);
        Uri uri = MultimaniaContract.TalkEntry.buildItemUri(talkId);
        Intent intent = new Intent(getActivity(), TalkActivity.class);
        intent.setData(uri);
        startActivity(intent);

    }

    private class MyScheduleAdapter extends CursorAdapter {

        private LayoutInflater mInflater;

        private int mTitleIndex;
        private int mFromIndex;
        private int mToIndex;
        private int mRoomIndex;
        private int mTagIndex;
        private int mIdIndex;

        public MyScheduleAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
            mInflater = LayoutInflater.from(context);
            swapCursor(c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_myschedule, null);
            v.setTag(new MyScheduleRowHolder(v));
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            MyScheduleRowHolder mViewHolder = (MyScheduleRowHolder) view.getTag();
            mViewHolder.TalkId = cursor.getLong(mIdIndex);
            final long id = cursor.getLong(mIdIndex);

            //get values
            String title = cursor.getString(mTitleIndex);
            String from = cursor.getString(mFromIndex);
            String to = cursor.getString(mToIndex);
            String room = cursor.getString(mRoomIndex);


            //room + title
            mViewHolder.txtTalkTitle.setText(title);
            mViewHolder.txtRoom.setText(room);

            //dates
            try {
                mViewHolder.txtTime.setText(Utility.getTimeString(from) + " - " + Utility.getTimeString(to));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //remove button
            mViewHolder.btnRemoveTalk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(id);
                    //show undobar
                    mUndoBar = new UndoBarController.UndoBar(getActivity())
                            .message("Removed from favorites")
                            .listener(new UndoBarController.UndoListener() {
                                @Override
                                public void onUndo(@Nullable Parcelable parcelable) {
                                    addItem(id);
                                }
                            }).show();
                }
            });
        }

        public void addItem(long id) {
            updateItemValue(id, true);
        }

        public void removeItem(long id) {
            updateItemValue(id, false);
        }

        private void updateItemValue(long id, boolean value) {
            ContentValues values = new ContentValues();
            values.put(MultimaniaContract.TalkEntry.IS_FAVORITE, value? 1:0);
            AsyncQueryHandler handler = new MyScheduleAsyncQueryHandler(getActivity().getContentResolver());
            handler.startUpdate(
                    0,
                    null,
                    MultimaniaContract.TalkEntry.CONTENT_URI,
                    values,
                    MultimaniaContract.TalkEntry._ID + "=?",
                    new String[]{"" + id}
            );
        }

        @Override
        public Cursor swapCursor(Cursor newCursor) {
            if (newCursor != null) {
                mTitleIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.TITLE);
                mFromIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.DATE_FROM);
                mToIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.DATE_UNTIL);
                mRoomIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.ROOM_NAME);
                mIdIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry._ID);
            }
            return super.swapCursor(newCursor);
        }
    }

    private class MyScheduleAsyncQueryHandler extends AsyncQueryHandler {

        public MyScheduleAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }
    }
}

