package edu.cmps121.app.activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.cmps121.app.R;
import edu.cmps121.app.dynamo.DynamoDB;
import edu.cmps121.app.utilities.State;
import edu.cmps121.app.dynamo.User;

import static edu.cmps121.app.utilities.CaravanUtils.shortToast;

public class FindCarActivity extends AppCompatActivity {

    private State state;
    private DynamoDB dynamoDB;
    @SuppressWarnings("All")
    private Optional<String> carName;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_car);

        dynamoDB = new DynamoDB(this);
        state = new State(this);

        createCarListView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createCarListView() {
        ListView carList = (ListView) findViewById(R.id.car_list_lv);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                getCarsWithDrivers()
        );
        carList.setAdapter(adapter);

        carList.setOnItemClickListener((parent, v, position, id) -> {
            if (carName.isPresent())
                shortToast(this, "You are already driving " + carName.get() + ". You cannot join another car");
            else {
                String carAndDriver = (String) parent.getItemAtPosition(position);
                state.car = carAndDriver.substring(carAndDriver.indexOf("'s ") + 3);

                dynamoDB.updateItem(User.class, state.user, (obj) -> {
                    User user = (User) obj;
                    user.setCar(state.car);
                    dynamoDB.saveItem(user);
                });

                state.nextActivity(parent.getContext(), PartyMenuActivity.class);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<String> getCarsWithDrivers() {
        List<Map<String, AttributeValue>> itemList = dynamoDB.queryTableByParty("cars", state.party);

        carName = itemList.stream()
                .filter(e -> e.get("driver").getS().equals(state.user))
                .map(e -> e.get("car").getS())
                .findFirst();

        return new ArrayList<>(itemList.stream()
                .map(e -> e.get("driver").getS() + "'s " + e.get("car").getS())
                .collect(Collectors.toList()));
    }
}
