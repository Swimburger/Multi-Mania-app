package be.ana.nmct.multimania.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.ana.nmct.multimania.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchedulesFragment extends Fragment {
    public static final String TAG = SchedulesFragment.class.getSimpleName();

    //TODO: get dates from database group by
    public String[] mDates = new String[]{"2014-05-19","2014-05-20"};
    private SchedulesPagerAdapter mViewPagerAdapter;

    public SchedulesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    private class SchedulesPagerAdapter extends FragmentPagerAdapter {

        private final String mDayString;

        public SchedulesPagerAdapter(FragmentManager fm) {
            super(fm);
            mDayString = getString(R.string.day);
        }

        @Override
        public Fragment getItem(int i) {
            return ScheduleFragment.newInstance(mDates[i]);
        }

        @Override
        public int getCount() {
            return mDates.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mDayString+ " "+(position+1);
        }
    }
}
