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
import android.widget.TextView;
import android.widget.Toast;

import com.application.photos.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;


public class PhotosSearchActivity extends AppCompatActivity{
    private AlbumList albumList;
    private Button buttonSearchTags;
    private Spinner spinnerSearchOptions;
    private Spinner spinnerConjunction;
    private TextView textViewTag1Type;
    private Spinner spinnerTag1Type;
    private TextView textViewTag1Value;
    private EditText editTextTag1Value;
    private TextView textViewConjunction;
    private TextView textViewTag2Value;
    private EditText editTextTag2Value;
    private TextView textViewTag2Type;
    private Spinner spinnerTag2Type;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photossearch);

        buttonSearchTags = (Button) findViewById(R.id.buttonSearchTags);


        spinnerSearchOptions = (Spinner) findViewById(R.id.spinnerSearchOptions);
        spinnerConjunction = (Spinner) findViewById(R.id.spinnerConjunction);
        textViewConjunction = (TextView) findViewById(R.id.textViewConjunction);

        textViewTag1Type = (TextView) findViewById(R.id.textViewTag1Type);
        spinnerTag1Type = (Spinner) findViewById(R.id.spinnerTag1Type);
        textViewTag1Value = (TextView) findViewById(R.id.textViewTag1Value);
        editTextTag1Value = (EditText) findViewById(R.id.editTextTag1Val);

        textViewTag2Type = (TextView) findViewById(R.id.textViewTag2Type);
        spinnerTag2Type = (Spinner) findViewById(R.id.spinnerTag2Type);
        textViewTag2Value = (TextView) findViewById(R.id.textViewTag2Value);
        editTextTag2Value = (EditText) findViewById(R.id.editTextTag2Val);

    }

    @Override
    protected void onStart(){
        super.onStart();
        albumList = (AlbumList) getIntent().getSerializableExtra("albumList");
        String[] searchOptions = {"1 Tag Value Pair", "2 Tag Value Pairs"};
        String[] conjunctions = {"and", "or"};
        String[] tagTypes = {"location", "person"};
        ArrayAdapter<String> searchOptionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, searchOptions);
        ArrayAdapter<String> conjunctionsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, conjunctions);
        ArrayAdapter<String> tagTypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tagTypes);
        spinnerSearchOptions.setAdapter(searchOptionsAdapter);
        spinnerConjunction.setAdapter(conjunctionsAdapter);
        spinnerTag1Type.setAdapter(tagTypesAdapter);
        spinnerTag2Type.setAdapter(tagTypesAdapter);
        spinnerTag1Type.setSelection(0);
        spinnerTag2Type.setSelection(0);
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
                        textViewConjunction.setVisibility(View.INVISIBLE);
                        spinnerConjunction.setVisibility(View.INVISIBLE);
                        spinnerConjunction.setEnabled(false);
                        textViewTag2Type.setVisibility(View.INVISIBLE);
                        spinnerTag2Type.setVisibility(View.INVISIBLE);
                        spinnerTag2Type.setEnabled(false);
                        textViewTag2Value.setVisibility(View.INVISIBLE);
                        editTextTag2Value.setVisibility(View.INVISIBLE);
                        editTextTag2Value.setEnabled(false);
                        break;
                    case 1:
                        textViewConjunction.setVisibility(View.VISIBLE);
                        spinnerConjunction.setVisibility(View.VISIBLE);
                        spinnerConjunction.setEnabled(true);
                        textViewTag2Type.setVisibility(View.VISIBLE);
                        spinnerTag2Type.setVisibility(View.VISIBLE);
                        spinnerTag2Type.setEnabled(true);
                        textViewTag2Value.setVisibility(View.VISIBLE);
                        editTextTag2Value.setVisibility(View.VISIBLE);
                        editTextTag2Value.setEnabled(true);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                textViewTag1Type.setVisibility(View.INVISIBLE);
                spinnerTag1Type.setVisibility(View.INVISIBLE);
                spinnerTag1Type.setEnabled(false);
                textViewTag1Value.setVisibility(View.INVISIBLE);
                editTextTag1Value.setVisibility(View.INVISIBLE);
                editTextTag1Value.setEnabled(false);
                textViewConjunction.setVisibility(View.INVISIBLE);
                spinnerConjunction.setVisibility(View.INVISIBLE);
                spinnerConjunction.setEnabled(false);
                textViewTag2Type.setVisibility(View.INVISIBLE);
                spinnerTag2Type.setVisibility(View.INVISIBLE);
                spinnerTag2Type.setEnabled(false);
                textViewTag2Value.setVisibility(View.INVISIBLE);
                editTextTag2Value.setVisibility(View.INVISIBLE);
                editTextTag2Value.setEnabled(false);
            }
        });
        buttonSearchTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, SearchResultsActivity.class);
                intent.putExtra("albumList", albumList);

                if(spinnerSearchOptions.getSelectedItemPosition() == 0){
                    if(editTextTag1Value.getText().toString() == null || editTextTag1Value.getText().toString().isEmpty()){
                        Toast.makeText(context, "Cannot search an empty string. Please try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    intent.putExtra("tag1Type", spinnerTag1Type.getSelectedItemPosition());
                    intent.putExtra("tag1Value", editTextTag1Value.getText().toString().toLowerCase());
                    intent.putExtra("conjunction", "none");

                }else if(spinnerSearchOptions.getSelectedItemPosition() == 1){
                    if(editTextTag1Value.getText().toString() == null || editTextTag1Value.getText().toString().isEmpty()){
                        Toast.makeText(context, "Search Parameters cannot be empty. Please try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    intent.putExtra("tag1Type", spinnerTag1Type.getSelectedItemPosition());
                    intent.putExtra("tag1Value", editTextTag1Value.getText().toString());
                    if(editTextTag2Value.getText().toString() == null || editTextTag2Value.getText().toString().isEmpty()){
                        Toast.makeText(context, "Search Parameters cannot be empty. Please try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    intent.putExtra("tag2Type", spinnerTag2Type.getSelectedItemPosition());
                    intent.putExtra("tag2Value", editTextTag2Value.getText().toString().toLowerCase());
                    intent.putExtra("conjunction", (String) spinnerConjunction.getSelectedItem());

                }  else {
                    System.out.println("ERROR: SPINNER");
                    return;
                }
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
