package ru.buryachenko.hw_look4films.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.viewmodel.FavoritesAdapter;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;

public class FragmentFavorites extends Fragment {

    private FilmsViewModel viewModel;
    private RecyclerView recyclerView;
    private View layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = view;
        viewModel = ViewModelProviders.of(getActivity()).get(FilmsViewModel.class);
        recyclerView = layout.findViewById(R.id.recyclerLayoutFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(layout.getContext()));
        recyclerView.setAdapter(new FavoritesAdapter(viewModel.getFavorites(), LayoutInflater.from(layout.getContext())));
    }

    @Override
    public void onResume() {
        super.onResume();
        //могли быть изменения из основного списка
    }
}
