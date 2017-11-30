package edu.cmps121.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    private ListView listView;
    private String party;
    public int lon;
    public int lat;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        Bundle arguments = this.getArguments();

        if(arguments == null)
            throw new RuntimeException("Party was not passed within Bundle. Halting execution");

        party = arguments.getString("party");
        dynamoDB = new DynamoDB(getContext());
        listView = new ListView(getContext());

        listCars();
        setListener();

        linearLayout.addView(listView);
        return linearLayout;
    }

    private void setListener() {
        listView.setOnItemClickListener((parent, v, position, id) -> {
            String carName = (String) parent.getItemAtPosition(position);
            Car carItem = (Car) dynamoDB.getItem(Car.class, carName);

            if (carItem == null)
                throw new RuntimeException("Car could not be found in the DB");

            Bundle bundle = new Bundle();

        });
    }

    private void listCars() {
        List<Map<String, AttributeValue>> carItems = dynamoDB.queryTableByParty("cars", party);

        List<String> cars = carItems.stream()
                .map(e -> e.get("car").getS())
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getContext(),
                android.R.layout.simple_list_item_1,
                cars
        );

        listView.setAdapter(adapter);
    }

    public interface CameraMovement {
        void moveCamera();
    }
}
