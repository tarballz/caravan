package edu.cmps121.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.cmps121.app.api.DynamoDB;
import edu.cmps121.app.model.Car;

public class ButtonFragment extends Fragment {

    private DynamoDB dynamoDB;
    public int lon;
    public int lat;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        dynamoDB = new DynamoDB(getContext());

        Bundle arguments = this.getArguments();
        if(arguments == null)
            throw new RuntimeException("Party was not passed within Bundle. Halting execution");

        String party = arguments.getString("party");

        List<Map<String, AttributeValue>> carItems = dynamoDB.queryTableByParty("cars", party);

        List<String> cars = carItems.stream()
                .map(e -> e.get("car").getS())
                .collect(Collectors.toList());

        ListView listView = new ListView(getContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getContext(),
                android.R.layout.simple_list_item_1,
                cars
        );

        // need to reintroduce context, as when we set it up before it didn't work and broke app
        LinearLayout linearLayout = new LinearLayout(getContext());

        /* TODO: set up an onClickListener like the one in FindCarActivity. When a car is selected,
         * call dynamoDb.getItem using the car's primary key, then set the lon and lat to local
         * variables.
         */

        listView.setOnClickListener((parent, v, position, id) -> {
            String carName = (String) parent.getItemAtPosition(position);
            (Car) carItem = (Car) dynamoDB.getItem(Car.class, carName);

            if (carItem == null)
                throw new RuntimeException("Car could not be found in the DB");

            Bundle bundle = new Bundle();

        };

        // figure out how to use primary key to get car
        // set lon and lat to these new values from dynamoDB.getItem()


//         Button button = new Button(getContext());
//         linearLayout.addView(button);

        return linearLayout;
    }

}
