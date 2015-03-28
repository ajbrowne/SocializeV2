package com.itoxygen.socializev2.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.itoxygen.socializev2.app.Fragments.EventFragment;
import com.itoxygen.socializev2.app.R;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("Meetups");

        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("username", currentUser.getUsername());
        installation.saveInBackground();

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
                        if(canback){
                            getActionBar().setDisplayHomeAsUpEnabled(true);
                        } else{
                            getActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                    }
                });

        setContentView(R.layout.activity_home);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                //Intent intent = new Intent(this, SettingsActivity.class);
                //startActivity(intent);
                return true;
            case R.id.action_new:
                FragmentManager fragmentManager = getSupportFragmentManager();
                EventFragment fragment = new EventFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.newEventContainer, fragment);
                fragmentTransaction.addToBackStack("event");
                fragmentTransaction.commit();
                return true;
            case android.R.id.home:
                onBackPressed();
                getActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
            case R.id.action_logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
