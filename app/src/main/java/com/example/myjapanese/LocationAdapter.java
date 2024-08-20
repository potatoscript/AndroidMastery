package com.example.myjapanese;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LocationAdapter extends ArrayAdapter<Location> {

    public LocationAdapter(@NonNull Context context, int resource, @NonNull List<Location> locations) {
        super(context, resource, locations);
    }

    @NonNull
    @Override
    public Location getItem(int position) {
        return super.getItem(position);
    }

    @Nullable
    public String getItemAsString(int position) {
        return getItem(position).getLocationName();
    }
}
