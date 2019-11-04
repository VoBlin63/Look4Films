package ru.buryachenko.hw_look4films.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.recycler.ListFilmsAdapter;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;

import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;

public class FragmentListFilms extends Fragment {

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
        final LinearLayoutManager layoutManager = new LinearLayoutManager(layout.getContext());
        recyclerView.setLayoutManager(layoutManager);
        ListFilmsAdapter adapter = new ListFilmsAdapter(LayoutInflater.from(layout.getContext()), viewModel);
        recyclerView.setAdapter(adapter);

        LiveData<FilmInApp> changedFilm = viewModel.getChangedFilm();
        changedFilm.observe(this, film -> notifyChanges(adapter, film));

        LiveData<Boolean> isBusy = viewModel.getIsBusy();
        isBusy.observe(this, busy -> MainActivity.showBusy(busy));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager.findFirstCompletelyVisibleItemPosition() > viewModel.getList().size() - 5) {
                    viewModel.loadNext();
                }
            }
        });
    }

    private void notifyChanges(ListFilmsAdapter adapter, FilmInApp film) {
        int position = viewModel.whoWasChanged(film);
        if (position < 0) {
            Log.d(LOGTAG, "viewModel.whoWasChanged(film) = Upssssssssssssssss!");
            //не должно такого быть
        } else {
            adapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //могли быть изменения из виджета, нужно перечитать
        FilmInApp saved = viewModel.loadSavedSelected();
        if (saved != null) {
            viewModel.putFilm(saved);
        }
    }

}