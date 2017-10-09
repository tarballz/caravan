package edu.cmps121.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CreateFindParty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_find_party);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String usersName = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text.
        TextView textView = (TextView) findViewById(R.id.displayName);
        textView.setText("Hello " + usersName + "!");
    }
}
