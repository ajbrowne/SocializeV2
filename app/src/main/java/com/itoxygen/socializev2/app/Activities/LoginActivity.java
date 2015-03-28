package com.itoxygen.socializev2.app.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itoxygen.socializev2.app.Models.User;
import com.itoxygen.socializev2.app.R;
import com.itoxygen.socializev2.app.Fragments.LoginFragment;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseActivity {

    private User user;
    private Map<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Parse.initialize(this, "KNZvn9YyTZ4c0EQIp37V5oQZgdiLl6aaBEYbGyJ6", "pymeDxLGAkxNkuF2pchL1tbNGxegLPx0sLCL9iqC");
        registerNotifications();
        user = new User();
        params = new HashMap<String, String>();
        ParseUser currUser = ParseUser.getCurrentUser();
        if (currUser != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        } else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            LoginFragment fragment = new LoginFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.loginContainer, fragment, "initial");
            fragmentTransaction.commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public User getUser(){return user;}

    public void register(final String email, final String password, final String first, final String last) {
        final ParseUser userNew = new ParseUser();
        userNew.setUsername(email);
        userNew.setPassword(password);
        userNew.put("first_name", first);
        userNew.put("last_name", last);

        userNew.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    onStop();
                    onPause();
                    startActivity(intent);
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Toast.makeText(getApplicationContext(),
                            e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void login(String email, String password) {
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser theUser, ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    onStop();
                    onPause();
                    startActivity(intent);
                } else{
                    Toast.makeText(
                            getApplicationContext(),
                            "Login error.",
                            Toast.LENGTH_LONG).show();
                    System.out.println(e);
                }
            }
        });
    }

    public void registerNotifications(){
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}
