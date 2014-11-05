package com.itoxygen.socializev2.app.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;

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

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseActivity {

    private User user;
    private Map<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = new User();
        params = new HashMap<String, String>();
        FragmentManager fragmentManager = getSupportFragmentManager();
        LoginFragment fragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.loginContainer, fragment, "initial");
        fragmentTransaction.commit();
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

    public void register(final String email, final String password){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://141.219.159.47:8080/api/users", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Success");
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                String source="admin"+":"+"adminpassword";
                String base64EncodedCredentials = Base64.encodeToString(source.getBytes(), Base64.NO_WRAP);

                params.put(
                        "Authorization", "Basic " +
                                base64EncodedCredentials);
                params.put("username",email);
                params.put("password",password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }


}
