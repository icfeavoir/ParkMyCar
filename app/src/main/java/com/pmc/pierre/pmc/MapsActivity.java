package com.pmc.pierre.pmc;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PMCSearch pmc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        Button search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //on enlève tous les marqueurs
                mMap.clear();
                // on effectue la recherche des capteurs dispo
                pmc = new PMCSearch();
                pmc.getDataFromDB();
                List<Capteur> listeCapteurs = pmc.getListeCapteur();

                //pour la map
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for(int i=0; i<listeCapteurs.size(); i++){
                    Capteur c = listeCapteurs.get(i);
                    LatLng capteur = new LatLng(c.getLat(), c.getLon());
                    Marker m = mMap.addMarker(new MarkerOptions().position(capteur).title("Capteur "+(i+1)));

                    builder.include(m.getPosition());
                }
                // animation pour zoomer en contenant tous les markers
                if(listeCapteurs.size() != 0) {
                    LatLngBounds bounds = builder.build();
                    int padding = 500;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.animateCamera(cu);
                }
            }
        });
    }
}
