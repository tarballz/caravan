package edu.cmps121.app.api;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.cmps121.app.R;

public class CustomListViewAdapter extends ArrayAdapter {

    // To reference the Activity
    private final Activity activity;

    // To store the NearbyPlace names.
    private final String[] nameArray;

    // TODO: Add more arrays for the rest of the info in NearbyPlace

    public CustomListViewAdapter(Activity activity, String[] nameArray) {
        super(activity, R.layout.nearby_listview_row, nameArray);

        this.activity = activity;
        this.nameArray = nameArray;
    }

    // Used automatically to populate the data into each row.
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.nearby_listview_row, null, true);

        // Get references to objects in the nearby_listview_row
        TextView nameTextField = (TextView) rowView.findViewById(R.id.rowName);
        nameTextField.setText(nameArray[position]);

        return rowView;
    }
}
