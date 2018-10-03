package io.github.httpmattpvaughn.spaghetti.settings;

import android.content.Intent;
import android.os.Bundle;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import io.github.httpmattpvaughn.spaghetti.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.preferences);

        Preference preference = findPreference("license");
        preference.setOnPreferenceClickListener(preference1 -> {
            new LibsBuilder()
                    .withActivityStyle(Libs.ActivityStyle.DARK)
                    .start(getContext());
            return false;
        });
    }

}