package be.ana.nmct.multimania.ui;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bulletnoid.android.widget.StaggeredGridView.BulletStaggeredGridView;

import be.ana.nmct.multimania.R;
import be.ana.nmct.multimania.utils.Utility;


public class AboutFragment extends Fragment {

    private AboutAdapter mAdapter;
    private static final Integer[] mSponsors = new Integer[]{
            R.drawable.adobe,
            R.drawable.chili_publish,
            R.drawable.adobe_user_group,
            R.drawable.richting_morgen,
            R.drawable.signpost,
            R.drawable.wacom,
            R.drawable.kortrijk_xpo,
            R.drawable.design_express,
            R.drawable.howest,
            R.drawable.wetenschap_maakt_knap,
            R.drawable.combell

    };
    private BulletStaggeredGridView mGrid;

    public AboutFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAdapter = new AboutAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_about, container, false);

        mGrid = (BulletStaggeredGridView)v.findViewById(R.id.about_grid);
        mGrid.setAdapter(mAdapter);
        mGrid.setItemMargin(Utility.dpToPx(getActivity(), 8));

        return v;
    }

    private class AboutAdapter extends BaseAdapter{

        private final LayoutInflater mInflater;

        public AboutAdapter(){
            mInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public int getCount() {
            return 2+mSponsors.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            switch(position){
                case 0:
                    return 0;
                case 1:
                    return 1;
                default:
                    return 2;
            }
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {

            final int viewType = getItemViewType(position);
            if(v == null||v.getId()!=viewType) {
                v = createView( v,parent,viewType);
            }

            bindView(position, v);

            return v;
        }

        private void bindView(int position, View v) {


            switch(position){
                //TODO: case 0 can be removed
                case 0:
                    ViewHolder viewholder = (ViewHolder) v.getTag();
                    viewholder.txtTitleEvent.setText(R.string.about_title);
                    viewholder.txtInfoEvent.setText(R.string.about_info);
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_multimania);
                    viewholder.imgAbout.setImageDrawable(drawable);
                    break;
                case 1:
                    viewholder = (ViewHolder) v.getTag();
                //TODO: everything except the clicklistener can be removed
                    viewholder.txtVenue.setText(R.string.name_venue);
                    viewholder.txtStreet.setText(R.string.street);
                    viewholder.txtCity.setText(R.string.city);
                    viewholder.txtCountry.setText(R.string.country);
                    viewholder.imgMaps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String longitude = "3.277815";
                            String lat = "50.807796";
                            String uri = "http://maps.google.com/maps?q=loc:" + lat + "," + longitude;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
                    int imgResource =getSponsorImage(position-2);
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), imgResource, o);
                    int w = bmp.getWidth();
                    int h = bmp.getHeight();
                    ImageView img =   ((SponsorViewHolder) v.getTag()).imgSponsor;
                    img.setImageResource(imgResource);
                    img.getLayoutParams().height=h;
            }
        }

        private View createView(View v,ViewGroup parent,int viewType) {
            ViewHolder holder = new ViewHolder();
            switch(viewType){
                case 0:
                    v = mInflater.inflate(R.layout.row_about_intro, parent,false);
                    holder.txtTitleEvent = (TextView) v.findViewById(R.id.txtAboutTitle);
                    holder.txtInfoEvent = (TextView) v.findViewById(R.id.txtAboutInfo);
                    holder.imgAbout = (ImageView) v.findViewById(R.id.imgAbout);
                    v.setId(viewType);
                    break;
                case 1:
                    v = mInflater.inflate(R.layout.row_about_location, parent,false);
                    holder.txtVenue = (TextView) v.findViewById(R.id.txtNameVenue);
                    holder.txtStreet = (TextView) v.findViewById(R.id.txtStreet);
                    holder.txtCity = (TextView) v.findViewById(R.id.txtCity);
                    holder.txtCountry = (TextView) v.findViewById(R.id.txtCountry);
                    holder.imgMaps = (ImageView) v.findViewById(R.id.imgAboutMap);
                    v.setId(viewType);
                    break;
                case 2:
                    v = mInflater.inflate(R.layout.row_about_sponsor, parent,false);
                    v.setTag(new SponsorViewHolder(v));
                    v.setId(viewType);
                    return v;
            }

            v.setTag(holder);
            return v;
        }
    }

    private int getSponsorImage(int i) {
        return mSponsors[i];
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

    private static class SponsorViewHolder {
        public final ImageView imgSponsor;

        public SponsorViewHolder(View v) {
            imgSponsor = (ImageView) v.findViewById(R.id.imgSponsor);
        }
    }
}
