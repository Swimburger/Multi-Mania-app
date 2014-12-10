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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cocosw.undobar.UndoBarController;
import com.cocosw.undobar.UndoBarStyle;
import com.etsy.android.grid.StaggeredGridView;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.util.Insertable;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.SettingsHelper;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.ScheduleTalkVm;

/**
 * This fragment show the favorited talks of the user, sorted in a pageradapter per day
 */
public class MyScheduleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private final String TAG = this.getClass().getCanonicalName();

    public static final String DATE_KEY = "date_key";
    public static final String POSITION_KEY = "position_key";

    private String mDate;
    private int mPosition;

    private MyScheduleAdapter mMyScheduleAdapter;
    private StaggeredGridView mGridview;
    public UndoBarController.UndoBar mUndoBar;
    private UndoBarStyle mUndoBarStyle;
    private SettingsHelper mSettingsHelper;
    private TextView mPlaceholder;
    private ImageView mPlaceholderImg;

    private LocalTime mLunchbreakStart;
    private int mRowHeight = 0;

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
        this.mItems = new ArrayList<>();
        this.mSettingsHelper = new SettingsHelper(getActivity());
        this.mUndoBarStyle = new UndoBarStyle(0, R.string.undo_item, 0, 2000);
        this.mLunchbreakStart = Utility.convertStringToLocalTime(getString(R.string.lunchbreak_starttime));
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getLoaderManager().initLoader(MainActivity.LOADER_MYSCHEDULE_TALK_ID + mPosition, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_schedule, container, false);

        mPlaceholder = (TextView) v.findViewById(R.id.placeholder);
        mPlaceholderImg = (ImageView) v.findViewById(R.id.placeholderIcon);
        mGridview = (StaggeredGridView) v.findViewById(R.id.gridViewMySchedule);
        mGridview.setAdapter(mMyScheduleAdapter);
        mGridview.setOnItemClickListener(this);

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), MultimaniaContract.TalkEntry.CONTENT_URI,
                null,
                MultimaniaContract.TalkEntry.IS_FAVORITE + "=1 AND " +
                        MultimaniaContract.TalkEntry.DATE_FROM + " LIKE ?"
                , new String[]{mDate + "%"}, MultimaniaContract.TalkEntry.DATE_FROM);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        BuildItemsAsync task = new BuildItemsAsync();
        task.doInBackground(cursor);
        loader.abandon();


        if (mItems == null || mItems.size() <= 0) {
            mPlaceholder.setVisibility(View.VISIBLE);
            mPlaceholderImg.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        if (mItems != null) {
            mItems.clear();
        }
        if (mMyScheduleAdapter != null) {
            mMyScheduleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        ScheduleTalkVm vm = mItems.get(position);
        if(!vm.isSuggestionItem){
            Uri uri = MultimaniaContract.TalkEntry.buildItemUri(vm.id);
            Intent intent = new Intent(getActivity(), TalkActivity.class);
            intent.setData(uri);
            startActivity(intent);
        }
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
        final int tagsIndex = c.getColumnIndex(MultimaniaContract.TalkEntry.TAGS);
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
                vm.to = Utility.convertStringToDate(c.getString(dateUntilIndex));
                vm.description = c.getString(descriptionIndex);
                vm.tags = c.getString(tagsIndex);

                vm.isDoubleBooked = checkDoubleBookings(vm);

                mItems.add(vm);
            } while (c.moveToNext());
        }

        checkForTimeGaps();

        if (mGridview != null) {
            mMyScheduleAdapter = new MyScheduleAdapter(getActivity(), 0, mItems);
            ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(mMyScheduleAdapter);
            animationAdapter.setAbsListView(mGridview);
            mGridview.setAdapter(animationAdapter);
        }
    }

    private void checkForTimeGaps() {
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.size() >= i + 1) {
                DateTime start = new DateTime(mItems.get(i).to);
                start = start.plusMinutes(getResources().getInteger(R.integer.time_gap_between_talks_in_minutes));
                DateTime end = new DateTime(mItems.get(i + 1).from);

                if (!start.equals(end) && start.toLocalTime() != mLunchbreakStart) {
                    ScheduleTalkVm vm = new ScheduleTalkVm();
                    vm.isSuggestionItem = true;
                    mItems.add(i + 1, vm);
                    break;
                }

            }
        }
    }

    private boolean checkDoubleBookings(ScheduleTalkVm vm) {
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).from.equals(vm.from)) {
                mItems.get(i).isDoubleBooked = true;
                return true;
            }
        }
        return false;
    }

    private class MyScheduleAdapter extends ArrayAdapter<ScheduleTalkVm> implements Insertable<ScheduleTalkVm> {

        private LayoutInflater mInflater;
        private ScheduleTalkVm mRemovedItem;

        public MyScheduleAdapter(Context context, int resource, List<ScheduleTalkVm> objects) {
            super(context, resource, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ScheduleTalkVm vm = mItems.get(position);

            if (convertView == null) {
                if (!vm.isSuggestionItem) {
                    convertView = mInflater.inflate(R.layout.row_myschedule, null);
                    Utility.enlargeTouchArea(convertView.findViewById(R.id.myscheduleRowRoot), convertView.findViewById(R.id.btnRemoveTalk), 10);
                    convertView.setTag(new MyScheduleRowHolder(convertView));
                } else {
                    convertView = mInflater.inflate(R.layout.row_myschedule_suggestion, null);
                    convertView.setTag(new MyScheduleSuggestionRowHolder(convertView));
                }
            }


            bindView(convertView, vm);
            return convertView;
        }

        public void bindView(View view, final ScheduleTalkVm vm) {

            if (!vm.isSuggestionItem) {

                MyScheduleRowHolder vh = (MyScheduleRowHolder) view.getTag();

                //room + title
                vh.txtTalkTitle.setText(vm.title);
                vh.txtRoom.setText(vm.room);
                vh.txtTag.setText(vm.tags);
                vh.txtTime.setText(vm.fromString + " - " + vm.untilString);

                if (vm.isDoubleBooked) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        vh.root.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.double_booking_myschedule));
                    } else {
                        vh.root.setBackground(getActivity().getResources().getDrawable((R.drawable.double_booking_myschedule)));
                    }
                    vh.txtTime.setTextColor(getActivity().getResources().getColor(R.color.danger));
                    vh.bottomBorder.setBackgroundColor(getActivity().getResources().getColor(R.color.danger));
                } else {
                    vh.root.setBackgroundColor(getActivity().getResources().getColor((R.color.white)));
                    vh.txtTime.setTextColor(getActivity().getResources().getColor(R.color.fontcolor));
                    vh.bottomBorder.setBackgroundColor(getActivity().getResources().getColor(R.color.primaryColor));
                }

                //remove button
                vh.btnRemoveTalk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        removeItem(vm);

                        //clear previous undobars when needed
                        if (mUndoBar != null) {
                            mUndoBar.clear();
                        }

                        //show undobar
                        mUndoBar = new UndoBarController.UndoBar(getActivity());
                        mUndoBar.style(mUndoBarStyle);
                        mUndoBar.message(getActivity().getString(R.string.unfavorite_undobar));
                        mUndoBar.listener(new UndoBarController.UndoListener() {
                            @Override
                            public void onUndo(@Nullable Parcelable parcelable) {
                                add(0, mRemovedItem);
                            }
                        });
                        mUndoBar.show();
                    }
                });

                //get height of the view so we can use it for the suggestion items
                mRowHeight = view.getMeasuredHeight();

            } else {
                //TODO: inflate suggestion item
                MyScheduleSuggestionRowHolder vh = (MyScheduleSuggestionRowHolder)view.getTag();
                vh.root.setMinimumHeight(mRowHeight);
            }

        }

        public void removeItem(ScheduleTalkVm vm) {
            updateItemValue(vm.id, false);
            mRemovedItem = vm;
            mItems.remove(vm);
            remove(vm);
            vm.isFavorite = false;
            vm.isDoubleBooked = checkDoubleBookings(vm);
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

        @Override
        public void add(int i, @NonNull ScheduleTalkVm vm) {
            updateItemValue(vm.id, true);
            vm.isFavorite = true;
            vm.isDoubleBooked = checkDoubleBookings(vm);
            mPlaceholder.setVisibility(View.INVISIBLE);
            mPlaceholderImg.setVisibility(View.INVISIBLE);
            mSettingsHelper.settingsHandler(vm);

            super.add(vm);
        }
    }

    private class MyScheduleRowHolder {

        public TextView txtTalkTitle;
        public TextView txtRoom;
        public TextView txtTime;
        public TextView txtTag;
        public ImageView btnRemoveTalk;
        public RelativeLayout root;
        public View bottomBorder;

        public MyScheduleRowHolder(View v) {
            this.txtTalkTitle = (TextView) v.findViewById(R.id.txtTalkTitle);
            this.txtRoom = (TextView) v.findViewById(R.id.txtRoom);
            this.txtTime = (TextView) v.findViewById(R.id.txtTime);
            this.txtTag = (TextView) v.findViewById(R.id.txtTag);
            this.btnRemoveTalk = (ImageView) v.findViewById(R.id.btnRemoveTalk);
            this.root = (RelativeLayout) v.findViewById(R.id.myscheduleRowRoot);
            this.bottomBorder = v.findViewById(R.id.borderBottom);
        }
    }

    private class MyScheduleSuggestionRowHolder {

        private RelativeLayout root;

        public MyScheduleSuggestionRowHolder(View v) {
            this.root = (RelativeLayout)v.findViewById(R.id.suggestionContainer);
        }

    }

    private class MyScheduleAsyncQueryHandler extends AsyncQueryHandler {
        public MyScheduleAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }
    }

    private class BuildItemsAsync extends AsyncTask<Cursor, Void, Void> {

        @Override
        protected Void doInBackground(Cursor... c) {
            try {
                buildItems(c[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

