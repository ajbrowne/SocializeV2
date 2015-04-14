package com.itoxygen.socializev2.app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by Dazztrazak on 4/10/15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "KNZvn9YyTZ4c0EQIp37V5oQZgdiLl6aaBEYbGyJ6", "pymeDxLGAkxNkuF2pchL1tbNGxegLPx0sLCL9iqC");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
