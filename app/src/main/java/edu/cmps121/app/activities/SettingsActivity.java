package edu.cmps121.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import edu.cmps121.app.R;
import edu.cmps121.app.utilities.State;

// TODO: allow users to delete cars and change parties.
// Also let them customize the layout for the maps fragment. See res/raw
public class SettingsActivity extends AppCompatActivity {
    State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        state = new State(this);
    }

}
