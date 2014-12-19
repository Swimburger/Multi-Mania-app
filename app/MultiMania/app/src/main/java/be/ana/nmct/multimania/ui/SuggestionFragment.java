package be.ana.nmct.multimania.ui;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.ApiActions;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.GoogleCalUtil;
import be.ana.nmct.multimania.utils.SettingsHelper;
import be.ana.nmct.multimania.utils.SettingsUtil;
import be.ana.nmct.multimania.utils.Utility;
import be.ana.nmct.multimania.vm.ScheduleTalkVm;

/**
 * This fragment handles showing an AlertDialog containing suggestions for the user
 */
public class SuggestionFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public final static String BUNDLE_FROMSTRING = "be.ana.nmct.multimania.suggestion.FROMSTRING";
    public final static String BUNDLE_UNTILSTRING = "be.ana.nmct.multimania.suggestion.UNTILSTRING";
    public final static String BUNDLE_DATE = "be.ana.nmct.multimania.suggestion.DATE";

    private ListView mListview;
    private Cursor mData;
    private Date mDate;
    private String mFromString;
    private String mUntilString;
    private SuggestionRowAdapter mAdapter;
    private String mAccountName;
    private boolean mWasItemAdded;
    private SettingsHelper mSettingsHelper;

    public SuggestionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingsHelper = new SettingsHelper(getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mFromString = getArguments().getString(BUNDLE_FROMSTRING);
        mUntilString = getArguments().getString(BUNDLE_UNTILSTRING);
        try {
            mDate = Utility.convertStringToDate(getArguments().getString(BUNDLE_DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mAccountName = new SettingsUtil(getActivity().getApplicationContext(), GoogleCalUtil.PREFERENCE_NAME).getStringPreference(GoogleCalUtil.PREFERENCE_ACCOUNTNAME);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_suggestion, null);
        TextView txtTimeInfo = (TextView) v.findViewById(R.id.txtSuggestionInfo);

        mListview = (ListView) v.findViewById(R.id.suggestion_list);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mData.moveToPosition(position)) {
                    int talkIdIndex = mData.getColumnIndexOrThrow(MultimaniaContract.TalkEntry._ID);
                    long talkId = mData.getLong(talkIdIndex);
                    updateItemValue(talkId, true);
                    mWasItemAdded = true;
                    updateSettings(position);
                    dismiss();
                }
            }
        });
        getLoaderManager().initLoader(MainActivity.LOADER_SUGGESTIONS, null, this);

        txtTimeInfo.setText(String.format(getString(R.string.myschedule_suggestion_time), mFromString, mUntilString));

        builder.setView(v);
        builder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Fragment frag = getTargetFragment();
        if (frag instanceof MyScheduleFragment) {
            ((MyScheduleFragment) frag).onDialogDismissedListener(mWasItemAdded);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MultimaniaContract.TalkEntry.CONTENT_URI,
                null,
                MultimaniaContract.TalkEntry.DATE_FROM + " = ?",
                new String[]{Utility.convertDateToString(mDate)},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mData = data;
        mAdapter = new SuggestionRowAdapter(getActivity(), data, 0);
        mListview.setAdapter(mAdapter);
        mAdapter.swapCursor(data);
        loader.abandon();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            mAdapter.swapCursor(null);
        }
    }

    private void updateSettings(int position) {
        if (mData.moveToPosition(position)) {
            ScheduleTalkVm vm = new ScheduleTalkVm();

            final int dateFromIndex = mData.getColumnIndex(MultimaniaContract.TalkEntry.DATE_FROM);
            final int dateUntilIndex = mData.getColumnIndex(MultimaniaContract.TalkEntry.DATE_UNTIL);
            final int titleIndex = mData.getColumnIndex(MultimaniaContract.TalkEntry.TITLE);
            final int roomIndex = mData.getColumnIndex(MultimaniaContract.TalkEntry.ROOM_NAME);
            final int idIndex = mData.getColumnIndex(MultimaniaContract.TalkEntry._ID);
            final int calEventIdIndex = mData.getColumnIndex(MultimaniaContract.TalkEntry.CALEVENT_ID);
            final int descriptionIndex = mData.getColumnIndex(MultimaniaContract.TalkEntry.DESCRIPTION);

            try {
                vm.fromString = Utility.getTimeString(mData.getString(dateFromIndex));
                vm.untilString = Utility.getTimeString(mData.getString(dateUntilIndex));
                vm.from = Utility.convertStringToDate(mData.getString(dateFromIndex));
                vm.to = Utility.convertStringToDate(mData.getString(dateUntilIndex));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            vm.id = mData.getLong(idIndex);
            vm.title = mData.getString(titleIndex);
            vm.room = mData.getString(roomIndex);
            vm.calEventId = mData.getLong(calEventIdIndex);
            vm.description = mData.getString(descriptionIndex);

            mSettingsHelper.settingsHandler(vm);
        }
    }

    private void updateItemValue(long id, boolean value) {
        ContentValues values = new ContentValues();
        values.put(MultimaniaContract.TalkEntry.IS_FAVORITE, value ? 1 : 0);
        AsyncQueryHandler handler = new SuggestionAsyncQueryHandler(getActivity().getContentResolver());
        handler.startUpdate(
                0,
                null,
                MultimaniaContract.TalkEntry.CONTENT_URI,
                values,
                MultimaniaContract.TalkEntry._ID + "=?",
                new String[]{"" + id}
        );
        if (mAccountName != null) {
            if (value) {
                ApiActions.postFavoriteTalk(getActivity(), mAccountName, id);
            } else {
                ApiActions.deleteFavoriteTalk(getActivity(), mAccountName, id);
            }
        }
    }

    private class SuggestionRowAdapter extends CursorAdapter {

        private int mTitleIndex;
        private int mTagIndex;
        private LayoutInflater mInflater;

        public SuggestionRowAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Cursor swapCursor(Cursor newCursor) {
            if (newCursor != null) {
                mTitleIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.TITLE);
                mTagIndex = newCursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.TAGS);
            }
            return super.swapCursor(newCursor);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = mInflater.inflate(R.layout.row_suggestion, parent, false);

            SuggestionRowHolder vh = new SuggestionRowHolder();
            vh.txtTalkTitle = (TextView) v.findViewById(R.id.txtSuggestionTitle);
            vh.txtTalkTags = (TextView) v.findViewById(R.id.txtSuggestionTag);
            v.setTag(vh);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            if (cursor == null || cursor.isClosed()) return;

            SuggestionRowHolder holder = (SuggestionRowHolder) view.getTag();
            holder.txtTalkTags.setText(cursor.getString(mTagIndex));
            holder.txtTalkTitle.setText(cursor.getString(mTitleIndex));

        }
    }

    private static class SuggestionRowHolder {

        public TextView txtTalkTitle;
        public TextView txtTalkTags;

    }

    private class SuggestionAsyncQueryHandler extends AsyncQueryHandler {
        public SuggestionAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }
    }

    public interface OnDialogDismissedListener {
        void onDialogDismissedListener(boolean wasItemAdded);
    }

}
