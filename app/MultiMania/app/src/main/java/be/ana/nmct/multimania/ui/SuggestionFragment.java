package be.ana.nmct.multimania.ui;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.utils.SettingsUtil;


public class SuggestionFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Date mDate;
    private String mFromString;
    private String mUntilString;

    public SuggestionFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SettingsUtil suggestionSettings = new SettingsUtil(getActivity(), SettingsFragment.PREFERENCE_NAME);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (suggestionSettings.getBooleanPreference(SettingsFragment.PREFERENCE_SUGGESTIONS, true)) {

            View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_suggestion, null);
            CheckBox chkShowAgain = (CheckBox) v.findViewById(R.id.chkShowAgain);
            TextView txtTimeInfo = (TextView) v.findViewById(R.id.txtSuggestionInfo);

            //TODO: make listadapter & get items from date
            ListView listview = (ListView) v.findViewById(R.id.suggestion_list);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO: add item to my schedule
                }
            });

            txtTimeInfo.setText(String.format(getString(R.string.myschedule_suggestion_time), mFromString, mUntilString));
            chkShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    suggestionSettings.setPreference(SettingsFragment.PREFERENCE_SUGGESTIONS, isChecked);
                }
            });


            builder.setView(v);
            builder.setTitle(R.string.myschedule_timegap_info);

        }
        return builder.create();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class SuggestionRowAdapter extends CursorAdapter {

        public SuggestionRowAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }
    }

    private class SuggestionRowHolder {

        public TextView txtTalkTitle;
        public TextView txtTalkTags;

        private SuggestionRowHolder(View v) {
            txtTalkTitle = (TextView) v.findViewById(R.id.txtSuggestionTitle);
            txtTalkTags = (TextView) v.findViewById(R.id.txtSuggestionTag);
        }
    }

}
