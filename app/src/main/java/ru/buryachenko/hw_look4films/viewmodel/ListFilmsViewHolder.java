package ru.buryachenko.hw_look4films.viewmodel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static ru.buryachenko.hw_look4films.activities.MainActivity.isFavorite;

class ListFilmsViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private ImageView picture;
    private ImageView liked;
    private CardView card;
    private TextView details;
    private TextView detailsButton;
    private ImageView favorite;
    private View layout;

    ListFilmsViewHolder(@NonNull CardView itemView) {
        super(itemView);
        layout = itemView;
        name = itemView.findViewById(R.id.listName);
        picture = itemView.findViewById(R.id.listPicture);
        liked = itemView.findViewById(R.id.listLiked);
        details = itemView.findViewById(R.id.listDetails);
        detailsButton = itemView.findViewById(R.id.listDetailsButton);
        favorite = itemView.findViewById(R.id.listFavorite);
        card = itemView;
    }

    void bind(FilmInApp film) {
        //itemView.setSelected(film.isSelected());
        card.setSelected(film.isSelected());
        name.setText(film.getName());
        picture.setImageDrawable(film.getPicture(layout.getContext()));
        liked.setImageResource(film.getLiked() ? R.drawable.liked : R.drawable.notliked);
        card.setCardElevation(film.isSelected() ? 0F : card.getMaxCardElevation());
        details.setText(film.getDetails());
        favorite.setVisibility(isFavorite(film) ? View.VISIBLE : View.GONE);
        ListFilmsAdapter.doAnimationPushRight(!film.isDisclosed(), liked, name, detailsButton);
        ListFilmsAdapter.doAnimationDetails(!film.isDisclosed(), details);
    }

}
