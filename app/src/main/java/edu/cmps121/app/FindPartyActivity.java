package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.cmps121.app.api.State;

public class FindPartyActivity extends AppCompatActivity {
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_party);

        state = new State(this);
    }
}
