package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.cmps121.app.api.DynamoDB;
import edu.cmps121.app.api.State;

public class FindCarActivity extends AppCompatActivity {
    private State state;
    DynamoDB dynamoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_car);

        dynamoDB = new DynamoDB(this);
        state = new State(this);

        createCarListView();
    }

    public void createCarListView() {
        ListView carList = (ListView) findViewById(R.id.car_list_lv);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                getCarsItems()
        );
        carList.setAdapter(adapter);

        carList.setOnItemClickListener((parent, v, position, id) -> {
            state.car = (String) parent.getItemAtPosition(position);
            state.nextActivity(parent.getContext(), PartyMenuActivity.class);
        });
    }

    private ArrayList<String> getCarsItems() {
        List<Map<String, AttributeValue>> itemList = dynamoDB.queryTable("cars", state.party);

        return new ArrayList<>(itemList.stream()
//                .filter(e -> e.get("party").getS().equals(state.party))
                .map(e -> e.get("driver").getS() + "'s " + e.get("car").getS())
                .collect(Collectors.toList()));
    }
}
