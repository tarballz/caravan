package edu.cmps121.app.utilities;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static edu.cmps121.app.activities.PartyMenuActivity.addNearbyFoodPlace;
import static edu.cmps121.app.activities.PartyMenuActivity.addNearbyGasPlace;
import static edu.cmps121.app.activities.PartyMenuActivity.addNearbyRestPlace;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String keyword;

    private final String TAG = GetNearbyPlacesData.class.getSimpleName();

    @Override
    protected String doInBackground(Object... params) {
        try {
            String url = (String) params[0];
            keyword = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            return downloadUrl.readUrl(url);
        } catch (IOException e) {
            throw new RuntimeException(TAG, e);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        DataParser dataParser = new DataParser();
        ShowNearbyPlaces(dataParser.parse(result));
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");

            switch (keyword) {
                case "food":
                    addNearbyFoodPlace(new NearbyPlace(placeName, vicinity, lat, lng));
                    break;
                case "gas":
                    addNearbyGasPlace(new NearbyPlace(placeName, vicinity, lat, lng));
                    break;
                case "rest":
                    addNearbyRestPlace(new NearbyPlace(placeName, vicinity, lat, lng));
                    break;
                default:
                    throw new RuntimeException("Bad switch case. Invalid keyword");
            }
        }
    }

    public class DownloadUrl {

        String readUrl(String strUrl) throws IOException {
            URL url = new URL(strUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            InputStream in = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
                sb.append(line);

            String data = sb.toString();
            br.close();
            in.close();
            urlConnection.disconnect();

            return data;
        }
    }

    public class DataParser {
        List<HashMap<String, String>> parse(String jsonData) {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);

                return getPlaces(jsonObject.getJSONArray("results"));
            } catch (JSONException e) {
                throw new RuntimeException(TAG + ": DataParser", e);
            }
        }

        private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) throws JSONException {
            List<HashMap<String, String>> placesList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++)
                placesList.add(getPlace((JSONObject) jsonArray.get(i)));

            return placesList;
        }

        private HashMap<String, String> getPlace(JSONObject googlePlaceJson) throws JSONException {
            HashMap<String, String> googlePlaceMap = new HashMap<>();
            String placeName = "-NA-";
            String vicinity = "-NA-";

            if (!googlePlaceJson.isNull("name"))
                placeName = googlePlaceJson.getString("name");

            if (!googlePlaceJson.isNull("vicinity"))
                vicinity = googlePlaceJson.getString("vicinity");

            String latitude = googlePlaceJson.getJSONObject("geometry")
                    .getJSONObject("location")
                    .getString("lat");
            String longitude = googlePlaceJson
                    .getJSONObject("geometry")
                    .getJSONObject("location")
                    .getString("lng");
            String reference = googlePlaceJson
                    .getString("reference");

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);

            return googlePlaceMap;
        }
    }


}
