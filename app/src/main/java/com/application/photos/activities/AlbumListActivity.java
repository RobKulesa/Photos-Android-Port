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
import com.application.photos.adapters.AlbumAdapter;
import com.application.photos.structures.Album;
import com.application.photos.structures.AlbumList;

import java.io.IOException;

public class AlbumListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    private Button buttonCreateAlbum;

    private AlbumList albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumlist);

        buttonCreateAlbum = (Button) findViewById(R.id.buttonCreateAlbum);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewAlbumList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        albumList = AlbumList.readAlbumList(this);

        albumAdapter = new AlbumAdapter(this, albumList);
        recyclerView.setAdapter(albumAdapter);
        albumAdapter.notifyDataSetChanged();

        buttonCreateAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AlbumListActivity.this);
                dialogBuilder.setTitle("New Album Name");

                EditText input = new EditText(AlbumListActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogBuilder.setView(input);

                dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newName = input.getText().toString();
                        if(newName == null || newName.isEmpty() || albumList.containsAlbum(newName)) {
                            Toast.makeText(AlbumListActivity.this, "Album Name cannot be empty or the name of another album!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        albumList.addAlbum(new Album(newName));
                        albumAdapter.notifyItemInserted(albumList.getLength());
                        Toast.makeText(AlbumListActivity.this, "Album Created Successfully!", Toast.LENGTH_SHORT).show();
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
    protected void onStop() {
        super.onStop();
        try {
            AlbumList.writeAlbumList(this, albumList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}