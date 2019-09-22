package ru.buryachenko.hw_look4films.viewmodel;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.activities.DetailsActivity;
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static ru.buryachenko.hw_look4films.utils.Constants.DURATION_DETAILS_ANIMAYION;
import static ru.buryachenko.hw_look4films.utils.Constants.FILM_PARAMETER;
import static ru.buryachenko.hw_look4films.utils.Constants.REQUEST_DETAILS;

public class RecyclerFilmsAdapter extends RecyclerView.Adapter<RecyclerFilmsAdapter.RecyclerFilmsHolder> {
    private List<FilmInApp> films;

    public List<FilmInApp> getData() {
        return films;
    }

    private static int rightSide = 900;

    public void setData(List<FilmInApp> newFilms) {
        films = newFilms;
    }

    public static class RecyclerFilmsHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView picture;
        ImageView liked;
        CardView card;
        TextView details;
        TextView detailsButton;

        RecyclerFilmsHolder(CardView row) {
            super(row);
            picture = row.findViewById(R.id.picture);
            name = row.findViewById(R.id.name);
            liked = row.findViewById(R.id.likedStar);
            details = row.findViewById(R.id.details);
            detailsButton = row.findViewById(R.id.detailsButton);
            card = row;
        }
    }

    public RecyclerFilmsAdapter(List<FilmInApp> dataset) {
        films = dataset;
    }

    @Override
    public RecyclerFilmsHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        CardView filmRow = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_films_layout, parent, false);
        RecyclerFilmsHolder res = new RecyclerFilmsHolder(filmRow);
        WindowManager wm = (WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        rightSide = size.x;
        View[] slaves = {filmRow.findViewById(R.id.likedStar), filmRow.findViewById(R.id.name), filmRow.findViewById(R.id.detailsButton)};
        TextView details = filmRow.findViewById(R.id.details);
        filmRow.findViewById(R.id.picture).setOnClickListener(view -> doAnimationPush(details, false, slaves));
        filmRow.findViewById(R.id.detailsButton).setOnClickListener(view -> doClick(view, res.getAdapterPosition(), parent.getContext()));
        details.setOnClickListener(view -> doAnimationPush(details, false, slaves));
        return res;
    }

    private void doAnimationPush(View major, boolean instantMode, View... slaves) {
        boolean onScreen = (major.getVisibility() == View.VISIBLE);
        major.setAlpha(onScreen ? 1F : 0F);
        if (!onScreen)
            major.setVisibility(View.VISIBLE);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(major, "alpha", onScreen ? 0F : 1F);
        final AnimatorSet set = new AnimatorSet();
        set.play(alpha);
        set.setDuration(instantMode ? 0 : DURATION_DETAILS_ANIMAYION);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onScreen) {
                    major.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        for (View slave : slaves) {
            pushRightView(slave, instantMode, onScreen);
        }
    }

    private void pushRightView(View view, boolean instandMode, boolean returnBack) {
        ObjectAnimator push = ObjectAnimator.ofFloat(view, "translationX", returnBack ? 0 : rightSide);
        final AnimatorSet set = new AnimatorSet();
        set.play(push);
        set.setDuration(instandMode ? 0 : DURATION_DETAILS_ANIMAYION);
        set.start();
    }

    @Override
    public void onBindViewHolder(RecyclerFilmsHolder holder, int position) {
        FilmInApp film = films.get(position);
        boolean detailsShowed = (holder.details.getVisibility() == View.VISIBLE);
        if (detailsShowed) {
            doAnimationPush(holder.details, true, holder.name, holder.liked, holder.detailsButton);
        }
        holder.itemView.setSelected(film.getSelected());
        holder.picture.setImageDrawable(film.getPicture(holder.picture.getContext()));
        holder.name.setText(film.getName());
        holder.liked.setImageResource(film.getLiked() ? R.drawable.liked : R.drawable.notliked);
        holder.card.setCardElevation(film.getSelected() ? 0F : holder.card.getMaxCardElevation());
        holder.details.setText(film.getDetails());
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    private void doClick(View view, int position, Context context) {
        if (position != RecyclerView.NO_POSITION) {
            if (!films.get(position).getSelected()) {
                //отменим предыдущий выбор
                for (int i = 0; i < films.size(); i++) {
                    if (films.get(i).getSelected()) {
                        films.get(i).setSelected(false);
                        notifyItemChanged(i);
                        break;
                    }
                }
            }
            films.get(position).setSelected(true);
            callDetailsActivity(context, films.get(position));
            notifyItemChanged(position);
        }
    }

    private void callDetailsActivity(Context context, FilmInApp film) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(FILM_PARAMETER, film);
        ((Activity) context).startActivityForResult(intent, REQUEST_DETAILS);
    }

}