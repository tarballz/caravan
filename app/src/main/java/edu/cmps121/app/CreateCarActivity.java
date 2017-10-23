package edu.cmps121.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.cmps121.app.api.State;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

public class CreateCarActivity extends AppCompatActivity {
    State state;

    // TODO: link state.car with the created car's name
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_car);

        state = new State(this);
    }

    public void onClickCreateCar(View view) {
        EditText editText = (EditText) findViewById(R.id.enter_create_car_name_et);
        String potentialCar = editText.getText().toString();

        if (isUnique(potentialCar)) {
            state.car = potentialCar;
            state.nextActivity(this, PartyMenuActivity.class);
        } else
            shortToast(this, potentialCar + " has already been taken");
    }

    private boolean isUnique(String potentialCar) {
        // TODO: check db here
        return true; 
    }

    public void createCar(View view) {
        Intent intent = new Intent(this, PartyMenuActivity.class);
        startActivity(intent);
    }
}
