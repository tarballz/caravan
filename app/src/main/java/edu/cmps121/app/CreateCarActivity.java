package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.cmps121.app.api.State;

public class CreateCarActivity extends AppCompatActivity {
    State state;

    // TODO: link state.car with the created car's name
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_car);

        state = new State(this);
    }
}
