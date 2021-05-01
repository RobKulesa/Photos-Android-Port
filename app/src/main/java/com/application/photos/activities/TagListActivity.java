package com.application.photos.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.application.photos.R;
import com.application.photos.adapters.TagAdapter;
import com.application.photos.structures.Album;
import com.application.photos.structures.AlbumList;
import com.application.photos.structures.Photo;
import java.io.IOException;

public class TagListActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private TagAdapter tagAdapter;
    private Button buttonCreatePersonTag;
    private Button buttonCreateLocationTag;

    private AlbumList albumList;
    private Album album;
    private Photo photo;
    private int albIndex;
    private int photoIndex;


    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_taglist);

        buttonCreateLocationTag = (Button) findViewById(R.id.buttonCreateLocationTag);
        buttonCreatePersonTag = (Button) findViewById(R.id.buttonCreatePersonTag);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewTagList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        albumList = (AlbumList) getIntent().getSerializableExtra("albumList");
        albIndex = getIntent().getIntExtra("album", 0);
        album = albumList.getAlbum(albIndex);
        photoIndex = getIntent().getIntExtra("photo", 0);
        photo = album.getPhoto(photoIndex);
        tagAdapter = new TagAdapter(this, albumList, albIndex, photoIndex);
        recyclerView.setAdapter(tagAdapter);
        tagAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart(){
        super.onStart();


        //Set onclickListener for addPersonTag
        buttonCreatePersonTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TagListActivity.this);
                dialogBuilder.setTitle("New Person Tag");

                EditText input = new EditText(TagListActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogBuilder.setView(input);

                dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newPerson = input.getText().toString();
                        if(newPerson == null || newPerson.isEmpty() || photo.hasTag(Photo.isPerson, newPerson)){
                            Toast.makeText(TagListActivity.this, "Person Tag Value cannot be empty or the value of another Person Tag", Toast.LENGTH_LONG).show();
                            return;
                        }
                        albumList.getAlbum(albIndex).getPhoto(photoIndex).addTag(Photo.isPerson, newPerson);
                        tagAdapter.notifyItemInserted(photo.getTags().size());
                        Toast.makeText(TagListActivity.this, "Person Tag Created Successfully!", Toast.LENGTH_LONG).show();
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


        buttonCreateLocationTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TagListActivity.this);
                dialogBuilder.setTitle("New Location Tag");

                EditText input = new EditText(TagListActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogBuilder.setView(input);

                dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newLocation = input.getText().toString();
                        if(newLocation == null || newLocation.isEmpty() || photo.hasTag(Photo.isLocation, newLocation)){
                            Toast.makeText(TagListActivity.this, "Location Tag Value cannot be empty or the value of another Location Tag", Toast.LENGTH_LONG).show();
                            return;
                        }
                        albumList.getAlbum(albIndex).getPhoto(photoIndex).addTag(Photo.isLocation,newLocation);
                        tagAdapter.notifyItemInserted(photo.getTags().size());
                        Toast.makeText(TagListActivity.this, "Location Tag Created Successfully!", Toast.LENGTH_LONG).show();
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

    }

    @Override
    public void onBackPressed() {
        System.out.println("back pressed");
        try {
            AlbumList.writeAlbumList(this, albumList);
            System.out.println("writing albumlist");
            for(String str : photo.getTags()){
                System.out.println("\t" + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("caught an error");
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        System.out.println("back pressed");
        try {
            AlbumList.writeAlbumList(this, albumList);
            System.out.println("writing albumlist");

            for(String str : photo.getTags()){
                System.out.println("\t" + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }
}
