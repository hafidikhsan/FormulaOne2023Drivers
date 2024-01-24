package com.hafidikhsana.formulaonedrivers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ListView listView = findViewById(R.id.listview);
        LinearLayout errorView = findViewById(R.id.error_display);
        LinearLayout loadingView = findViewById(R.id.progress_circle);
        TextView errorMessage = findViewById(R.id.error_message);

        APIClient apiClient = APIClient.getInstance();
        OpenF1API openF1Api = apiClient.getOpenF1Api();

        Call<List<Drivers>> call = openF1Api.getDrivers();

        call.enqueue(new Callback<List<Drivers>>() {
            @Override
            public void onResponse(Call<List<Drivers>> call, Response<List<Drivers>> response) {
                if (response.isSuccessful()) {
                    List<Drivers> drivers = response.body();
                    DriverListAdapter adapter = new DriverListAdapter(MainActivity.this, drivers);
                    listView.setAdapter(adapter);
                    loadingView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(MainActivity.this,DriverDetail.class);
                            Drivers driver = adapter.getItem(i);
                            intent.putExtra("name", driver.getFullName());
                            intent.putExtra("number", Integer.toString(driver.getDriverNumber()));
                            intent.putExtra("session", Integer.toString(driver.getSessionKey()));
                            startActivity(intent);
                        }
                    });
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