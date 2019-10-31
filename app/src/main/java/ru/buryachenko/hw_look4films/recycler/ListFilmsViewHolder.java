package ru.buryachenko.hw_look4films.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static ru.buryachenko.hw_look4films.activities.MainActivity.getRightSide;
import static ru.buryachenko.hw_look4films.activities.MainActivity.isFavorite;
import static ru.buryachenko.hw_look4films.utils.SomeAnimation.doAnimationDetails;
import static ru.buryachenko.hw_look4films.utils.SomeAnimation.doAnimationPushRight;

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
        card.setSelected(film.isSelected());
        name.setText(film.getName());
        Glide.with(itemView.getContext())
                .load(film.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_favorite)
                .into(picture);
        liked.setImageResource(film.getLiked() ? R.drawable.liked : R.drawable.notliked);
        card.setCardElevation(film.isSelected() ? 0F : card.getMaxCardElevation());
        details.setText(film.getDetails());
        favorite.setVisibility(isFavorite(film) ? View.VISIBLE : View.GONE);
        doAnimationPushRight(!film.isDisclosed(), getRightSide(),  liked, name, detailsButton);
        doAnimationDetails(!film.isDisclosed(), details);
    }

}
