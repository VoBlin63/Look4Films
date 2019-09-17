package ru.buryachenko.hw_look4films.viewmodel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

public class RecyclerFilmsAdapter extends RecyclerView.Adapter<RecyclerFilmsAdapter.RecyclerFilmsHolder> {
    private FilmInApp[] films;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class RecyclerFilmsHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public ImageView picture;
        public Button detailsButton;
        RecyclerFilmsHolder(ConstraintLayout row) {
            super(row);
            picture = row.findViewById(R.id.picture);
            name = row.findViewById(R.id.name);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerFilmsAdapter(FilmInApp[] dataset) {
        films = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerFilmsHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        ConstraintLayout filmRow = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_films_layout, parent, false);
        RecyclerFilmsHolder res = new RecyclerFilmsHolder(filmRow);
        filmRow.getViewById(R.id.detailsButton).setOnClickListener(view -> doClick(res.getAdapterPosition(), filmRow));
        Log.d("MMS","res.isSelected = " );
        return res;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerFilmsHolder holder, int position) {
        holder.itemView.setSelected(films[position].getSelected());
        holder.picture.setImageDrawable(films[position].getPicture());
        holder.name.setText(films[position].getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return films.length;
    }

    private void doClick(int position, ConstraintLayout layout) {
        if (position != RecyclerView.NO_POSITION) {
            if (films[position].getSelected())
                return;
            for (int i = 0; i < films.length; i++) {
                if (films[i].getSelected()) {
                    films[i].setSelected(false);
                    notifyItemChanged(i);
                    break;
                }
            }
            films[position].setSelected(true);
            notifyItemChanged(position);
        }
    }
}