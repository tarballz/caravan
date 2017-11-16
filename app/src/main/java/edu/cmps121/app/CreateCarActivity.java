package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.cmps121.app.api.DynamoDB;
import edu.cmps121.app.api.State;
import edu.cmps121.app.model.Car;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

public class CreateCarActivity extends AppCompatActivity {
    private State state;
    private DynamoDB dynamoDB;
    private String driver;
    private String carName;
    private String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_car);

        state = new State(this);
        dynamoDB = new DynamoDB(this);

        initializeDriverSpinner();
        initializeColorSpinner();
    }

    private void initializeColorSpinner() {
        Spinner colorSpinner = (Spinner) findViewById(R.id.select_color_sp);

        ArrayList<String> colors = new ArrayList<>();
        colors.add("cyan");
        colors.add("yellow");
        colors.add("red");
        colors.add("green");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                colors
        );
        colorSpinner.setAdapter(adapter);

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void initializeDriverSpinner() {
        Spinner carSpinner = (Spinner) findViewById(R.id.select_driver_sp);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                getDriverItem()
        );
        carSpinner.setAdapter(adapter);

        carSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                driver = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private ArrayList<String> getDriverItem() {
        List<Map<String, AttributeValue>> driversItem = dynamoDB.queryTableByParty("cars", state.party);
        List<Map<String, AttributeValue>> usersItems = dynamoDB.queryTableByParty("users", state.party);

        List<String> driversList = driversItem.stream()
                .map(e -> e.get("driver").getS())
                .collect(Collectors.toList());

        return new ArrayList<>(usersItems.stream()
                .filter(e -> !driversList.contains(e.get("user").getS()))
                .map(e -> e.get("user").getS())
                .collect(Collectors.toList()));
    }

    public void onClickCreateCar(View view) {
        EditText editText = (EditText) findViewById(R.id.enter_create_car_name_et);
        carName = editText.getText().toString();

        if (!carName.isEmpty() && driver != null && color != null) {
            Car car = new Car();
            car.setCar(carName);

            if (!dynamoDB.itemExists(Car.class, carName)) {
                state.car = car.getCar();
                dynamoDB.saveItem(car);
                state.nextActivity(this, PartyMenuActivity.class);
            } else
                // TODO: make sure this checks only current party
                shortToast(this, carName + " has already been taken");
        } else {
            shortToast(this,
                    (carName.isEmpty()? "Name of car, " : "") +
                            (color == null ? "Color, " : "") +
                            (driver == null ? "Driver, " : "") +
                            "cannot be left empty"
            );
        }
    }
}
