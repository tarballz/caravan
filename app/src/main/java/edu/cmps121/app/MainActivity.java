package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import edu.cmps121.app.api.DynamoDB;
import edu.cmps121.app.api.State;
import edu.cmps121.app.model.User;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

// TODO: possibly override onBackPress() to also pass state when the back button is used

public class MainActivity extends AppCompatActivity {
    private State state;
    private DynamoDB dynamoDb;
    private User user;

    private int MIN_USER_LEN = 3;
    private int MAX_USER_LEN = 8;
    private int MIN_PASS_LEN = 5;
    private int MAX_PASS_LEN = 16;

    public enum AccountStatus {
        ACCOUNT_AVAILABLE, ACCOUNT_EXISTS, IMPROPER_USER, IMPROPER_PASS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        state = new State(this);
        dynamoDb = new DynamoDB(this);
    }

    public void onClickCreateAccount(View view) {
        switch (validateInfo()) {
            case ACCOUNT_AVAILABLE:
                saveNewAccount();
                break;
            case ACCOUNT_EXISTS:
                shortToast(this, "Username is already taken");
                break;
            case IMPROPER_USER:
                shortToast(this, "Username must be between 3 and 8 characters long");
                break;
            case IMPROPER_PASS:
                shortToast(this, "Password must be 5 and 16 characters long");
                break;
        }
    }

    public void onClickLogin(View view) {
        switch (validateInfo()) {
            case IMPROPER_USER:
            case ACCOUNT_AVAILABLE:
                shortToast(this, "Username incorrect");
                break;
            case ACCOUNT_EXISTS:
                state.nextActivity(this, PartyOptionsActivity.class);
                break;
            case IMPROPER_PASS:
                shortToast(this, "Password incorrect");
                break;
        }
    }

    private AccountStatus validateInfo() {
        user = new User();
        String username = ((EditText) findViewById(R.id.enter_username_et)).getText().toString();
        String password = ((EditText) findViewById(R.id.enter_password_et)).getText().toString();
        user.setUser(username);
        user.setPassword(password);
        state.username = username;

        if (username.length() < MIN_USER_LEN || username.length() > MAX_USER_LEN)
            return AccountStatus.IMPROPER_USER;
        if (password.length() < MIN_PASS_LEN || password.length() > MAX_PASS_LEN)
            return AccountStatus.IMPROPER_PASS;

        return analyzeUser(User.class, user);
    }

    private void saveNewAccount() {
        try {
            if (analyzeUser(User.class, user) != AccountStatus.ACCOUNT_AVAILABLE)
                throw new RuntimeException("Account should not exist. Validate Failed.");

            dynamoDb.saveItem(user);
            state.nextActivity(this, PartyOptionsActivity.class);
        } catch (ResourceNotFoundException e) {
            Log.w("DynamoDB", "Table does not exist or invalid POJO");
            shortToast(this, "Failed to save account data");
        }
    }

    public AccountStatus analyzeUser(Class<User> userClass, User user) {
        User userItem = (User) dynamoDb.getItem(userClass, user.getUser());

        if (userItem == null)
            return AccountStatus.ACCOUNT_AVAILABLE;
        if (userItem.getPassword().equals(user.getPassword()))
            return AccountStatus.ACCOUNT_EXISTS;
        else
            return AccountStatus.IMPROPER_PASS;
    }
}
