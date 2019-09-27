package ru.buryachenko.hw_look4films.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.utils.RandomPicture;

import static ru.buryachenko.hw_look4films.utils.Constants.FILM_PARAMETER;

public class NewFilmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_film);

        Toolbar toolbar = findViewById(R.id.newFilmToolbar);
        toolbar.setTitle(getString(R.string.titleWindowNewFilm));
        setSupportActionBar(toolbar);

        ImageView pictureNewFilm = findViewById(R.id.pictureNewFilm);
        pictureNewFilm.setImageDrawable(RandomPicture.make(getResources().getDimensionPixelSize(R.dimen.recyclerImageWidth), getResources().getDimensionPixelSize(R.dimen.recyclerImageHeight)));

        findViewById(R.id.doneFloat).setOnClickListener((view) -> makeNewFilm());
    }

    private void makeNewFilm() {
        TextView name = findViewById(R.id.nameNewFilm);
        TextView details = findViewById(R.id.detailsNewFilm);
        FilmInApp newFilm = FilmInApp.create(name.getText().toString(), details.getText().toString());
        if (newFilm == null) {
            //TODO пожалуй, ошибку надо из create вытащить в будущем
            Snackbar.make(name, getString(R.string.messageNeed4NEwFilm)
                            + (name.getText().toString().trim().isEmpty() ? getString(R.string.messageNeed4NEwFilm_name) : "")
                            + (details.getText().toString().trim().isEmpty() && name.getText().toString().trim().isEmpty() ? getString(R.string.messageNeed4NEwFilm_and) : "")
                            + (details.getText().toString().trim().isEmpty() ? getString(R.string.messageNeed4NEwFilm_Details) : "")
                            + getString(R.string.messageNeed4NEwFilm_end)
                    , Snackbar.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(FILM_PARAMETER, newFilm);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_new_film_activity, menu);
        return true;//super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backToMainScreen:
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
