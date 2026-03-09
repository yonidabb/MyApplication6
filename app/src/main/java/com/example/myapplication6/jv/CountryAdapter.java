package com.example.myapplication6.jv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication6.R;
import com.example.myapplication6.models.CountryItem;

import java.util.List;

public class CountryAdapter extends ArrayAdapter<CountryItem> {

    private final LayoutInflater inflater;

    public CountryAdapter(@NonNull Context context, @NonNull List<CountryItem> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createRow(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createRow(position, convertView, parent);
    }

    private View createRow(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_country_spinner, parent, false);
        }

        TextView countryText = view.findViewById(R.id.countryText);
        CountryItem item = getItem(position);

        if (item != null) {
            countryText.setText(item.getFlag() + "  " + item.getName());
        }

        return view;
    }
}