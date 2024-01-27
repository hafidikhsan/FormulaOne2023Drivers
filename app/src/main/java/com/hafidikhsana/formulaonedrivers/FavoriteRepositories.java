package com.hafidikhsana.formulaonedrivers;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavoriteRepositories {
    private FavoritesDao favoriteItemDao;
    private LiveData<List<Favorites>> allFavoriteItems;

    public FavoriteRepositories(Application application) {
        FavoriteItemDatabase database = FavoriteItemDatabase.getDatabase(application);
        favoriteItemDao = database.favoriteItemDao();
        allFavoriteItems = favoriteItemDao.getAll();
    }

    public LiveData<List<Favorites>> getAllFavoriteItems() {
        return allFavoriteItems;
    }

    public boolean isFavorite(int itemId) {
        return favoriteItemDao.isFavorite(itemId) > 0;
    }

    public void insert(Favorites favoriteItem) {
        new insertAsyncTask(favoriteItemDao).execute(favoriteItem);
    }

    public void delete(int itemId) {
        new deleteAsyncTask(favoriteItemDao).execute(itemId);
    }

    private static class insertAsyncTask extends AsyncTask<Favorites, Void, Void> {
        private FavoritesDao favoriteDao;

        insertAsyncTask(FavoritesDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorites... favorites) {
            favoriteDao.insert(favorites[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Integer, Void, Void> {
        private FavoritesDao favoriteDao;

        deleteAsyncTask(FavoritesDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Integer... favorites) {
            favoriteDao.deleteById(favorites[0]);
            return null;
        }
    }
}