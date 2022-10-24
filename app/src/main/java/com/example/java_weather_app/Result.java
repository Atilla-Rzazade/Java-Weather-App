package com.example.java_weather_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Result extends AppCompatActivity {

    private Button btnBack;
    private TextView outLocation;
    private TextView outConditions;
    private TextView outTemperature;
    private TextView outHumidity;
    private int tempMin, tempMax;
    private int humidMin, humidMax;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);


        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this::goBack);

        outLocation = findViewById(R.id.outLocation);
        outConditions = findViewById(R.id.outConditions);
        outTemperature = findViewById(R.id.outTemp);
        outHumidity = findViewById(R.id.outHumidity);

        Bundle extras = getIntent().getExtras();

        if(extras != null){

            String location = extras.getString("location");
            String conditions = extras.getString("conditions");
            String temperature = extras.getString("temp");
            String humidity = extras.getString("humidity");

            humidMin = Integer.parseInt(extras.getString("tempMin"));
            humidMin = Integer.parseInt(extras.getString("tempMax"));
            humidMin = Integer.parseInt(extras.getString("humidMin"));
            humidMin = Integer.parseInt(extras.getString("humidMax"));

            outLocation.setText(location);
            outConditions.setText(conditions);
            outTemperature.setText(temperature);
            outHumidity.setText(humidity);
        }

    }

    private void goBack(View view) {
        finish();
    }
}

