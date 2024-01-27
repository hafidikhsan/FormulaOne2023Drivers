package com.hafidikhsana.formulaonedrivers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {
    private FavoriteRepositories repository;
    private LiveData<List<Favorites>> allFavorites;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoriteRepositories(application);
        allFavorites = repository.getAllFavoriteItems();
    }

    public void insert(Favorites favorite) {
        repository.insert(favorite);
    }

    public void delete(Integer item) {
        repository.delete(item);
    }

    public boolean isFavorite(Integer item) {
        return repository.isFavorite(item);
    }

    public LiveData<List<Favorites>> getAllFavorites() {
        return allFavorites;
    }
}