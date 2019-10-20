package ru.buryachenko.hw_look4films.recycler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import static ru.buryachenko.hw_look4films.recycler.FavoritesAdapter.HOLDER_ITEM;


public class FavoritesTouch extends ItemTouchHelper.Callback {

    private FavoritesAdapter adapter;

    public FavoritesTouch(FavoritesAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == HOLDER_ITEM) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            return makeMovementFlags(0,0);
        }

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder.getItemViewType() == HOLDER_ITEM) {
            adapter.removeItem(viewHolder.getAdapterPosition());
        }
    }
}
