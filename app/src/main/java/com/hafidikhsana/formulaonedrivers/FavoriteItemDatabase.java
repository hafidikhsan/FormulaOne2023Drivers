package com.hafidikhsana.formulaonedrivers;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Favorites.class}, version = 1)
public abstract class FavoriteItemDatabase extends RoomDatabase {
    public abstract FavoritesDao favoriteItemDao();

    private static volatile FavoriteItemDatabase INSTANCE;

    public static FavoriteItemDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavoriteItemDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FavoriteItemDatabase.class, "favorite_items_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}