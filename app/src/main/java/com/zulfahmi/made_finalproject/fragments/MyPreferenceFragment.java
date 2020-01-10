package com.zulfahmi.made_finalproject.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.zulfahmi.made_finalproject.reminder.AlarmReceiver;
import com.zulfahmi.made_finalproject.R;

public class MyPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    private String DAILY;
    private String RELEASE;

    private SwitchPreference dailySwitch;
    private SwitchPreference releaseSwitch;

    AlarmReceiver alarmReceiver;

    public MyPreferenceFragment() { }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        alarmReceiver = new AlarmReceiver();
        init();
        setSummaries();
    }

    private void init() {
        DAILY = getResources().getString(R.string.key_daily);
        RELEASE = getResources().getString(R.string.key_release);

        dailySwitch = findPreference(DAILY);
        releaseSwitch = findPreference(RELEASE);
    }

    private void setSummaries() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        dailySwitch.setChecked(sh.getBoolean(DAILY, true));
        releaseSwitch.setChecked(sh.getBoolean(RELEASE, true));
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(DAILY)) {
            Boolean state = sharedPreferences.getBoolean(DAILY, true);
            if(state){
                dailySwitch.setChecked(true);
                alarmReceiver.setAlarm(getContext(), AlarmReceiver.TYPE_DAILY, "07:00", getResources().getString(R.string.message_daily));
                Toast.makeText(getContext(), "Daily Reminder is Enabled", Toast.LENGTH_SHORT).show();
            }else{
                dailySwitch.setChecked(false);
                alarmReceiver.cancelAlarm(getContext(), AlarmReceiver.TYPE_DAILY);
                Toast.makeText(getContext(), "Daily Reminder is Disabled", Toast.LENGTH_SHORT).show();
            }
        }
        if (s.equals(RELEASE)) {
            Boolean state = sharedPreferences.getBoolean(RELEASE, true);
            if(state){
                releaseSwitch.setChecked(true);
                alarmReceiver.setAlarm(getContext(), AlarmReceiver.TYPE_RELEASE, "08:00", getResources().getString(R.string.message_release_today));
                Toast.makeText(getContext(), "Release Today Reminder is Enabled", Toast.LENGTH_SHORT).show();
            }else{
                releaseSwitch.setChecked(false);
                alarmReceiver.cancelAlarm(getContext(), AlarmReceiver.TYPE_RELEASE);
                Toast.makeText(getContext(), "Release Today Reminder is Disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
