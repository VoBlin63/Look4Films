package ru.buryachenko.hw_look4films.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.utils.FilmsDiffUtilCallback;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;
import ru.buryachenko.hw_look4films.viewmodel.RecyclerFilmsAdapter;

public class FragmentListOfFilms extends Fragment {

    private RecyclerFilmsAdapter adapterRecyclerFilms;
    private FilmsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_films, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(FilmsViewModel.class);
        RecyclerView recyclerFilms = view.findViewById(R.id.recyclerLayoutFilms);
        RecyclerView.LayoutManager layoutManagerFilms = new LinearLayoutManager(view.getContext());
        adapterRecyclerFilms = new RecyclerFilmsAdapter(viewModel.getList());
        recyclerFilms.setHasFixedSize(true);
        recyclerFilms.setLayoutManager(layoutManagerFilms);
        recyclerFilms.setAdapter(adapterRecyclerFilms);

        LiveData<FilmInApp> changedFilm = viewModel.getChangedFilm();
        changedFilm.observe(this, filmInApp -> {
            FilmsDiffUtilCallback productDiffUtilCallback = new FilmsDiffUtilCallback(adapterRecyclerFilms.getData(), viewModel.getList());
            DiffUtil.DiffResult filmsDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
            adapterRecyclerFilms.setData(viewModel.getList());
            filmsDiffResult.dispatchUpdatesTo(adapterRecyclerFilms);
        });
    }


}
