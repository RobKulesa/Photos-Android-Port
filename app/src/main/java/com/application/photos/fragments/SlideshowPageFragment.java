package com.application.photos.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.application.photos.R;
import com.application.photos.activities.TagListActivity;
import com.application.photos.structures.Photo;
import com.application.photos.structures.AlbumList;
import com.application.photos.adapters.TagAdapter;

import java.io.IOException;

public class SlideshowPageFragment extends Fragment {
    private ImageView imageViewSlideshowImage;
    private RecyclerView recyclerViewSlideshowImageTags;
    private Button addPersonTag;
    private Button addLocationTag;



    private Photo photo;
    private Context context;
    private AlbumList albumList;
    private int albumIndex;
    private int photoIndex;
    private TagAdapter tagAdapter;

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
        addPersonTag = view.findViewById(R.id.buttonAddPerson);
        addLocationTag = view.findViewById(R.id.buttonAddLocation);

        tagAdapter = new TagAdapter(context, albumList, albumIndex, photoIndex);
        recyclerViewSlideshowImageTags.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewSlideshowImageTags.setHasFixedSize(false);
        recyclerViewSlideshowImageTags.setAdapter(tagAdapter);
        tagAdapter.notifyDataSetChanged();

        //TODO: Create Person Button onClickListener
        addPersonTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setTitle("New Person Tag");

                EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogBuilder.setView(input);

                dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newPerson = input.getText().toString();
                        if(newPerson == null || newPerson.isEmpty() || photo.hasTag(Photo.isPerson, newPerson)){
                            Toast.makeText(context, "Person Tag Value cannot be empty or the value of another Person Tag", Toast.LENGTH_LONG).show();
                            return;
                        }
                        albumList.getAlbum(albumIndex).getPhoto(photoIndex).addTag(Photo.isPerson, newPerson);
                        tagAdapter.notifyItemInserted(photo.getTags().size());
                        Toast.makeText(context, "Person Tag Created Successfully!", Toast.LENGTH_LONG).show();
                        try {
                            AlbumList.writeAlbumList(context, albumList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        return;
                    }
                });
                dialogBuilder.show();

            }
        });


        //TODO: Create Locatio nButton Onclicklistener
        addLocationTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setTitle("New Location Tag");

                EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogBuilder.setView(input);

                dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newLocation = input.getText().toString();
                        if(newLocation == null || newLocation.isEmpty() || photo.hasTag(Photo.isLocation, newLocation)){
                            Toast.makeText(context, "Location Tag Value cannot be empty or the value of another Location Tag", Toast.LENGTH_LONG).show();
                            return;
                        }
                        albumList.getAlbum(albumIndex).getPhoto(photoIndex).addTag(Photo.isLocation, newLocation);
                        tagAdapter.notifyItemInserted(photo.getTags().size());
                        Toast.makeText(context, "Location Tag Created Successfully!", Toast.LENGTH_LONG).show();
                    }
                });

                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        return;
                    }
                });
                dialogBuilder.show();
            }
        });


        return view;
    }

}