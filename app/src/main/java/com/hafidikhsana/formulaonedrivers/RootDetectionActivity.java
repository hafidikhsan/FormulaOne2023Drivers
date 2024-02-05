package com.hafidikhsana.formulaonedrivers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.rootbeer.RootBeer;

public class RootDetectionActivity extends AppCompatActivity {

    SwitchCompat switchToogle;
    Button rootCheck;
    RootBeer rootBeer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_detection);

        rootBeer = new RootBeer(this);
        switchToogle = findViewById(R.id.switch_compat);
        rootCheck = findViewById(R.id.root_check_button);

        switchToogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchToogle.isChecked()) {
                    Toast.makeText(RootDetectionActivity.this, "Root Check", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RootDetectionActivity.this, "Root Uncheck", Toast.LENGTH_LONG).show();
                }
            }
        });

//        92:f4:c1:44:b3:a5:e0:06:2c:35:c4:a8:5d:50:9f:e1:63:6d:ca:c7

        rootCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rootBeer.isRooted()) {
                    Toast.makeText(RootDetectionActivity.this, "Phone is Rooted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RootDetectionActivity.this, "Phone is Clear", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}