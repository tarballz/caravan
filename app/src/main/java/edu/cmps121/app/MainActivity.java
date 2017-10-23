package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.cmps121.app.api.State;
import edu.cmps121.app.model.User;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

// TODO: possibly override onBackPress() to also pass state.

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

        if (state.db.saveItem(this, user))
            state.nextActivity(this, PartyOptionsActivity.class);
    }

    public void onClickSave(View view) {
        state.db.saveCarDB();
        shortToast(this, "You pressed save");
    }
}
