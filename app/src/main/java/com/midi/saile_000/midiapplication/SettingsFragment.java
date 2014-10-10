package com.midi.saile_000.midiapplication;


import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by saile_000 on 10.10.2014.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource

        addPreferencesFromResource(R.xml.pref_programlist);
    }

}