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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.ApiActions;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.GoogleCalUtil;
import be.ana.nmct.multimania.utils.SettingsUtil;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.ScheduleGridHeader;
import be.ana.nmct.multimania.vm.ScheduleGridItem;

public class ScheduleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    public static final String TAG = ScheduleFragment.class.getSimpleName();
    public static final String DATE_KEY = "date_key";
    public static final String POSITION_KEY = "position_key";

    private AsymmetricGridView mScheduleGrid;
    private ScheduleAdapter mAdapter;
    private ArrayList<AsymmetricItem> mItems;
    private int mAmountOfColumns=6;
    private int mAmountOfScheduleItemColums=6;
    private int mAmountofScheduleItemRows=6;
    private Cursor mCursor;
    private String mDate;
    private int mPosition;
    private String mAccountName;

    public ScheduleFragment() {}

    public static ScheduleFragment newInstance(String date,int position) {
        ScheduleFragment fragmentFirst = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(DATE_KEY, date);
        args.putInt(POSITION_KEY,position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDate = getArguments().getString(DATE_KEY);
        mPosition = getArguments().getInt(POSITION_KEY);
        Loader loader = getLoaderManager().initLoader(MainActivity.LOADER_SCHEDULE_TALK_ID+mPosition, null, this);
        mItems = new ArrayList<AsymmetricItem>();
        mAccountName = new SettingsUtil(getActivity(), GoogleCalUtil.PREFERENCE_NAME).getStringPreference(GoogleCalUtil.PREFERENCE_ACCOUNTNAME);
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
        mScheduleGrid.setOnItemClickListener(this);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MultimaniaContract.TalkEntry.CONTENT_URI,null,
                MultimaniaContract.TalkEntry.DATE_FROM + " LIKE ?"
                ,new String[]{mDate+"%"}, MultimaniaContract.TalkEntry.DATE_FROM);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        createGridItems(mCursor);
        if(mScheduleGrid!=null)
        {
            mScheduleGrid.determineColumns();
            mScheduleGrid.setAdapter(mAdapter);
        }

        if(mAdapter!=null)
            mAdapter.notifyDataSetChanged();
        loader.abandon();
    }

    private void createGridItems(Cursor cursor) {
        if(cursor==null||mItems==null)return;
        mItems.clear();
        List<String> dates = new ArrayList<String>();
        final int dateFromIndex     = cursor.getColumnIndex(MultimaniaContract.TalkEntry.DATE_FROM);
        final int dateUntilIndex    = cursor.getColumnIndex(MultimaniaContract.TalkEntry.DATE_UNTIL);
        final int isKeynoteIndex    = cursor.getColumnIndex(MultimaniaContract.TalkEntry.IS_KEYNOTE);
        final int isFavoriteIndex   = cursor.getColumnIndex(MultimaniaContract.TalkEntry.IS_FAVORITE);
        final int titleIndex        = cursor.getColumnIndex(MultimaniaContract.TalkEntry.TITLE);
        final int roomIndex         = cursor.getColumnIndex(MultimaniaContract.TalkEntry.ROOM_NAME);
        final int idIndex           = cursor.getColumnIndex(MultimaniaContract.TalkEntry._ID);
        if(cursor.moveToFirst())
        {
            do{
                String dateFrom = cursor.getString(dateFromIndex);
                String dateUntil = cursor.getString(dateUntilIndex);
                boolean isKeynote = cursor.getInt(isKeynoteIndex)==1; //Boolean.parseBoolean(cursor.getString(isKeynoteIndex));
                boolean isFavorite = cursor.getInt(isFavoriteIndex)==1;
                String title = cursor.getString(titleIndex);
                String room = cursor.getString(roomIndex);
                final long talkId = cursor.getLong(idIndex);
                if(!dates.contains(dateFrom)){
                    try {
                        mItems.add(new ScheduleGridHeader(mAmountOfColumns,mItems.size(),
                                        Utility.convertStringToDate(dateFrom),
                                        Utility.convertStringToDate(dateUntil))
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
                item.isKeynote=isKeynote;
                item.isFavorite=isFavorite;
                getLoaderManager().initLoader(1000+(int)talkId,null,new LoaderManager.LoaderCallbacks<Cursor>() {
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
                        loader.abandon();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object item =  mAdapter.getItem(position);
        if(item instanceof ScheduleGridItem){
            ScheduleGridItem gridItem = (ScheduleGridItem) item;
            Uri uri = MultimaniaContract.TalkEntry.buildItemUri(((ScheduleGridItem) item).id);
            Intent intent = new Intent(getActivity(),TalkActivity.class);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private class ScheduleAdapter extends AsymmetricGridViewAdapter<AsymmetricItem> {

        private static final int SCHEDULE_GRID_HEADER_TYPE = 0;
        private static final int SCHEDULE_GRID_ITEM_TYPE = 1;
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

        private void bindItemView(View view,final ScheduleGridItem item) {
            ((TextView)view.findViewById(R.id.txtTitle)).setText(item.title);
            final ImageButton imgButton = (ImageButton) view.findViewById(R.id.imageButton);
            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.isFavorite=!item.isFavorite;
                    int value = item.isFavorite?1:0;
                    imgButton.setImageResource(getStarDrawabale(item.isFavorite));

                    ContentValues values = new ContentValues();
                    values.put(MultimaniaContract.TalkEntry.IS_FAVORITE,value);
                    AsyncQueryHandler handler = new ScheduleAsyncQueryHandler(getActivity().getContentResolver());
                    handler.startUpdate(
                            0,
                            null,
                            MultimaniaContract.TalkEntry.CONTENT_URI,
                            values,
                            MultimaniaContract.TalkEntry._ID+"=?",
                            new String[]{""+item.id}
                    );
                    if(mAccountName!=null){
                        ApiActions.postFavoriteTalk(getActivity(),mAccountName,item.id);
                    }
                }
            });
            imgButton.setImageResource(getStarDrawabale(item.isFavorite));
            ((TextView) view.findViewById(R.id.txtRoom)).setText(item.room);
            ((TextView) view.findViewById(R.id.txtTag)).setText(item.tags);
        }

        private int getStarDrawabale(boolean isFavorite) {
            return  isFavorite  ? android.R.drawable.star_on :  android.R.drawable.star_off;
        }

        private void bindHeaderView(View convertView, ScheduleGridHeader item) {
            try {
                ((TextView)convertView).setText(
                        Utility.getTimeString(item.getDateFrom())
                        +" - "+
                        Utility.getTimeString(item.getDateUntil())
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
    private class ScheduleAsyncQueryHandler extends AsyncQueryHandler{

        public ScheduleAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }
    }
}
