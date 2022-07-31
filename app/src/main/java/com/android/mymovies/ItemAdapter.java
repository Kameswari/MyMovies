package com.android.mymovies;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mymovies.network.MovieListItem;
import com.android.mymovies.util.Util;
import com.android.mymovies.util.db.DBHelper;
import com.android.mymovies.viewmodels.MovieListViewModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to show movie list
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public List<MovieListItem> getMovieList() {
        return movieList;
    }

    private List<MovieListItem> movieList = new ArrayList<>();
    MovieListViewModel movieListViewModel;

    public ItemAdapter(MovieListViewModel model){
        movieListViewModel = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieListItem item = movieList.get(position);

        holder.title.setText(item.title);
        holder.year.setText(item.year);

        Util.loadImageWithGlide(item.poster, holder.poster);

        holder.movieLayout.setOnClickListener(view -> {
            // navigate to details screen
            BaseActivity context = MyApplication.getInstance().getCurrentActivity();
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(DBHelper.TITLE, item.title);
            intent.putExtra(DBHelper.IMDB_ID, item.imdbID);
            intent.putExtra(DBHelper.POSTER, item.poster);
            intent.putExtra(DBHelper.YEAR, item.year);
            intent.putExtra("isFavorite", item.isFavorite);
            intent.putExtra(DBHelper.TYPE, item.type);
            context.startActivity(intent);
        });

        updateFavoriteIcon(item.isFavorite, holder.favorite);

        holder.favorite.setOnClickListener(view -> {
            if(item.isFavorite){
                movieListViewModel.removeFavorite(item.imdbID, () -> {
                    // remove from favorite list
                    // if it's for favorites screen
                    if(MyApplication.getInstance().getCurrentActivity() instanceof FavoritesActivity){
                        if(movieList.size()!=0){
                            movieList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,movieList.size());
                        }
                    }else{
                        item.isFavorite = false;
                        updateFavoriteIcon(false, holder.favorite);
                    }
                });
            }else{
                movieListViewModel.addFavorite(item, () -> {
                    holder.favorite.setImageResource(R.drawable.ic_star_filled);
                    item.isFavorite = true;
                });
            }
        });
    }

    private void updateFavoriteIcon(boolean isFavorite, ImageView imageView){
        if (isFavorite) {
            imageView.setImageResource(R.drawable.ic_star_filled);
        } else {
            imageView.setImageResource(R.drawable.ic_star_outline);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster;
        public TextView title;
        public TextView year;
        public ImageView favorite;
        public ConstraintLayout movieLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.poster = (ImageView) itemView.findViewById(R.id.poster);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.year = (TextView) itemView.findViewById(R.id.year);
            this.favorite = (ImageView) itemView.findViewById(R.id.favorite);
            this.movieLayout = (ConstraintLayout) itemView.findViewById(R.id.movie_item);
        }
    }

    public void updateList(List<MovieListItem> list){
        movieList = list;
        notifyDataSetChanged();
    }

}
