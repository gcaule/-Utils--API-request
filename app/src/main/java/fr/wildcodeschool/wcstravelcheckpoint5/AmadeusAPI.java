package fr.wildcodeschool.wcstravelcheckpoint5;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by bastienwcs on 31/01/18.
 */

public class AmadeusAPI {

    private static String API_URL = "https://api.sandbox.amadeus.com/v1.2/";
    private static String EXTENSIVE_SEARCH = "flights/extensive-search";

    public static void getTravels(Context context, final AmadeusAPIResponse listener) {
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(context);
        String url = API_URL + EXTENSIVE_SEARCH;
        String params = "?apikey=BHwXBlZOnJoABSAuAgdvbYKpGIg5rp1G&origin=NYC&destination=LAS&departure_date=2018-01-31--2018-01-31";
        url += params;

        // Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject root = new JSONObject(response);
                            String origin = root.getString("origin");
                            JSONArray results = root.getJSONArray("results");
                            List<TravelModel> travels = new ArrayList<>();
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject result = results.getJSONObject(i);
                                /*TravelModel travel = new TravelModel();
                                travel.setOrigin(origin);
                                travel.setDestination(result.getString("destination"));
                                travel.setDeparture_date(result.getString("departure_date"));
                                travel.setReturn_date(result.getString("return_date"));
                                travel.setPrice(result.getString("price"));
                                travel.setAirline(result.getString("airline"));*/

                                JsonParser parser = new JsonParser();
                                JsonElement jsonElement = parser.parse(result.toString());
                                Gson gson = new Gson();
                                TravelModel travel = gson.fromJson(jsonElement, TravelModel.class);
                                travel.setOrigin(origin);
                                travels.add(travel);
                            }
                            listener.onSuccess(travels);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                        listener.onError(error.getLocalizedMessage());
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public interface AmadeusAPIResponse {

        void onSuccess(List<TravelModel> travels);

        void onError(String error);
    }
}
