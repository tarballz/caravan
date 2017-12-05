package edu.cmps121.app.activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
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

import edu.cmps121.app.R;
import edu.cmps121.app.dynamo.DynamoDB;
import edu.cmps121.app.utilities.State;
import edu.cmps121.app.dynamo.Car;
import edu.cmps121.app.dynamo.User;

import static edu.cmps121.app.utilities.CaravanUtils.isValidString;
import static edu.cmps121.app.utilities.CaravanUtils.shortToast;
import static edu.cmps121.app.utilities.CaravanUtils.startDriverService;

public class CreateCarActivity extends AppCompatActivity {

    private State state;
    private DynamoDB dynamoDB;
    private String driver;
    private String carName;
    private String color;

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        @SuppressWarnings("All")
        Spinner colorSpinner = (Spinner) findViewById(R.id.select_color_sp);
        ArrayList<String> colors = new ArrayList<>();

        colors.add("cyan");
        colors.add("yellow");
        colors.add("red");
        colors.add("green");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initializeDriverSpinner() {
        @SuppressWarnings("All")
        Spinner carSpinner = (Spinner) findViewById(R.id.select_driver_sp);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                getDrivers()
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<String> getDrivers() {
        List<Map<String, AttributeValue>> usersItems = dynamoDB.queryTableByParty("users", state.party);

        return new ArrayList<>(usersItems.stream()
                .filter(e -> e.get("car") == null)
                .map(e -> e.get("user").getS())
                .collect(Collectors.toList()));
    }

    public void onClickCreateCar(View view) {
        @SuppressWarnings("All")
        EditText editText = (EditText) findViewById(R.id.enter_create_car_name_et);
        carName = editText.getText().toString();
        if (isValidString(carName) && isValidString(driver) && isValidString(color))
            saveCar();
        else
            shortToast(this,
                    (isValidString(carName) ? "Name of car, " : "") +
                            (isValidString(color) ? "Color, " : "") +
                            (isValidString(driver) ? "Driver, " : "") +
                            "cannot be left empty"
            );
    }

    private void saveCar() {
        Car car = (Car) dynamoDB.getItem(Car.class, carName);

        if (car != null && car.getParty().equals(state.party))
            shortToast(this, carName + " has already been taken");
        else {
            car = new Car();
            car.setDriver(driver);
            car.setParty(state.party);
            car.setCar(carName);
            car.setColor(color);

            state.car = carName;

            dynamoDB.saveItem(car);
            updateUsers();

            state.nextActivity(this, PartyMenuActivity.class);
        }
    }

    private void updateUsers() {
        if (state.user.equals(driver))
            startDriverService(carName, this);
        else {
            dynamoDB.updateItem(User.class, driver, (obj) -> {
                User user = (User) obj;
                user.setCar(carName);

                dynamoDB.saveItem(user);
            });
        }

        dynamoDB.updateItem(User.class, state.user, (obj) -> {
            User user = (User) obj;
            user.setCar(carName);

            dynamoDB.saveItem(user);
        });
    }
}
