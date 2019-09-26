package ru.buryachenko.hw_look4films.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static ru.buryachenko.hw_look4films.utils.Constants.FILM_PARAMETER;


public class DetailsActivity extends AppCompatActivity {

    private FilmInApp film;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = findViewById(R.id.detailsToolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        film = (FilmInApp) intent.getSerializableExtra(FILM_PARAMETER);
        CheckBox liked = findViewById(R.id.liked);
        liked.setChecked(film.getLiked());
        liked.setOnCheckedChangeListener((checkbox, checked) -> film.setLiked(checked));
        ((TextView) findViewById(R.id.details)).setText(film.getDetails());
        ((TextView) findViewById(R.id.name)).setText(film.getName());
        ((EditText) findViewById(R.id.comment)).setText(film.getComment());
        ((ImageView) findViewById(R.id.picture)).setImageDrawable(film.getPicture(this));
        findViewById(R.id.doneFloat).setOnClickListener((view) -> doSaveDetails(film));

    }

    private void doSaveDetails(FilmInApp film) {
        film.setComment(((EditText) findViewById(R.id.comment)).getText().toString());
        Intent intent = new Intent();
        intent.putExtra(FILM_PARAMETER, film);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_details_activity, menu);
        return true;//super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareSelectedFilm: {
                String shareThing = film.getLinkToShare();
                String mimeType = "text/plain";
                ShareCompat.IntentBuilder
                        .from(this)
                        .setType(mimeType)
                        .setChooserTitle(getString(R.string.detailsShareMenu))
                        .setText(shareThing)
                        .startChooser();
                }
                break;
            case R.id.backToMainScreen:
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }
}
