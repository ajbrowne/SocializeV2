package com.itoxygen.socializev2.app.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.itoxygen.socializev2.app.Activities.HomeActivity;
import com.itoxygen.socializev2.app.Adapters.VolleyAdapter;
import com.itoxygen.socializev2.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {

    private View friendView;
    private List<String> friends = new ArrayList<String>();
    private Button invite;

    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        friendView = inflater.inflate(R.layout.fragment_friend, container, false);
        invite = (Button) friendView.findViewById(R.id.friendButton);
        ListView makes = (ListView) friendView.findViewById(R.id.friend_search_listview);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        makes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(friends.contains(((TextView) view).getText().toString())){
                    friends.remove(((TextView) view).getText().toString());
                    view.setBackgroundColor(Color.parseColor("#EFEFEF"));
                } else{
                    friends.add(((TextView) view).getText().toString());
                    view.setBackgroundColor(Color.parseColor("#1abc9c"));
                }
                if (!friends.isEmpty()){
                    invite.setVisibility(View.VISIBLE);
                } else{
                    invite.setVisibility(View.GONE);
                }
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).setFriends(friends);
                getActivity().onBackPressed();
            }
        });

        ArrayList<String> friendArray = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.friends)));
        VolleyAdapter alphaAdapter = new VolleyAdapter(getActivity(), R.layout.custom_item, friendArray);
        makes.setAdapter(alphaAdapter);

        return friendView;
    }
}
