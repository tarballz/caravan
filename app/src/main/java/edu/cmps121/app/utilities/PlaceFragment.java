package edu.cmps121.app.utilities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.cmps121.app.R;

public class PlaceFragment extends Fragment implements Button.OnClickListener {

    FoundPlace callback;
    Button foodButton;
    Button gasButton;
    Button restButton;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        @SuppressWarnings("All")
        ConstraintLayout navigationLayout = (ConstraintLayout) inflater.inflate(R.layout.fragment_place, null);

        foodButton = navigationLayout.findViewById(R.id.food_places_b);
        gasButton = navigationLayout.findViewById(R.id.gas_places_b);
        restButton = navigationLayout.findViewById(R.id.rest_places_b);

        setListeners();

        return navigationLayout;
    }

    private void setListeners() {
        foodButton.setOnClickListener(this);
        gasButton.setOnClickListener(this);
        restButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.food_places_b:
                callback.addFoodMarkers();
                break;
            case R.id.gas_places_b:
                callback.addGasMarkers();
                break;
            case R.id.rest_places_b:
                callback.addRestMarkers();
                break;
            default:
                throw new RuntimeException("Bad button case");
        }
    }

    public void initializePlaceFragment(FoundPlace callback) {
        this.callback = callback;
    }

    public interface FoundPlace {
        void addFoodMarkers();
        void addGasMarkers();
        void addRestMarkers();
    }
}
