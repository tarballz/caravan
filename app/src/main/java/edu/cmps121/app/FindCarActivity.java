package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.cmps121.app.api.State;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

public class FindCarActivity extends AppCompatActivity {
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_car);
        
        state = new State(this);
    }

    public void onClickFindCar(View view) {
        EditText editText = (EditText) findViewById(R.id.enter_find_car_name_et);
        String potentialCar = editText.getText().toString();

        if (isReal(potentialCar)) {
            state.car = potentialCar;
            state.nextActivity(this, PartyMenuActivity.class);
        }
        else
            shortToast(this, "Unable to find the car: " + potentialCar);
    }

    private boolean isReal(String potentialCar) {
        // TODO: db check here
        return true; 
    }
}
