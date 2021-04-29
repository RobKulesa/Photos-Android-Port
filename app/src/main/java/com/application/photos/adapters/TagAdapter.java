package com.application.photos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.photos.R;
import com.application.photos.structures.AlbumList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagHolder> {

    /*private TagsDatastruct tags;*/
    private Context context;

    public TagAdapter(Context context /*, TagsDatastruct tags*/) {
        this.context = context;
        /*this.tags = tags;*/
    }

    @NonNull
    @Override
    public TagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tagView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tagholder_layout, parent, false);
        return new TagAdapter.TagHolder(tagView);
    }

    @Override
    public void onBindViewHolder(@NonNull TagHolder holder, int position) {
        //TODO: load tag into holder
        /*Tag tag = tags.getTag(position)*/
        /*holder.setDetails(<tag string representation>)*/
        //TODO: set tag holder click listener to allow for removing tags from photo
    }

    @Override
    public int getItemCount() {
        //TODO: get number of tags
        /*return tags.getNumTags();*/
        return 0;
    }

    public class TagHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        private ItemClickListener itemClickListener;

        public TagHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.view.setOnClickListener(this);
        }

        public void setDetails(String tag) {
            TextView textViewTagHolder = (TextView) view.findViewById(R.id.textViewTagHolder);

            textViewTagHolder.setText(tag);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(view, getAdapterPosition());
        }
    }
}
