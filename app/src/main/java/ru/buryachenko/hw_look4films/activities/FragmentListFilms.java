package ru.buryachenko.hw_look4films.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.buryachenko.hw_look4films.App;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.db.ServiceDb;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.recycler.ListFilmsAdapter;
import ru.buryachenko.hw_look4films.utils.SharedPreferencesOperation;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;

import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;
import static ru.buryachenko.hw_look4films.utils.Constants.PREFERENCES_TIME_TO_UPDATE;
import static ru.buryachenko.hw_look4films.utils.Constants.STATUS_SERVICE_BUSY;

public class FragmentListFilms extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FilmsViewModel viewModel;
    private View layout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresher;

    private Observable<String> serviceUpdateStatus;

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

        // SwipeRefreshLayout
        swipeRefresher = layout.findViewById(R.id.swipeRefresh);
        swipeRefresher.setOnRefreshListener(this);
        swipeRefresher.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        serviceUpdateStatus = ServiceDb.getStatus();
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                if (STATUS_SERVICE_BUSY.equals(s)) {
                    swipeRefresher.setRefreshing(true);
                } else {
                    swipeRefresher.setRefreshing(false);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        serviceUpdateStatus.subscribe(observer);

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

    @Override
    public void onRefresh() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        DialogInterface.OnClickListener listener =
                (dialog, which) -> {
                    if (which == Dialog.BUTTON_POSITIVE) {
                        callDbUpdateService();
                    } else {
                        swipeRefresher.setRefreshing(false);
                    }
                    dialog.dismiss();
                };
        builder.setMessage(App.getInstance().getString(R.string.confirmationUpdateDbText));
        builder.setNegativeButton(App.getInstance().getString(R.string.confirmationUpdateDbCancel), listener);
        builder.setPositiveButton(App.getInstance().getString(R.string.confirmationUpdateDbYes), listener);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void callDbUpdateService() {
        SharedPreferencesOperation.save(PREFERENCES_TIME_TO_UPDATE, "0"); //сбрасываем время запланированного обновления
        ((MainActivity) getActivity()).callServiceDbUpdate();
    }
}
