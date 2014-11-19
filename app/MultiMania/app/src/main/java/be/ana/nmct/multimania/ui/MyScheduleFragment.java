package be.ana.nmct.multimania.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cocosw.undobar.UndoBarController;

import java.text.ParseException;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.MyScheduleRowHolder;

public class MyScheduleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = this.getClass().getCanonicalName();

    public static final String DATE_KEY = "date_key";
    public static final String POSITION_KEY = "position_key";
    public static final String SELECTED_TALK = "selected_talk";

    private String mDate;
    private int mPosition;
    private MyScheduleAdapter mMyScheduleAdapter;
    private Cursor mData;
    private UndoBarController mUndoBar;
    private RecyclerView mRecyclerView;

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

        mMyScheduleAdapter = new MyScheduleAdapter();
        //setRetainInstance(true);//apperently the pageradapter takes care of the retaining//nested fragments cannot retaininstance
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getLoaderManager().initLoader(MainActivity.LOADER_MYSCHEDULE_TALK_ID+mPosition, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_schedule, container, false);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
        mData = cursor;
        if(mMyScheduleAdapter != null){
            mMyScheduleAdapter.swapCursor(cursor);
            //Log.d("", DatabaseUtils.dumpCursorToString(mData));
            mRecyclerView.setAdapter(mMyScheduleAdapter);
            mMyScheduleAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mMyScheduleAdapter.swapCursor(null);
    }

    private class MyScheduleAdapter extends RecyclerView.Adapter<MyScheduleRowHolder>{

        private int mTitleIndex;
        private int mFromIndex;
        private int mToIndex;
        private int mRoomIndex;
        private int mTagIndex;
        private int mIdIndex;

        private MyScheduleAdapter() {
        }

        public void swapCursor(Cursor newCursor){
            if(newCursor != null){
                mTitleIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.TITLE);
                mFromIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.DATE_FROM);
                mToIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.DATE_UNTIL);
                mRoomIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.ROOM_NAME);
                mIdIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry._ID);
            }
        }

        @Override
        public MyScheduleRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_myschedule, null);
            final MyScheduleRowHolder vh = new MyScheduleRowHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cursor c = mData;
                    if(c.move(vh.getPosition())){
                        final int idIndex = mData.getColumnIndex(MultimaniaContract.TalkEntry._ID);
                        final long talkId = mData.getLong(idIndex);
                        Uri uri = MultimaniaContract.TalkEntry.buildItemUri(talkId);
                        Intent intent = new Intent(getActivity(),TalkActivity.class);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(MyScheduleRowHolder vh, int i) {

            if(mData.move(i)){

                //get values
                String title = mData.getString(mTitleIndex);
                String from = mData.getString(mFromIndex);
                String to = mData.getString(mToIndex);
                String room = mData.getString(mRoomIndex);

                //room + title
                vh.txtTalkTitle.setText(title);
                vh.txtRoom.setText(room);

                //dates
                try {
                    vh.txtTime.setText(Utility.getTimeString(from) + " - " + Utility.getTimeString(to));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //remove button
                vh.btnRemoveTalk.setTag(new ValueHolder(mData.getLong(mIdIndex), i));
                vh.btnRemoveTalk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ImageView btn = (ImageView)v.findViewById(R.id.btnRemoveTalk);
                        final ValueHolder vh = (ValueHolder)btn.getTag();

                        Uri uri = ContentUris.withAppendedId(MultimaniaContract.TalkEntry.CONTENT_URI, vh.id);
                        Cursor c = getActivity().getContentResolver().query(uri, null, null, null, null);

                        addItem(vh.position, vh.id);

                        //show undobar
                        mUndoBar =  new UndoBarController.UndoBar(getActivity())
                                .message("Removed from favorites")
                                .listener(new UndoBarController.UndoListener() {
                                    @Override
                                    public void onUndo(@Nullable Parcelable parcelable) {
                                        removeItem(vh.position, vh.id);
                                    }
                                }).show();
                    }
                });
            }

        }

        public void addItem(int position, long id) {
            updateItemValue(id, false);
            notifyItemInserted(position);
        }

        public void removeItem(int position, long id) {
            updateItemValue(id, true);
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount() {
            int result = mData.getCount();
            Log.d("mData.getCount: ", "" + result);
                return result;

        }

        private void updateItemValue(long id, boolean value) {
            ContentValues values = new ContentValues();
            values.put(MultimaniaContract.TalkEntry.IS_FAVORITE, value);
            AsyncQueryHandler handler = new MyScheduleAsyncQueryHandler(getActivity().getContentResolver());
            handler.startUpdate(
                    0,
                    null,
                    MultimaniaContract.TalkEntry.CONTENT_URI,
                    values,
                    MultimaniaContract.TalkEntry._ID+"=?",
                    new String[]{""+id}
            );
        }

    }

    private class MyScheduleAsyncQueryHandler extends AsyncQueryHandler{

        public MyScheduleAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }
    }

    private class ValueHolder{
        public long id;
        public int position;

        private ValueHolder(long id, int position) {
            this.id = id;
            this.position = position;
        }
    }

}
