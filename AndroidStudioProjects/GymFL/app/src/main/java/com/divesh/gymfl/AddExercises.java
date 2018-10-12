package com.divesh.gymfl;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AddExercises extends AppCompatActivity {
    private Spinner muscleGroup;
    private EditText newExercise;
    private Button Enter;
    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercises);
        //tab view
        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("Add");
        tabLayout.getTabAt(1).setText("Library");
        mViewPager.setCurrentItem(1);
        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_AddExercise:
                                break;
                            case R.id.action_routine:
                                openScheduler();
                                break;
                            case R.id.action_MainActivity:
                                openHome();
                                break;
                        }
                        return true;
                    }
                });




    }

    private void openScheduler() {
        Intent intent = new Intent(AddExercises.this, RoutinePlanner.class);
        startActivity(intent);
    }

    //start add exercise activity

    private void openHome(){
        Intent intent2 = new Intent(AddExercises.this, MainActivity.class);
        startActivity(intent2);
    }


    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragment_add_exercises());
        adapter.addFragment(new fragment_all_exercises());

        viewPager.setAdapter(adapter);
    }
}


