package com.application.photos.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.application.photos.R;
import com.application.photos.structures.Photo;

public class SlideshowPageFragment extends Fragment {
    private ImageView imageViewSlideshowImage;
    private RecyclerView recyclerViewSlideshowImageTags;

    private Photo photo;
    private Context context;

    public SlideshowPageFragment(Context context, Photo photo) {
        this.context = context;
        this.photo = photo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_slideshow_page, container, false);
        imageViewSlideshowImage = view.findViewById(R.id.imageViewSlideshowImage);
        recyclerViewSlideshowImageTags = view.findViewById(R.id.recyclerViewSlideshowImageTags);
        imageViewSlideshowImage.setImageBitmap(Photo.getBitmap(this.context, photo));

        imageViewSlideshowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Image Clicked!", Toast.LENGTH_SHORT).show();
                //TODO: Allow user to add tags by tapping on the photo
            }
        });

        return view;
    }

}