package be.ana.nmct.multimania.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.ApiActions;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.service.SyncAdapter;
import be.ana.nmct.multimania.utils.GoogleCalUtil;
import be.ana.nmct.multimania.utils.SettingsHelper;
import be.ana.nmct.multimania.utils.SettingsUtil;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.ScheduleTalkVm;

/**
 * This fragment show the favorited talks of the user, sorted in a pageradapter per day
 */
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
    private UndoBarStyle mUndoBarStyle;
    private SettingsHelper mSettingsHelper;
    private TextView mPlaceholder;
    private ImageView mPlaceholderImg;

    private List<ScheduleTalkVm> mItems;
    private String mAccountName;

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
        mItems = new ArrayList<ScheduleTalkVm>();
        mSettingsHelper = new SettingsHelper(getActivity());
        mUndoBarStyle = new UndoBarStyle(0,R.string.undo_item, 0,2000);
        mAccountName = new SettingsUtil(getActivity(), GoogleCalUtil.PREFERENCE_NAME).getStringPreference(GoogleCalUtil.PREFERENCE_ACCOUNTNAME);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SyncAdapter.SYNC_READY_BROADCAST);
        activity.registerReceiver(syncCompleteReceiver, intentFilter);
    }

    @Override
    public void onDetach() {
        getActivity().unregisterReceiver(syncCompleteReceiver);
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getLoaderManager().initLoader(MainActivity.LOADER_MY_SCHEDULE_TALK_ID + mPosition, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_schedule, container, false);

        mPlaceholder = (TextView)v.findViewById(R.id.placeholder);
        mPlaceholderImg = (ImageView)v.findViewById(R.id.placeholderIcon);
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
        try {
            buildItems(cursor);
            loader.abandon();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(mItems == null || mItems.size() <= 0){
            mPlaceholder.setVisibility(View.VISIBLE);
            mPlaceholderImg.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        if(mItems!=null){
            mItems.clear();
        }
        if(mMyScheduleAdapter!=null){
            mMyScheduleAdapter.notifyDataSetChanged();
        }
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
        final int tagsIndex         = c.getColumnIndex(MultimaniaContract.TalkEntry.TAGS);
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

                vm.tags = c.getString(tagsIndex);

                vm.isDoubleBooked = checkDoubleBookings(vm);
                mItems.add(vm);
            } while (c.moveToNext());
        }
        if (mGridview != null) {
            mMyScheduleAdapter = new MyScheduleAdapter(getActivity(), 0, mItems);
            ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(mMyScheduleAdapter);
            animationAdapter.setAbsListView(mGridview);
            mGridview.setAdapter(animationAdapter);
        }
    }

    private boolean checkDoubleBookings(ScheduleTalkVm vm) {
        for(int i = 0; i < mItems.size(); i++){
            if(mItems.get(i).from.equals(vm.from)){
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

            if(vm.isDoubleBooked){
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
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
            if(mAccountName!=null){
                if(value){
                    ApiActions.postFavoriteTalk(getActivity(), mAccountName, id);
                }else{
                    ApiActions.deleteFavoriteTalk(getActivity(), mAccountName, id);
                }
            }
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

    private class MyScheduleRowHolder{

        public TextView txtTalkTitle;
        public TextView txtRoom;
        public TextView txtTime;
        public TextView txtTag;
        public ImageView btnRemoveTalk;
        public RelativeLayout root;
        public View bottomBorder;

        public MyScheduleRowHolder(View v) {
            this.txtTalkTitle = (TextView)v.findViewById(R.id.txtTalkTitle);
            this.txtRoom = (TextView)v.findViewById(R.id.txtRoom);
            this.txtTime = (TextView)v.findViewById(R.id.txtTime);
            this.txtTag = (TextView)v.findViewById(R.id.txtTag);
            this.btnRemoveTalk = (ImageView)v.findViewById(R.id.btnRemoveTalk);
            this.root = (RelativeLayout)v.findViewById(R.id.myscheduleRowRoot);
            this.bottomBorder = v.findViewById(R.id.borderBottom);
        }
    }

    private class MyScheduleAsyncQueryHandler extends AsyncQueryHandler {
        public MyScheduleAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }
    }

    private BroadcastReceiver syncCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Sync ready received");
            getLoaderManager().restartLoader(MainActivity.LOADER_MY_SCHEDULE_TALK_ID,null,MyScheduleFragment.this);
        }
    };
}

