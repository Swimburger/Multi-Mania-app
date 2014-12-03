package be.ana.nmct.multimania.ui;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.service.NotificationSender;
import be.ana.nmct.multimania.utils.GoogleCalUtil;
import be.ana.nmct.multimania.utils.SettingsUtil;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.ScheduleTalkVm;

public class SettingsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String TAG = SettingsFragment.class.getCanonicalName();

    private static final int REQUEST_CODE_EMAIL = 1;

    private static String mCalendarName;

    public static final String PREFERENCE_NAME = "setting_values";
    public static final String PREFERENCE_NOTIFY = "setting_notify";
    public static final String PREFERENCE_SYNC = "setting_sync";

    private static CheckBox mChkNotify;
    private static CheckBox mChkSync;
    private static SettingsUtil mUtil;
    private static GoogleCalUtil mCalUtil;
    private static NotificationSender mNotificationSender;

    private List<ScheduleTalkVm> mTalkList;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mCalendarName = this.getActivity().getResources().getString(R.string.calendar_name);
        this.mUtil = new SettingsUtil(this.getActivity(), PREFERENCE_NAME);
        this.mCalUtil = new GoogleCalUtil(this.getActivity(), mCalendarName);
        this.mTalkList = new ArrayList<ScheduleTalkVm>();
        this.mNotificationSender = new NotificationSender(getActivity());
        this.getLoaderManager().initLoader(MainActivity.LOADER_SETTINGS, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        mChkNotify = (CheckBox) v.findViewById(R.id.checkbox_notify);
        mChkNotify.setChecked(mUtil.getBooleanPreference(PREFERENCE_NOTIFY, false));

        mChkSync = (CheckBox) v.findViewById(R.id.checkbox_sync);
        mChkSync.setChecked(mUtil.getBooleanPreference(PREFERENCE_SYNC, false));


        mChkNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    if(mTalkList != null && mTalkList.size() > 0){
                        mNotificationSender.setAlarmForTalkList(mTalkList);
                    }
                } else {
                        mNotificationSender.cancelAlarmForTalkList(mTalkList);
                }

                mUtil.setPreference(PREFERENCE_NOTIFY, isChecked);
            }
        });

        mChkSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                mUtil.setPreference(PREFERENCE_SYNC, isChecked);

                if (isChecked) {
                    Toast.makeText(getActivity(), "Please choose your Google Account", Toast.LENGTH_LONG).show();
                    askUserEmail();

                    if(mTalkList != null && mTalkList.size() > 0){
                        mCalUtil.addTalkList(mTalkList);
                    }

                } else {
                    mCalUtil.deleteCalendar();
                }
            }
        });


        return v;
    }

    private void buildItems(Cursor c) throws ParseException {
        if (c == null || mTalkList == null) return;
        mTalkList.clear();

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

                mTalkList.add(vm);
            } while (c.moveToNext());
        }
    }

    private void askUserEmail() {
        try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
            startActivityForResult(intent, REQUEST_CODE_EMAIL);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EMAIL && data != null && resultCode == Activity.RESULT_OK) {

            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            if (accountName != "") {
                SettingsUtil util = new SettingsUtil(getActivity(), GoogleCalUtil.PREFERENCE_NAME);
                util.setPreference(GoogleCalUtil.PREFERENCE_ACCOUNTNAME, accountName);
                mCalUtil.createCalendar();
            } else {
                Toast.makeText(getActivity(), "No valid account selected", Toast.LENGTH_LONG).show();
                mChkSync.setChecked(false);
            }
        } else {
            Toast.makeText(getActivity(), "No account selected", Toast.LENGTH_LONG).show();
            mChkSync.setChecked(false);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                getActivity(),
                MultimaniaContract.TalkEntry.CONTENT_URI,
                null,
                MultimaniaContract.TalkEntry.IS_FAVORITE + "=1 ",
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        try {
            buildItems(cursor);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }
}
