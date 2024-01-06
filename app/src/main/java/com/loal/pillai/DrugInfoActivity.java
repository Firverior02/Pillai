package com.loal.pillai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class DrugInfoActivity extends AppCompatActivity {

    String drugEANCode;

    LinearLayout backBtn;

    RecyclerView recyclerView;

    ArrayList<String> options;
    ArrayList<Boolean> selectedOptions;

    DrugInfoAdapter adapter;

    SearchView searchView;

    CardView videoBtn, readBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_info);

        // Get additional info (EAN Code)
        Intent intent = getIntent();
        drugEANCode = intent.getStringExtra("CODE");

        // Get all options
        options = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.drug_info_options)));

        // All deselected
        selectedOptions = new ArrayList<>();
        for (int i = 0; i < options.size(); i++, selectedOptions.add(false));


        // Find views
        recyclerView = findViewById(R.id.drugInfoRecycler);
        backBtn = findViewById(R.id.backBtn);
        searchView = findViewById(R.id.searchView);
        videoBtn = findViewById(R.id.play_button);
        readBtn = findViewById(R.id.read_button);

        // Back button
        backBtn.setOnClickListener(view -> this.finish());

        // Video button
        videoBtn.setOnClickListener(view -> {
            Intent videoIntent = new Intent(getApplicationContext(), VideoActivity.class);
            startActivity(videoIntent);
        });

        // Read button
        readBtn.setOnClickListener(view -> {
            Intent readIntent = new Intent(getApplicationContext(), ReadActivity.class);
            startActivity(readIntent);
        });

        // Recycler view with options
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new DrugInfoAdapter(this, options, selectedOptions);
        recyclerView.setAdapter(adapter);

        // Search options
        searchView.clearFocus();
        searchView.setOnClickListener(view -> {

        });

        // Listener for search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // Filter options based on the search term
                filterOptions(s);
                return false;
            }
        });

    }

    private void filterOptions(String text) {
        //TODO: We need to save old selections and new ones when user does a search...
        ArrayList<String> filteredTitles = new ArrayList<>();
        ArrayList<Boolean> filteredSelection = new ArrayList<>();

        // Add all titles that contain the searched string
        int i = 0;
        for (String title : options) {
            if (title.toLowerCase().contains(text.toLowerCase())) {
                filteredTitles.add(title);
                filteredSelection.add(selectedOptions.get(i));
            }
            i++;
        }

        // Output
        if(filteredTitles.isEmpty()) {
            Toast.makeText(this, "No options available", Toast.LENGTH_SHORT).show();
        }
        adapter.setFilteredList(filteredTitles, filteredSelection);
    }
}