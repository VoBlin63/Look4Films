package ru.buryachenko.hw_look4films.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.utils.RandomPicture;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;

import static ru.buryachenko.hw_look4films.activities.MainActivity.FRAGMENT_LIST;
import static ru.buryachenko.hw_look4films.activities.MainActivity.callFragment;

public class FragmentCreate extends Fragment {

    private FilmsViewModel viewModel;
    private View layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = view;
        viewModel = ViewModelProviders.of(getActivity()).get(FilmsViewModel.class);
        ImageView pictureNewFilm = layout.findViewById(R.id.pictureNewFilm);
        pictureNewFilm.setImageDrawable(RandomPicture.make(getResources().getDimensionPixelSize(R.dimen.recyclerImageWidth), getResources().getDimensionPixelSize(R.dimen.recyclerImageHeight)));

        layout.findViewById(R.id.doneFloat).setOnClickListener((view_tmp) ->
                doCreateFilm(((TextView) layout.findViewById(R.id.nameNewFilm)).getText().toString(), ((TextView) layout.findViewById(R.id.detailsNewFilm)).getText().toString()));
    }

    private void doCreateFilm(String name, String details) {
        FilmInApp newFilm = FilmInApp.create(name, details);
        if (newFilm == null) {
            Snackbar.make(layout, getString(R.string.messageNeed4NEwFilm) + " "
                            + (name.trim().isEmpty() ? getString(R.string.messageNeed4NEwFilm_name) + " " : "")
                            + (details.trim().isEmpty() && name.trim().isEmpty() ? getString(R.string.messageNeed4NEwFilm_and) + " " : "")
                            + (details.trim().isEmpty() ? getString(R.string.messageNeed4NEwFilm_Details) : "")
                            + " " + getString(R.string.messageNeed4NEwFilm_end)
                    , Snackbar.LENGTH_LONG).show();
            return;
        }
        viewModel.putFilm(newFilm);
        ((TextView) layout.findViewById(R.id.nameNewFilm)).setText("");
        ((TextView) layout.findViewById(R.id.detailsNewFilm)).setText("");
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        callFragment(FRAGMENT_LIST);
    }

}
