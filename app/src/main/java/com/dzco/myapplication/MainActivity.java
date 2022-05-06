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

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private int savedFragment;
    private User user;
    private RelativeLayout root;
    private InProgress progress;
    private DatabaseReference loadProfileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = new User();
        progress = new InProgress(MainActivity.this);
        root = findViewById(R.id.main_activity);
        savedFragment = R.id.nav_profile;
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener);
        loadProfileData = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            progress.show();
            loadProfileData.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    bottomNavigation.setSelectedItemId(R.id.nav_profile);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentProfile.newInstance()).commit();
                    progress.hide();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.hide();
                    bottomNavigation.setSelectedItemId(R.id.nav_profile);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmentProfile.newInstance()).commit();
                    Snackbar.make(root, R.string.failed_to_load_data, Snackbar.LENGTH_SHORT).show();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected_fragment", bottomNavigation.getSelectedItemId());
        
        outState.putString("user_name", user.getName());
        outState.putBoolean("user_sex", user.isSex());
        outState.putInt("user_age", user.getAge());
        outState.putBoolean("user_chronicDiseases", user.isChronicDiseases());
        outState.putBoolean("user_badHabits", user.isBadHabits());
        outState.putString("user_measure", user.getMeasure());
        outState.putString("user_vaccine", user.getVaccine());
        outState.putInt("user_first_day", user.getFirst().getDay());
        outState.putInt("user_first_month", user.getFirst().getMonth());
        outState.putInt("user_first_year", user.getFirst().getYear());
        outState.putInt("user_second_day", user.getSecond().getDay());
        outState.putInt("user_second_month", user.getSecond().getMonth());
        outState.putInt("user_second_year", user.getSecond().getYear());
        outState.putString("user_lifePlace", user.getLifePlace());
        outState.putString("user_email", user.getEmail());
        outState.putString("user_password", user.getPassword());
        outState.putString("user_imageURI", user.getImageURI());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        user.setName(savedInstanceState.getString("user_name"));
        user.setSex(savedInstanceState.getBoolean("user_sex"));
        user.setAge(savedInstanceState.getInt("user_age"));
        user.setChronicDiseases(savedInstanceState.getBoolean("user_chronicDiseases"));
        user.setBadHabits(savedInstanceState.getBoolean("user_badHabits"));
        user.setMeasure(savedInstanceState.getString("user_measure"));
        user.setVaccine(savedInstanceState.getString("user_vaccine"));
        user.setFirst(new Date(savedInstanceState.getInt("user_first_day"),
                savedInstanceState.getInt("user_first_month"),
                savedInstanceState.getInt("user_first_year")));
        user.setSecond(new Date(savedInstanceState.getInt("user_second_day"),
                savedInstanceState.getInt("user_second_month"),
                savedInstanceState.getInt("user_second_year")));
        user.setLifePlace(savedInstanceState.getString("user_lifePlace"));
        user.setEmail(savedInstanceState.getString("user_email"));
        user.setPassword(savedInstanceState.getString("user_password"));
        user.setImageURI(savedInstanceState.getString("user_imageURI"));

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