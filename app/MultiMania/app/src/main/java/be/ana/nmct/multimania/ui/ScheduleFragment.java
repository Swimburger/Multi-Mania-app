package be.ana.nmct.multimania.ui;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.ScheduleGridHeader;
import be.ana.nmct.multimania.vm.ScheduleGridItem;

public class ScheduleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = ScheduleFragment.class.getSimpleName();
    public static final String TITLE_KEY = "title_key";
    public static final String PAGE_KEY = "title_key";
    public static final String DATE_KEY = "date_key";

    private AsymmetricGridView mScheduleGrid;
    private ScheduleAdapter mAdapter;
    private ArrayList<AsymmetricItem> mItems;
    private int mAmountOfColumns=6;
    private int mAmountOfScheduleItemColums=6;
    private int mAmountofScheduleItemRows=6;
    private Cursor mCursor;
    private String mDate;

    public ScheduleFragment() {}

    public static ScheduleFragment newInstance(String date) {
        ScheduleFragment fragmentFirst = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(DATE_KEY, date);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDate = getArguments().getString(DATE_KEY);
        getLoaderManager().initLoader(0, null, this);
        mItems = new ArrayList<AsymmetricItem>();

        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        mScheduleGrid = (AsymmetricGridView) v.findViewById(R.id.scheduleGrid);

        mAmountOfColumns = getResources().getInteger(R.integer.amount_of_schedule_columns);
        mAmountOfScheduleItemColums = getResources().getInteger(R.integer.amount_of_schedule_item_columns);
        mAmountofScheduleItemRows = getResources().getInteger(R.integer.amount_of_schedule_item_rows);

        // Choose your own preferred column width
        //mScheduleGrid.setRequestedColumnWidth(Utils.dpToPx(getActivity(), 120));
        mScheduleGrid.setRequestedColumnCount(mAmountOfColumns);
        mScheduleGrid.setRequestedHorizontalSpacing(8);

        // initialize your items array
        mAdapter = new ScheduleAdapter(getActivity(), mScheduleGrid, mItems);

        createGridItems(mCursor);
        mScheduleGrid.determineColumns();
        mScheduleGrid.setAdapter(mAdapter);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MultimaniaContract.TalkEntry.CONTENT_URI,null,
                MultimaniaContract.TalkEntry.TABLE_NAME + "." + MultimaniaContract.TalkEntry.DATE_FROM + " LIKE ?"
                ,new String[]{mDate+"%"},null);
        //return new CursorLoader(getActivity(), MultimaniaContract.TalkEntry.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        createGridItems(mCursor);
        mScheduleGrid.determineColumns();
        mAdapter.notifyDataSetChanged();
        mScheduleGrid.setAdapter(mAdapter);
    }

    private void createGridItems(Cursor cursor) {
        if(cursor==null)return;
        mItems.clear();
        List<String> dates = new ArrayList<String>();
        final int dateFromIndex     = cursor.getColumnIndex(MultimaniaContract.TalkEntry.DATE_FROM);
        final int dateUntilIndex    = cursor.getColumnIndex(MultimaniaContract.TalkEntry.DATE_UNTIL);
        final int isKeynoteIndex    = cursor.getColumnIndex(MultimaniaContract.TalkEntry.IS_KEYNOTE);
        final int titleIndex        = cursor.getColumnIndex(MultimaniaContract.TalkEntry.TITLE);
        final int roomIndex         = cursor.getColumnIndex(MultimaniaContract.TalkEntry.ROOM_NAME);
        final int idIndex         = cursor.getColumnIndex(MultimaniaContract.TalkEntry._ID);
        if(cursor.moveToFirst())
        {
            do{
                String dateFrom = cursor.getString(dateFromIndex);
                Log.d(TAG,"Date:"+dateFrom);
                String dateUntil = cursor.getString(dateUntilIndex);
                boolean isKeynote = cursor.getInt(isKeynoteIndex)==1; //Boolean.parseBoolean(cursor.getString(isKeynoteIndex));
                String title = cursor.getString(titleIndex);
                String room = cursor.getString(roomIndex);
                final long talkId = cursor.getLong(idIndex);
                if(!dates.contains(dateFrom)){
                    try {
                        mItems.add(new ScheduleGridHeader(mAmountOfColumns,mItems.size(),
                                        Utility.ConvertStringToDate(dateFrom),
                                        Utility.ConvertStringToDate(dateUntil))
                        );
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dates.add(dateFrom);
                }
                int scheduleGridColumnSpan = mAmountOfScheduleItemColums;
                if(isKeynote)
                    scheduleGridColumnSpan = mAmountOfColumns;
                final ScheduleGridItem item = new ScheduleGridItem(scheduleGridColumnSpan,mAmountofScheduleItemRows, mItems.size());
                item.title=title;
                item.room=room;
                item.id=talkId;
                getLoaderManager().restartLoader(100+(int)talkId,null,new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        return new CursorLoader(getActivity(),
                                ContentUris.appendId(MultimaniaContract.TagEntry.CONTENT_URI.buildUpon()
                                .appendPath(MultimaniaContract.PATH_TALK),talkId).build()
                                ,null,null,null,null);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                        item.tags = "";
                        if(data.moveToFirst()){
                            final int nameIndex = data.getColumnIndex(MultimaniaContract.TagEntry.NAME);
                            do{
                                item.tags +=data.getString(nameIndex)+", ";
                            }while(data.moveToNext());
                            if(item.tags.lastIndexOf(", ")>-1)
                                item.tags=item.tags.substring(0,item.tags.length()-2);
                        }

                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> loader) {

                    }
                });


                mItems.add(item);
            }while(cursor.moveToNext());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //mScheduleGrid.setAdapter(null);
    }

    private class ScheduleAdapter extends AsymmetricGridViewAdapter<AsymmetricItem> {

        private static final int SCHEDULE_GRID_HEADER_TYPE = 0;
        private static final int SCHEDULE_GRID_ITEM_TYPE = 1;
        private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        private final LayoutInflater mInflater;

        public ScheduleAdapter(Context context, AsymmetricGridView listView, List<AsymmetricItem> items) {
            super(context, listView, items);
            mInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public View getActualView(int position, View convertView, ViewGroup viewGroup) {

            AsymmetricItem item = mItems.get(position);
            final int viewType = getItemViewType(position);
            if (convertView == null||!isCorrectType(viewType,convertView)) {
                convertView = newView(viewType);
            }
            bindView(convertView,item,viewType);

            return convertView;
        }

        private boolean isCorrectType(final int viewType,View convertView) {
            Class cls = convertView.getClass();
            switch (viewType){
                case SCHEDULE_GRID_HEADER_TYPE:
                    return cls == TextView.class;
                case SCHEDULE_GRID_ITEM_TYPE:
                    return cls == RelativeLayout.class;
                default:
                    return false;
            }

        }

        private void bindView(View convertView, AsymmetricItem item,final int viewType) {
            switch (viewType){
                case SCHEDULE_GRID_HEADER_TYPE:
                    bindHeaderView(convertView,(ScheduleGridHeader)item);
                    break;
                case SCHEDULE_GRID_ITEM_TYPE:
                    bindItemView(convertView, (ScheduleGridItem) item);
                    break;
            }
        }

        private void bindItemView(View view, ScheduleGridItem item) {
            ((TextView)view.findViewById(R.id.txtTitle)).setText(item.title);
            ImageButton imgButton = (ImageButton) view.findViewById(R.id.imageButton);
            ((TextView) view.findViewById(R.id.txtRoom)).setText(item.room);
            ((TextView) view.findViewById(R.id.txtTag)).setText(item.tags);
        }

        private void bindHeaderView(View convertView, ScheduleGridHeader item) {
            ((TextView)convertView).setText(
                    timeFormat.format(item.getDateFrom())
                    +" - "+
                    timeFormat.format(item.getDateUntil())
            );
        }

        private View newView(final int viewType) {
            View view = null;
            switch(viewType){
                case SCHEDULE_GRID_HEADER_TYPE:
                    view = mInflater.inflate(R.layout.row_schedule_header,null);
                    break;
                case SCHEDULE_GRID_ITEM_TYPE:
                    view = mInflater.inflate(R.layout.row_schedule,null);
                    break;
            }
            return view;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            AsymmetricItem item = getItem(position);
            if(item instanceof ScheduleGridHeader){
                return SCHEDULE_GRID_HEADER_TYPE;
            }else{
                return SCHEDULE_GRID_ITEM_TYPE;
            }
        }
    }
}
