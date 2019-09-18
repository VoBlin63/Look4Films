package ru.buryachenko.hw_look4films.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static ru.buryachenko.hw_look4films.constants.Constants.FILM_PARAMETER;


public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        FilmInApp film = (FilmInApp) intent.getSerializableExtra(FILM_PARAMETER);
        ((TextView) findViewById(R.id.details)).setText(film.getDetails());
        ((ImageView) findViewById(R.id.picture)).setImageDrawable(film.getPicture(this));
        findViewById(R.id.done).setOnClickListener((view) -> doSaveDetails(film));
    }

    private void doSaveDetails(FilmInApp film) {
        Intent intent = new Intent();
        intent.putExtra(FILM_PARAMETER, film);
        setResult(RESULT_OK, intent);
        finish();
    }
}
