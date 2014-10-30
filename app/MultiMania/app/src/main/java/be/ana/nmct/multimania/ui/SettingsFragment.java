package be.ana.nmct.multimania.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.utils.GoogleCalUtil;

public class SettingsFragment extends Fragment {

    private final static String TAG = SettingsFragment.class.getCanonicalName();

    private static CheckBox checkbox_notify;
    private static CheckBox checkbox_sync;

    public SettingsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_settings, container, false);

        checkbox_notify = (CheckBox)v.findViewById(R.id.checkbox_notify);
        checkbox_sync = (CheckBox)v.findViewById(R.id.checkbox_sync);

        checkbox_notify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.d(TAG, "checked");
            }
        });

        checkbox_sync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                GoogleCalUtil cal = new GoogleCalUtil(getActivity());

                if(isChecked){
                    //cal.createCalendar();
                }else{
                    //cal.deleteCalendar();
                }
            }
        });


        return v;
    }






}
