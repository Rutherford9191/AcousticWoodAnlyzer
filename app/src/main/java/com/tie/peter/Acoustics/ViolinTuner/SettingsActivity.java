package com.tie.peter.Acoustics.ViolinTuner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.MenuItem;
import android.widget.Toast;

import com.tie.peter.Acoustics.R;


public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    public static final String SETTINGS_FILE = "com.darshancomputing.tuner_preferences";
    //public static final String SP_STORE_FILE = "sp_store";

    //public static final String KEY_LARGER_BUFFER = "larger_buffer";
    public static final String KEY_A4_HZ = "a4_hz";
    public static final String KEY_A4_HZ_OTHER = "a4_hz_other";
    public static final String KEY_FLAT_SHARP_HINT = "flat_sharp_hint";

    private static final String[] PARENTS    = {};
    private static final String[] DEPENDENTS = {};

    private static final String[] LIST_PREFS = {KEY_A4_HZ};

    private Resources res;
    private PreferenceScreen mPreferenceScreen;
    private SharedPreferences mSharedPreferences;

    //private static SharedPreferences sp_store;

    private String pref_screen;

    //private int menu_res = R.menu.settings;

    private static final Object[] EMPTY_OBJECT_ARRAY = {};
    private static final  Class[]  EMPTY_CLASS_ARRAY = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        res = getResources();
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        PreferenceManager pm = getPreferenceManager();
        pm.setSharedPreferencesName(SETTINGS_FILE);
        pm.setSharedPreferencesMode(Context.MODE_MULTI_PROCESS);
        mSharedPreferences = pm.getSharedPreferences();

        setPrefScreen(R.xml.settings);
        setTitle(res.getString(R.string.settings_activity_subtitle));

        setEnablednessOfA4Other();
        setA4OtherSummary();

        for (int i=0; i < PARENTS.length; i++)
            setEnablednessOfDeps(i);

        for (int i=0; i < LIST_PREFS.length; i++)
            updateListPrefSummary(LIST_PREFS[i]);
    }

    private void setPrefScreen(int resource) {
        addPreferencesFromResource(resource);

        mPreferenceScreen  = getPreferenceScreen();
    }

    private void restartThisScreen() {
        ComponentName comp = new ComponentName(getPackageName(), SettingsActivity.class.getName());
        Intent intent = new Intent().setComponent(comp);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

        if (key.equals(KEY_A4_HZ))
            setEnablednessOfA4Other();

        if (key.equals(KEY_A4_HZ_OTHER)) {
            EditTextPreference p = (EditTextPreference) mPreferenceScreen.findPreference(KEY_A4_HZ_OTHER);
            String text = p.getText();

            if (text.isEmpty()) {
                String default_a4_hz = res.getString(R.string.default_a4_hz);
                p.setText(default_a4_hz);
            }

            float f = java.lang.Float.parseFloat(text);

            if (f < 220)
                p.setText("220.0");
            else if (f > 880)
                p.setText("880.0");
        }

        if (key.equals(KEY_A4_HZ) || key.equals(KEY_A4_HZ_OTHER))
            setA4OtherSummary();

        for (int i=0; i < PARENTS.length; i++) {
            if (key.equals(PARENTS[i])) {
                setEnablednessOfDeps(i);
                break;
            }
        }

        for (int i=0; i < LIST_PREFS.length; i++) {
            if (key.equals(LIST_PREFS[i])) {
                updateListPrefSummary(LIST_PREFS[i]);
                break;
            }
        }

        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void setEnablednessOfA4Other() {
        String default_a4_hz = res.getString(R.string.default_a4_hz);
        String a4_hz = mSharedPreferences.getString(KEY_A4_HZ, default_a4_hz);
        if ("other".equals(a4_hz))
            mPreferenceScreen.findPreference(KEY_A4_HZ_OTHER).setEnabled(true);
        else
            mPreferenceScreen.findPreference(KEY_A4_HZ_OTHER).setEnabled(false);
    }

    private void setA4OtherSummary() {
        EditTextPreference p = (EditTextPreference) mPreferenceScreen.findPreference(KEY_A4_HZ_OTHER);

        if (p.isEnabled()) {
            p.setSummary(res.getString(R.string.currently_set_to) + p.getText());
        } else {
            p.setSummary(res.getString(R.string.currently_disabled));
        }
    }

    private void setEnablednessOfDeps(int index) {
        Preference dependent = mPreferenceScreen.findPreference(DEPENDENTS[index]);
        if (dependent == null) return;

        if (mSharedPreferences.getBoolean(PARENTS[index], false))
            dependent.setEnabled(true);
        else
            dependent.setEnabled(false);

        updateListPrefSummary(DEPENDENTS[index]);
    }

    private void setEnablednessOfMutuallyExclusive(String key1, String key2) {
        Preference pref1 = mPreferenceScreen.findPreference(key1);
        Preference pref2 = mPreferenceScreen.findPreference(key2);

        if (pref1 == null) return;

        if (mSharedPreferences.getBoolean(key1, false))
            pref2.setEnabled(false);
        else if (mSharedPreferences.getBoolean(key2, false))
            pref1.setEnabled(false);
        else {
            pref1.setEnabled(true);
            pref2.setEnabled(true);
        }
    }

    private void updateListPrefSummary(String key) {
        ListPreference pref;
        try { /* Code is simplest elsewhere if we call this on all dependents, but some aren't ListPreferences. */
            pref = (ListPreference) mPreferenceScreen.findPreference(key);
        } catch (java.lang.ClassCastException e) {
            return;
        }

        if (pref == null) return;

        if (pref.isEnabled()) {
            pref.setSummary(res.getString(R.string.currently_set_to) + pref.getEntry());
        } else {
            pref.setSummary(res.getString(R.string.currently_disabled));
        }
    }

    public void donateButtonClick(android.view.View v) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                                     Uri.parse("market://details?id=com.darshancomputing.BatteryIndicatorPro")));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Sorry, can't launch Market!", Toast.LENGTH_SHORT).show();
        }
    }
}
