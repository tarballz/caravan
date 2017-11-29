package edu.cmps121.app.activities;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import edu.cmps121.app.R;
import edu.cmps121.app.dynamo.DynamoDB;
import edu.cmps121.app.utilities.State;
import edu.cmps121.app.dynamo.User;

import static edu.cmps121.app.utilities.CaravanUtils.isValidString;
import static edu.cmps121.app.utilities.CaravanUtils.shortToast;
import static edu.cmps121.app.utilities.CaravanUtils.startDriverService;
import static edu.cmps121.app.utilities.CaravanUtils.stopDriverService;
import static edu.cmps121.app.utilities.CaravanUtils.trackingEnabled;

public class MainActivity extends AppCompatActivity {
    private State state;
    private DynamoDB dynamoDb;
    private User user;

    private static final int TAG_CODE_PERMISSION_LOCATION = 123;
    @SuppressWarnings("All")
    private static final int MIN_USER_LEN = 3;
    @SuppressWarnings("All")
    private static final int MAX_USER_LEN = 8;
    @SuppressWarnings("All")
    private static final int MIN_PASS_LEN = 5;
    @SuppressWarnings("All")
    private static final int MAX_PASS_LEN = 16;
    @SuppressWarnings("unused")
    private static final String TAG = MainActivity.class.getSimpleName();

    public enum AccountStatus {
        ACCOUNT_AVAILABLE, ACCOUNT_EXISTS, IMPROPER_USER, IMPROPER_PASS, NO_MATCH_PASS_USER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();

        state = new State(this);
        dynamoDb = new DynamoDB(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopDriverService(this);
    }

    public void onClickCreateAccount(View view) {
        switch (validateInfo()) {
            case ACCOUNT_AVAILABLE:
                saveNewAccount();
                break;
            case NO_MATCH_PASS_USER:
            case ACCOUNT_EXISTS:
                shortToast(this, "Username is already taken");
                break;
            case IMPROPER_USER:
                shortToast(this, "Username must be between 3 and 8 characters long");
                break;
            case IMPROPER_PASS:
                shortToast(this, "Password must be 5 and 16 characters long");
                break;
            default:
                throw new RuntimeException("Bad switch case in MainActivity#onClickCreateAccount");
        }
    }

    public void onClickLogin(View view) {
        switch (validateInfo()) {
            case ACCOUNT_AVAILABLE:
                shortToast(this, "Username incorrect");
                break;
            case ACCOUNT_EXISTS:
                trackDrivers();
                state.nextActivity(this, PartyOptionsActivity.class);
                break;
            case NO_MATCH_PASS_USER:
                shortToast(this, "Password incorrect");
                break;
            case IMPROPER_USER:
                shortToast(this, "Username must be between 3 and 8 characters long");
                break;
            case IMPROPER_PASS:
                shortToast(this, "Password must be 5 and 16 characters long");
                break;
            default:
                throw new RuntimeException("Bad switch case in MainActivity#onClickLogin");
        }
    }

    private void trackDrivers() {
        if (!isValidString(state.party) || !isValidString(state.car) || !isValidString(state.user))
            return;

        List<Map<String, AttributeValue>> carItems = dynamoDb.queryTableByParty("cars", state.party);

        Optional<String> driver = carItems.stream()
                .filter(e -> isValidString(e.get("driver").getS()))
                .filter(e -> e.get("driver").getS().equals(state.user))
                .map(e -> e.get("car").getS())
                .findFirst();

        driver.ifPresent(s -> startDriverService(s, this));
    }

    private AccountStatus validateInfo() {
        String username = ((EditText) findViewById(R.id.enter_username_et)).getText().toString();
        String password = ((EditText) findViewById(R.id.enter_password_et)).getText().toString();

        user = new User();
        user.setUser(username);
        user.setPassword(password);

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

            state.user = user.getUser();
            state.party = null;
            state.car = null;

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
        if (!userItem.getPassword().equals(user.getPassword()))
            return AccountStatus.NO_MATCH_PASS_USER;

        state.user = userItem.getUser();
        state.car = userItem.getCar();
        state.party = userItem.getParty();

        return AccountStatus.ACCOUNT_EXISTS;
    }

    private void requestPermissions() {
        if (!trackingEnabled(this))
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
    }
}
