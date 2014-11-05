package be.ana.nmct.multimania.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.ana.nmct.multimania.R;
import uk.co.senab.photoview.PhotoView;


public class MapFragment extends Fragment {

    public MapFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        PhotoView photoView = (PhotoView)v.findViewById(R.id.indoormap);
        photoView.setMaximumScale(16);
        photoView.setImageDrawable(getResources().getDrawable(R.drawable.ic_map));
        return v;
    }



}
