package com.hafidikhsana.formulaonedrivers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

public class RootDetectionActivity extends AppCompatActivity {

    SwitchCompat switchToogle;
    Button rootCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_detection);

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
    }
}