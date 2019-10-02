package ru.buryachenko.hw_look4films.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.buryachenko.hw_look4films.R;

public class BottomSheetNewFilm extends BottomSheetDialogFragment
        implements View.OnClickListener {

    private ItemClickListener bottomSheetListener;

    static BottomSheetNewFilm newInstance() {
        return new BottomSheetNewFilm();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_new_film, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.bottomSheetNewFilmChangePicture).setOnClickListener(this);
        view.findViewById(R.id.bottomSheetNewFilmMoreAction).setOnClickListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            bottomSheetListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bottomSheetListener = null;
    }

    @Override
    public void onClick(View view) {
        bottomSheetListener.onItemClick(view);
        dismiss();
    }

    public interface ItemClickListener {
        void onItemClick(View view);
    }
}

