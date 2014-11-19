package be.ana.nmct.multimania.vm;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import be.ana.nmct.multimania.R;

/**
 * Created by Axel on 19/11/2014.
 */
public class MyScheduleRowHolder extends RecyclerView.ViewHolder{

    public TextView txtTalkTitle;
    public TextView txtRoom;
    public TextView txtTime;
    public TextView txtTag;
    public ImageView btnRemoveTalk;

    public MyScheduleRowHolder(View v) {
        super(v);
        this.txtTalkTitle = (TextView)v.findViewById(R.id.txtTalkTitle);
        this.txtRoom = (TextView)v.findViewById(R.id.txtRoom);
        this.txtTime = (TextView)v.findViewById(R.id.txtTime);
        this.txtTag = (TextView)v.findViewById(R.id.txtTag);
        this.btnRemoveTalk = (ImageView)v.findViewById(R.id.btnRemoveTalk);
    }
}
