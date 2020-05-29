package android.singidunum.ac.flickrapp;


import android.content.Context;
import android.content.Intent;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


import java.util.List;

import static android.singidunum.ac.flickrapp.GalleryFragment.PHOTO_TRANSFER;

class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder> {

    //adapter za rad sa recyclerview-om
    private static List<Photo> photoList;
    private Context context;

    public ImageRecyclerViewAdapter(List<Photo> photoList, Context context) {
        this.photoList = photoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //pozivan do strane layout manager-a kada mu je potreban novi view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        //ukljucuje piccasso
        Photo photoItem = photoList.get(position);
        Picasso.with(context).load(photoItem.getImage())
                .error(R.drawable.baseline_photo_black_48dp)
                .placeholder(R.drawable.baseline_photo_black_48dp)
                .into(holder.thumbnail);
        holder.title.setText(photoItem.getTitle());
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoDetailActivity.class);
                intent.putExtra(PHOTO_TRANSFER, ImageRecyclerViewAdapter.this.getPhoto(position));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return ((photoList != null) && (photoList.size() != 0) ? photoList.size():0);
   }

   //dinamicko ucitavanje novih podataka u recyclerview

    void loadNewData(List<Photo> newPhotos){
        photoList = newPhotos;
        notifyDataSetChanged();
    }

    //vracamo sliku na trenutnoj poziciji

    public Photo getPhoto(int position){
        return ((photoList != null) && (photoList.size() != 0) ? photoList.get(position) : null);
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail = null;
        TextView title = null;
        Button button = null;

        public ImageViewHolder(View itemView){
            super(itemView);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.imageTitle);
            this.button = (Button) itemView.findViewById(R.id.likeBtn);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.setBackgroundResource(R.drawable.ic_thumb_up_yello_24dp);
                }
            });
        }
    }
}
