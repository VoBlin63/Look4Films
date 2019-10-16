package ru.buryachenko.hw_look4films.viewmodel;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private List<FilmInApp> items;
    private LayoutInflater inflater;

    public FavoritesAdapter(List<FilmInApp> items, LayoutInflater inflater) {
        this.items = items;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoritesViewHolder(inflater.inflate(R.layout.favorites_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
