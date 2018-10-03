package io.github.httpmattpvaughn.spaghetti.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import io.github.httpmattpvaughn.spaghetti.R;

/**
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public class AppSettingsImpl implements AppSettings {

    private static final String NSFW_KEY_STRING = "NSFW_KEY";
    private SharedPreferences prefs;

    public AppSettingsImpl(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public boolean isNSFW() {
        return prefs.getBoolean(NSFW_KEY_STRING, false);
    }

    @Override
    public void setNSFW(boolean isNSFW) {
        this.prefs.edit().putBoolean(NSFW_KEY_STRING, isNSFW).apply();
    }
}
