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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.viewmodel.FavoritesAdapter;
import ru.buryachenko.hw_look4films.viewmodel.FavoritesTouch;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;
import ru.buryachenko.hw_look4films.viewmodel.ListFilmsAdapter;

public class FragmentListFilms extends Fragment {

    //private ListFilmsAdapter adapterRecyclerFilms;
    private FilmsViewModel viewModel;
    private View layout;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_films, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(FilmsViewModel.class);
        layout = view;

        recyclerView = layout.findViewById(R.id.recyclerListFilms);
        recyclerView.setLayoutManager(new LinearLayoutManager(layout.getContext()));
        ListFilmsAdapter adapter = new ListFilmsAdapter(LayoutInflater.from(layout.getContext()), viewModel);
//        ItemTouchHelper touch = new ItemTouchHelper(new FavoritesTouch(adapter));
//        touch.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

//        RecyclerView recyclerFilms = view.findViewById(R.id.recyclerLayoutFilms);
//        RecyclerView.LayoutManager layoutManagerFilms = new LinearLayoutManager(view.getContext());
//        adapterRecyclerFilms = new RecyclerFilmsAdapter(viewModel);
//        recyclerFilms.setHasFixedSize(true);
//        recyclerFilms.setLayoutManager(layoutManagerFilms);
//        recyclerFilms.setAdapter(adapterRecyclerFilms);

//        LiveData<FilmInApp> changedFilm = viewModel.getChangedFilm();
//        changedFilm.observe(this, filmInApp -> {
//            FilmsDiffUtilCallback productDiffUtilCallback = new FilmsDiffUtilCallback(adapterRecyclerFilms.getData(), viewModel.getList());
//            DiffUtil.DiffResult filmsDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
//            adapterRecyclerFilms.setData(viewModel.getList());
//            filmsDiffResult.dispatchUpdatesTo(adapterRecyclerFilms);
//        });
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        //могли быть изменения из виджета, нужно перечитать
//        FilmInApp saved = viewModel.loadSavedSelected();
//        if (saved != null) {
//            viewModel.putFilm(saved);
//        }
//    }

}
