package edu.cmps121.app;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.cmps121.app.api.State;

public class PartyOptionsActivity extends AppCompatActivity {
    private State state; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_find_party);
        
        setState(); 

        TextView textView = (TextView) findViewById(R.id.displayName);
        textView.setText("Hello " + state.username + "!");
    }

    private void setState() {
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        state = extras.getParcelable("state");
    }

    public void gotoCreateParty(View view) {
        // new Intent(from, to), I think
        Intent intent = new Intent(this, CreatePartyActivity.class);
        startActivity(intent);
    }

    public void gotoFindParty(View view) {
        Intent intent = new Intent(this, FindPartyActivity.class);
        startActivity(intent);
    }
}
