package ru.buryachenko.hw_look4films.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
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

import static ru.buryachenko.hw_look4films.utils.Constants.ADD_NEW_FILM;
import static ru.buryachenko.hw_look4films.utils.Constants.FILM_PARAMETER;
import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;
import static ru.buryachenko.hw_look4films.utils.Constants.REQUEST_DETAILS;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_LIST ="ru.buryachenko.hw_look4films.ListOfFilm.Fragment";
    private static final String FRAGMENT_DETAILS ="ru.buryachenko.hw_look4films.Details.Fragment";
    private static final String FRAGMENT_CREATE_NEW ="ru.buryachenko.hw_look4films.CreateNew.Fragment";
    private static FilmsViewModel viewModel;
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_drawer);
        viewModel = ViewModelProviders.of(this).get(FilmsViewModel.class);
        viewModel.init();

        fragmentManager = getSupportFragmentManager();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        BottomNavigationView navigation = findViewById(R.id.bottomNavigation);
        navigation.setOnNavigationItemSelectedListener(bottomNavigationListener);

        FragmentListOfFilms listOfFilms = (FragmentListOfFilms) getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST);
//        if (listOfFilms != null) {
//            //а нужно ли это ?
//            getSupportFragmentManager().beginTransaction().remove(listOfFilms).commit();
//        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new FragmentListOfFilms(), FRAGMENT_LIST)
                .addToBackStack(null)
                .commit();

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            navigationView.getHeaderView(0).setBackgroundColor(this.getApplicationContext().getColor(R.color.drawerHead));
        } else {
            navigationView.getHeaderView(0).setBackgroundColor(Color.CYAN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if ((requestCode == REQUEST_DETAILS) || (requestCode == ADD_NEW_FILM)) {
            FilmInApp film = (FilmInApp) data.getSerializableExtra(FILM_PARAMETER);
            viewModel.put(film);
            Log.d(LOGTAG, "Для фильма '" + film.getName() + "' возвращено значение Нравится " + film.getLiked() + " и комментарий '" + film.getComment() + "'");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNewFilm:
                callNewFilmActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainScreen:
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.messageWeAlredyInMainScreen), Toast.LENGTH_SHORT).show();
                break;
            case R.id.aboutApplication:
                showToast(getApplicationContext().getString(R.string.messageAboutApp));
                break;
            case R.id.exitApp:
                tryToExit(getApplicationContext());
        }
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContainer = (LinearLayout) toast.getView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toastContainer.setBackground(getApplicationContext().getDrawable(R.drawable.about_app));
        }
        toast.show();
    }

    private void callNewFilmActivity() {
        Intent intent = new Intent(this, NewFilmActivity.class);
        startActivityForResult(intent, ADD_NEW_FILM);
    }

    public void tryToExit(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener listener =
                (dialog, which) -> {
                    if (which == Dialog.BUTTON_POSITIVE) {
                        finish();
                    }
                    dialog.dismiss();
                };
        builder.setMessage(context.getString(R.string.exitDialogQuestion));
        builder.setNegativeButton(context.getString(R.string.exitDialogKeep), listener);
        builder.setPositiveButton(context.getString(R.string.exitDialogExit), listener);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.bottomNavQuit:
                tryToExit(this);
                break;
            case R.id.bottomNavLookDetails:
                FilmInApp selectedFilm = viewModel.getSelected();
                if (selectedFilm != null) {
                    callDetailsActivity(this, selectedFilm);
                }
                break;
            case R.id.bottomNavAddFilm:
                callNewFilmActivity();
                break;
        }
        return true;
    };

    public static void callDetailsActivity(Context context, FilmInApp film) {
        viewModel.setSelectedFilm(film);
        FragmentDetails fragmentDetails = (FragmentDetails) fragmentManager.findFragmentByTag(FRAGMENT_DETAILS);
        if (fragmentDetails != null) {
            //а нужно ли это ?
            fragmentManager.beginTransaction().remove(fragmentDetails).commit();
        }
        fragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, new FragmentDetails(), FRAGMENT_DETAILS)
                .addToBackStack(null)
                .commit();
    }
}
