package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.cmps121.app.api.State;

public class FindCarActivity extends AppCompatActivity {
    private State state;
    ListView carList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_car);

        state = new State(this);

        createCarListView();
    }

    private void createCarListView() {
        carList = (ListView) findViewById(R.id.car_list_lv);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                getCarsItems()
        );
        carList.setAdapter(adapter);

        carList.setOnItemClickListener((parent, view, position, id) -> {
            // TODO: test that state.car is accurate and that state.nextActivity works with Context as a param
            state.car = (String) parent.getItemAtPosition(position);
            state.nextActivity(parent.getContext(), PartyMenuActivity.class);
        });
    }

    private ArrayList<String> getCarsItems() {
        // TODO: scan Cars table here and put each car's primary key into an ArrayList
        ArrayList<String> cars = new ArrayList<>();
        cars.add("Gabe's Car");
        cars.add("Batmobile");
        cars.add("Joey's car");

        return cars;
    }
}
