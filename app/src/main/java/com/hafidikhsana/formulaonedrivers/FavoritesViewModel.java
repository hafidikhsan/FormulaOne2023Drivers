package com.hafidikhsana.formulaonedrivers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {
    private FavoriteRepositories repository;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoriteRepositories(application);
    }

    public void insert(Favorites favorite) {
        repository.insert(favorite);
    }

    public void delete(Integer item, String email) {
        repository.delete(item, email);
    }

    public boolean isFavorite(Integer item, String email) {
        return repository.isFavorite(item, email);
    }

    public LiveData<List<Favorites>> getAllFavorites(String email) {
        return repository.getAllFavoriteItems(email);
    }
}