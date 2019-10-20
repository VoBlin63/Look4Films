package ru.buryachenko.hw_look4films.viewmodel;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

public class FavoritesAdapter extends RecyclerView.Adapter {
    static final int HOLDER_ITEM = 0;
    private static final int HOLDER_ADD_NEW = 1;
    List<FilmInApp> films;

    private LayoutInflater inflater;
    private FilmsViewModel viewModel;

    public FavoritesAdapter(LayoutInflater inflater, FilmsViewModel viewModel) {
        this.inflater = inflater;
        this.viewModel = viewModel;
        this.films = viewModel.getFavorites();
    }

    public List<FilmInApp> getFavoritesFromAdapter() {
        return films;
    }

    void removeItem(int position) {
        if (position > 0) {
            viewModel.removeFromFavorites(films.get(position - 1));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HOLDER_ADD_NEW)
            return new FavoritesAddHolder(inflater.inflate(R.layout.favorites_add_card, parent, false), viewModel);
        else
            return new FavoritesViewHolder(inflater.inflate(R.layout.favorites_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FavoritesViewHolder) {
            ((FavoritesViewHolder)holder).bind(films.get(position-1));
        }
        else  {
            ((FavoritesAddHolder)holder).bind();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HOLDER_ADD_NEW : HOLDER_ITEM;
    }

    @Override
    public int getItemCount() {
        return 1 + films.size();
    }

}
