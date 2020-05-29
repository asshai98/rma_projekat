package android.singidunum.ac.flickrapp;


import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class CameraRecyclerViewAdapter extends RecyclerView.Adapter<CameraRecyclerViewAdapter.CameraViewHolder> {

    private static ArrayList<ImageModelClass> objectModelClassList;
    private static DatabaseHelper databaseHelper;
    private static Context context;


    public CameraRecyclerViewAdapter(ArrayList<ImageModelClass> objectModelClassList, Context context) {
        this.objectModelClassList = objectModelClassList;
        this.context = context;
    }


    @NonNull
    @Override
    public CameraRecyclerViewAdapter.CameraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        databaseHelper = new DatabaseHelper(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        return  new CameraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CameraRecyclerViewAdapter.CameraViewHolder holder, int position) {
        ImageModelClass objectModelClass = objectModelClassList.get(position);
        holder.uploadTitle.setText(objectModelClass.getImageName());
        holder.uploadImage.setImageBitmap(objectModelClass.getImage());
        holder.uploadAuthor.setText(objectModelClass.getImageAuthor());
        holder.uploadDesc.setText(objectModelClass.getImageDescription());

        readCursorData(objectModelClass, holder);

    }



    @Override
    public int getItemCount() {
        return objectModelClassList.size();
    }

    static class CameraViewHolder extends RecyclerView.ViewHolder{
        TextView uploadTitle = null;
        ImageView uploadImage = null;
        TextView uploadAuthor = null;
        TextView uploadDesc = null;

        Button addToFavourites = null;


        public CameraViewHolder(View itemView){
            super(itemView);
            this.uploadTitle = itemView.findViewById(R.id.uploadTitle);
            this.uploadImage = itemView.findViewById(R.id.uploadView);
            this.uploadAuthor = itemView.findViewById(R.id.uploadAuthor);
            this.uploadDesc = itemView.findViewById(R.id.uploadDesc);

            this.addToFavourites = itemView.findViewById(R.id.addToFavourites);
            addToFavourites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ImageModelClass imageModel = objectModelClassList.get(position);
                    if(imageModel.getFavStatus().equals("0")){
                        imageModel.setFavStatus("1");
                        databaseHelper.addFavourite(imageModel.getImageName(), imageModel.getImageAuthor(), imageModel.getFavStatus());
                        Toast.makeText(context, "Added to favoruties", Toast.LENGTH_SHORT).show();
                        addToFavourites.setBackgroundResource(R.drawable.ic_favorite_red_24dp);

                    } else {
                        imageModel.setFavStatus("0");
                        databaseHelper.removeFromFavourites(imageModel.getImageAuthor());
                        addToFavourites.setBackgroundResource(R.drawable.ic_favorite_shadow_24dp);
                    }
                }
            });

        }

    }

    private void readCursorData(ImageModelClass imageModelClass, CameraViewHolder viewHolder){
        Cursor cursor = databaseHelper.readAllFavourites(imageModelClass.getImageAuthor());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try{
            while(cursor.moveToNext()){
                String fav_status = cursor.getString(3);
                imageModelClass.setFavStatus(fav_status);

                //provera statusa
                if(fav_status != null && fav_status.equals("1")){
                    viewHolder.addToFavourites.setBackgroundResource(R.drawable.ic_favorite_red_24dp);
                } else if(fav_status != null && fav_status.equals("0")){
                    viewHolder.addToFavourites.setBackgroundResource(R.drawable.ic_favorite_shadow_24dp);
                }
            }
        } finally {
            if(cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }
    }
}
