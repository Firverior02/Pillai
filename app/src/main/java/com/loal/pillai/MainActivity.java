package com.loal.pillai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton scanBtn;
    CardView continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the default fragment to scanner
        replaceFragment(new ScannerFragment());

        scanBtn = findViewById(R.id.scanBtn);
        continueBtn = findViewById(R.id.continueBtn);

        scanBtn.setOnClickListener(view -> replaceFragment(new ScannerFragment()));

        // Setup for bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        // Switch pages
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.help:
                    showButtons(false);
                    replaceFragment(new HelpFragment());
                    break;
                case R.id.account:
                    showButtons(false);
                    replaceFragment(new AccountFragment());
                    break;
                case R.id.scan:
                    replaceFragment(new ScannerFragment());
                    break;
            }
            return true;
        });
    }

    /**
     * Replace the visible fragment
     * @param fragment Fragment to be displayed
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void showButtons(boolean hasCode) {
        if(hasCode) {
            scanBtn.setVisibility(View.INVISIBLE);
            continueBtn.setVisibility(View.VISIBLE);
        } else {
            scanBtn.setVisibility(View.VISIBLE);
            continueBtn.setVisibility(View.INVISIBLE);
        }
    }
}