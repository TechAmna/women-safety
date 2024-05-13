package com.example.womenssafety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LocationFragment extends Fragment {

    private TextView locationTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        locationTextView = view.findViewById(R.id.locationTextView);
        return view;
    }

    public void updateLocation(String location) {
        if (locationTextView != null) {
            locationTextView.setText(location);
        }
    }
}
