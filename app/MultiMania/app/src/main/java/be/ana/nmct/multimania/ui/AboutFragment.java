package be.ana.nmct.multimania.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import be.ana.nmct.multimania.R;


public class AboutFragment extends Fragment {

    private Animation animFadein;
    private Context context;
    private ImageView imgAbout;
    private TextView txtTitleEvent;
    private TextView txtInfoEvent;
    private TextView txtVenue;
    private TextView txtStreet;
    private TextView txtCity;
    private TextView txtCountry;
    private ImageView imgMaps;

    public AboutFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //animFadein = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.fade_in);

        View v = inflater.inflate(R.layout.fragment_about, container, false);
        imgAbout = (ImageView)v.findViewById(R.id.imgAbout);
        txtTitleEvent = (TextView)v.findViewById(R.id.txtAboutTitle);
        txtInfoEvent = (TextView)v.findViewById(R.id.txtAboutInfo);
        txtVenue = (TextView)v.findViewById(R.id.txtNameVenue);
        txtStreet = (TextView)v.findViewById(R.id.txtStreet);
        txtCity = (TextView)v.findViewById(R.id.txtCity);
        txtCountry = (TextView)v.findViewById(R.id.txtCountry);
        imgMaps = (ImageView) v.findViewById(R.id.imgAboutMap);

        txtTitleEvent.setText(R.string.about_title);
        txtInfoEvent.setText(R.string.about_info);
        txtVenue.setText(R.string.name_venue);
        txtStreet.setText(R.string.street);
        txtCity.setText(R.string.city);
        txtCountry.setText(R.string.country);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_multimania);
        imgAbout.setImageDrawable(drawable);

        Drawable drawMap = getResources().getDrawable(R.drawable.ic_aboutmap);
        imgMaps.setImageDrawable(drawMap);

        imgMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String longitude = "3.277815";
                String lat = "50.807796";
                String uri ="http://maps.google.com/maps?q=loc:" + lat + "," + longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
        return v;
    }

}
