package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.cmps121.app.api.State;

// TODO: possibly override onBackPress() to also pass state.

public class MainActivity extends AppCompatActivity {
    private State state;
    private int MIN_LENGTH = 2;
    private int MAX_LENGTH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        state = new State(this);
    }

    public void onClickCreateUsername(View view) {
        EditText editText = (EditText) findViewById(R.id.enter_username_et);
        state.username = editText.getText().toString();

        // TODO: check that username is unique in DB
        if (state.username.length() >= MIN_LENGTH && state.username.length() <= MAX_LENGTH) {
            state.nextActivity(this, PartyOptionsActivity.class);
        } else {
            Toast.makeText(MainActivity.this, "Name is too short!", Toast.LENGTH_SHORT).show();
        }
    }
}
