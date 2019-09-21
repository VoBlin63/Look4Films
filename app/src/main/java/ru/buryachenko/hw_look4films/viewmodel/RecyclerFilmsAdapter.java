package ru.buryachenko.hw_look4films.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.activities.DetailsActivity;
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static ru.buryachenko.hw_look4films.utils.Constants.FILM_PARAMETER;
import static ru.buryachenko.hw_look4films.utils.Constants.REQUEST_DETAILS;

public class RecyclerFilmsAdapter extends RecyclerView.Adapter<RecyclerFilmsAdapter.RecyclerFilmsHolder> {
    private List<FilmInApp> films;

    public List<FilmInApp> getData() {
        return films;
    }

    public void setData(List<FilmInApp> newFilms) {
        films = newFilms;
    }

    public static class RecyclerFilmsHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView picture;
        public ImageView liked;
        public CardView card;
        //public TextView details;

        RecyclerFilmsHolder(CardView row) {
            super(row);
            picture = row.findViewById(R.id.picture);
            name = row.findViewById(R.id.name);
            liked = row.findViewById(R.id.likedRow);
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
        filmRow.findViewById(R.id.picture).setOnClickListener(view -> doClick(view, res.getAdapterPosition(), parent.getContext()));
        filmRow.findViewById(R.id.likedRow).setOnClickListener(view -> doClick(view, res.getAdapterPosition(), parent.getContext()));
        filmRow.findViewById(R.id.name).setOnClickListener(view -> doClick(view, res.getAdapterPosition(), parent.getContext()));
        filmRow.findViewById(R.id.detailsButton).setOnClickListener(view -> doClick(view, res.getAdapterPosition(), parent.getContext()));
        return res;
    }

    @Override
    public void onBindViewHolder(RecyclerFilmsHolder holder, int position) {
        FilmInApp film =films.get(position);
        holder.itemView.setSelected(film.getSelected());
        holder.picture.setImageDrawable(film.getPicture(holder.picture.getContext()));
        holder.name.setText(film.getName());
        holder.liked.setImageResource(film.getLiked() ? R.drawable.liked : R.drawable.notliked);
        holder.card.setCardElevation(film.getSelected()? 0F : holder.card.getMaxCardElevation());
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
            if (view.getId() == R.id.likedRow) {
                //если вызывали по лайку - только его меняем не вызывая деталей
                films.get(position).setLiked(!films.get(position).getLiked());
            }
            else {
                callDetailsActivity(context, films.get(position));
            }
            notifyItemChanged(position);
        }
    }

    private void callDetailsActivity(Context context, FilmInApp film) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(FILM_PARAMETER, film);
        ((Activity) context).startActivityForResult(intent, REQUEST_DETAILS);
    }

}