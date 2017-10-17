package edu.cmps121.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PartyMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_menu);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String teamName = intent.getStringExtra(CreateParty.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text.
        TextView textView = (TextView) findViewById(R.id.teamNameTV);
        textView.setText(teamName + "\'s");
    }
}
