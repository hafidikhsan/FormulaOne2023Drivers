package com.hafidikhsana.formulaonedrivers;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FavoriteRepositories {
    private FavoritesDao favoriteItemDao;

    public FavoriteRepositories(Application application) {
        FavoriteItemDatabase database = FavoriteItemDatabase.getDatabase(application);
        favoriteItemDao = database.favoriteItemDao();
    }

    public LiveData<List<Favorites>> getAllFavoriteItems(String email) {
        return favoriteItemDao.getAll(email);
    }

    public boolean isFavorite(int itemId, String email) {
        return favoriteItemDao.isFavorite(itemId, email) > 0;
    }

    public void insert(Favorites favoriteItem) {
        new insertAsyncTask(favoriteItemDao).execute(favoriteItem);
    }

    private static class DeleteTask {
        int item;
        String email;

        DeleteTask(int item, String email) {
            this.item = item;
            this.email = email;
        }
    }

    public void delete(int itemId, String email) {
        DeleteTask deleteModel = new DeleteTask(itemId, email);
        new deleteAsyncTask(favoriteItemDao).execute(deleteModel);
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

    private static class deleteAsyncTask extends AsyncTask<DeleteTask, String, Void> {
        private FavoritesDao favoriteDao;

        deleteAsyncTask(FavoritesDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(DeleteTask... deleteItem) {
            favoriteDao.deleteById(deleteItem[0].item, deleteItem[0].email);
            return null;
        }
    }
}