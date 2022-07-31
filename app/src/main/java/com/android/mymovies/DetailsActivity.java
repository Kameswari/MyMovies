package com.android.mymovies;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.android.mymovies.network.MovieDetails;
import com.android.mymovies.network.MovieListItem;
import com.android.mymovies.util.Util;
import com.android.mymovies.util.db.DBHelper;
import com.android.mymovies.viewmodels.DetailsViewModel;
import com.android.mymovies.viewmodels.MovieListViewModel;
import com.android.mymovies.viewmodels.UpdateListener;

/**
 * Activity to show movie details
 */
public class DetailsActivity extends BaseActivity {
    private DetailsViewModel detailsViewModel;
    private boolean isFavorite;
    private ImageView favoriteItem;
    private String title;
    private String year;
    private String id;
    private String poster;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        detailsViewModel = new DetailsViewModel();
        initUI();

        detailsViewModel.setUpdateListener(new UpdateListener() {
            @Override
            public void onUpdate(Object details) {
                hideProgressBar();
                updateOtherDetails((MovieDetails)details);
            }

            @Override
            public void onError(int error) {
                hideProgressBar();
                showToast(error);
            }
        });
    }

    private void updateOtherDetails(MovieDetails details){
        ((TextView)findViewById(R.id.plot)).setText(details.plot);
        ((TextView)findViewById(R.id.mpaa)).setText(details.rating);
    }

    // initialize the UI elements and update the data
    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showAndHandleBackIcon(toolbar);

        // update the existing values
        title = getIntent().getStringExtra(DBHelper.TITLE);
        toolbar.setTitle(title);
        year = getIntent().getStringExtra(DBHelper.YEAR);
        ((TextView) findViewById(R.id.year)).setText(year);
        poster = getIntent().getStringExtra(DBHelper.POSTER);
        Util.loadImageWithGlide(poster, findViewById(R.id.poster));
        id = getIntent().getStringExtra(DBHelper.IMDB_ID);
        type = getIntent().getStringExtra(DBHelper.TYPE);

        handleFavorite();
        showProgressBar(getString(R.string.please_wait));
        detailsViewModel.getMovieDetails(id);
    }

    // handle the UI changes and click listener for favorite functionality
    private void handleFavorite(){
        favoriteItem = findViewById(R.id.favorite);
        isFavorite = getIntent().getBooleanExtra("isFavorite", false);
        updateFavoriteIcon();

        MovieListViewModel movieListViewModel = new MovieListViewModel();
        favoriteItem.setOnClickListener(menuItem -> {
            if (isFavorite) {
                movieListViewModel.removeFavorite(getIntent().getStringExtra(DBHelper.IMDB_ID), () -> {
                    isFavorite = false;
                    updateFavoriteIcon();
                });
            } else {
                MovieListItem item = new MovieListItem();
                item.title = title;
                item.year = year;
                item.poster = poster;
                item.imdbID = id;
                item.type = type;
                movieListViewModel.addFavorite(item, () -> {
                    isFavorite = true;
                    updateFavoriteIcon();
                });
            }
        });

    }

    private void updateFavoriteIcon() {
        if (isFavorite) {
            favoriteItem.setImageResource(R.drawable.ic_star_filled);
        } else {
            favoriteItem.setImageResource(R.drawable.ic_star_outline);
        }
    }
}
