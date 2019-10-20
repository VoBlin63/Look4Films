package ru.buryachenko.hw_look4films.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

class FavoritesViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private ImageView picture;
    private View layout;

    FavoritesViewHolder(@NonNull View itemView) {
        super(itemView);
        layout = itemView;
        name = itemView.findViewById(R.id.favoritesName);
        picture = itemView.findViewById(R.id.favoritesPicture);
    }

    void bind(FilmInApp film) {
        name.setText(film.getName());
        picture.setImageDrawable(film.getPicture(layout.getContext()));
    }
}
