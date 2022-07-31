package com.android.mymovies.viewmodels;

import com.android.mymovies.R;
import com.android.mymovies.network.MovieDetails;
import com.android.mymovies.network.NetworkService;
import com.android.mymovies.util.Constants;
import com.android.mymovies.util.Util;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * View model for DetailsActivity
 */
public class DetailsViewModel {
    private UpdateListener updateListener;

    public void setUpdateListener(UpdateListener listener){
        updateListener = listener;
    }

    public void getMovieDetails(String id){
        if(!Util.isNetworkConnected()){
            updateListener.onError(R.string.please_connect_internet_complete_details);
            return;
        }
        NetworkService networkService = new NetworkService();
        // get the movie details from server
        networkService.getApi().fetchDetails(Constants.API_KEY, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDetails>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(MovieDetails movieDetails) {
                        updateListener.onUpdate(movieDetails);
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateListener.onError(R.string.problem_connecting_server);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
