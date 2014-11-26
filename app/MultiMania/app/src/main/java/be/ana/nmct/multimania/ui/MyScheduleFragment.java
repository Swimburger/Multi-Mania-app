package be.ana.nmct.multimania.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
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
import android.widget.ArrayAdapter;

import com.cocosw.undobar.UndoBarController;
import com.etsy.android.grid.StaggeredGridView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.utils.GoogleCalUtil;
import be.ana.nmct.multimania.utils.SettingsHelper;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.MyScheduleRowHolder;
import be.ana.nmct.multimania.vm.ScheduleTalkVm;

public class MyScheduleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private final String TAG = this.getClass().getCanonicalName();

    public static final String DATE_KEY = "date_key";
    public static final String POSITION_KEY = "position_key";
    public static final String SELECTED_TALK = "selected_talk";

    private String mDate;
    private int mPosition;

    private MyScheduleAdapter mMyScheduleAdapter;
    private StaggeredGridView mGridview;
    public UndoBarController.UndoBar mUndoBar;
    private SettingsHelper mSettingsHelper;

    private List<ScheduleTalkVm> mItems;

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
        this.mDate = args.getString(DATE_KEY);
        this.mPosition = args.getInt(POSITION_KEY);
        this.mItems = new ArrayList<ScheduleTalkVm>();
        this.mSettingsHelper = new SettingsHelper(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getLoaderManager().initLoader(MainActivity.LOADER_MYSCHEDULE_TALK_ID + mPosition, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_schedule, container, false);
        mGridview = (StaggeredGridView) v.findViewById(R.id.gridViewMySchedule);
        mGridview.setAdapter(mMyScheduleAdapter);
        mGridview.setOnItemClickListener(this);

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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        try {
            buildItems(cursor);
            //loader.abandon();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mItems.clear();
        mMyScheduleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final long talkId = mItems.get(position).id;
        Uri uri = MultimaniaContract.TalkEntry.buildItemUri(talkId);
        Intent intent = new Intent(getActivity(), TalkActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }

    private void buildItems(Cursor c) throws ParseException {
        if (c == null || mItems == null) return;
        mItems.clear();

        final int dateFromIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.DATE_FROM);
        final int dateUntilIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.DATE_UNTIL);
        final int isFavoriteIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.IS_FAVORITE);
        final int titleIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.TITLE);
        final int roomIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.ROOM_NAME);
        final int idIndex = c.getColumnIndex(MultimaniaContract.TalkEntry._ID);
        final int calEventIdIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.CALEVENT_ID);
        final int descriptionIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.DESCRIPTION);

        if (c.moveToFirst()) {
            do {
                final ScheduleTalkVm vm = new ScheduleTalkVm();
                final long talkId = c.getLong(idIndex);

                vm.isFavorite = c.getInt(isFavoriteIndex) == 1;
                vm.title = c.getString(titleIndex);
                vm.room = c.getString(roomIndex);
                vm.id = talkId;
                vm.fromString = Utility.getTimeString(c.getString(dateFromIndex));
                vm.untilString = Utility.getTimeString(c.getString(dateUntilIndex));

                //stuff to store for the calendar actions
                vm.calEventId = c.getLong(calEventIdIndex);
                vm.from = Utility.convertStringToDate(c.getString(dateFromIndex));
                vm.to = Utility.convertStringToDate(c.getString(dateFromIndex));
                vm.description = c.getString(descriptionIndex);

                getLoaderManager().initLoader(1000 + (int) talkId, null, new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        return new CursorLoader(getActivity(),
                                ContentUris.appendId(MultimaniaContract.TagEntry.CONTENT_URI.buildUpon()
                                        .appendPath(MultimaniaContract.PATH_TALK), talkId).build()
                                , null, null, null, null);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                        vm.tags = "";
                        if (data.moveToFirst()) {
                            final int nameIndex = data.getColumnIndex(MultimaniaContract.TagEntry.NAME);
                            do {
                                vm.tags += data.getString(nameIndex) + ", ";
                            } while (data.moveToNext());
                            if (vm.tags.lastIndexOf(", ") > -1)
                                vm.tags = vm.tags.substring(0, vm.tags.length() - 2);
                        }
                        loader.abandon();
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> loader) {

                    }
                });
                mItems.add(vm);
            } while (c.moveToNext());
        }
        if (mGridview != null) {
            mGridview.setAdapter(new MyScheduleAdapter(getActivity(), 0, mItems));
        }
    }

    private class MyScheduleAdapter extends ArrayAdapter<ScheduleTalkVm> {

        private LayoutInflater mInflater;

        public MyScheduleAdapter(Context context, int resource, List<ScheduleTalkVm> objects) {
            super(context, resource, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.row_myschedule, null);
                Utility.enlargeTouchArea(convertView.findViewById(R.id.myscheduleRowRoot), convertView.findViewById(R.id.btnRemoveTalk), 10);

                convertView.setTag(new MyScheduleRowHolder(convertView));
            }

            ScheduleTalkVm vm = mItems.get(position);
            bindView(convertView, vm);
            return convertView;
        }

        public void bindView(View view, final ScheduleTalkVm vm) {
            MyScheduleRowHolder vh = (MyScheduleRowHolder) view.getTag();

            //room + title
            vh.txtTalkTitle.setText(vm.title);
            vh.txtRoom.setText(vm.room);
            vh.txtTag.setText(vm.tags);
            vh.txtTime.setText(vm.fromString + " - " + vm.untilString);


            //remove button
            vh.btnRemoveTalk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    removeItem(vm);

                    //clear previous undobars when needed
                    if(mUndoBar != null){
                        mUndoBar.clear();
                    }

                    //show undobar
                    mUndoBar = new UndoBarController.UndoBar(getActivity());
                    mUndoBar.message(getActivity().getString(R.string.unfavorite_undobar));
                    mUndoBar.listener(new UndoBarController.UndoListener() {
                        @Override
                        public void onUndo(@Nullable Parcelable parcelable) {
                            addItem(vm);
                        }
                    });
                    mUndoBar.show();
                }
            });
        }

        public void addItem(ScheduleTalkVm vm) {
            updateItemValue(vm.id, true);
            mSettingsHelper.settingsHandler(vm);
        }

        public void removeItem(ScheduleTalkVm vm) {
            updateItemValue(vm.id, false);
            mSettingsHelper.settingsHandler(vm);
        }

        private void updateItemValue(long id, boolean value) {
            ContentValues values = new ContentValues();
            values.put(MultimaniaContract.TalkEntry.IS_FAVORITE, value ? 1 : 0);
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

    }

    private class MyScheduleAsyncQueryHandler extends AsyncQueryHandler {

        public MyScheduleAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }
    }
}

