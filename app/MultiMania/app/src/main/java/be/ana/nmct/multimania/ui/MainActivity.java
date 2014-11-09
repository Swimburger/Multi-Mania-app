package be.ana.nmct.multimania.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import be.ana.nmct.multimania.R;




public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }
    
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        boolean isNull = fragment==null;
        boolean isNotRetained = true;
        switch (position) {
            case 0:
                /*isNotRetained = (isNull||!(fragment instanceof  ScheduleFragment));
                if(isNotRetained)*/
                    fragment = new ScheduleFragment();
                break;
            case 1:
                fragment = new MyScheduleFragment();
                break;
            case 2:
                fragment = new MapFragment();
                break;
            case 3:
                isNotRetained=(isNull||!(fragment instanceof NewsFragment));
                if(isNotRetained){
                    fragment = new NewsFragment();
                }
                break;
            case 4:
                fragment = new AboutFragment();
                break;
            case 5:
                fragment = new SettingsFragment();
                break;
        }
        if(isNotRetained){
            fragmentManager.beginTransaction().replace(R.id.container,fragment).commit();
        }
        onSectionAttached(position + 1);
    }
    public void onSectionAttached(int number) {

        switch (number) {
            case 0:
                mTitle = getString(R.string.title_schedule);
                break;
            case 1:
                mTitle = getString(R.string.title_myschedule);
                break;
            case 2:
                mTitle = getString(R.string.title_map);
                break;
            case 3:
                mTitle = getString(R.string.title_news);
                break;
            case 4:
                mTitle = getString(R.string.title_about);
                break;
            case 5:
                mTitle = getString(R.string.title_settings);
                break;
            //add more cases here
        }
        getActionBar().setTitle(mTitle);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
