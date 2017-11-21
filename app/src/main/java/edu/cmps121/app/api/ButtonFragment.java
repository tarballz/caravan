package edu.cmps121.app.api;

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

public class ButtonFragment extends Fragment {

    private DynamoDB dynamoDB;

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

        LinearLayout linearLayout = new LinearLayout(getContext());

//         Button button = new Button(getContext());
//         linearLayout.addView(button);

        return linearLayout;

    }

}
