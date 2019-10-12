package ru.buryachenko.hw_look4films.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;

public class FragmentDetails extends Fragment {
    private FilmsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(FilmsViewModel.class);
        FilmInApp film = viewModel.getSelected();
        if (film == null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        } else {
            ((CheckBox) view.findViewById(R.id.liked)).setChecked(film.getLiked());
            ((TextView) view.findViewById(R.id.details)).setText(film.getDetails());
            ((TextView) view.findViewById(R.id.name)).setText(film.getName());
            ((EditText) view.findViewById(R.id.comment)).setText(film.getComment());
            ((ImageView) view.findViewById(R.id.picture)).setImageDrawable(film.getPicture(getActivity()));
            view.findViewById(R.id.doneFloat).setOnClickListener((doneView) -> doSaveDetails(view, film));
        }
    }

    private void doSaveDetails(View view, FilmInApp film) {
        film.setComment(((EditText) view.findViewById(R.id.comment)).getText().toString());
        film.setLiked(((CheckBox) view.findViewById(R.id.liked)).isChecked());
        viewModel.put(film);
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
