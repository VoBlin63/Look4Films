package ru.buryachenko.hw_look4films.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.activities.DetailsActivity;
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static ru.buryachenko.hw_look4films.constants.Constants.FILM_PARAMETER;
import static ru.buryachenko.hw_look4films.constants.Constants.REQUEST_DETAILS;

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
        Button detailsButton = (Button) filmRow.getViewById(R.id.detailsButton);
        detailsButton.setOnClickListener(view -> doClick(res.getAdapterPosition(), detailsButton.getContext()));
        return res;
    }

    @Override
    public void onBindViewHolder(RecyclerFilmsHolder holder, int position) {
        holder.itemView.setSelected(films[position].getSelected());
        holder.picture.setImageDrawable(films[position].getPicture(holder.picture.getContext()));
        holder.name.setText(films[position].getName());
    }

    @Override
    public int getItemCount() {
        return films.length;
    }

    private void doClick(int position, Context context) {
        if (position != RecyclerView.NO_POSITION) {
            if (!films[position].getSelected()) {
                //отменим предыдущий выбор
                for (int i = 0; i < films.length; i++) {
                    if (films[i].getSelected()) {
                        films[i].setSelected(false);
                        notifyItemChanged(i);
                        break;
                    }
                }
            }
            films[position].setSelected(true);
            callDetailsActivity(context, films[position]);
            notifyItemChanged(position);
        }
    }

    private void callDetailsActivity(Context context, FilmInApp film) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(FILM_PARAMETER, film);
        ((Activity) context).startActivityForResult(intent, REQUEST_DETAILS);
    }

}