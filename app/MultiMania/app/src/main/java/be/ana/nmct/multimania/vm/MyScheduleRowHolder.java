package be.ana.nmct.multimania.vm;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import be.ana.nmct.multimania.R;

/**
 * Created by Axel on 19/11/2014.
 */
public class MyScheduleRowHolder{

    public TextView txtTalkTitle;
    public TextView txtRoom;
    public TextView txtTime;
    public TextView txtTag;
    public ImageView btnRemoveTalk;
    public RelativeLayout root;
    public View bottomBorder;

    /**
     * The constructor method
     * @param v The view to get the inner views from
     */
    public MyScheduleRowHolder(View v) {
        this.txtTalkTitle = (TextView)v.findViewById(R.id.txtTalkTitle);
        this.txtRoom = (TextView)v.findViewById(R.id.txtRoom);
        this.txtTime = (TextView)v.findViewById(R.id.txtTime);
        this.txtTag = (TextView)v.findViewById(R.id.txtTag);
        this.btnRemoveTalk = (ImageView)v.findViewById(R.id.btnRemoveTalk);
        this.root = (RelativeLayout)v.findViewById(R.id.myscheduleRowRoot);
        this.bottomBorder = v.findViewById(R.id.borderBottom);
    }
}
