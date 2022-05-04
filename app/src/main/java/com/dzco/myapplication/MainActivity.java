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
        DatabaseReference loadProfileData = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        InProgress progressDialog = new InProgress(MainActivity.this);
        progressDialog.show();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                loadProfileData.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        progressDialog.hide();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (savedInstanceState != null) {
                            user = (User) savedInstanceState.getSerializable("user");
                        }
                        progressDialog.hide();
                        Snackbar.make(root, R.string.failed_to_load_data, Snackbar.LENGTH_SHORT).show();
                    }
                });
                if (savedInstanceState == null) {
                    bottomNavigation.setSelectedItemId(R.id.nav_profile);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentProfile.newInstance()).commit();
                } else {
                    user = (User) savedInstanceState.getSerializable("user");
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
        });
        th.start();
        savedFragment = R.id.nav_profile;
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("user", User.class);
        outState.putInt("selected_fragment", bottomNavigation.getSelectedItemId());
        outState.putString("name", user.getName());
        outState.putBoolean("sex", user.isSex());
        outState.putInt("age", user.getAge());
        outState.putBoolean("chronicDiseases", user.isChronicDiseases());
        outState.putBoolean("badHabits", user.isBadHabits());
        outState.putString("measure", user.getMeasure());
        outState.putString("vaccine", user.getVaccine());
        outState.putInt("day1", user.getFirst().getDay());
        outState.putInt("month1", user.getFirst().getMonth());
        outState.putInt("year1", user.getFirst().getYear());
        outState.putInt("day2", user.getSecond().getDay());
        outState.putInt("month2", user.getSecond().getMonth());
        outState.putInt("year2", user.getSecond().getYear());
        outState.putString("lifePlace", user.getLifePlace());
        outState.putString("email", user.getEmail());
        outState.putString("password", user.getPassword());
        outState.putString("imageURI", user.getImageURI());
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