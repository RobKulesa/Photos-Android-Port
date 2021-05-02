package com.application.photos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.photos.R;
import com.application.photos.structures.AlbumList;
import com.application.photos.structures.Photo;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagHolder> {

    /*private TagsDatastruct tags;*/
    private Context context;
    private AlbumList albumList;
    private int albumIndex;
    private int photoIndex;

    public TagAdapter(Context context, AlbumList albumList, int albumIndex, int photoIndex ) {
        this.context = context;
        this.albumList = albumList;
        this.albumIndex = albumIndex;
        this.photoIndex = photoIndex;
    }

    @NonNull
    @Override
    public TagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tagView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tagholder_layout, parent, false);
        return new TagAdapter.TagHolder(tagView);
    }

    @Override
    public void onBindViewHolder(@NonNull TagHolder holder, int position) {
        Photo photo = albumList.getAlbum(albumIndex).getPhoto(photoIndex);
        String tag = photo.getTags().get(position);
        holder.setDetails(photo.getTagName(tag), photo.getTagValue(tag));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.inflate(R.menu.menu_selecttagitempopup);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch(menuItem.getItemId()){
                        case R.id.menuitemdeletetag:
                            albumList.getAlbum(albumIndex).getPhoto(photoIndex).getTags().remove(position);
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
        return albumList.getAlbum(albumIndex).getPhoto(photoIndex).getTags() == null ? 0 : albumList.getAlbum(albumIndex).getPhoto(photoIndex).getTags().size();
    }

    public class TagHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        private ItemClickListener itemClickListener;

        public TagHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.view.setOnClickListener(this);
        }

        public void setDetails(String tagName, String tagValue) {
            TextView textViewTagHolderTagName = (TextView) view.findViewById(R.id.textViewTagHolderTagName);
            TextView textViewTagHolderTagValue = (TextView) view.findViewById(R.id.textViewTagHolderTagValue);

            textViewTagHolderTagName.setText(tagName);
            textViewTagHolderTagValue.setText(tagValue);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(view, getAdapterPosition());
        }
    }
}
