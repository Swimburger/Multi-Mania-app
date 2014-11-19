package be.ana.nmct.multimania.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.TextView;

import java.text.ParseException;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.data.MultimaniaContract;
import be.ana.nmct.multimania.utils.Utility;

public class TalkFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String URI_KEY = "uri_key";
    public static final int LOADER_TALK_ID = 0;
    public static final int LOADER_SPEAKER_ID = 1;
    public static final int LOADER_TAGS_ID = 2;

    private Uri mUri=null;
    private long mTalkId = -1;
    private TitleLoadListener mTitleLoadListener;
    private Cursor mTalkData;
    private Cursor mSpeakersData;
    private Cursor mTagsData;

    private WebView webTalkInfo;
    private TextView txtSpeaker;
    private TextView txtTalkTime;
    private TextView txtTalkRoom;
    private TextView txtTalkTag;
    private Animation mFadeInAnimation;
    private Animation mFadeInAnimationHtml;

    public TalkFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mUri = getArguments().getParcelable(URI_KEY);
            mTalkId = ContentUris.parseId(mUri);
        }
        //TODO: loader for talk_tags, loader for talk_speakers
        getLoaderManager().initLoader(LOADER_TALK_ID,null,this);
        getLoaderManager().initLoader(LOADER_SPEAKER_ID,null,this);
        getLoaderManager().initLoader(LOADER_TAGS_ID,null,this);

        mFadeInAnimation = getAlphaAnimation(0,1,500,600);
        mFadeInAnimationHtml = getAlphaAnimation(0, 1, 400, 300);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
       Loader<Cursor> retLoader = null;
       Context context = getActivity();
       switch(id){
           case LOADER_TALK_ID:
               retLoader = new CursorLoader(context,mUri,null,null,null,null);
               break;
           case LOADER_SPEAKER_ID:
               retLoader = new CursorLoader(context,MultimaniaContract.SpeakerEntry.builtGetSpeakersByTalkIdUri(mTalkId),null,null,null,null);
               break;
           case LOADER_TAGS_ID:
               retLoader = new CursorLoader(context,MultimaniaContract.TagEntry.builtGetTagsByTalkIdUri(mTalkId),null,null,null,null);
               break;
       }
       return retLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()){
            case LOADER_TALK_ID:
                BindTalkData(data);
                mTalkData = data;
                break;
            case LOADER_SPEAKER_ID:
                BindSpeakersData(data);
                mSpeakersData = data;
                break;
            case LOADER_TAGS_ID:
                BindTagsData(data);
                mTagsData = data;
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talk, container, false);

        webTalkInfo = (WebView) view.findViewById(R.id.webTalkInfo);
        webTalkInfo.setBackgroundColor(0x00000000);
        if (Build.VERSION.SDK_INT >= 11) webTalkInfo.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        txtSpeaker = (TextView) view.findViewById(R.id.txtTalkSpeaker);
        txtTalkTime = (TextView) view.findViewById(R.id.txtTalkTime);
        txtTalkRoom = (TextView) view.findViewById(R.id.txtTalkRoom);
        txtTalkTag = (TextView) view.findViewById(R.id.txtTalkTag);

        txtSpeaker.setAnimation(mFadeInAnimation);
        txtTalkTime.setAnimation(mFadeInAnimation);
        txtTalkRoom.setAnimation(mFadeInAnimation);
        txtTalkTag.setAnimation(mFadeInAnimation);
        webTalkInfo.setAnimation(mFadeInAnimationHtml);
        BindData();
        return view;
    }

    private void BindData() {
        if(mTalkData!=null)
            BindTalkData(mTalkData);
        if(mSpeakersData !=null)
            BindSpeakersData(mSpeakersData);
        if(mTagsData!=null)
            BindTagsData(mTagsData);
    }


    private void BindTalkData(Cursor cursor){

        if(cursor == null)return;
        if(cursor.moveToFirst()){
            int titleCol = cursor.getColumnIndex(MultimaniaContract.TalkEntry.TITLE);
            int infoCol = cursor.getColumnIndex(MultimaniaContract.TalkEntry.DESCRIPTION);
            int timeFromCol = cursor.getColumnIndex(MultimaniaContract.TalkEntry.DATE_FROM);
            int timeUntilCol = cursor.getColumnIndex(MultimaniaContract.TalkEntry.DATE_UNTIL);
            int roomCol = cursor.getColumnIndex(MultimaniaContract.TalkEntry.ROOM_NAME);


            String title = cursor.getString(titleCol);
            String info = cursor.getString(infoCol);
            String from = cursor.getString(timeFromCol);
            String until = cursor.getString(timeUntilCol);
            String room = cursor.getString(roomCol);

            if(mTitleLoadListener != null) {
                mTitleLoadListener.onTitleloaded(title);
            }

            String mime = "text/html";
            String encoding = "utf-8";
            String html = Utility.getHtml(info);
            webTalkInfo.getSettings().setJavaScriptEnabled(true);
            webTalkInfo.loadDataWithBaseURL("file:///android_asset/", html, mime, encoding, null);

            try {
                txtTalkTime.setText(Utility.getTimeString(from) + " - " + Utility.getTimeString(until));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            txtTalkRoom.setText(room);

            //mFadeInAnimationHtml.setAnimationListener(this);
            mFadeInAnimationHtml.start();
            mFadeInAnimation.start();
        }
    }

    private void BindTagsData(Cursor data) {
        String tags = "";
        final int nameIndex = data.getColumnIndex(MultimaniaContract.TagEntry.NAME);
        if(data.moveToFirst()){
            do{
                tags +=data.getString(nameIndex)+", ";
            }while(data.moveToNext());
            if(tags.lastIndexOf(", ")>-1)
                tags=tags.substring(0,tags.length()-2);
        }
        txtTalkTag.setText(tags);
    }

    private void BindSpeakersData(Cursor data) {
        String speakers = "";
        final int nameIndex = data.getColumnIndex(MultimaniaContract.SpeakerEntry.NAME);
        if(data.moveToFirst()){
            speakers+= getString(R.string.speakers)+": ";
            do{
                speakers +=data.getString(nameIndex)+", ";
            }while(data.moveToNext());
            if(speakers.lastIndexOf(", ")>-1)
                speakers=speakers.substring(0,speakers.length()-2);
        }
        txtSpeaker.setText(speakers);
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

    private AlphaAnimation getAlphaAnimation(float from, float to, long duration,long offset){
        return Utility.getAlphaAnimation(from,to,duration,offset);
    }
}
