package edu.cmps121.app;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.cmps121.app.api.State;

public class MainActivity extends AppCompatActivity {
    private State state;
    private int MIN = 2;
    private int MAX = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        state = new State();
    }

    public void onClickCreateUsername(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        state.username = editText.getText().toString();

        // TODO: check that username is unique in DB
        if (state.username.length() >= MIN && state.username.length() <= MAX) {
            state.transitionActivity(this, PartyOptionsActivity.class);
        } else {
            Toast.makeText(MainActivity.this, "Name is too short!", Toast.LENGTH_SHORT).show();
        }
    }
}
