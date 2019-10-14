package ru.buryachenko.hw_look4films.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static ru.buryachenko.hw_look4films.activities.MainActivity.FRAGMENT_LIST;
import static ru.buryachenko.hw_look4films.activities.MainActivity.callFragmentFromSaver;


public class FragmentForSave extends Fragment {

    private static String savedTag = "";

    static FragmentForSave newInstance(String callFragment) {
        FragmentForSave saver = new FragmentForSave();
        if (!callFragment.isEmpty()) {
            savedTag = callFragment;
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
        if (savedTag.isEmpty())
            savedTag = FRAGMENT_LIST;
        callFragmentFromSaver(savedTag);
        return null;
    }

}
