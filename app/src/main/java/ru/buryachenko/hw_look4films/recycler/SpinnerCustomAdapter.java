package ru.buryachenko.hw_look4films.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

public class SpinnerCustomAdapter extends ArrayAdapter implements SpinnerAdapter {

    private List<FilmInApp> items;
    private Context context;

    SpinnerCustomAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.items = objects;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.spinner_main, null);
        TextView textView = view.findViewById(R.id.spinnerName);
        textView.setText(items.get(position).getName());
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view;
        view = View.inflate(context, R.layout.spinner_dropdown, null);
        final TextView textView = view.findViewById(R.id.spinnerDropdownName);
        textView.setText(items.get(position).getName());
        return view;
    }
}
