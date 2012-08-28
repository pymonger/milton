package com.ettrema.android.photouploader;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *
 * @author brad
 */
public class Config {

    private final MiltonPhotoUploader application;

    public Config( MiltonPhotoUploader application ) {
        this.application = application;
    }

    public String getUserName() {
        return prefs().getString( "userName", "" );
    }

    public String getPassword() {
        return prefs().getString( "password", "" );
    }

    public String getBaseUrl() {
        return "http://" + prefs().getString( "serverAddress", "" );
    }

    private SharedPreferences prefs() {
        return PreferenceManager.getDefaultSharedPreferences( application.getBaseContext() );
    }
}
