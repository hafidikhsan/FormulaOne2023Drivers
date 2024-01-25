package com.hafidikhsana.formulaonedrivers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDetail extends AppCompatActivity {

    TextView driverName;
    TextView driverDisplayName;
    TextView driverTeamName;
    TextView driverCountry;
    TextView errorText;
    ImageView driverUrl;
    ImageView errorImage;
    LinearLayout loaded;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);
        Intent intent = getIntent();
        driverName = findViewById(R.id.driver_name);
        driverDisplayName = findViewById(R.id.driver_display_name);
        driverTeamName = findViewById(R.id.driver_team);
        driverCountry = findViewById(R.id.driver_country);
        errorText = findViewById(R.id.driver_error_loaded);
        driverUrl = findViewById(R.id.driver_url);
        errorImage = findViewById(R.id.error_image);
        loaded = findViewById(R.id.driver_loaded);
        loading = findViewById(R.id.driver_detail_loading);

        String parameterNumber = intent.getStringExtra("number");
        String parameterSession = intent.getStringExtra("session");

        APIClient apiClient = APIClient.getInstance();
        OpenF1API openF1Api = apiClient.getOpenF1Api();

        Call<List<Drivers>> call = openF1Api.getDriverDetail(Integer.parseInt(parameterNumber), Integer.parseInt(parameterSession));

        call.enqueue(new Callback<List<Drivers>>() {
            @Override
            public void onResponse(Call<List<Drivers>> call, Response<List<Drivers>> response) {
                if (response.isSuccessful()) {
                    List<Drivers> drivers = response.body();
                    if (drivers != null) {
                        Drivers driver = drivers.get(0);

                        driverName.setText(driver.getFullName());
                        driverDisplayName.setText(driver.getNameAcronym());
                        driverTeamName.setText(driver.getTeamName());
                        driverCountry.setText(driver.getCountryCode());
                        loading.setVisibility(View.GONE);
                        loaded.setVisibility(View.VISIBLE);

                        if (driver.getHeadshotUrl() != null) {
                            errorImage.setVisibility(View.GONE);
                            driverUrl.setVisibility(View.VISIBLE);
                            Glide.with(DriverDetail.this)
                                    .load(driver.getHeadshotUrl())
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.placeholder)
                                            .error(R.drawable.error_image)
                                    )
                                    .into(driverUrl);
                        } else {
                            errorImage.setVisibility(View.VISIBLE);
                            driverUrl.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Drivers>> call, Throwable t) {
                loaded.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
            }
        });
    }
}