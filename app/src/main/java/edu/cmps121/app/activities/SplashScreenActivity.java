package edu.cmps121.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import edu.cmps121.app.R;

/**
 * Created by jqLiu on 12/4/2017.
 */

public class SplashScreenActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("SplashScreen", "Starting Splash Screen");

        Intent intent = new Intent(this, MainActivity.class);
        Log.i("SplashScreen", "Sending to MainActivity");
        startActivity(intent);
        finish();
    }

}

