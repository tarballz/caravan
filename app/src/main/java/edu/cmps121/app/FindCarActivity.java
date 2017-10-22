package edu.cmps121.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FindCarActivity extends AppCompatActivity {

    ListView carList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_car);

        carList = (ListView) findViewById(R.id.carList);

        //display dummy information to the ListView
        String[] arr = new String[] {"Gabe's Car\n- Gabe (driving)\n- Joey",
                "Payton's Car\n- Payton (driving)\n- Czar",
                "Narges' Car\n- Narges (driving)\n- John\n- Qua"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        carList.setAdapter(adapter);

        carList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(parent.getContext(), PartyMenuActivity.class));
            }
        });
    }
}
