package com.hafidikhsana.formulaonedrivers;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

interface OpenF1API {
    @GET("v1/drivers?session_key=latest")
    Call<List<Drivers>> getDrivers();
}
