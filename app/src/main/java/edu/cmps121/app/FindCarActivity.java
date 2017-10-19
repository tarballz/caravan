package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FindCarActivity extends AppCompatActivity {

    ListView carList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_car);

        //map to XML
        carList = (ListView) findViewById(R.id.carList);

        //display dummy information to the ListView
        String[] arr = new String[] {"Gabe's Car\n- Gabe (driving)\n- Joey",
                "Payton's Car\n- Payton (driving)\n- Czar",
                "Narges' Car\n- Narges (driving)\n- John\n- Qua"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        carList.setAdapter(adapter);
    }
}
