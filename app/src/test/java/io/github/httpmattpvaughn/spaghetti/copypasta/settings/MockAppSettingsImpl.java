package io.github.httpmattpvaughn.spaghetti.copypasta.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import io.github.httpmattpvaughn.spaghetti.settings.AppSettings;

/**
 * A fake appsettings which does not depend on Context. Settings can set/get
 * as needed for testing.
 *
 * Created by Matt Vaughn: http://mattpvaughn.github.io/
 */
public class MockAppSettingsImpl implements AppSettings {

    private boolean isNSFW;

    public MockAppSettingsImpl(boolean isNSFW) {
        this.isNSFW = isNSFW;
    }

    @Override
    public void setNSFW(boolean NSFW) {
        isNSFW = NSFW;
    }

    @Override
    public boolean isNSFW() {
        return isNSFW;
    }
}
