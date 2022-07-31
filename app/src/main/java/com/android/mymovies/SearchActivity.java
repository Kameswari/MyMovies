package com.android.mymovies;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mymovies.network.MovieListItem;
import com.android.mymovies.viewmodels.MovieListViewModel;
import com.android.mymovies.viewmodels.UpdateListener;

import java.util.List;

/**
 * Search screen
 */
public class SearchActivity extends BaseActivity{
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private TextView noResultsView;
    private MovieListViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchViewModel = new MovieListViewModel();
        initUI();

        searchViewModel.setUpdateListener(new UpdateListener() {
            @Override
            public void onUpdate(Object movieListItemList) {
                hideProgressBar();
                updateMovieList((List<MovieListItem>)movieListItemList);
            }

            @Override
            public void onError(int error) {
                hideProgressBar();
                showToast(error);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchViewModel.mapFavoritesToSearchResults(itemAdapter.getMovieList());
        itemAdapter.notifyDataSetChanged();
    }

    private void initUI(){
        recyclerView = findViewById(R.id.recycler_view);
        noResultsView = findViewById(R.id.no_results_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showAndHandleBackIcon(toolbar);

        itemAdapter = new ItemAdapter(searchViewModel);
        recyclerView.setAdapter(itemAdapter);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnClickListener(view -> searchView.onActionViewExpanded());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showProgressBar(getString(R.string.please_wait));
                // get the search results by making api call
                searchViewModel.searchMovies(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // update recycler view
    private void updateMovieList(List<MovieListItem> list){
        if(list == null || list.isEmpty()){
            recyclerView.setVisibility(View.INVISIBLE);
            noResultsView.setVisibility(View.VISIBLE);
        }else{
            noResultsView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            itemAdapter.updateList(list);
        }
    }

}
