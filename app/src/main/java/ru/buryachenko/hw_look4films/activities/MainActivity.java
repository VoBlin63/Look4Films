package ru.buryachenko.hw_look4films.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.utils.FilmsDiffUtilCallback;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;
import ru.buryachenko.hw_look4films.viewmodel.RecyclerFilmsAdapter;

import static ru.buryachenko.hw_look4films.utils.Constants.FILM_PARAMETER;
import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;
import static ru.buryachenko.hw_look4films.utils.Constants.REQUEST_DETAILS;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerFilms;
    private RecyclerView.Adapter adapterRecyclerFilms;
    private RecyclerView.LayoutManager layoutManagerFilms;
    private FilmsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(FilmsViewModel.class);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(this.getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this.getApplicationContext(), R.drawable.divider_recycler)));

        recyclerFilms = findViewById(R.id.recyclerLayoutFilms);
        layoutManagerFilms = new LinearLayoutManager(this);
        adapterRecyclerFilms = new RecyclerFilmsAdapter(viewModel.getList(this));
        recyclerFilms.addItemDecoration(itemDecorator);
        recyclerFilms.setHasFixedSize(true);
        recyclerFilms.setLayoutManager(layoutManagerFilms);
        recyclerFilms.setAdapter(adapterRecyclerFilms);

        LiveData<FilmInApp> changedFilm = viewModel.getChangedFilm();
        changedFilm.observe(this, filmInApp -> {
            FilmsDiffUtilCallback productDiffUtilCallback = new FilmsDiffUtilCallback(((RecyclerFilmsAdapter)adapterRecyclerFilms).getData(), viewModel.getList(this));
            DiffUtil.DiffResult filmsDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
            ((RecyclerFilmsAdapter)adapterRecyclerFilms).setData(viewModel.getList(this));
            filmsDiffResult.dispatchUpdatesTo(adapterRecyclerFilms);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_DETAILS) {
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        FilmInApp film = (FilmInApp) data.getSerializableExtra(FILM_PARAMETER);
        viewModel.put(film);
        Log.d(LOGTAG,"Для фильма '" + film.getName() +"' возвращено значение Нравится " + film.getLiked() + " и комментарий '" + film.getComment() + "'");
    }

}
