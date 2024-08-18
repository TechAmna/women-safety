package com.example.womenssafety;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private final List<LocationHelperClass> locationList;

    public LocationAdapter(List<LocationHelperClass> locationList) {
        this.locationList = locationList;
    }

    @NonNull
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_row, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationHelperClass location = locationList.get(position);
        holder.latitudeTextView.setText(String.valueOf(location.getLatitude()));
        holder.longitudeTextView.setText(String.valueOf(location.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        public TextView latitudeTextView;
        public TextView longitudeTextView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            latitudeTextView = itemView.findViewById(R.id.latitudeTextView);
            longitudeTextView = itemView.findViewById(R.id.longitudeTextView);
        }
    }
}

