package be.ana.nmct.multimania.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
<<<<<<< HEAD:app/MultiMania/app/src/main/java/be/ana/nmct/multimania/ui/ScheduleActivity.java
import android.util.Log;
import android.view.LayoutInflater;
=======
import android.support.v4.widget.DrawerLayout;
>>>>>>> 0644150d4819ae86720320c77aa49de511e2ca67:app/MultiMania/app/src/main/java/be/ana/nmct/multimania/ui/MainActivity.java
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import be.ana.nmct.multimania.R;
<<<<<<< HEAD:app/MultiMania/app/src/main/java/be/ana/nmct/multimania/ui/ScheduleActivity.java
import be.ana.nmct.multimania.data.ApiService;
import be.ana.nmct.multimania.data.AsyncResponse;
import be.ana.nmct.multimania.data.GsonLoader;
import be.ana.nmct.multimania.model.NewsItem;
import be.ana.nmct.multimania.model.Speaker;
import be.ana.nmct.multimania.model.Talk;
import be.ana.nmct.multimania.utils.GoogleCalUtil;
=======
>>>>>>> 0644150d4819ae86720320c77aa49de511e2ca67:app/MultiMania/app/src/main/java/be/ana/nmct/multimania/ui/MainActivity.java


public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        AsyncResponse response = new AsyncResponse() {
            @Override
            public void onTaskCompleted(Object result) {
                Log.d("", result.toString());
            }
        };

        ApiService<NewsItem> newsService = new ApiService<NewsItem>(this, "news", this.getLoaderManager(), response);
        newsService.loadData();

    }
    
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        switch (position) {
            case 0:

                break;
            case 1:
                fragmentManager.beginTransaction().replace(R.id.container, new MyScheduleFragment()).commit();
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.container, new MapFragment()).commit();
                break;
            case 3:
                fragmentManager.beginTransaction().replace(R.id.container, new NewsFragment()).commit();
                break;
            case 4:
                fragmentManager.beginTransaction().replace(R.id.container, new AboutFragment()).commit();
                break;
            case 5:
                fragmentManager.beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
                break;
        }
    }
    public void onSectionAttached(int number) {

        switch (number) {
            case 1:
                mTitle = getString(R.string.title_schedule);
                break;
            case 2:
                mTitle = getString(R.string.title_myschedule);
                break;
            case 3:
                mTitle = getString(R.string.title_map);
                break;
            case 4:
                mTitle = getString(R.string.title_news);
                break;
            case 5:
                mTitle = getString(R.string.title_about);
                break;
            case 6:
                mTitle = getString(R.string.title_settings);
                break;
            //add more cases here
    }
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
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.schedule, menu);
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
