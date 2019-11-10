package ru.buryachenko.hw_look4films.recycler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import static ru.buryachenko.hw_look4films.recycler.FavoritesAdapter.HOLDER_ITEM;
import static ru.buryachenko.hw_look4films.utils.SomeAnimation.doAnimationFly;

public class FavoritesItemAnimator extends RecyclerView.ItemAnimator {

    private AnimatorSet anime;

    @Override
    public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @Nullable ItemHolderInfo postLayoutInfo) {
        if (viewHolder.getItemViewType() == HOLDER_ITEM) {
            viewHolder.itemView.animate().cancel();
            anime = doAnimationFly(viewHolder.itemView, false);
            anime.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    anime.removeAllListeners();
                    viewHolder.itemView.setVisibility(View.GONE);
                }
            });
            anime.start();
            return true;
        }
        return false;
    }

    @Override
    public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder, @Nullable ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        if (viewHolder.getItemViewType() == HOLDER_ITEM) {
            anime = doAnimationFly(viewHolder.itemView, true);
            anime.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    anime.removeAllListeners();
                    viewHolder.setIsRecyclable(true);
                }
            });
            anime.start();
            return true;
        }
        return false;
    }

    @Override
    public boolean animatePersistence(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        return false;
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        return false;
    }

    @Override
    public void runPendingAnimations() {
    }

    @Override
    public void endAnimation(@NonNull RecyclerView.ViewHolder item) {
    }

    @Override
    public void endAnimations() {
    }

    @Override
    public boolean isRunning() {
        return anime != null && anime.isRunning();
    }
}