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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static ru.buryachenko.hw_look4films.activities.MainActivity.FRAGMENT_DETAILS;
import static ru.buryachenko.hw_look4films.activities.MainActivity.callFragment;
import static ru.buryachenko.hw_look4films.activities.MainActivity.turnInFavorites;
import static ru.buryachenko.hw_look4films.utils.Constants.DURATION_DETAILS_ANIMAYION;

public class ListFilmsAdapter extends RecyclerView.Adapter<ListFilmsViewHolder> {

    private static int rightSide = 0;
    private FilmsViewModel viewModel;

    public ListFilmsAdapter(LayoutInflater inflater, FilmsViewModel viewModel) {
        this.viewModel = viewModel;
        WindowManager wm = (WindowManager) inflater.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        rightSide = size.x;
    }

    @NonNull
    @Override
    public ListFilmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView filmRow = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_films_item, parent, false);
        ListFilmsViewHolder res = new ListFilmsViewHolder(filmRow);

        filmRow.findViewById(R.id.listPicture).setOnClickListener(view -> doChangeDisclosed(filmRow, res.getAdapterPosition()));
        filmRow.findViewById(R.id.listPicture).setOnLongClickListener(view -> doTurnInFavorites(filmRow, res.getAdapterPosition()));

        View.OnClickListener detailsCallClick = view -> callDetails(res.getAdapterPosition());
        filmRow.findViewById(R.id.listDetailsButton).setOnClickListener(detailsCallClick);
        filmRow.findViewById(R.id.listDetails).setOnClickListener(detailsCallClick);
        return res;
    }

    private void doChangeDisclosed(CardView filmRow, int position) {
        if (position != RecyclerView.NO_POSITION) {
            boolean newDisclosed = !viewModel.getList().get(position).isDisclosed();
            doAnimationPicture(filmRow.findViewById(R.id.listPicture));
            doAnimationPushRight(!newDisclosed, filmRow.findViewById(R.id.listLiked), filmRow.findViewById(R.id.listName), filmRow.findViewById(R.id.listDetailsButton));
            doAnimationDetails(!newDisclosed, filmRow.findViewById(R.id.listDetails));
            viewModel.getList().get(position).setDisclosed(newDisclosed);
        }
    }

    private boolean doTurnInFavorites(CardView filmRow, int position) {
        if (position != RecyclerView.NO_POSITION) {
            turnInFavorites(viewModel.getList().get(position));
            notifyItemChanged(position);
        }
        return true;
    }

    @Override
    public void onBindViewHolder(@NonNull ListFilmsViewHolder holder, int position) {
        holder.bind(viewModel.getList().get(position));
    }

    @Override
    public int getItemCount() {
        return viewModel.getList().size();
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

    static void doAnimationPushRight(boolean returnBack, View... views) {
        for (View view : views) {
            ObjectAnimator push = ObjectAnimator.ofFloat(view, "translationX", returnBack ? 0 : rightSide);
            AnimatorSet set = new AnimatorSet();
            set.play(push);
            set.setDuration(DURATION_DETAILS_ANIMAYION);
            set.start();
        }
    }

    static void doAnimationDetails(boolean returnBack, View view) {
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

    private void callDetails(int position) {
        if (position != RecyclerView.NO_POSITION) {
            Integer previousSelected;
            if (!viewModel.getList().get(position).isSelected()) {
                previousSelected = FilmInApp.getSelected();
                if (previousSelected != null) {
                    FilmInApp.clearSelected();
                    for (int i = 0; i < viewModel.getList().size(); i++) {
                        if (viewModel.getList().get(i).getFilmId() == previousSelected) {
                            notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }
            viewModel.getList().get(position).setSelected();
            callFragment(FRAGMENT_DETAILS);
            notifyItemChanged(position);
        }
    }
}
