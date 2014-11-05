package be.ana.nmct.multimania.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

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


        txtTitleEvent.setText(R.string.about_title);
        txtInfoEvent.setText(R.string.about_info);
        txtVenue.setText(R.string.name_venue);
        txtStreet.setText(R.string.street);
        txtCity.setText(R.string.city);
        txtCountry.setText(R.string.country);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_multimania);
        imgAbout.setImageDrawable(drawable);

        return v;
    }
}
