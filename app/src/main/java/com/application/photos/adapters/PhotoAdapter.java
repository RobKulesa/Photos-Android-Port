package com.application.photos.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.photos.R;
import com.application.photos.activities.OpenAlbumActivity;
import com.application.photos.activities.SlideshowActivity;
import com.application.photos.structures.Album;
import com.application.photos.structures.AlbumList;
import com.application.photos.structures.Photo;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private AlbumList albumList;
    private int albumIndex;
    private Context context;

    public PhotoAdapter(Context context, AlbumList albumList, int albumIndex) {
        this.context = context;
        this.albumList = albumList;
        this.albumIndex = albumIndex;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View photoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_photoholder_layout, parent, false);
        return new PhotoAdapter.PhotoViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = albumList.getAlbum(albumIndex).getPhoto(position);
        holder.setDetails(Photo.getThumbnail(context, photo), photo.getFileName(context), photo.getTagStrings());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int menuItemPosition) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.inflate(R.menu.menu_selectphotoitempopup);
                popup.show();
                popup.setOnMenuItemClickListener(menuItem -> {
                    switch(menuItem.getItemId()) {
                        case R.id.menuitemviewphoto:
                            Intent intent = new Intent(context, SlideshowActivity.class);
                            intent.putExtra("albumList", albumList);
                            intent.putExtra("album", albumIndex);
                            intent.putExtra("photo", position);
                            context.startActivity(intent);
                            return true;
                        case R.id.menuitemdeletephoto:
                            albumList.getAlbum(albumIndex).removePhoto(photo);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
                            return true;
                        case R.id.menuitemmovephoto:
                            //TODO: Add functionality for moving photo to diff album
                        default:
                            return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.getAlbum(albumIndex).getNumPhotos();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final View view;
        private ItemClickListener itemClickListener;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.view.setOnClickListener(this);
        }

        public void setDetails(Bitmap bitmap, String fileName, ArrayList<String> tagsList) {
            ImageView imageViewPhotoPreview = (ImageView) view.findViewById(R.id.imageViewPhotoPreview);
            TextView textViewFilename = (TextView) view.findViewById(R.id.textViewFilename);
            TextView textViewTags = (TextView) view.findViewById(R.id.textViewPhotoTags);
            //TODO: set text view tags based on tags for photo
            imageViewPhotoPreview.setImageBitmap(bitmap);
            textViewFilename.setText(fileName);
            textViewTags.setText(tagsList.toString());
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition());
        }
    }
}
