package edu.cmps121.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.cmps121.app.R;
import edu.cmps121.app.utilities.CaravanUtils;
import edu.cmps121.app.utilities.State;

public class SettingsActivity extends AppCompatActivity {

    State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        state = new State(this);

        initializeThemeSpinner();
    }

    public void initializeThemeSpinner() {
        Spinner carSpinner = (Spinner) findViewById(R.id.select_theme_sp);
        ArrayList<String> themes = new ArrayList<>();

        themes.add("Retro");
        themes.add("GreyScale");
        themes.add("Night");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                themes
        );
        carSpinner.setAdapter(adapter);

        carSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String theme = (String) parent.getItemAtPosition(position);
                switch (theme) {
                    case "Retro":
                        state.jsonOption = CaravanUtils.JsonOptions.RETRO;
                        break;
                    case "GreyScale":
                        state.jsonOption = CaravanUtils.JsonOptions.GREYSCALE;
                        break;
                    case "Night":
                        state.jsonOption = CaravanUtils.JsonOptions.NIGHT;
                        break;
                    default:
                        throw new RuntimeException("Bad switch case");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // This allows us to pass the updated state value back to the rest of the app
    @Override
    public void onBackPressed() {
        state.nextActivity(this, PartyMenuActivity.class);
    }

}
