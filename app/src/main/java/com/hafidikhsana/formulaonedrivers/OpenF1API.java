package com.hafidikhsana.formulaonedrivers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

interface OpenF1API {
    @GET("v1/drivers?session_key=latest")
    Call<List<Drivers>> getDrivers();

    @GET("v1/drivers?")
    Call<List<Drivers>> getDriverDetail(
            @Query("driver_number") int number,
            @Query("session_key") int session
    );
}
