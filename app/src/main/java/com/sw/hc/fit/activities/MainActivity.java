package com.sw.hc.fit.activities;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.sw.hc.fit.R;
import com.sw.hc.fit.util.GoogleFitUtils;
import com.sw.hc.fit.util.SwitchToolbar;
import com.sw.hc.fit.util.ToolbarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sw.hc.fit.util.GoogleFitUtils.requestGoogleFitPermissionResult;


/**
 * Created by caio on 07/09/2017.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private static final String LOG_TAG = MainActivity.class.getName();

    public static final int HOME_MAIN = 0;
    public static final int HOME_FITNESS = 1;
    public static final int HOME_NUTRITION = 2;
    public static final int HOME_GOALS = 3;
    public static final int HOME_MORE = 4;
    public static final int UNDER_DEVELOPMENT_FRAGMENT = 5;

    public static GoogleApiClient client = null;
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    public static MainActivity mainActivity;
    private Boolean exit = false;


    @Override
    protected void onResume() {
        super.onResume();

        buildFitnessClient();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mainActivity = this;

        toolbarActionBarSetUP();

        bottomBarNavigationView();
    }


    private void bottomBarNavigationView() {
        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.selectTabAtPosition(HOME_MAIN); // default

        // Listeners
        bottomBar.setOnTabSelectListener(onTabSelect);
        bottomBar.setOnTabReselectListener(onTabReselect);
    }


    private OnTabSelectListener onTabSelect = new OnTabSelectListener() {
        @Override
        public void onTabSelected(@IdRes int tabId) {

            selectTab(tabId);
        }
    };


    private OnTabReselectListener onTabReselect = new OnTabReselectListener() {
        @Override
        public void onTabReSelected(@IdRes int tabId) {

            selectTab(tabId);
        }

    };


    private void selectTab(int tabId) {

        switch (tabId) {

            case R.id.tab_home:

                displayView(HOME_MAIN);

                break;

            case R.id.tab_fitness:

                displayView(HOME_FITNESS);

                break;

            case R.id.tab_nutrition:

                displayView(HOME_NUTRITION);

                break;

            case R.id.tab_eap:

                displayView(HOME_GOALS);

                break;

            case R.id.tab_more:

                displayView(HOME_MORE);

                break;

        }

    }


    public void setToolBarUI(int TAG) {
        new ToolbarLayout(this, TAG).invoke();
    }


    public void toolbarActionBarSetUP() {

        // Toolbar (ActionBar) setup
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_actionbar_hamburger); // set Hamburger default back to the Actionbar ;)
    }


    @Override
    public void onBackPressed() {


        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {

            pressAgainToExit();

        } else {

            getSupportFragmentManager().popBackStack();
        }

    }


    private void pressAgainToExit() {

        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();

            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.toolbar_menu_main, menu);

        return true;
    }


    public void displayView(int position) {

        Fragment f;
        int TAG;

        SwitchToolbar switchToolbar = new SwitchToolbar(this, position).invoke();
        TAG = switchToolbar.getTAG();
        f = switchToolbar.getF();

        // Setup Toolbar for the selected Fragment visible
        setToolBarUI(TAG);

        // Replace the current Fragment :)
        replaceFragmentInUI(f, TAG);

    }


    private void replaceFragmentInUI(Fragment f, int TAG) {

        if (f != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // if is not one of the 5 Main Tabs (Home, Fitness, Goals, Rewards, Nutrition)
            // Do the animation then ;)
            if (shouldBeAddedToStackNavigation(TAG)) {

                // for History back Navigation ;)
                fragmentTransaction.addToBackStack(String.valueOf(TAG));

                // animation :)
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

            }

            fragmentTransaction.replace(R.id.container_body, f, String.valueOf(TAG));

            fragmentTransaction.commit();

        }

    }


    // The 5 main Tabs (Home, Fitness, Goals, Rewards, Nutrition)
    private boolean shouldBeAddedToStackNavigation(int TAG) {

        switch (TAG) {
            case HOME_MAIN:
            case HOME_FITNESS:
            case HOME_NUTRITION:
            case HOME_GOALS:
            case HOME_MORE:
                return false;
            default:
                return true;
        }
    }


    /**
     * ----------------- Google Fit code --------------------------
     */
    // Callback received when a permission request has been completed
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        requestGoogleFitPermissionResult(requestCode, grantResults, client, this);
    }

    public void buildFitnessClient() {
        client = GoogleFitUtils.buildFitnessClient(client, this);
    }

}