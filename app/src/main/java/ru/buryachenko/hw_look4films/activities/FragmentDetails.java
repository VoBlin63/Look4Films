package ru.buryachenko.hw_look4films.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;

import static ru.buryachenko.hw_look4films.activities.MainActivity.FRAGMENT_LIST;
import static ru.buryachenko.hw_look4films.activities.MainActivity.callFragment;
import static ru.buryachenko.hw_look4films.utils.Constants.PREFERENCES_SELECTED_FILM;

public class FragmentDetails extends Fragment {
    private FilmsViewModel viewModel;
    private View layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(FilmsViewModel.class);
        layout = view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //если присваивания в onViewCreated - беда, они переприсваиваются
        FilmInApp film = viewModel.getSelected();
        if (film == null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            callFragment(FRAGMENT_LIST);
        } else {
            writeSelectedFilm(film);
            layout.findViewById(R.id.doneFloat).setOnClickListener((doneView) -> doSaveDetails(layout, film));
        }
        ((CheckBox) layout.findViewById(R.id.liked)).setChecked(film.getLiked());
        ((TextView) layout.findViewById(R.id.details)).setText(film.getDetails());
        ((TextView) layout.findViewById(R.id.name)).setText(film.getName());
        ((EditText) layout.findViewById(R.id.comment)).setText(film.getComment());
        Glide.with(layout.getContext())
                .load(film.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_favorite)
                .into((ImageView)layout.findViewById(R.id.picture));
    }

    private void doSaveDetails(View view, FilmInApp film) {
        film = new FilmInApp(film);
        film.setComment(((EditText) view.findViewById(R.id.comment)).getText().toString());
        film.setLiked(((CheckBox) view.findViewById(R.id.liked)).isChecked());
        writeSelectedFilm(film);
        viewModel.putFilm(film);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();
        callFragment(FRAGMENT_LIST);
    }

    private void refreshWidget() {
        int[] ids = AppWidgetManager.getInstance(getContext()).getAppWidgetIds(new ComponentName(getContext(), WidgetProvider.class));
        WidgetProvider widget = new WidgetProvider();
        widget.onUpdate(getContext(), AppWidgetManager.getInstance(getContext()), ids);
    }

    private void writeSelectedFilm(FilmInApp film) {
        if (film == null)
            return;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFERENCES_SELECTED_FILM, film.toWidgetString());
        editor.apply();
        refreshWidget();
    }
}
