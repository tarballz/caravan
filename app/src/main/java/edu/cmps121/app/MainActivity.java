package edu.cmps121.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import edu.cmps121.app.api.State;
import edu.cmps121.app.model.User;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

// TODO: possibly override onBackPress() to also pass state.
// TODO: We need two buttons here, one for new users and one for return users. Return user's state should be gathered from the DB

public class MainActivity extends AppCompatActivity {
    private State state;
    private int MIN_LENGTH = 3;
    private int MAX_LENGTH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        state = new State(this);
    }

    public void onClickCreateUsername(View view) {
        EditText editText = (EditText) findViewById(R.id.enter_username_et);
        User user = new User();
        String potentialUsername = editText.getText().toString();

        // TODO: check that username is unique in DB

        if (potentialUsername.length() < MIN_LENGTH || potentialUsername.length() > MAX_LENGTH)
            Toast.makeText(MainActivity.this, "Name is too short!", Toast.LENGTH_SHORT).show();

        state.username = potentialUsername;
        user.setUser(potentialUsername);

        try {
            state.db.saveItem(user);
            state.nextActivity(this, PartyOptionsActivity.class);
        } catch (ResourceNotFoundException e) {
            Log.w("DB", "Table does not exist or invalid POJO");
            shortToast(this, "Failed to save data");
        }
    }
}
