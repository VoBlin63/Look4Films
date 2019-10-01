package ru.buryachenko.hw_look4films.viewmodel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
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
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static ru.buryachenko.hw_look4films.activities.MainActivity.callDetailsActivity;
import static ru.buryachenko.hw_look4films.utils.Constants.DURATION_DETAILS_ANIMAYION;

public class RecyclerFilmsAdapter extends RecyclerView.Adapter<RecyclerFilmsAdapter.RecyclerFilmsHolder> {
    private List<FilmInApp> films;

    public List<FilmInApp> getData() {
        return films;
    }

    private static int rightSide = 900;

    public void setData(List<FilmInApp> newFilms) {
        films = newFilms;
    }

    static class RecyclerFilmsHolder extends RecyclerView.ViewHolder {
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

        filmRow.findViewById(R.id.picture).setOnClickListener(view -> doChangeDisclosed(filmRow, res.getAdapterPosition()));
        filmRow.findViewById(R.id.detailsButton).setOnClickListener(view -> doClick(view, res.getAdapterPosition(), parent.getContext()));
        //здесь клик возможен только на раскрытых деталях и логично в детали пойти
        filmRow.findViewById(R.id.details).setOnClickListener(view -> doClick(view, res.getAdapterPosition(), parent.getContext()));
        return res;
    }

    private void doChangeDisclosed(CardView filmRow, int position) {
        if (position != RecyclerView.NO_POSITION) {
            boolean newDisclosed = !films.get(position).isDisclosed();
            doAnimationPicture(filmRow.findViewById(R.id.picture));
            doAnimationPushRight(!newDisclosed, filmRow.findViewById(R.id.likedStar), filmRow.findViewById(R.id.name), filmRow.findViewById(R.id.detailsButton));
            doAnimationDetails(!newDisclosed, filmRow.findViewById(R.id.details));
            films.get(position).setDisclosed(newDisclosed);
        }
    }

    private void doAnimationDetails(boolean returnBack, View view) {
        float from = 1F;
        float to = 0F;
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, returnBack ? from : to, returnBack ? to : from);
        AnimatorSet set = new AnimatorSet();
        set.play(alpha);
        set.setDuration(DURATION_DETAILS_ANIMAYION);
        if (returnBack) {
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                }
            });
        } else {
            view.setVisibility(View.VISIBLE);
        }

        set.start();
    }

    private void doAnimationPicture(View view) {
        float from = 1F;
        float to = 0.87F;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, from, to);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, from, to);
        AnimatorSet setForward = new AnimatorSet();
        setForward.playTogether(scaleX, scaleY);
        setForward.setDuration(DURATION_DETAILS_ANIMAYION / 4);
        ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(view, View.SCALE_X, to, from);
        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(view, View.SCALE_Y, to, from);
        AnimatorSet setBack = new AnimatorSet();
        setBack.playTogether(scaleXBack, scaleYBack);
        setBack.setDuration(DURATION_DETAILS_ANIMAYION / 3);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(setForward, setBack);
        set.start();
    }

    private void doAnimationPushRight(boolean returnBack, View... views) {
        for (View view : views) {
            ObjectAnimator push = ObjectAnimator.ofFloat(view, "translationX", returnBack ? 0 : rightSide);
            AnimatorSet set = new AnimatorSet();
            set.play(push);
            set.setDuration(DURATION_DETAILS_ANIMAYION);
            set.start();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerFilmsHolder holder, int position) {
        FilmInApp film = films.get(position);
        holder.itemView.setSelected(film.isSelected());
        holder.picture.setImageDrawable(film.getPicture(holder.picture.getContext()));
        holder.name.setText(film.getName());
        holder.liked.setImageResource(film.getLiked() ? R.drawable.liked : R.drawable.notliked);
        holder.card.setCardElevation(film.isSelected() ? 0F : holder.card.getMaxCardElevation());
        holder.details.setText(film.getDetails());
        doAnimationPushRight(!film.isDisclosed(), holder.liked, holder.name, holder.detailsButton);
        doAnimationDetails(!film.isDisclosed(), holder.details);
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    private void doClick(View view, int position, Context context) {
        if (position != RecyclerView.NO_POSITION) {
            if (!films.get(position).isSelected()) {
                //отменим предыдущий выбор
                for (int i = 0; i < films.size(); i++) {
                    if (films.get(i).isSelected()) {
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

}