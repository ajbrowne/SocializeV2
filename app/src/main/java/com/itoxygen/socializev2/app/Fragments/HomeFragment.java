package com.itoxygen.socializev2.app.Fragments;



import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.itoxygen.socializev2.app.Activities.EventDetailActivity;
import com.itoxygen.socializev2.app.Activities.HomeActivity;
import com.itoxygen.socializev2.app.Cards.EventCard;
import com.itoxygen.socializev2.app.Models.Event;
import com.itoxygen.socializev2.app.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays.*;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class HomeFragment extends Fragment implements AbsListView.OnScrollListener, OnRefreshListener {

    private PullToRefreshLayout mPullToRefreshLayout;
    private CardArrayAdapter mCardArrayAdapter;
    private ArrayList<Card> cards;
    private View homeView;
    private Double latitude;
    private Double longitude;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_home, container, false);
        cards = new ArrayList<Card>();

        mPullToRefreshLayout = (PullToRefreshLayout) homeView.findViewById(R.id.carddemo_extra_ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        makeRequestAttending();
        return homeView;
    }

    public void makeRequestAttending(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("meetup");
        query.whereEqualTo("guests", ((HomeActivity) getActivity()).getUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> eventList, ParseException e) {
                if (e == null) {
                    createEvents(eventList, false);
                    makeRequestInvited();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

    public void makeRequestInvited(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("meetup");
        query.whereEqualTo("invitees", ((HomeActivity) getActivity()).getUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> eventList, ParseException e) {
                if (e == null) {
                    createEvents(eventList, true);
                    createCards();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }


    /**
     * Callback for GET request with Google Volley
     */
    private class ResponseListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray response) {
            if (response.length() == 0) {
                TextView result = (TextView) homeView.findViewById(R.id.noMatches);
                result.setVisibility(View.VISIBLE);
            } else {
                //createEvents(response);
            }
        }

    }

    /**
     * Error listener for GET request
     */
    private class ErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("error", "Http request failed");
        }
    }

    public void createEvents(List<ParseObject> events, boolean invited){
        for (int i = 0; i < events.size(); i++) {
            EventCard card = new EventCard(getActivity(), R.layout.event_card);
            Event event = new Event();
            event.setDate((String) events.get(i).get("date"));
            card.setDate((String) events.get(i).get("date"));
            event.setAddress((String) events.get(i).get("address"));
            card.setAddress((String) events.get(i).get("address"));

            String invitees = "ajbrowne, tlinman, brmahar";
//            for (String person: (String[]) events.get(i).get("attendees")){
//                    invitees += person + ",";
//            }
            event.setInvitees(invitees);
            card.setInvitees(invitees);
            event.setEventId(events.get(i).getObjectId());
            card.setEventId(events.get(i).getObjectId());
            event.setLocation((String) events.get(i).get("location"));
            card.setLocation((String) events.get(i).get("location"));
            event.setName((String) events.get(i).get("name"));
            card.setName((String) events.get(i).get("name"));
            event.setTime((String) events.get(i).get("time"));
            card.setTime((String) events.get(i).get("time"));
            card.setUsername(((HomeActivity) getActivity()).getUser().getUsername());
            card.setActivity(getActivity());
            if (invited){
                card.setInvited(true);
            }
            card.setOnClickListener(getCardOnClickListener(event));
            cards.add(card);
        }
    }

    private Card.OnCardClickListener getCardOnClickListener(final Event event) {
        return new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                EventCard temp = (EventCard) card;

                Geocoder geocode = new Geocoder(getActivity());
                String addr = temp.getAddress();
                geocodeAddress(addr, geocode);

                intent.putExtra("name", temp.getName());
                intent.putExtra("date", temp.getDate());
                intent.putExtra("location", temp.getLocation());
                intent.putExtra("address", temp.getAddress());
                intent.putExtra("time", temp.getTime());
                intent.putExtra("invitees", temp.getInvitees());
                intent.putExtra("fromHome", true);
                intent.putExtra("lat", latitude);
                intent.putExtra("long", longitude);
                startActivity(intent);
            }
        };
    }

    public void createCards(){
        mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
        CardListView cardListView = (CardListView) homeView.findViewById(R.id.eventList);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
            cardListView.setOnScrollListener(this);
            cardListView.deferNotifyDataSetChanged();
        }

        if (mPullToRefreshLayout != null) {
            mPullToRefreshLayout.setRefreshComplete();
        }
    }


    public View getHomeView() {
        return homeView;
    }

    @Override
    public void onRefreshStarted(View view) {
        cards = new ArrayList<Card>();
        makeRequestAttending();
    }

    public void notifyChange(String eventId){
        for (Card card: cards){
            if (((EventCard) card).getEventId().equals(eventId)){
                ((EventCard) card).setInvited(false);
                mCardArrayAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
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
