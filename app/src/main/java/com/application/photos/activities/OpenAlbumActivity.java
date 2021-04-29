package com.application.photos.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.application.photos.R;
import com.application.photos.adapters.AlbumAdapter;
import com.application.photos.adapters.ItemClickListener;
import com.application.photos.adapters.PhotoAdapter;
import com.application.photos.structures.Album;
import com.application.photos.structures.AlbumList;
import com.application.photos.structures.Photo;

import java.io.IOException;

public class OpenAlbumActivity extends AppCompatActivity {
    private Button buttonAddPhoto;
    private RecyclerView recyclerViewPhotoList;
    private PhotoAdapter photoAdapter;

    private AlbumList albumList;
    private Album album;

    private static final int REQUEST_IMAGE_GET = 1;

    static final int REQUEST_IMAGE_OPEN = 1;

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            album.addPhoto(new Photo(fullPhotoUri));
            photoAdapter.notifyItemInserted(album.getNumPhotos());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openalbum);

        buttonAddPhoto = (Button) findViewById(R.id.buttonAddPhoto);

        recyclerViewPhotoList = (RecyclerView) findViewById(R.id.recyclerViewPhotoList);
        recyclerViewPhotoList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPhotoList.setHasFixedSize(false);

        albumList = (AlbumList) getIntent().getSerializableExtra("albumList");
        int index = getIntent().getIntExtra("album", 0);
        album = albumList.getAlbum(index);

        photoAdapter = new PhotoAdapter(this, album);
        recyclerViewPhotoList.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();

        buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                photoAdapter.notifyItemInserted(album.getNumPhotos());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            AlbumList.writeAlbumList(this, albumList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

