package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.policy.Resource;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import edu.cmps121.app.api.DynamoDB;
import edu.cmps121.app.api.State;
import edu.cmps121.app.model.User;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

// TODO: possibly override onBackPress() to also pass state when the back button is used
// TODO: We need two buttons here, one for new users and one for return users. Return user's state should be gathered from the DynamoDB

public class MainActivity extends AppCompatActivity {
    private State state;
    private DynamoDB dynamoDb;

    private int MIN_LENGTH = 3;
    private int MAX_LENGTH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        state = new State(this);
        dynamoDb = new DynamoDB(this);
    }

    public void onClickCreateUsername(View view) {
        try {
            EditText editText = (EditText) findViewById(R.id.enter_username_et);
            String potentialUsername = editText.getText().toString();

            if (potentialUsername.length() < MIN_LENGTH || potentialUsername.length() > MAX_LENGTH)
                shortToast(this, "Invalid length: " + potentialUsername.length());
            else
                validateItem(potentialUsername);
        } catch (ResourceNotFoundException e) {
            Log.w("DynamoDB", "Table does not exist or invalid POJO");
            shortToast(this, "Failed to save data");
        }
    }

    private void validateItem(String potentialUsername) throws ResourceNotFoundException {
        User user = new User();
        state.username = potentialUsername;

        if (dynamoDb.itemExists(User.class, potentialUsername))
            shortToast(this, "This name is already taken");
        else {
            user.setUser(potentialUsername);
            dynamoDb.saveItem(user);
            state.nextActivity(this, PartyOptionsActivity.class);
        }
    }
}
