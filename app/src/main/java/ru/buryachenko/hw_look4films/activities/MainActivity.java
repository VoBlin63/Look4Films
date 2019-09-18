package ru.buryachenko.hw_look4films.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;
import ru.buryachenko.hw_look4films.viewmodel.RecyclerFilmsAdapter;

import static ru.buryachenko.hw_look4films.constants.Constants.FILM_PARAMETER;
import static ru.buryachenko.hw_look4films.constants.Constants.REQUEST_DETAILS;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerFilms;
    private RecyclerView.Adapter adapterRecyclerFilms;
    private RecyclerView.LayoutManager layoutManagerFilms;
    private FilmsViewModel viewModel;
    private Boolean possibleToShare = true; //TODO сделать false до того как будет выбран фильм, как из recycler adapter поменять значение тут?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(FilmsViewModel.class);

        recyclerFilms = findViewById(R.id.recyclerLayoutFilms);
        layoutManagerFilms = new LinearLayoutManager(this);
        adapterRecyclerFilms = new RecyclerFilmsAdapter(viewModel.getList(this));
        recyclerFilms.setHasFixedSize(true);
        recyclerFilms.setLayoutManager(layoutManagerFilms);
        recyclerFilms.setAdapter(adapterRecyclerFilms);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.shareSelectedFilm).setEnabled(possibleToShare);
        return true;//super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareSelectedFilm: {
                String shareThing = viewModel.getFilmToShare();
                String mimeType = "text/plain";
                ShareCompat.IntentBuilder
                        .from(this)
                        .setType(mimeType)
                        .setChooserTitle(getString(R.string.shareTitle))
                        .setText(shareThing)
                        .startChooser();
            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    public void setPossibleToShare(Boolean possibleToShare) {
        this.possibleToShare = possibleToShare;

    }
}
