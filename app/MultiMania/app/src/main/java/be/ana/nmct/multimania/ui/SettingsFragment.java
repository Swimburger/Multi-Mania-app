package be.ana.nmct.multimania.ui;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
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

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.utils.GoogleCalUtil;
import be.ana.nmct.multimania.utils.SettingsUtil;

public class SettingsFragment extends Fragment {

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

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mCalendarName = this.getActivity().getResources().getString(R.string.calendar_name);
        this.mUtil = new SettingsUtil(this.getActivity(), PREFERENCE_NAME);
        this.mCalUtil = new GoogleCalUtil(this.getActivity(), mCalendarName);
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

                } else {
                    mCalUtil.deleteCalendar();
                }
            }
        });


        return v;
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



}
