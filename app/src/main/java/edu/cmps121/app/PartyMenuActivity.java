package edu.cmps121.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PartyMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_menu);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String teamName = intent.getStringExtra(CreatePartyActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text.
        TextView textView = (TextView) findViewById(R.id.teamNameTV);
        textView.setText(teamName + "\'s");
    }

    public void gotoMapsActivity(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void gotoSettingsActivity(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
