package android.singidunum.ac.flickrapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {
    private Context context;
    private static ArrayList<FavItem> favItemArrayList;
    private static DatabaseHelper databaseHelper;

    public CollectionAdapter(Context context, ArrayList<FavItem> favItemArrayList) {
        this.context = context;
        this.favItemArrayList = favItemArrayList;
    }

    @NonNull
    @Override
    public CollectionAdapter.CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item, parent, false);
        databaseHelper = new DatabaseHelper(context);
        return  new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionAdapter.CollectionViewHolder holder, int position) {
        FavItem favItem = favItemArrayList.get(position);
        holder.favItemTitle.setText(favItem.getImageTitle());
    }

    @Override
    public int getItemCount() {
        return favItemArrayList.size();
    }

    class CollectionViewHolder extends RecyclerView.ViewHolder{
        TextView favItemTitle = null;
        Button removeFromFavourites = null;

        public CollectionViewHolder(View itemView){
            super(itemView);
            this.favItemTitle = itemView.findViewById(R.id.favItemTitle);
            this.removeFromFavourites = itemView.findViewById(R.id.removeFromFavourites);

            removeFromFavourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final FavItem favItem = favItemArrayList.get(position);
                    databaseHelper.removeFromFavourites(favItem.getUserEmail());
                    removeItem(position);
                }
            });

        }

        private void removeItem(int position){
            favItemArrayList.remove(position);
           notifyItemRemoved(position);
           notifyItemRangeChanged(position,favItemArrayList.size());
        }
    }
}
