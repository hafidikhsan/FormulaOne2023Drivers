package com.hafidikhsana.formulaonedrivers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoritesDao {
    @Insert
    long insert(Favorites favoriteItem);

    @Query("SELECT * FROM favorites WHERE email = :email")
    LiveData<List<Favorites>> getAll(String email);

    @Query("DELETE FROM favorites WHERE driver_number = :driverNumber and email = :email")
    void deleteById(int driverNumber, String email);

    @Query("SELECT COUNT(*) FROM favorites WHERE driver_number = :driverNumber and email = :email")
    int isFavorite(int driverNumber, String email);
}
