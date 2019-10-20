package ru.buryachenko.hw_look4films.recycler;


import android.animation.Animator;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class FavoritesItemAnimator extends RecyclerView.ItemAnimator {
    private final static int MOVE_DURATION = 555;
    private final ArrayList<FavoritesAnimationContainer> pending = new ArrayList<>();
    private final HashMap<RecyclerView.ViewHolder, FavoritesAnimationContainer> persistences = new HashMap<>();

    @Override
    public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @Nullable ItemHolderInfo postLayoutInfo) {
        return false;
    }

    @Override
    public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder, @Nullable ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        return false;
    }

    @Override
    public boolean animatePersistence(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        if (preLayoutInfo.top != postLayoutInfo.top) {
            int toTop = preLayoutInfo.top - postLayoutInfo.top;
            FavoritesAnimationContainer persistence = persistences.get(viewHolder);
            if (persistence != null && persistence.isRunning) {
                persistence.top = persistence.holder.itemView.getTranslationY() + toTop;
                persistence.start();
                dispatchAnimationFinished(viewHolder);
                return false;
            }
            pending.add(new FavoritesAnimationContainer(viewHolder, toTop));
            dispatchAnimationStarted(viewHolder);
            return true;
        }
        dispatchAnimationFinished(viewHolder);
        return false;
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        dispatchAnimationFinished(oldHolder);
        dispatchAnimationFinished(newHolder);
        return false;
    }

    @Override
    public void runPendingAnimations() {
        for (FavoritesAnimationContainer ai : pending) {
            ai.start();
        }
        pending.clear();
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        FavoritesAnimationContainer ai = persistences.get(item);
        if (ai != null && ai.isRunning) {
            ai.holder.itemView.animate().cancel();
        }
    }

    @Override
    public void endAnimations() {
        for (FavoritesAnimationContainer ai : persistences.values())
            if (ai.isRunning)
                ai.holder.itemView.animate().cancel();
    }

    @Override
    public boolean isRunning() {
        return !pending.isEmpty() &&
                !persistences.isEmpty();
    }

    private final class FavoritesAnimationContainer implements Animator.AnimatorListener {
        private final RecyclerView.ViewHolder holder;
        private float top;
        private boolean isRunning = false;

        private FavoritesAnimationContainer(RecyclerView.ViewHolder holder, float top) {
            this.holder = holder;
            this.top = top;
        }

        void start() {
            View itemView = holder.itemView;
            itemView.animate().setListener(this);
            itemView.setTranslationY(top);
            itemView.animate().translationY(0f).setDuration(MOVE_DURATION);
            persistences.put(holder, this);
            isRunning = true;
        }

        private void resetViewHolderState() {
            holder.itemView.setTranslationY(0f);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            persistences.remove(holder);
            resetViewHolderState();
            holder.itemView.animate().setListener(null);
            dispatchAnimationFinished(holder);
            if (!isRunning())
                dispatchAnimationsFinished();
            isRunning = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            holder.itemView.setTranslationY(0f);
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}