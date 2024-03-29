package com.application.photos.activities;

import android.content.Intent;
import android.os.Bundle;

import com.application.photos.adapters.PhotoAdapter;
import com.application.photos.adapters.SlideshowPagerAdapter;
import com.application.photos.structures.Album;
import com.application.photos.structures.AlbumList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.View;
import android.widget.Toast;

import com.application.photos.R;

import java.io.IOException;

public class SlideshowActivity extends FragmentActivity {
    private ViewPager2 viewPager;
    private SlideshowPagerAdapter adapter;

    private AlbumList albumList;
    private Album album;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        viewPager = (ViewPager2) findViewById(R.id.viewPagerSlideshow);
        Toast.makeText(this, "Swipe to view slideshow!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        albumList = (AlbumList) getIntent().getSerializableExtra("albumList");
        int albumIndex = getIntent().getIntExtra("album", 0);
        int photoIndex = getIntent().getIntExtra("photo", 0);
        album = albumList.getAlbum(albumIndex);
        adapter = new SlideshowPagerAdapter(this, this, albumList, albumIndex);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(photoIndex, true);
    }

    @Override
    public void onBackPressed() {
        try {
            AlbumList.writeAlbumList(this, albumList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, OpenAlbumActivity.class);
        intent.putExtra("albumList", albumList);
        intent.putExtra("album", getIntent().getIntExtra("album", 0));
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        try {
            AlbumList.writeAlbumList(this, albumList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }
}