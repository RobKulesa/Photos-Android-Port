package com.application.photos.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.photos.R;
import com.application.photos.activities.AlbumListActivity;
import com.application.photos.activities.OpenAlbumActivity;
import com.application.photos.structures.Album;
import com.application.photos.structures.AlbumList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>{
    private AlbumList albumList;
    private Context context;

    public AlbumAdapter(Context context, AlbumList albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View albumView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_albumholder_layout, parent, false);
        return new AlbumViewHolder(albumView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = albumList.getAlbum(position);
        holder.setDetails(album.getName(), album.getNumPhotos());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int menuItemPosition) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.inflate(R.menu.menu_selectalbumitempopup);
                popup.show();
                popup.setOnMenuItemClickListener(menuItem -> {
                    switch(menuItem.getItemId()) {
                        case R.id.menuitemalbumopen:
                            Intent intent = new Intent(context, OpenAlbumActivity.class);
                            intent.putExtra("albumList", albumList);
                            intent.putExtra("album", position);
                            context.startActivity(intent);
                            return true;

                        case R.id.menuitemalbumedit:
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                            dialogBuilder.setTitle("Rename Album");

                            EditText input = new EditText(context);
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            dialogBuilder.setView(input);

                            dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String newName = input.getText().toString();
                                    if(newName == null || newName.isEmpty() || albumList.containsAlbum(newName)) {
                                        Toast.makeText(context, "Album Name cannot be empty or the name of another album!", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    album.setName(newName);
                                    AlbumAdapter.super.notifyItemChanged(position);
                                    Toast.makeText(context, "Album Renamed successfully!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            dialogBuilder.show();
                            return true;
                        case R.id.menuitemalbumdelete:
                            albumList.removeAlbum(position);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
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
        return albumList.getLength();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        private ItemClickListener itemClickListener;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.view.setOnClickListener(this);
        }

        public void setDetails(String name, int numPhotos) {
            TextView textViewAlbumName = (TextView) view.findViewById(R.id.textViewAlbumHolderAlbumName);
            TextView textViewNumPhotos = (TextView) view.findViewById(R.id.textViewAlbumHolderNumPhotos);

            textViewAlbumName.setText(name);
            textViewNumPhotos.setText(String.valueOf(numPhotos));
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
