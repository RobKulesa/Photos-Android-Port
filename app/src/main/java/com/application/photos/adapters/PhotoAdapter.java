package com.application.photos.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.application.photos.structures.Album;
import com.application.photos.structures.AlbumList;
import com.application.photos.structures.Photo;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>{
    private Album album;
    private Context context;

    public PhotoAdapter(Context context, Album album) {
        this.context = context;
        this.album = album;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View photoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_photoholder_layout, parent, false);
        return new PhotoAdapter.PhotoViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = album.getPhoto(position);
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
                            //Forward to photo slideshow open
                            return true;
                        case R.id.menuitemdeletephoto:
                            album.removePhoto(photo);
                            PhotoAdapter.super.notifyItemRemoved(position);
                            return true;
                        default:
                            return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return album.getNumPhotos();
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
