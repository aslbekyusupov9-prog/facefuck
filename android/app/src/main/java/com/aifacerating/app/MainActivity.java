package com.aifacerating.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.aifacerating.app.utils.UpdateManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        
        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new com.aifacerating.app.fragments.UploadFragment())
                .commit();
        }
        
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_upload) {
                selectedFragment = new com.aifacerating.app.fragments.UploadFragment();
            } else if (itemId == R.id.nav_leaderboard) {
                selectedFragment = new com.aifacerating.app.fragments.LeaderboardFragment();
            } else if (itemId == R.id.nav_tips) {
                selectedFragment = new com.aifacerating.app.fragments.TipsFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new com.aifacerating.app.fragments.ProfileFragment();
            }
            
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
                return true;
            }
            return false;
        });

        // Trigger In-App Direct Auto-Update check asynchronously
        UpdateManager.checkForUpdates(this, false);
    }
}
