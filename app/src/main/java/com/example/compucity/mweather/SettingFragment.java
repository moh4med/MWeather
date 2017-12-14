package com.example.compucity.mweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.example.compucity.mweather.R;

/**
 * Created by CompuCity on 12/14/2017.
 */

public class SettingFragment
        extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);
        SharedPreferences sharedpreference = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefscreen=getPreferenceScreen();
        int count=prefscreen.getPreferenceCount();
        for (int i=0;i<count;i++){
            Preference p=prefscreen.getPreference(i);
            if(!(p instanceof CheckBoxPreference)){
                String val=sharedpreference.getString(p.getKey(),"");
                setPreferenceSummary(p,val);
            }
        }
    }

    public void setPreferenceSummary(Preference p, Object o) {
        String value = o.toString();
        String key = p.getKey();
        if (p instanceof ListPreference) {
            ListPreference lp= (ListPreference) p;
            int pid=lp.findIndexOfValue(value);
            if(pid>=0){
                p.setSummary(lp.getEntries()[pid]);
            }
        }else{
            p.setSummary(value);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference p=findPreference(s);
        if(p!=null){
            if(!(p instanceof CheckBoxPreference)){
                setPreferenceSummary(p,sharedPreferences.getString(s,""));
            }
        }
    }
}
