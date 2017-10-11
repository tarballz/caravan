package edu.cmps121.app;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Tutorial says to prefix our intent extras with our package name
    // to avoid conflicts.
    public static final String EXTRA_MESSAGE = "edu.cmps121.app.USERSNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Called when user taps the "Maps?" button.
    public void gotoCreateFindParty(View view) {
        // new Intent(from, to), I think
        Intent intent = new Intent(this, CreateFindParty.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String usersName = editText.getText().toString();
        // Adds (key, value) to our intent
        intent.putExtra(EXTRA_MESSAGE, usersName);
        startActivity(intent);
    }
}
