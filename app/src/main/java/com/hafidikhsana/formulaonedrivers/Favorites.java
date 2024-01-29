package com.hafidikhsana.formulaonedrivers;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Favorites {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "driver_number")
    public int driverNumber;

    @ColumnInfo(name = "driver_name")
    public String driver_name;

    @ColumnInfo(name = "driver_team")
    public String driver_team;

    @ColumnInfo(name = "driver_acronym")
    public String driver_acronym;

    public Favorites(int driverNumber, String driver_name, String driver_team, String driver_acronym, String email) {
        this.driverNumber = driverNumber;
        this.driver_name = driver_name;
        this.driver_team = driver_team;
        this.driver_acronym = driver_acronym;
        this.email = email;
    }
}
