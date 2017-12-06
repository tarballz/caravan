package edu.cmps121.app.utilities;

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

import edu.cmps121.app.R;
import edu.cmps121.app.dynamo.Car;
import edu.cmps121.app.dynamo.DynamoDB;

public class NavigationFragment extends Fragment {

    private DynamoDB dynamoDB;
    private ListView listView;
    private String party;
    private CameraMovement callback;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        @SuppressWarnings("All")
        LinearLayout navigationLayout = (LinearLayout) inflater.inflate(R.layout.fragment_navigation, null);

        listView = (ListView) navigationLayout.findViewById(R.id.navigation_lv);
        dynamoDB = new DynamoDB(getContext());

        return navigationLayout;
    }

    private void listCars() {
        List<Map<String, AttributeValue>> carItems = dynamoDB.queryTableByParty("cars", party);

        List<String> cars = carItems.stream()
                .map(e -> e.get("car").getS())
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this.getContext(),
                R.layout.spinner_item,
                cars
        );

        listView.setAdapter(adapter);
    }

    private void setListener() {
        listView.setOnItemClickListener((parent, v, position, id) -> {
            String carName = (String) parent.getItemAtPosition(position);

            if (!dynamoDB.itemExists(Car.class, carName))
                throw new RuntimeException("Car could not be found in the DB");

            callback.moveCamera(carName);
        });
    }

    public void initializeNavFragment(CameraMovement callback, String party) {
        this.callback = callback;
        this.party = party;

        listCars();
        setListener();
    }

    public interface CameraMovement {
        void moveCamera(String carName);
    }
}
