package ru.buryachenko.hw_look4films.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.buryachenko.hw_look4films.App;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.api.responce.FilmJson;
import ru.buryachenko.hw_look4films.api.responce.WholeResponce;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;

import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String FRAGMENT_LIST = "ListOfFilm.F";
    public static final String FRAGMENT_DETAILS = "Details.F";
    public static final String FRAGMENT_CREATE = "CreateNew.F";
    public static final String FRAGMENT_SAVER = "Saver.F";
    public static final String FRAGMENT_FAVORITES = "Favorites.F";
    private static int rightSide = 0;

    private static final String apiKey = "c3e17ff26735628669886b00d573ab4d";
    private static final String query = "whiplash";
    private static final String language = "ru-RU";
    private static final String region = "RU";

    private static FilmsViewModel viewModel;
    private static FragmentManager fragmentManager;
    public static BottomNavigationView bottomNavigation;
    private static View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_drawer);
        mainView = findViewById(R.id.mainLayout);

        viewModel = ViewModelProviders.of(this).get(FilmsViewModel.class);


        App.getInstance().service.getFilms(apiKey,query,language,region).enqueue(new Callback<WholeResponce>() {
            @Override
            public void onResponse(Call<WholeResponce> call, Response<WholeResponce> response) {
                if (response.isSuccessful()) {
                    viewModel.clear();

                    for (FilmJson filmJson : response.body().getResults()) {
//                        Log.d(LOGTAG,filmJson.getTitle().toString());
                        viewModel.putFilm(new FilmInApp(filmJson));
                    }
//                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    Log.d(LOGTAG, "Что-то пошло не так : response.message() = " + response.message());
                }
            }

            @Override
            public void onFailure(Call<WholeResponce> call, Throwable throwable) {
                Log.d(LOGTAG, "Что-то пошло не так : onFailure throwable = " + throwable.getMessage());
            }
        });

        fragmentManager = getSupportFragmentManager();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener);

        callFragment("");

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView drawerNavigation = findViewById(R.id.navigation_view);
        //TODO не работает листенер этот - почему ?
        drawerNavigation.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawerNavigation.getHeaderView(0).setBackgroundColor(this.getApplicationContext().getColor(R.color.drawerHead));
        } else {
            drawerNavigation.getHeaderView(0).setBackgroundColor(Color.CYAN);
        }
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        rightSide = size.x;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentManager.getBackStackEntryCount() <= 1)
                finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOGTAG,"topMenuQuit");
        switch (item.getItemId()) {
            case R.id.topMenuQuit:
                tryToExit(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainScreen:
                callFragment(FRAGMENT_LIST);
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
                callFragment(FRAGMENT_LIST);
                break;
            case R.id.bottomNavLookDetails:
                FilmInApp selectedFilm = viewModel.getSelected();
                if (selectedFilm == null) {
                    snackMessage(mainView.getResources().getString(R.string.messageNoSelectedFilm));
                } else {
                    callFragment(FRAGMENT_DETAILS);
                }
                break;
            case R.id.bottomNavAddFilm:
                callFragment(FRAGMENT_CREATE);
                break;
            case R.id.bottomNavFavorite:
                callFragment(FRAGMENT_FAVORITES);
                break;
        }
        return true;
    };

    public static void callFragment(String screenTag) {
        fragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, FragmentForSave.newInstance(screenTag), FRAGMENT_SAVER)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public static void callFragmentFromSaver(String screenTag) {
//        Log.d(LOGTAG, "callFragment '" + screenTag + "': StCount = " + fragmentManager.getBackStackEntryCount());
        Fragment toCall = fragmentManager.findFragmentByTag(screenTag);
        if (toCall != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, toCall, screenTag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            return;
        }
        switch (screenTag) {
            case FRAGMENT_DETAILS:
                toCall = new FragmentDetails();
                break;
            case FRAGMENT_CREATE:
                toCall = new FragmentCreate();
                break;
            case FRAGMENT_LIST:
                toCall = new FragmentListFilms();
                break;
            case FRAGMENT_FAVORITES:
                toCall = new FragmentFavorites();
                break;
            default:
                toCall = null;
        }
        if (toCall != null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer, toCall, screenTag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public static void snackMessage(String message) {
        Snackbar snack = Snackbar.make(mainView,
                message, Snackbar.LENGTH_LONG);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                snack.getView().getLayoutParams();
        params.setMargins(0, 0, 0, bottomNavigation.getHeight());
        snack.getView().setLayoutParams(params);
        snack.show();
    }

    public static boolean isFavorite(FilmInApp film) {
        return viewModel.isFavorite(film);
    }

    public static int getRightSide() {
        return rightSide;
    }
}
