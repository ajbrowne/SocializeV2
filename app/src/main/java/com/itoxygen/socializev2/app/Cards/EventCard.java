package com.itoxygen.socializev2.app.Cards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itoxygen.socializev2.app.Activities.HomeActivity;
import com.itoxygen.socializev2.app.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * This class provides a simple Google Play card to display
 * specials.
 *
 * @author brownea
 */
public class EventCard extends Card {

    private Context context;
    private String name;
    private String location;
    private String date;
    private String time;
    private String invitees;
    private String address;
    private String eventId;
    private Boolean invited = false;
    private String username;
    private Activity activity;

    public EventCard(Context context) {
        this(context, R.layout.event_card);
        this.context = context;
    }

    public EventCard(Context context, int innerLayout) {
        super(context, innerLayout);
        this.context = context;
    }

    /**
     * Automatically called upon a listener being added to a card array (I think).
     * Sets up all elements on a card.
     *
     * @param parent - the parent view
     * @param view   - the current view where cards will be added
     */
    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView mTitle = (TextView) parent.findViewById(R.id.eventCardTitle);
        TextView mDate = (TextView) parent.findViewById(R.id.eventCardDate);
        TextView mLocation = (TextView) parent.findViewById(R.id.eventCardLocation);
        TextView mAddress = (TextView) parent.findViewById(R.id.eventCardAddress);
        TextView mTime = (TextView) parent.findViewById(R.id.eventCardTime);
        ImageView mAccept = (ImageView) parent.findViewById(R.id.acceptButton);
        ImageView mCancel = (ImageView) parent.findViewById(R.id.cancelButton);

        mTitle.setText(name);
        mDate.setText(date);
        mLocation.setText(location);
        mAddress.setText(address);
        mTime.setText(time);

        if (invited == false){
            mAccept.setVisibility(View.GONE);
            mCancel.setVisibility(View.GONE);
        }

        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("meetup");

                query.getInBackground(eventId, new GetCallback<ParseObject>() {
                    public void done(ParseObject event, ParseException e) {
                        if (e == null) {
                            List<String> guests = event.getList("guests");
                            guests.add(username);
                            event.put("guests", guests);
                            System.out.println(guests.toString());
                            List<String> invitees = new ArrayList<String>();
                            invitees.add(username);
                            event.removeAll("invitees", invitees);
                            event.saveInBackground();
                        } else{
                            System.out.println(e);
                        }
                    }
                });

                ((HomeActivity) activity).refreshAccept(eventId);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("meetup");

                query.getInBackground(eventId, new GetCallback<ParseObject>() {
                    public void done(ParseObject event, ParseException e) {
                        if (e == null) {
                            List<String> invitees = new ArrayList<String>();
                            invitees.add(username);
                            event.removeAll("invitees", invitees);
                            event.saveInBackground();
                        } else{
                            System.out.println(e);
                        }
                    }
                });

                ((HomeActivity) activity).refresh();
            }
        });

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInvitees() {
        return invitees;
    }

    public void setInvitees(String invitees) {
        this.invitees = invitees;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Boolean getInvited() {
        return invited;
    }

    public void setInvited(Boolean invited) {
        this.invited = invited;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}

