package com.application.photos.activities;
import android.annotation.SuppressLint;
import android.content.Context;
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


public class PhotosSearchActivity extends AppCompatActivity{
    private AlbumList albumList;
    private Button buttonSearchTags;
    private EditText editTextPersonName;
    private EditText editTextLocationName;
    private Spinner spinnerSearchOptions;
    private Spinner spinnerConjunction;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photossearch);

        buttonSearchTags = (Button) findViewById(R.id.buttonSearchTags);
        editTextPersonName = (EditText) findViewById(R.id.editTextPersonName);
        editTextLocationName = (EditText) findViewById(R.id.editTextLocationName);
        spinnerSearchOptions = (Spinner) findViewById(R.id.spinnerSearchOptions);
        spinnerConjunction = (Spinner) findViewById(R.id.spinnerConjunction);
    }

    @Override
    protected void onStart(){
        super.onStart();
        albumList = (AlbumList) getIntent().getSerializableExtra("albumList");
        String[] searchOptions = {"Using Person Tag", "Using Location Tag", "Using Both Tags"};
        String[] conjunctions = {"and", "or"};
        ArrayAdapter<String> searchOptionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, searchOptions);
        ArrayAdapter<String> conjunctionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conjunctions);
        spinnerSearchOptions.setAdapter(searchOptionsAdapter);
        spinnerConjunction.setAdapter(conjunctionsAdapter);
        spinnerSearchOptions.setSelection(0);
        spinnerConjunction.setSelection(0);
        spinnerConjunction.setGravity(Gravity.CENTER);
        spinnerSearchOptions.setGravity(Gravity.CENTER);
        Context context = this;
        spinnerSearchOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position){
                    case 0:
                        editTextPersonName.setVisibility(View.VISIBLE);
                        editTextPersonName.setEnabled(true);
                        editTextLocationName.setVisibility(View.INVISIBLE);
                        editTextLocationName.setEnabled(false);
                        spinnerConjunction.setEnabled(false);
                        spinnerConjunction.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        editTextPersonName.setVisibility(View.INVISIBLE);
                        editTextPersonName.setEnabled(false);
                        editTextLocationName.setVisibility(View.VISIBLE);
                        editTextLocationName.setEnabled(true);
                        spinnerConjunction.setEnabled(false);
                        spinnerConjunction.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        editTextPersonName.setVisibility(View.VISIBLE);
                        editTextPersonName.setEnabled(true);
                        editTextLocationName.setVisibility(View.VISIBLE);
                        editTextLocationName.setEnabled(true);
                        spinnerConjunction.setEnabled(true);
                        spinnerConjunction.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                editTextPersonName.setVisibility(View.INVISIBLE);
                editTextPersonName.setEnabled(false);
                editTextLocationName.setVisibility(View.INVISIBLE);
                editTextLocationName.setEnabled(false);
                spinnerConjunction.setEnabled(false);
                spinnerConjunction.setVisibility(View.INVISIBLE);
            }
        });
        buttonSearchTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, SearchResultsActivity.class);
                intent.putExtra("albumList", albumList);

                if(spinnerSearchOptions.getSelectedItemPosition() == 0){
                    if(editTextPersonName.getText().toString() == null || editTextPersonName.getText().toString().isEmpty()){
                        Toast.makeText(context, "Cannot search an empty string. Please try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    intent.putExtra("personName", editTextPersonName.getText().toString());
                    intent.putExtra("locationName", "");

                }else if(spinnerSearchOptions.getSelectedItemPosition() == 1){
                    if(editTextLocationName.getText().toString() == null || editTextLocationName.getText().toString().isEmpty()){
                        Toast.makeText(context, "Cannot search an empty string. Please try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    intent.putExtra("personName", "");
                    intent.putExtra("locationName", editTextLocationName.getText().toString());

                } else if(spinnerSearchOptions.getSelectedItemPosition() == 2){
                    if(editTextPersonName.getText().toString() == null || editTextPersonName.getText().toString().isEmpty() ||
                            editTextLocationName.getText().toString() == null || editTextLocationName.getText().toString().isEmpty()){
                        Toast.makeText(context, "Search parameters cannot be left blank. Please try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    intent.putExtra("personName", editTextPersonName.getText().toString());
                    intent.putExtra("locationName", editTextLocationName.getText().toString());
                    String conjunction;
                    if(spinnerConjunction.getSelectedItemPosition() == 0)
                        conjunction = "and";
                    else
                        conjunction = "or";
                    intent.putExtra("conjunction", conjunction);
                } else {
                    System.out.println("ERROR: SPINNER");
                    return;
                }
                //TODO start the activity
                context.startActivity(intent);
            }
        });

    }



    @Override
    public void onBackPressed() {
        try {
            AlbumList.writeAlbumList(this, albumList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, AlbumListActivity.class);
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
