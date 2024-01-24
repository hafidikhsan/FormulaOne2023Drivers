package com.hafidikhsana.formulaonedrivers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView driversList = findViewById(R.id.list_view_drivers);

        Drivers[] drivers = {
                new Drivers(1, "Max Verstapen", "MAX", "Red Bull Racing"),
                new Drivers(11, "Sergio Perez", "PER", "Red Bull Racing"),
                new Drivers(3, "Daniel Ricciardi", "RIC", "Scuderria Alphatauri"),
                new Drivers(22, "Yuki Tsunoda", "Tsu", "Scuderria Alphatauri"),
                new Drivers(16, "Charles Leclere", "LEC", "Scuderria Ferrari"),
                new Drivers(55, "Carlos Sainz", "SAI", "Scuderria Ferrari"),
                new Drivers(2, "Logan Sargent", "SAR", "Williams"),
                new Drivers(23, "Alex Albon", "ALB", "Williams"),
                new Drivers(4, "Lando Norris", "NOR", "McLaren"),
                new Drivers(81, "Oscar Piastri", "PIA", "McLaren"),
        };

        ArrayAdapter<Drivers> driversAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drivers);

        driversList.setAdapter(driversAdapter);
    }
}