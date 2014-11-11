package be.ana.nmct.multimania.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;

public class TalkFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String URI_KEY = "uri_key";
    private Uri mUri=null;
    private TitleLoadListener mTitleLoadListener;
    private Cursor mData;

    private TextView txtTalkInfo;
    private TextView txtSpeaker;
    private TextView txtTalkTime;
    private TextView txtTalkRoom;
    private TextView txtTalkTag;

    public TalkFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mUri = getArguments().getParcelable(URI_KEY);
        }
        getLoaderManager().initLoader(0,null,this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talk, container, false);

        txtTalkInfo = (TextView) view.findViewById(R.id.txtTalkInfo);
        txtSpeaker = (TextView) view.findViewById(R.id.txtTalkSpeaker);
        txtTalkTime = (TextView) view.findViewById(R.id.txtTalkTime);
        txtTalkRoom = (TextView) view.findViewById(R.id.txtTalkRoom);
        txtTalkTag = (TextView) view.findViewById(R.id.txtTalkTag);
        BindData(mData);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), mUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mData = data;
        BindData(data);
    }

    private void BindData(Cursor cursor){

        if(cursor == null)return;
        if(cursor.moveToFirst()){
            int titleCol = cursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.TITLE);
            String title = cursor.getString(titleCol);

            if(mTitleLoadListener != null) {
                mTitleLoadListener.onTitleloaded(title);
            }

            int infoCol = cursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.DESCRIPTION);
            int speakerCol = cursor.getColumnIndexOrThrow(MultimaniaContract.SpeakerEntry.NAME);
            int timeFromCol = cursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.DATE_FROM);
            int timeUntilCol = cursor.getColumnIndexOrThrow(MultimaniaContract.TalkEntry.DATE_UNTIL);
            int roomCol = cursor.getColumnIndexOrThrow(MultimaniaContract.RoomEntry.NAME);
            int tagCol = cursor.getColumnIndexOrThrow(MultimaniaContract.TagEntry.NAME);


            String info = cursor.getString(infoCol);
            String speaker = cursor.getString(speakerCol);
            String from = cursor.getString(timeFromCol);
            String until = cursor.getString(timeUntilCol);
            String room = cursor.getString(roomCol);
            String tag = cursor.getString(tagCol);

                txtTalkInfo.setText("test");
              //  txtSpeaker.setText(speaker);
             //   txtTalkTime.setText("From: " + from + " Until: " + until);
              //  txtTalkRoom.setText(room);
               // txtTalkTag.setText(tag);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void setTitleLoadListener(TitleLoadListener listener){
        mTitleLoadListener =listener;
    }

    public interface TitleLoadListener {
        public void onTitleloaded(String title);
    }
}
