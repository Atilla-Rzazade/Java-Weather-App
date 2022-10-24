package com.example.java_weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button getLocation;
    private Button getData;
    private EditText latitude;
    private EditText longitude;
    private EditText txtDTempMin;
    private EditText txtDHumidMin;
    private EditText txtDTempMax;
    private EditText txtDHumidMax;
    private EditText txtCity;


    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?";
    private final String GEO_BASE_URL = "http://api.openweathermap.org/geo/1.0/direct?q=";
    protected final String APP_ID = "&appid=730cd1b7f47724b979bef85d86c83dc8";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        latitude = findViewById(R.id.txtLatitude);
        longitude = findViewById(R.id.txtLongitude);

        getData = findViewById(R.id.btnGetData);
        getLocation = findViewById(R.id.btnGetLocation);

        txtCity = findViewById(R.id.txtCity);

        txtDTempMin = findViewById(R.id.txtDTempMin);
        txtDHumidMin = findViewById(R.id.txtDHumidMin);
        txtDTempMax = findViewById(R.id.txtDTempMax);
        txtDHumidMax = findViewById(R.id.txtDHumidMax);

        getLocation.setOnClickListener(this::getGeoLocation);
        getData.setOnClickListener(this::getWeather);


    }

    public void getWeather(View view){
        // get desired min values
        String dTempMin = txtDTempMin.getText().toString().trim();
        String dHumidMin = txtDHumidMin.getText().toString().trim();

        // get desired max values
        String dTempMax = txtDTempMax.getText().toString().trim();
        String dHumidMax = txtDHumidMax.getText().toString().trim();

        // start if the values are not empty
        if (!dTempMin.equals("") && !dTempMax.equals("") && !dHumidMin.equals("") && !dHumidMax.equals("")){

            String lat, lon;


            lat = latitude.getText().toString().trim();
            lon = longitude.getText().toString().trim();

            // create API call url
            String tempUrl = BASE_URL+String.format("lat=%s&lon=%s",lat,lon)+APP_ID+"&mode=json&units=metric";

            // error handles for empty values
            if (lat.equals("") || lon.equals("")){
                Toast.makeText(getApplicationContext(),"Can't use empty values",Toast.LENGTH_SHORT).show();
            } else{

                // POST query to the API
                StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            // get the values from the JSON response
                            JSONObject jsonResponse = new JSONObject(response);
                            String name = !jsonResponse.getString("name").equals("")? jsonResponse.getString("name"): "";
                            String country = !jsonResponse.getJSONObject("sys").getString("country").equals("") ? jsonResponse.getJSONObject("sys").getString("country") : "";
                            String location = String.format("%s, %s", name,country);

                            String temp = ""+jsonResponse.getJSONObject("main").getDouble("temp");
                            String humidity = ""+jsonResponse.getJSONObject("main").getInt("humidity");

                            String description = jsonResponse.getJSONArray("weather")
                                    .getJSONObject(0)
                                    .getString("description");

                            // capitalize the first char of the description i.e. "clouds" -> "Clouds"
                            String ch = ""+Character.toUpperCase(description.charAt(0));
                            description = ch+description.substring(1);

                            // send all the information to the Results page for display
                            Intent intent = new Intent(MainActivity.this, Result.class);
                            intent.putExtra("location", location);
                            intent.putExtra("conditions", description);
                            intent.putExtra("temp", temp);
                            intent.putExtra("humidity", humidity);
                            intent.putExtra("tempMin", dTempMin);
                            intent.putExtra("tempMax", dTempMax);
                            intent.putExtra("humidMin", dHumidMin);
                            intent.putExtra("humidMax", dHumidMax);
                            startActivity(intent);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });

                // queue the query
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        } else{
            // handle empty desired values
            Toast.makeText(getApplicationContext(),"Please enter desired temperature and humidity", Toast.LENGTH_SHORT).show();
        }


    }

    public void getGeoLocation(View view){

        // get city address
        String address = txtCity.getText().toString().trim();

        // create the API call URL
        String url = GEO_BASE_URL+address+APP_ID;

        // create GET request for the server
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    // write output to the lat/lon TextViews
                    JSONArray jsonResponse = new JSONArray(response);
                    String lat,lon;
                    lat = ""+jsonResponse.getJSONObject(0).getInt("lat");
                    lon = ""+jsonResponse.getJSONObject(0).getInt("lon");

                    // set up the latitude/longitude info
                    latitude.setText(lat);
                    longitude.setText(lon);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });


        // queue the query
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}