package com.itoxygen.socializev2.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itoxygen.socializev2.app.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventDetailActivity extends BaseActivity {

    private TextView name, date, time, invitees, location, address, activityHeader;
    private Activity activity = this;
    private GoogleMap googleMap;
    private Geocoder mGeoCoder;
    private MarkerOptions mMarkerOptions;
    private Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Intent intent = getIntent();
        getActionBar().setTitle(intent.getStringExtra("name"));
        latitude = getIntent().getDoubleExtra("lat", 0);
        longitude = getIntent().getDoubleExtra("long", 0);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mGeoCoder = new Geocoder(this, Locale.getDefault());
        //declare the textviews and get the actual views
        float ZOOM = 13;
        loadMap();
        configureMap(ZOOM);

        name = (TextView) findViewById(R.id.event);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        invitees = (TextView) findViewById(R.id.inv);
        location = (TextView) findViewById(R.id.location);
        address = (TextView) findViewById(R.id.address);
        activityHeader = (TextView) findViewById(R.id.activityHeader);

        if (intent.getBooleanExtra("fromHome", true)){
            activityHeader.setText("Meetup Details");
        }

        name.setText(intent.getStringExtra("name"));
        date.setText(intent.getStringExtra("date"));
        time.setText(intent.getStringExtra("time"));
        address.setText(intent.getStringExtra("address"));
        location.setText(intent.getStringExtra("location"));
        invitees.setText(intent.getStringExtra("invitees"));
    }

    private void loadMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // check if map is created successfully or not
            if (googleMap == null) {

                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void configureMap(float ZOOM) {
        mMarkerOptions = new MarkerOptions().position(new LatLng(latitude, longitude)).title(getIntent().getStringExtra("location") + ": " + getIntent().getStringExtra("name"));
        Marker marker = googleMap.addMarker(mMarkerOptions);
        marker.showInfoWindow();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(ZOOM).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String uriBegin = "geo:" + latitude + "," + longitude;
                String encodedQuery = Uri.encode(getIntent().getStringExtra("location") + ": " + getIntent().getStringExtra("name"));
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    /**
     * Override onResume so that the map reloads
     * when the activity is re-opened
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetAddressTask extends AsyncTask<Double, Void, String> {

        @Override
        protected String doInBackground(Double... doubles) {
            double lat = doubles[0];
            double longitude = doubles[1];
            String address = "No Address Found";
            List<Address> addresses = new ArrayList<Address>();
            while (address.equals("No Address Found")) {
                try {
                    addresses = mGeoCoder.getFromLocation(lat, longitude, 1);
                    address = addresses.get(0).getAddressLine(0) + "\n" + addresses.get(0).getAddressLine(1);
                } catch (IOException e) {
                    Log.d("error", "No Address Found");
                }
                if (addresses.size() != 0) {
                    return address;
                }
            }
            return address;
        }

        protected void onPostExecute(String result) {
            googleMap.clear();
            Marker marker = googleMap.addMarker(mMarkerOptions.snippet(result));
            marker.showInfoWindow();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
