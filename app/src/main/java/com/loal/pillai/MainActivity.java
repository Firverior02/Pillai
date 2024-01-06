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

        continueBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DrugInfoActivity.class);
            intent.putExtra("CODE", lastCode);
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

    public void showButtons(String code) {
        lastCode = code;
        if(code != null) {
            scanBtn.setVisibility(View.GONE);
            continueCard.setVisibility(View.VISIBLE);
        } else {
            scanBtn.setVisibility(View.VISIBLE);
            continueCard.setVisibility(View.GONE);
        }
    }
}