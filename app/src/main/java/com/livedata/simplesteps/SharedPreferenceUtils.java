package com.livedata.simplesteps;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {

    private SharedPreferenceUtils mUtils;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    SharedPreferenceUtils(Context context) {
        mPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.apply();
//        if (mUtils == null)
//            mUtils = new SharedPreferenceUtils(context.getApplicationContext());
    }

    void setValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    String getStringValue(String key) {
        return mPreferences.getString(key, null);
    }

    public void removeKey(String key) {
        if (mEditor != null) {
            mEditor.remove(key);
            mEditor.commit();
        }
    }

    public void clear() {
        mEditor.clear().commit();
    }
}