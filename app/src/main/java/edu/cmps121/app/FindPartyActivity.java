package edu.cmps121.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FindPartyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_party);
    }

    public void searchParty(View view) {
        //implements search Button
        Intent intent = new Intent(this, PartyMenuActivity.class);
        startActivity(intent);
    }
}
