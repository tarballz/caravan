package edu.cmps121.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        Intent intent = new Intent(this, PartyOptionsActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String usersName = editText.getText().toString();
        if (usersName.length() > 0) {
            // Adds (key, value) to our intent
            // Consider writing this to a file.
            intent.putExtra(EXTRA_MESSAGE, usersName);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Name is too short!", Toast.LENGTH_SHORT).show();
        }

    }
}
