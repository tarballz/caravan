package edu.cmps121.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.cmps121.app.R;
import edu.cmps121.app.dynamo.DynamoDB;
import edu.cmps121.app.utilities.State;
import edu.cmps121.app.dynamo.User;

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
                getCarsWithDrivers()
        );
        carList.setAdapter(adapter);

        carList.setOnItemClickListener((parent, v, position, id) -> {
            state.car = (String) parent.getItemAtPosition(position);

            dynamoDB.updateItem(User.class, state.user, (obj) -> {
                User user = (User) obj;
                user.setCar(state.car);
                dynamoDB.saveItem(user);
            });

            state.nextActivity(parent.getContext(), PartyMenuActivity.class);
        });
    }

    private ArrayList<String> getCarsWithDrivers() {
        List<Map<String, AttributeValue>> itemList = dynamoDB.queryTableByParty("cars", state.party);

        return new ArrayList<>(itemList.stream()
                .map(e -> e.get("driver").getS() + "'s " + e.get("car").getS())
                .collect(Collectors.toList()));
    }
}
