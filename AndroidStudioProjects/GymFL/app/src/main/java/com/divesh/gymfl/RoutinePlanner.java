package com.divesh.gymfl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class RoutinePlanner extends AppCompatActivity {




    private ImageButton backButton;

    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;
    private LockableViewPager mLockableViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context mContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_planner);
        //fragments
        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
//        mViewPager = (ViewPager) findViewById(R.id.container);
        mLockableViewPager = (LockableViewPager) findViewById(R.id.container);
        mLockableViewPager.setSwipeable(true);

        backButton = (ImageButton) findViewById(R.id.back);




        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLockableViewPager.setCurrentItem(0);
            }
        };

        backButton.setOnClickListener(backListener);

        setupViewPager(mLockableViewPager);
        //navigation fragment opens first
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            int val = bundle.getInt("key");
            if (val == 3) {
                mLockableViewPager.setCurrentItem(3);
            }
        }else{
            mLockableViewPager.setCurrentItem(0);
        }







        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_AddExercise:
                                openAddExercise();
                                break;
                            case R.id.action_routine:
                                break;
                            case R.id.action_MainActivity:
                                openHome();
                                break;
                        }
                        return true;
                    }
                });




    }

    public void setupViewPager(ViewPager viewPager){
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        sectionPageAdapter.addFragment(new fragment_navigation());
        sectionPageAdapter.addFragment(new fragment_schedule());
        sectionPageAdapter.addFragment(new fragment_gcalendar());
        sectionPageAdapter.addFragment(new fragment_goals());

        viewPager.setAdapter(sectionPageAdapter);
    }

    public void setViewPager(int position){
        mLockableViewPager.setCurrentItem(position);
    }


    private void openHome() {
        Intent intent = new Intent(RoutinePlanner.this, MainActivity.class);
        startActivity(intent);
    }

    //start add exercise activity

    private void openAddExercise(){
        Intent intent2 = new Intent(RoutinePlanner.this, AddExercises.class);
        startActivity(intent2);
    }

}
