package com.application.photos.activities;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.application.photos.adapters.PhotoAdapter;
import com.application.photos.adapters.SlideshowPagerAdapter;
import com.application.photos.structures.Album;
import com.application.photos.structures.AlbumList;
import com.application.photos.structures.Photo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.application.photos.R;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class SearchResultsActivity extends AppCompatActivity{
    private AlbumList albumList;
    private RecyclerView recyclerViewSearchResults;

    private PhotoAdapter photoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresults);

        this.albumList = (AlbumList) getIntent().getSerializableExtra("albumList");
        recyclerViewSearchResults = (RecyclerView) findViewById(R.id.recyclerViewSearchResults);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearchResults.setHasFixedSize(false);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Album searchResults;
        ArrayList<Photo> allPhotos = new ArrayList<Photo>();
        String conjunction = getIntent().getStringExtra("conjunction");
        for(Album alb: albumList.getAlbums()){
            for(Photo p : alb.getPhotos()){
                allPhotos.add(p);
            }
        }
        int tag1Type = getIntent().getIntExtra("tag1Type", 0);
        String tag1Value = getIntent().getStringExtra("tag1Value");
        ArrayList<Photo> finalList = new ArrayList<Photo>();
        if(conjunction.equals("none")){

            for(Photo p: allPhotos){
                for(String tag: p.getTags()){
                    if(p.isTagType(tag1Type, tag) && p.getTagValue(tag).length() >= tag1Value.length() &&
                            p.getTagValue(tag).substring(0, tag1Value.length()).equals(tag1Value)){
                        finalList.add(p);
                        break;
                    }
                }
            }

        } else{
            int tag2Type = getIntent().getIntExtra("tag2Type", 0);
            String tag2Value = getIntent().getStringExtra("tag2Value");

            if(conjunction.equals("and")){
                ArrayList<Photo> tempList = new ArrayList<Photo>();
                for(Photo p: allPhotos){
                    for(String tag: p.getTags()){
                        if(p.isTagType(tag1Type, tag) && p.getTagValue(tag).length() >= tag1Value.length() &&
                                p.getTagValue(tag).substring(0, tag1Value.length()).equals(tag1Value)){
                            tempList.add(p);
                            break;
                        }
                    }
                }

                for(Photo p: tempList){
                    for(String tag: p.getTags()){
                        if(p.isTagType(tag2Type, tag) && p.getTagValue(tag).length() >= tag2Value.length() &&
                                p.getTagValue(tag).substring(0, tag2Value.length()).equals(tag2Value)){
                            finalList.add(p);
                            break;
                        }
                    }
                }


            } else {
                for(Photo p: allPhotos){
                    for(String tag: p.getTags()){
                        if(p.isTagType(tag1Type, tag) && p.getTagValue(tag).length() >= tag1Value.length() &&
                                p.getTagValue(tag).substring(0, tag1Value.length()).equals(tag1Value)){
                            finalList.add(p);
                            break;
                        }

                        if(p.isTagType(tag2Type, tag) && p.getTagValue(tag).length() >= tag2Value.length() &&
                                p.getTagValue(tag).substring(0, tag2Value.length()).equals(tag2Value)){
                            finalList.add(p);
                            break;
                        }
                    }
                }


            }
        }
        int index = albumList.getLength();
        Album tempAlb = new Album("temp");
        for(Photo p : finalList){
            tempAlb.addPhoto(p);
            System.out.println("Added: " + p.toString());
        }
        if(tempAlb.getNumPhotos() < 1) {
            Toast.makeText(this, "Search Returned No Results!", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        albumList.addAlbum(tempAlb);
        photoAdapter = new PhotoAdapter(this, albumList, index, true);
        recyclerViewSearchResults.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();
        /*
        String location = getIntent().getStringExtra("locationName").toLowerCase();
        String person = getIntent().getStringExtra("personName").toLowerCase();
        for(Album alb: albumList.getAlbums()){
            for(Photo p : alb.getPhotos()){
                allPhotos.add(p);
            }
        }

        //Filter
        ArrayList<Photo> finalList = new ArrayList<Photo>();
        if(!location.equals("") && !person.equals("")){
            String conjunction = getIntent().getStringExtra("conjunction");
            if(conjunction.equals("and")){ //AND
                ArrayList<Photo> tempList = new ArrayList<Photo>();
                for(Photo p: allPhotos){
                    for(String tag: p.getTags()){
                        if(p.isTagType(Photo.isPerson, tag) && p.getTagValue(tag).length() >= person.length() &&
                                p.getTagValue(tag).substring(0, person.length()).equals(person)){
                            tempList.add(p);
                            break;
                        }
                    }
                }
                for(Photo p: tempList){
                    for(String tag: p.getTags()){
                        if(p.isTagType(Photo.isLocation, tag) && p.getTagValue(tag).length() >= location.length() &&
                                p.getTagValue(tag).substring(0, location.length()).equals(location)){
                            finalList.add(p);
                            break;
                        }
                    }
                }


            } else { //OR
                for(Photo p: allPhotos){
                    for(String tag: p.getTags()){
                        if(p.isTagType(Photo.isPerson, tag) && p.getTagValue(tag).length() >= person.length() &&
                                p.getTagValue(tag).substring(0, person.length()).equals(person)){
                            finalList.add(p);
                            break;
                        }

                        if(p.isTagType(Photo.isLocation, tag) && p.getTagValue(tag).length() >= location.length() &&
                                p.getTagValue(tag).substring(0, location.length()).equals(location)){
                            finalList.add(p);
                            break;
                        }
                    }
                }
            }

        } else {
            if(location.equals("")){
                for(Photo p: allPhotos){
                    for(String tag: p.getTags()){
                        if(p.isTagType(Photo.isPerson, tag) && p.getTagValue(tag).length() >= person.length() &&
                                p.getTagValue(tag).substring(0, person.length()).equals(person)){
                            finalList.add(p);
                            break;
                        }
                    }
                }

            } else {
                for(Photo p: allPhotos){
                    for(String tag: p.getTags()){
                        if(p.isTagType(Photo.isLocation, tag) && p.getTagValue(tag).length() >= location.length() &&
                                p.getTagValue(tag).substring(0, location.length()).equals(location)){
                            finalList.add(p);
                            break;
                        }
                    }
                }
            }
        }


        int index = albumList.getLength();
        Album tempAlb = new Album("temp");
        for(Photo p : finalList){
            tempAlb.addPhoto(p);
            System.out.println("Added: " + p.toString());
        }
        albumList.addAlbum(tempAlb);
        photoAdapter = new PhotoAdapter(this, albumList, index, true);
        recyclerViewSearchResults.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();

         */

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PhotosSearchActivity.class);
        for(Album a: albumList.getAlbums()){
            if(a.getName().equals("temp")){
                albumList.removeAlbum(a);
            }
        }
        intent.putExtra("albumList", albumList);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        for(Album a: albumList.getAlbums()){
            if(a.getName().equals("temp")){
                albumList.removeAlbum(a);
            }
        }

        super.onStop();
    }

}
