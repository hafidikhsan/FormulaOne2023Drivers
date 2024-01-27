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

    @Query("SELECT * FROM favorites")
    LiveData<List<Favorites>> getAll();

    @Query("DELETE FROM favorites WHERE driver_number = :driverNumber")
    void deleteById(int driverNumber);

    @Query("SELECT COUNT(*) FROM favorites WHERE driver_number = :driverNumber")
    int isFavorite(int driverNumber);
}
