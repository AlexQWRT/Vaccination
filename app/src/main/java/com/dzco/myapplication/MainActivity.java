package com.dzco.myapplication;

import androidx.annotation.NonNull;

import com.dzco.myapplication.Models.Date;
import com.dzco.myapplication.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private int savedFragment;
    private User user = new User();
    private RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.main_activity);
        savedFragment = R.id.nav_profile;
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener);
        DatabaseReference loadProfileData = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        InProgress progressDialog = new InProgress(MainActivity.this);

        progressDialog.show();
        loadProfileData.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                transit(savedInstanceState);
                progressDialog.hide();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.hide();
                transit(savedInstanceState);
                Snackbar.make(root, R.string.failed_to_load_data, Snackbar.LENGTH_SHORT).show();
            }
        });


    }

    public void transit(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            bottomNavigation.setSelectedItemId(R.id.nav_profile);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentProfile.newInstance()).commit();
        } else {
            savedFragment = savedInstanceState.getInt("selected_fragment");
            Fragment selectedFragment = null;
            bottomNavigation.setSelectedItemId(savedFragment);

            switch (savedFragment) {
                case R.id.nav_profile:
                    selectedFragment = FragmentProfile.newInstance();
                    break;
                case R.id.nav_news:
                    selectedFragment = FragmentNews.newInstance();
                    break;
                case R.id.nav_settings:
                    selectedFragment = FragmentSettings.newInstance();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected_fragment", bottomNavigation.getSelectedItemId());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_profile:
                            selectedFragment = FragmentProfile.newInstance();
                            savedFragment = R.id.nav_profile;
                            break;
                        case R.id.nav_news:
                            selectedFragment = FragmentNews.newInstance();
                            savedFragment = R.id.nav_news;
                            break;
                        case R.id.nav_settings:
                            selectedFragment = FragmentSettings.newInstance();
                            savedFragment = R.id.nav_settings;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}