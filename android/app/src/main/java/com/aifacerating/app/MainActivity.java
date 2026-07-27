package com.aifacerating.app;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_upload) {
                // TODO: Load Upload/Camera Fragment
                return true;
            } else if (itemId == R.id.nav_leaderboard) {
                // TODO: Load Leaderboard Fragment
                return true;
            } else if (itemId == R.id.nav_tips) {
                // TODO: Load Tips Fragment
                return true;
            } else if (itemId == R.id.nav_profile) {
                // TODO: Load Profile Fragment
                return true;
            }
            return false;
        });
    }
}
