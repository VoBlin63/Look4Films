package ru.buryachenko.hw_look4films.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ru.buryachenko.hw_look4films.App;


public class SharedPreferencesOperation {
    private static SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(App.getInstance());

    public static void save(String key, String value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    public static String load(String key, String defaultValue) {
        String res = defaultValue;
        if (settings.contains(key)) {
            res = settings.getString(key, defaultValue);
        }
        return res;
    }
}
