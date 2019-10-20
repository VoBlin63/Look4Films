package ru.buryachenko.hw_look4films.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static ru.buryachenko.hw_look4films.activities.MainActivity.FRAGMENT_DETAILS;
import static ru.buryachenko.hw_look4films.activities.MainActivity.callFragment;
import static ru.buryachenko.hw_look4films.activities.MainActivity.getRightSide;
import static ru.buryachenko.hw_look4films.utils.SomeAnimation.doAnimationDetails;
import static ru.buryachenko.hw_look4films.utils.SomeAnimation.doAnimationPressPicture;
import static ru.buryachenko.hw_look4films.utils.SomeAnimation.doAnimationPushRight;

public class ListFilmsAdapter extends RecyclerView.Adapter<ListFilmsViewHolder> {

    private FilmsViewModel viewModel;

    public ListFilmsAdapter(LayoutInflater inflater, FilmsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ListFilmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView filmRow = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_films_item, parent, false);
        ListFilmsViewHolder res = new ListFilmsViewHolder(filmRow);

        filmRow.findViewById(R.id.listPicture).setOnClickListener(view -> doChangeDisclosed(filmRow, res.getAdapterPosition()));
        filmRow.findViewById(R.id.listPicture).setOnLongClickListener(view -> doTurnInFavorites(res.getAdapterPosition()));

        View.OnClickListener detailsCallClick = view -> callDetails(res.getAdapterPosition());
        filmRow.findViewById(R.id.listDetailsButton).setOnClickListener(detailsCallClick);
        filmRow.findViewById(R.id.listDetails).setOnClickListener(detailsCallClick);
        return res;
    }

    private void doChangeDisclosed(CardView filmRow, int position) {
        if (position != RecyclerView.NO_POSITION) {
            boolean newDisclosed = !viewModel.getList().get(position).isDisclosed();
            doAnimationPressPicture(filmRow.findViewById(R.id.listPicture));
            doAnimationPushRight(!newDisclosed, getRightSide(), filmRow.findViewById(R.id.listLiked), filmRow.findViewById(R.id.listName), filmRow.findViewById(R.id.listDetailsButton));
            doAnimationDetails(!newDisclosed, filmRow.findViewById(R.id.listDetails));
            viewModel.getList().get(position).setDisclosed(newDisclosed);
        }
    }

    private boolean doTurnInFavorites(int position) {
        FilmInApp film = viewModel.getList().get(position);
        if (viewModel.isFavorite(film)) {
            viewModel.removeFromFavorites(film);
        } else {
            viewModel.addInFavorites(film);
        }
        notifyItemChanged(position);
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
