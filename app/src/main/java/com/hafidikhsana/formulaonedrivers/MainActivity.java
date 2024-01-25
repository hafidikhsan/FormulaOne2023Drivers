package com.hafidikhsana.formulaonedrivers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();

        RecyclerView list = findViewById(R.id.listview);
        LinearLayout errorView = findViewById(R.id.error_display);
        LinearLayout loadingView = findViewById(R.id.progress_circle);
        TextView errorMessage = findViewById(R.id.error_message);
        FloatingActionButton logout = findViewById(R.id.logout_button);
        RelativeLayout loaded = findViewById(R.id.drivers_loaded);

        APIClient apiClient = APIClient.getInstance();
        OpenF1API openF1Api = apiClient.getOpenF1Api();

        Call<List<Drivers>> call = openF1Api.getDrivers();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(getIntent());
            }
        });

        call.enqueue(new Callback<List<Drivers>>() {
            @Override
            public void onResponse(Call<List<Drivers>> call, Response<List<Drivers>> response) {
                if (response.isSuccessful()) {
                    List<Drivers> drivers = response.body();
                    DriversAdapter adapter = new DriversAdapter(drivers);
                    list.setAdapter(adapter);
                    loadingView.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                    logout.setVisibility(View.VISIBLE);
                    loaded.setVisibility(View.VISIBLE);


//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            Intent intent = new Intent(MainActivity.this,DriverDetail.class);
//                            Drivers driver = adapter.getItem(i);
//                            intent.putExtra("name", driver.getFullName());
//                            intent.putExtra("number", Integer.toString(driver.getDriverNumber()));
//                            intent.putExtra("session", Integer.toString(driver.getSessionKey()));
//                            startActivity(intent);
//                        }
//                    });
                }
            }

            @Override
            public void onFailure(Call<List<Drivers>> call, Throwable t) {
                loadingView.setVisibility(View.GONE);
                errorMessage.setText(t.getMessage());
                errorView.setVisibility(View.VISIBLE);
            }
        });
    }
}