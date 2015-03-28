package com.itoxygen.socializev2.app.Fragments;



import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
import com.itoxygen.socializev2.app.R;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.HashMap;
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
    private EditText inviteeOne;
    private EditText inviteeTwo;
    private Button submit;
    private String timeString;
    private String dateString;
    private Map<String,String> params;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        eventView = inflater.inflate(R.layout.fragment_event, container, false);
        title = (TextView) eventView.findViewById(R.id.event_title);
        submit = (Button) eventView.findViewById(R.id.createButton);
        params = new HashMap<String, String>();
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Pacifico.ttf");
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
                inviteeOne = (EditText) eventView.findViewById(R.id.inviteeOne);
                inviteeTwo = (EditText) eventView.findViewById(R.id.inviteeTwo);
                eventName = (EditText) eventView.findViewById(R.id.eventName);
                String name = eventName.getText().toString();
                String invite = inviteeOne.getText().toString();
                String invitee = inviteeTwo.getText().toString();
                createEvent(name, invite, invitee);
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

    public void createEvent(final String name, final String invite, final String invitee){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST,"http://141.219.214.187:8080/api/events", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getActivity().onBackPressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                params.put("name", "lol");
                params.put("invitees", invite);
                params.put("time", timeString);
                params.put("date", dateString);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

}
