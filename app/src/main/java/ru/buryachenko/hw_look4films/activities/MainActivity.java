package ru.buryachenko.hw_look4films.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.viewmodel.FilmsViewModel;
import ru.buryachenko.hw_look4films.viewmodel.RecyclerFilmsAdapter;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerFilms;
    private RecyclerView.Adapter adapterRecyclerFilms;
    private RecyclerView.LayoutManager layoutManagerFilms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FilmsViewModel viewModel = ViewModelProviders.of(this).get(FilmsViewModel.class);

        recyclerFilms = findViewById(R.id.recyclerLayoutFilms);
        layoutManagerFilms = new LinearLayoutManager(this);
        adapterRecyclerFilms = new RecyclerFilmsAdapter(viewModel.getList());
        recyclerFilms.setHasFixedSize(true);
        recyclerFilms.setLayoutManager(layoutManagerFilms);
        recyclerFilms.setAdapter(adapterRecyclerFilms);
    }
}
