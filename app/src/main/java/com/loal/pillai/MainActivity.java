package com.loal.pillai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton scanBtn;
    CardView continueCard;
    ImageButton continueBtn;

    String lastCode;

    ScannerFragment scannerFragment;
    HelpFragment helpFragment;
    AccountFragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize fragments
        scannerFragment = new ScannerFragment();
        helpFragment = new HelpFragment();
        accountFragment = new AccountFragment();

        // Set the default fragment to scanner
        replaceFragment(scannerFragment);

        scanBtn = findViewById(R.id.scanBtn);
        continueCard = findViewById(R.id.continueCard);
        continueBtn = findViewById(R.id.continueBtn);

        scanBtn.setOnClickListener(view -> replaceFragment(new ScannerFragment()));

        // Setup for bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        // Switch pages
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.help:
                    showButtons(null);
                    replaceFragment(helpFragment);
                    break;
                case R.id.account:
                    showButtons(null);
                    replaceFragment(accountFragment);
                    break;
                case R.id.scan:
                    replaceFragment(scannerFragment);
                    break;
            }
            return true;
        });

        // Set up the continue button to navigate to drug info
        continueBtn.setOnClickListener(view -> {
            // Set up a new intent (activity)
            Intent intent = new Intent(getApplicationContext(), DrugInfoActivity.class);

            // Send the scanned code to the new activity
            intent.putExtra("CODE", lastCode);

            // Start the new activity
            startActivity(intent);
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

    /**
     * Show and hide navigation buttons
     * TODO: Improve this as it is quite an ugly solution
     * @param code The scanned EAN-code, if null no code has been scanned
     */
    public void showButtons(String code) {
        // Update the last code scanned
        lastCode = code;
        if(code != null) {
            // Display continue button and hide scan button
            scanBtn.setVisibility(View.GONE);
            continueCard.setVisibility(View.VISIBLE);
        } else {
            // Display scan button and hide continue button
            scanBtn.setVisibility(View.VISIBLE);
            continueCard.setVisibility(View.GONE);
        }
    }
}