package be.ana.nmct.multimania.ui;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;

import be.ana.nmct.multimania.R;


public class AboutFragment extends Fragment {

    private AboutAdapter mAdapter;
    private static ArrayList<Integer> mDummyList;

    static{
        mDummyList = new ArrayList<Integer>();
        for(int i = 0; i < 3; i++){
            mDummyList.add(i);
        }
    }

    public AboutFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAdapter = new AboutAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_about, container, false);

        StaggeredGridView grid = (StaggeredGridView)v.findViewById(R.id.about_grid);
        grid.setAdapter(mAdapter);

        return v;
    }

    private class AboutAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mDummyList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDummyList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mDummyList.get(position);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {

            if(v == null){
                ViewHolder holder = new ViewHolder();
                switch(position){
                    case 0:
                        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_about_intro, null);
                        holder.txtTitleEvent = (TextView) v.findViewById(R.id.txtAboutTitle);
                        holder.txtInfoEvent = (TextView) v.findViewById(R.id.txtAboutInfo);
                        holder.imgAbout = (ImageView) v.findViewById(R.id.imgAbout);
                        break;
                    case 1:
                        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_about_location, null);
                        holder.txtVenue = (TextView) v.findViewById(R.id.txtNameVenue);
                        holder.txtStreet = (TextView) v.findViewById(R.id.txtStreet);
                        holder.txtCity = (TextView) v.findViewById(R.id.txtCity);
                        holder.txtCountry = (TextView) v.findViewById(R.id.txtCountry);
                        holder.imgMaps = (ImageView) v.findViewById(R.id.imgAboutMap);
                        break;
                    case 2:
                        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_about_sponsors, null);
                        break;
                }

                v.setTag(holder);
            }

            ViewHolder viewholder = (ViewHolder) v.getTag();

            switch(position){
                case 0:
                    viewholder.txtTitleEvent.setText(R.string.about_title);
                    viewholder.txtInfoEvent.setText(R.string.about_info);
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_multimania);
                    viewholder.imgAbout.setImageDrawable(drawable);
                    break;
                case 1:
                    viewholder.txtVenue.setText(R.string.name_venue);
                    viewholder.txtStreet.setText(R.string.street);
                    viewholder.txtCity.setText(R.string.city);
                    viewholder.txtCountry.setText(R.string.country);
                    viewholder.imgMaps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String longitude = "3.277815";
                            String lat = "50.807796";
                            String uri ="http://maps.google.com/maps?q=loc:" + lat + "," + longitude;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(intent);
                        }
                    });
                    break;
                case 2:

                    break;
            }

            return v;
        }
    }

    private static class ViewHolder{
        public ImageView imgAbout;
        public TextView txtTitleEvent;
        public TextView txtInfoEvent;
        public TextView txtVenue;
        public TextView txtStreet;
        public TextView txtCity;
        public TextView txtCountry;
        public ImageView imgMaps;
    }

}
