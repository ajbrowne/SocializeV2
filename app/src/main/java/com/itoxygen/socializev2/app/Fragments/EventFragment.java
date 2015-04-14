package com.itoxygen.socializev2.app.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itoxygen.socializev2.app.Activities.EventDetailActivity;
import com.itoxygen.socializev2.app.Activities.HomeActivity;
import com.itoxygen.socializev2.app.R;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class EventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    private View eventView;
    private TextView title;
    private EditText eventName;
    private TextView addFriends;
    private EditText location;
    private EditText address;
    private Button submit;
    private String timeString;
    private String dateString;
    private Map<String,String> params;
    private List<String> friends = new ArrayList<String>();
    private String loc;
    Double latitude;
    Double longitude;

    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        eventView = inflater.inflate(R.layout.fragment_event, container, false);
        addFriends = (TextView) eventView.findViewById(R.id.addFriends);
        title = (TextView) eventView.findViewById(R.id.event_title);
        submit = (Button) eventView.findViewById(R.id.createButton);
        params = new HashMap<String, String>();
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Pacifico.ttf");
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(typeFace);

        final Calendar calendar = Calendar.getInstance();

        TextView date = (TextView) eventView.findViewById(R.id.eventDate);
        date.setText(calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR));
        dateString = calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.YEAR);

        TextView time = (TextView) eventView.findViewById(R.id.eventTime);
        time.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE));
        timeString = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), isVibrate());
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

        eventView.findViewById(R.id.dateButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        eventView.findViewById(R.id.timeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(isVibrate());
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });

        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getActivity().getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) getActivity().getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventName = (EditText) eventView.findViewById(R.id.eventName);
                location = (EditText) eventView.findViewById(R.id.eventLocation);
                address = (EditText) eventView.findViewById(R.id.eventAddress);
                String name = eventName.getText().toString();
                friends = ((HomeActivity) getActivity()).getFriends();
                createEvent(name);
            }
        });

        addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FriendFragment fragment = new FriendFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.newEventContainer, fragment, "addFriend").addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return eventView;
    }

    private boolean isVibrate() {
        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        TextView date = (TextView) eventView.findViewById(R.id.eventDate);
        date.setText(month + "-" + day + "-" + year);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String hour = Integer.toString(hourOfDay);
        String min = Integer.toString(minute);
        TextView time = (TextView) eventView.findViewById(R.id.eventTime);
        if (minute < 10) {
            min = "0" + minute;
        }
        time.setText(hour + ":" + min);
    }

    public void createEvent(final String name) {

        ParseObject eventObj = new ParseObject("meetup");
        eventObj.put("name", name);
        eventObj.put("location", location.getText().toString());
        eventObj.put("date", dateString);
        eventObj.put("time", timeString);
        eventObj.put("address", address.getText().toString());

        List<String> guests = new ArrayList<String>();
        guests.add(ParseUser.getCurrentUser().getUsername());
        eventObj.put("guests", guests);
        eventObj.put("invitees", friends);
        eventObj.saveInBackground();

        Geocoder geocode = new Geocoder(getActivity());
        String addr = address.getText().toString();
        geocodeAddress(addr, geocode);
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("date", dateString);
        intent.putExtra("time", timeString);
        intent.putExtra("invitees", friends.toString());
        intent.putExtra("address", address.getText().toString());
        intent.putExtra("location", location.getText().toString());
        intent.putExtra("lat", latitude);
        intent.putExtra("long", longitude);
        startActivity(intent);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public void geocodeAddress(String addressStr, Geocoder gc) {
        Address address = null;
        List<Address> addressList = null;
        try {
            if (!TextUtils.isEmpty(addressStr)) {
                addressList = gc.getFromLocationName(addressStr, 5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != addressList && addressList.size() > 0) {
            address = addressList.get(0);
        }

        if (null != address && address.hasLatitude()
                && address.hasLongitude()) {

            latitude = address.getLatitude();
            longitude = address.getLongitude();
        }

    }

}
