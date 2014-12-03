package be.ana.nmct.multimania.ui;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchedulesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ActionBar.OnNavigationListener {
    public static final String TAG = SchedulesFragment.class.getSimpleName();

    public List<String> mDates = new ArrayList<String>();//[]{"2014-05-19","2014-05-20"};
    private SchedulesPagerAdapter mViewPagerAdapter;
    private List<String> mTags;
    private ArrayAdapter<String> mListAdapter;

    public SchedulesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(MainActivity.LOADER_SCHEDULE_DATES_ID, null, this);
        getLoaderManager().initLoader(MainActivity.LOADER_TAGS_ID,null,this);
        setRetainInstance(true);
        mTags = new ArrayList<String>();
        mListAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,mTags);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedules, container, false);
        ViewPager pager = (ViewPager) v.findViewById(R.id.schedulesPager);
        mViewPagerAdapter = new SchedulesPagerAdapter(getFragmentManager());
        pager.setAdapter(mViewPagerAdapter);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*ActionBar bar = getActivity().getActionBar();
        if(bar!=null){
            bar.setListNavigationCallbacks(mListAdapter,this);
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        }*/
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case MainActivity.LOADER_SCHEDULE_DATES_ID:
                return new CursorLoader(getActivity(),MultimaniaContract.TalkEntry.DATE_CONTENT_URI,null,null,null,null);

            case MainActivity.LOADER_TAGS_ID:
                return new CursorLoader(getActivity(),MultimaniaContract.TagEntry.CONTENT_URI,null,null,null,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch(loader.getId()){
            case MainActivity.LOADER_SCHEDULE_DATES_ID:
                addDates(cursor);
                break;
            case MainActivity.LOADER_TAGS_ID: {
                addTags(cursor);
                break;
            }
        }
    }

    private void addTags(Cursor cursor) {
        if(cursor.moveToFirst()){
            final int nameIndex = cursor.getColumnIndex(MultimaniaContract.TagEntry.NAME);
            do{
                mTags.add(cursor.getString(nameIndex));
            }while(cursor.moveToNext());
            mListAdapter.notifyDataSetChanged();
        }
    }

    private void addDates(Cursor cursor) {
        int dayIndex = cursor.getColumnIndex(MultimaniaContract.TalkEntry.DAY);
        mDates.clear();
        while(cursor.moveToNext()){
            String date = cursor.getString(dayIndex);
            mDates.add(date);
        }
        mViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDates.clear();
        mViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        for(int i = 0; i < mDates.size();i++){
            ScheduleFragment frag = (ScheduleFragment)  mViewPagerAdapter.getItem(i);
            frag.onFilterChanged(mTags.get(itemPosition));
        }
        return true;
    }

    private class SchedulesPagerAdapter extends FragmentStatePagerAdapter {

        private final String mDayString;
        private Configuration configuration;

        public SchedulesPagerAdapter(FragmentManager fm) {
            super(fm);
            mDayString = getString(R.string.day);
            configuration = getResources().getConfiguration();
        }

        @Override
        public Fragment getItem(int i) {
            return ScheduleFragment.newInstance(mDates.get(i),i);
        }

        @Override
        public int getCount() {
            return mDates.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mDayString+ " "+(position+1);
        }
    }
}
