package ru.buryachenko.hw_look4films.viewmodel;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;

public class RecyclerFilmsAdapter extends RecyclerView.Adapter<RecyclerFilmsAdapter.RecyclerFilmsHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class RecyclerFilmsHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public RecyclerFilmsHolder(ConstraintLayout row) {
            super(row);
            name = row.findViewById(R.id.recyclerFilmsName);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerFilmsAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerFilmsHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_films_layout, parent, false);
        //...тут onClick вешаем и тд
        return new RecyclerFilmsHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerFilmsHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}