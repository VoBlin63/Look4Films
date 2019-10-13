package ru.buryachenko.hw_look4films.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static ru.buryachenko.hw_look4films.activities.MainActivity.FRAGMENT_LIST;
import static ru.buryachenko.hw_look4films.activities.MainActivity.callFragmentFromSaver;
import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;


public class FragmentForSave extends Fragment {

    private static final String KEY_SAVE = "KEY4SAVE";
    private static String savedTag = "";

    static FragmentForSave newInstance(String text) {
        FragmentForSave saver = new FragmentForSave();
//        Log.d(LOGTAG, " FragmentForSave newInstance for " + text);
        if (!text.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_SAVE, text);
            saver.setArguments(bundle);
        }
        return saver;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            savedTag = getArguments().getString(KEY_SAVE, "");
        }
//        Log.d(LOGTAG, "onCreate Saver savedTag = " + savedTag);
        if (savedTag.isEmpty())
            callFragmentFromSaver(FRAGMENT_LIST);
        else
            callFragmentFromSaver(savedTag);
        return null;
    }

}
