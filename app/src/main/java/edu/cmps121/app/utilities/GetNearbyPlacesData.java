package edu.cmps121.app.utilities;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

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

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private Callback callback;
    private boolean running;

    private final String TAG = GetNearbyPlacesData.class.getSimpleName();

    public GetNearbyPlacesData(Callback callback) {
        this.callback = callback;
        this.running = true;
    }

    /**
     * Runs in its own thread. Do not make UI calls here
     **/
    @Override
    protected String doInBackground(Object... params) {
        try {
            String url = (String) params[0];
            DownloadUrl downloadUrl = new DownloadUrl();
            return downloadUrl.readUrl(url);
        } catch (IOException e) {
            throw new RuntimeException(TAG, e);
        }
    }

    /**
     * Runs in the UI thread
     **/
    @Override
    protected void onPostExecute(String result) {
        DataParser dataParser = new DataParser();

        List<HashMap<String, String>> nearbyPlaces = dataParser.parse(result);


        for (HashMap<String, String> place : nearbyPlaces) {
            if (!running)
                break;

            LatLng latLng = new LatLng(Double.parseDouble(place.get("lat")),
                    Double.parseDouble(place.get("lng")));

            callback.addPlace(
                    new NearbyPlace(place.get("place_name"),
                            place.get("vicinity"),
                            latLng));
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

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public interface Callback {
        void addPlace(NearbyPlace nearbyPlace);
    }

}
