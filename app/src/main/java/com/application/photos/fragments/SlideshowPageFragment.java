package com.application.photos.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

import com.application.photos.R;
import com.application.photos.activities.TagListActivity;
import com.application.photos.structures.Photo;
import com.application.photos.structures.AlbumList;

public class SlideshowPageFragment extends Fragment {
    private ImageView imageViewSlideshowImage;
    private RecyclerView recyclerViewSlideshowImageTags;
    private Button addTagButton;

    private Photo photo;
    private Context context;
    private AlbumList albumList;
    private int albumIndex;
    private int photoIndex;

    public SlideshowPageFragment(Context context, AlbumList albumList, int albumIndex, int photoIndex) {
        this.context = context;
        this.albumList = albumList;
        this.albumIndex = albumIndex;
        this.photoIndex = photoIndex;
        this.photo = this.albumList.getAlbum(this.albumIndex).getPhoto(this.photoIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_slideshow_page, container, false);
        imageViewSlideshowImage = view.findViewById(R.id.imageViewSlideshowImage);
        recyclerViewSlideshowImageTags = view.findViewById(R.id.recyclerViewSlideshowImageTags);
        imageViewSlideshowImage.setImageBitmap(Photo.getBitmap(this.context, photo));
        addTagButton = view.findViewById(R.id.buttonAddTag);
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TagListActivity.class);
                intent.putExtra("albumList", albumList);
                intent.putExtra("album", albumIndex);
                intent.putExtra("photo", photoIndex);
                context.startActivity(intent);
                return;
            }
        });

        /*
        imageViewSlideshowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Image Clicked!", Toast.LENGTH_SHORT).show();
                //TODO: Allow user to add tags by tapping on the photo
            }
        });*/

        return view;
    }

}