package com.hafidikhsana.formulaonedrivers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDetail extends AppCompatActivity {

    TextView driverName, driverDisplayName, driverTeamName, driverCountry, errorText, driverNumber;
    ImageView driverUrl, errorImage, loveImage, addImage;
    LinearLayout loaded;
    ProgressBar loading;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userEmail;

    FavoritesViewModel favoriteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);

        if (user != null) {
            userEmail = user.getEmail();
        } else {
            Toast.makeText(DriverDetail.this, "User not found",
                    Toast.LENGTH_SHORT).show();
        }

        Intent intent = getIntent();
        favoriteViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        driverName = findViewById(R.id.driver_name);
        driverNumber = findViewById(R.id.driver_number);
        driverDisplayName = findViewById(R.id.driver_display_name);
        driverTeamName = findViewById(R.id.driver_team);
        driverCountry = findViewById(R.id.driver_country);
        errorText = findViewById(R.id.driver_error_loaded);
        driverUrl = findViewById(R.id.driver_url);
        errorImage = findViewById(R.id.error_image);
        loaded = findViewById(R.id.driver_loaded);
        loading = findViewById(R.id.driver_detail_loading);
        loveImage = findViewById(R.id.is_favorite);
        addImage = findViewById(R.id.is_favorite_add);

        String parameterNumber = intent.getStringExtra("number");
        String parameterSession = intent.getStringExtra("session");

        APIClient apiClient = APIClient.getInstance();
        OpenF1API openF1Api = apiClient.getOpenF1Api();
        boolean isFavorite = favoriteViewModel.isFavorite(Integer.parseInt(parameterNumber), userEmail);

        if (isFavorite) {
            addImage.setVisibility(View.GONE);
            loveImage.setVisibility(View.VISIBLE);
        }

        Call<List<Drivers>> call = openF1Api.getDriverDetail(Integer.parseInt(parameterNumber), Integer.parseInt(parameterSession));

        call.enqueue(new Callback<List<Drivers>>() {
            @Override
            public void onResponse(Call<List<Drivers>> call, Response<List<Drivers>> response) {
                if (response.isSuccessful()) {
                    List<Drivers> drivers = response.body();
                    if (drivers != null) {
                        Drivers driver = drivers.get(0);

                        driverName.setText(driver.getFullName());
                        driverNumber.setText(Integer.toString(driver.getDriverNumber()));
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

                        loveImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                favoriteViewModel.delete(Integer.parseInt(parameterNumber), userEmail);
                                addImage.setVisibility(View.VISIBLE);
                                loveImage.setVisibility(View.GONE);
                            }
                        });

                        addImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                favoriteViewModel.insert(new Favorites(Integer.parseInt(parameterNumber), driver.getFullName(), driver.getTeamName(), driver.getNameAcronym(), userEmail));
                                addImage.setVisibility(View.GONE);
                                loveImage.setVisibility(View.VISIBLE);
                            }
                        });
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