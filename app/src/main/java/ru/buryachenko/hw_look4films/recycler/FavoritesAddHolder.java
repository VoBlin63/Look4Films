package ru.buryachenko.hw_look4films.recycler;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;

import static ru.buryachenko.hw_look4films.utils.SomeAnimation.doAnimationPressPicture;

class FavoritesAddHolder extends RecyclerView.ViewHolder {

    private Spinner spinner;
    private View layout;
    private FilmsViewModel viewModel;
    private SpinnerCustomAdapter spinnerAdapter;
    private ImageView addButton;

    private FilmInApp selectedFilm = null;

    FavoritesAddHolder(@NonNull View itemView, FilmsViewModel viewModel) {
        super(itemView);
        this.viewModel = viewModel;
        layout = itemView;
        spinner = itemView.findViewById(R.id.favoritesAddSpinner);
        addButton = itemView.findViewById(R.id.favoritesAddButton);
        addButton.setOnClickListener((view) -> {
            if (selectedFilm != null) {
                viewModel.addInFavorites(selectedFilm);
            }
            doAnimationPressPicture(view);
        });
    }

    void bind() {
        spinnerAdapter = new SpinnerCustomAdapter(layout.getContext(), android.R.layout.simple_spinner_item, viewModel.getNonFavorites());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        if (spinnerAdapter.getCount() == 0) {
            addButton.setAlpha(0.5F);
            return;
        }
        addButton.setAlpha(1F);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilm = ((FilmInApp)parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }
}
